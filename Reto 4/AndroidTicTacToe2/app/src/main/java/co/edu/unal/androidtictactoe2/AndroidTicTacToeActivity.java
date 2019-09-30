package co.edu.unal.androidtictactoe2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class AndroidTicTacToeActivity extends AppCompatActivity {

    private TicTacToeGame mGame;

    //human first or not
    private boolean mhumanFirst;

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

    //buttons making the board
    private Button mBoardButton[];

    //various text displayed
    private TextView mInfoTextView;


    static final int DIALOG_DIFFICULTY_ID = 0;
    static final int DIALOG_QUIT_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_tic_tac_toe);

        mBoardButton = new Button[TicTacToeGame.BOARD_SIZE];
        mBoardButton[0] = (Button) findViewById(R.id.one);
        mBoardButton[1] = (Button) findViewById(R.id.two);
        mBoardButton[2] = (Button) findViewById(R.id.three);
        mBoardButton[3] = (Button) findViewById(R.id.four);
        mBoardButton[4] = (Button) findViewById(R.id.five);
        mBoardButton[5] = (Button) findViewById(R.id.six);
        mBoardButton[6] = (Button) findViewById(R.id.seven);
        mBoardButton[7] = (Button) findViewById(R.id.eight);
        mBoardButton[8] = (Button) findViewById(R.id.nine);

        mInfoTextView = (TextView) findViewById(R.id.information);

        mGame = new TicTacToeGame();

        mInfoTextView = (TextView) findViewById(R.id.information);

        mNumberHuman = (TextView) findViewById(R.id.human);
        mNumberTie = (TextView) findViewById(R.id.tie);
        mNumberAndroid = (TextView) findViewById(R.id.android);

        mAndroidWins = 0;
        mHumanWins=0;
        mTie=0;
        mhumanFirst = false;

        startNewGame();
    }

    private void startNewGame()
    {
        mGame.clearBoard();

        mhumanFirst=!mhumanFirst;

        for (int i = 0 ;  i < mBoardButton.length; i++)
        {
            mBoardButton[i].setText("");
            mBoardButton[i].setEnabled(true);
            mBoardButton[i].setOnClickListener(new ButtonClickListener(i));
        }

        mGameOver=false;

        //human goes first
        if(mhumanFirst){
            mInfoTextView.setText(R.string.first_human);
        } else {
            mInfoTextView.setText(R.string.first_android);
            int move = mGame.getComputerMove();
            setMove(mGame.COMPUTER_PLAYER, move);
            mInfoTextView.setText(R.string.turn_human);
        }
    }

    private void disableButtons(){
        for (int i = 0 ;  i < mBoardButton.length; i++)
        {
            if (mBoardButton[i].getText() == "")
                mBoardButton[i].setEnabled(false);
        }
    }

    private class ButtonClickListener implements View.OnClickListener {

        int location;

        public ButtonClickListener (int location){
            this.location = location;

        }

        public void  onClick(View view){

            if (mBoardButton[location].isEnabled())
            {
                setMove(TicTacToeGame.HUMAN_PLAYER, location);

                int  winner = mGame.checkForWinner();
                if( winner  == 0 ){
                    mInfoTextView.setText("It's Android's turn.");
                    int move = mGame.getComputerMove();
                    setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                    winner = mGame.checkForWinner();
                }
                if( winner == 0)
                    mInfoTextView.setText("it's your turn");
                else if (winner == 1)
                {
                    mInfoTextView.setText("It's a tie!");
                    mTie++;
                    mNumberTie.setText("Ties : " + mTie);
                    mGameOver=true;
                    disableButtons();
                }
                else if (winner == 2)
                {
                    mHumanWins++;
                    mNumberHuman.setText("Human : " + mHumanWins);
                    mGameOver=true;
                    disableButtons();
                }
                else
                {
                    mInfoTextView.setText("Android won!");
                    mAndroidWins++;
                    mNumberAndroid.setText("Android : " + mAndroidWins);
                    mGameOver=true;
                    disableButtons();
                }
            }
        }
    }

    private void setMove(char player, int location){
        mGame.setMove(player, location);
        mBoardButton[location].setEnabled(false);
        mBoardButton[location].setText(String.valueOf(player));
        if(player == TicTacToeGame.HUMAN_PLAYER)
            mBoardButton[location].setTextColor(Color.rgb(0, 200, 0));
        else
            mBoardButton[location].setTextColor(Color.rgb( 200, 0, 0));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater  inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.new_game:
                startNewGame();
                return true;
            case R.id.ai_difficulty:
                showDialog(DIALOG_DIFFICULTY_ID);
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

        switch (id){
            case DIALOG_DIFFICULTY_ID:
                builder.setTitle(R.string.difficulty_choose);
                final CharSequence[] levels = {
                    getResources().getString(R.string.difficulty_easy),
                    getResources().getString(R.string.difficulty_harder),
                    getResources().getString(R.string.difficulty_expert),
                };

                int selected =0;
                TicTacToeGame.DifficultyLevel dif;
                dif = mGame.getDifficultyLevel();

                if(dif == TicTacToeGame.DifficultyLevel.Easy){
                    selected = 0;
                } else if ( dif == TicTacToeGame.DifficultyLevel.Harder){
                    selected = 1;
                } else if ( dif == TicTacToeGame.DifficultyLevel.Expert){
                    selected = 2;
                }

                builder.setSingleChoiceItems(levels, selected,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dialogInterface.dismiss();
                            if(i == 0)
                                mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
                            else if( i == 1)
                                mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
                            else if (i == 2)
                                mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);
                            Toast.makeText(getApplicationContext(), levels[i], Toast.LENGTH_SHORT).show();
                        }
                    });
                dialog = builder.create();
                break;
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

}

