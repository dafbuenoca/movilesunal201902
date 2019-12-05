package co.edu.unal.radionicawebservice;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import co.edu.unal.radionicawebservice.models.Municipio;

import static android.content.ContentValues.TAG;
import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private String path = "https://www.datos.gov.co/resource/ezp2-wxbu.json";

    private Button btnSubmit;
    private String responseText;
    private StringBuffer response;
    private URL url;
    private Activity activity;
    private List<Municipio> municipios = new ArrayList<>();
    private ProgressDialog processDialog;
    private ListView listView;
    private Spinner departamentosSpinner;

    private LinearLayout mLinearLayout;
    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        addListenerOnSpinnerItemSelection();
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        listView = (ListView) findViewById(R.id.listViewCompaniesFilter);

        btnSubmit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                municipios.clear();
                //Call WebService
                new GetServerData().execute();
            }
        });

        mLinearLayout = (LinearLayout) findViewById(R.id.linear_layout);
    }

    public void addListenerOnSpinnerItemSelection() {
        departamentosSpinner = (Spinner) findViewById(R.id.spinner1);

        departamentosSpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    class GetServerData extends AsyncTask {

        private List<String> codigos = new ArrayList<>();
        private final String CODIGO = "codigo_municipio";
        private String MUNICIPIO = "municipio";
        private String DEPARTAMENTO = "departamento";
        private String POBLACION = "poblaci_n";
        private String ESTACION = "estacion_que_cubre";
        private String FRECUENTCIA = "frecuencia_fm";


        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            processDialog = new ProgressDialog(MainActivity.this);
            processDialog.setMessage("Obteniendo datos...");
            processDialog.setCancelable(false);
            processDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            return getWebServiceResponseData();
        }

        @Override
        protected void onPostExecute(Object o){
            super.onPostExecute(o);

            //Dismiss the progress dialog
            if(processDialog.isShowing())
                processDialog.dismiss();
            //For populating list data
            CustomMunicipioList customMunicipioList = new CustomMunicipioList(activity, municipios);
            listView.setAdapter(customMunicipioList);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int posicion, long l) {
                    Toast.makeText(getApplicationContext(), "Usted seleccionó " + municipios.get(posicion).getMunicipio(), Toast.LENGTH_SHORT).show();

                    LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                    // Inflate the custom layout/view
                    View customView = inflater.inflate(R.layout.custom_layout,null);
                    ListView dotacionesView = (ListView) customView.findViewById(R.id.listViewDotaciones);
                    /*
                    if(dotacionesView == null)
                        System.out.println("Dotacionesview null");
                    else
                        System.out.println("Dotacionesview no null");
                    CustomDotacionList customDotacionList = new CustomDotacionList(activity, parques.get(posicion).getDotaciones());
                    dotacionesView.setAdapter(customDotacionList);
                    // Initialize a new instance of popup window
                    mPopupWindow = new PopupWindow(
                            customView,
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );
                    */
                    // Set an elevation value for popup window
                    // Call requires API level 21
                    mPopupWindow.setElevation(5.0f);


                    // Get a reference for the custom view close button
                    Button closeButton = (Button) customView.findViewById(R.id.ib_close);

                    // Set a click listener for the popup window close button
                    closeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Dismiss the popup window
                            mPopupWindow.dismiss();
                        }
                    });

                    mPopupWindow.showAtLocation(mLinearLayout, Gravity.CENTER,0,0);

                }
            });
        }

        protected Void getWebServiceResponseData(){
            try{
                Object departamentoSeleccionado = departamentosSpinner.getSelectedItem();
                String completePath = path;
                if(departamentoSeleccionado != null){
                    String departamento = String.valueOf(departamentoSeleccionado);
                    completePath += "?departamento=" + departamento;
                }
                url = new URL(completePath);
                Log.d(TAG, "ServerData: " + path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();
                Log.d(TAG, "Response code: " + responseCode);

                if(responseCode == HttpsURLConnection.HTTP_OK){
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String output;
                    response = new StringBuffer();
                    while((output = in.readLine()) != null)
                        response.append(output);
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            responseText = response.toString();
            Log.d(TAG, "data: " + responseText);

            try{
                JSONArray jsonArray = new JSONArray(responseText);
                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String codigo = jsonObject.getString(CODIGO);
                    String municipio = jsonObject.getString(MUNICIPIO);
                    String departamento = jsonObject.getString(DEPARTAMENTO);
                    String poblacion = jsonObject.getString(POBLACION);
                    String estacion = jsonObject.getString(ESTACION);
                    String frecuencia = jsonObject.getString(FRECUENTCIA);


                    Municipio municipioClass = new Municipio(departamento, municipio,codigo, poblacion, estacion, frecuencia);

                    if(!municipios.contains(municipioClass)){

                        municipios.add(municipioClass);
                    }



                }
                System.out.println("municipios: ");
                System.out.println(municipios);

            }catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
