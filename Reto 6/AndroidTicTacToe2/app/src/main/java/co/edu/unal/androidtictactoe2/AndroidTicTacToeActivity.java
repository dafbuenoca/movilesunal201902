package co.edu.unal.androidtictactoe2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class AndroidTicTacToeActivity extends AppCompatActivity{

    //Represents the internal state of the game
    private TicTacToeGame mGame;

    //human first or not
    private boolean mhumanFirst;

    private SharedPreferences mPrefs;


    //the numbers of wins
    private int mAndroidWins;
    private int mHumanWins;
    private int mTie;

    //text views
    private TextView mNumberHuman;
    private TextView mNumberTie;
    private TextView mNumberAndroid;

    //game over
    private boolean mGameOver;

    private  boolean mSoundOn;

    //various text displayed
    private TextView mInfoTextView;

    static final int DIALOG_QUIT_ID = 1;


    private BoardView mBoardView;

    private MediaPlayer mHumanMediaPlayer;
    private MediaPlayer mComputereMediaPlayer;

    @Override
    protected void onResume(){
        super.onResume();

        mHumanMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.zelda_start);
        mComputereMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.zelda_treasure);
    }

    @Override
    protected void onPause(){
        super.onPause();

        mHumanMediaPlayer.release();
        mComputereMediaPlayer.release();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_tic_tac_toe);

        mInfoTextView = (TextView) findViewById(R.id.information);

        mNumberHuman = (TextView) findViewById(R.id.human);
        mNumberTie = (TextView) findViewById(R.id.tie);
        mNumberAndroid = (TextView) findViewById(R.id.android);

        mGame = new TicTacToeGame();
        mBoardView = (BoardView) findViewById(R.id.board);
        mBoardView.setGame(mGame);
        mBoardView.setOnTouchListener(mTouchListener);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);



        mSoundOn = mPrefs.getBoolean("sound", true);


        mAndroidWins = 0;
        mHumanWins=0;
        mTie=0;
        mhumanFirst = false;
        startNewGame();
    }

    private void startNewGame(){
        mGame.clearBoard();
        mBoardView.invalidate();

        mhumanFirst=!mhumanFirst;

        mGameOver=false;

        //human goes first
        if(mhumanFirst){
            mInfoTextView.setText(R.string.first_human);
        } else {
            mInfoTextView.setText(R.string.first_android);
            int move = mGame.getComputerMove();
            setMove(TicTacToeGame.COMPUTER_PLAYER,move);
            mInfoTextView.setText(R.string.turn_human);
        }
    }

    private boolean setMove(char player,int location){
        if(mGame.setMove(player, location)){
            if(player == TicTacToeGame.HUMAN_PLAYER && mSoundOn)
                mHumanMediaPlayer.start();
            else if(player == TicTacToeGame.COMPUTER_PLAYER && mSoundOn)
                mComputereMediaPlayer.start();
            mBoardView.invalidate();
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.new_game:
                startNewGame();
                return true;
            case R.id.settings:
                startActivityForResult(new Intent(this, Settings.class), 0);
                return true;
            case R.id.quit:
                showDialog(DIALOG_QUIT_ID);
                return true;
        }
        return false;
    }

    @Override
    protected Dialog onCreateDialog(int id){
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch(id){

            case DIALOG_QUIT_ID:

                builder.setMessage(R.string.quit_question)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.no, null);
                dialog = builder.create();
                break;
        }
        return dialog;
    }


    protected  void onAactivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == RESULT_CANCELED){
            mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            mSoundOn = mPrefs.getBoolean("sound", true);

            String difficultyLevel = mPrefs.getString("difficulty_level",
                    getResources().getString(R.string.difficulty_harder));

            if (difficultyLevel.equals(getResources().getString(R.string.difficulty_easy)))
                mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
            else if ( difficultyLevel.equals(getResources().getString(R.string.difficulty_harder)) )
                mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
            else
                mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);
        }

    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            int col = (int) event.getX() / mBoardView.getBoardCellWidth();
            int row = (int) event.getY() / mBoardView.getBoardCellHeight();
            int pos = row * 3 + col;

            if(!mGameOver && setMove(TicTacToeGame.HUMAN_PLAYER, pos)) {
                int winner = mGame.checkForWinner();
                if(winner==0){
                    mInfoTextView.setText(R.string.turn_computer);
                    int move = mGame.getComputerMove();
                    setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                    winner = mGame.checkForWinner();
                }

                if(winner==0){
                    mInfoTextView.setText(R.string.turn_human);
                } else if ( winner == 1){
                    mInfoTextView.setText(R.string.result_tie);
                    mTie++;
                    mNumberTie.setText("Ties : " + mTie);
                    mGameOver=true;
                } else if (winner == 2){
                    String defaultMessage = getResources().getString(R.string.result_human_wins);
                    mInfoTextView.setText(mPrefs.getString("victory_message", defaultMessage ));
                    mHumanWins++;
                    mNumberHuman.setText("Human : " + mHumanWins);
                    mGameOver=true;
                } else {
                    mInfoTextView.setText(R.string.result_computer_wins);
                    mAndroidWins++;
                    mNumberAndroid.setText("Android : " + mAndroidWins);
                    mGameOver=true;
                }            }

            return false;
        }
    };

}
