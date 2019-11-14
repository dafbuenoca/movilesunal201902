package co.edu.unal.company;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import co.edu.unal.company.Db.CompanyOperations;


public class MainActivity extends AppCompatActivity {

    private Button addCompanyButton;
    private Button editCompanyButton;
    private Button deleteCompanyButton;
    private Button viewAllCompanyButton;
    private CompanyOperations companyOperations;
    private static final String EXTRA_CMP_ID = "co.edu.unal.empId";
    private static final String EXTRA_ADD_UPDATE = "co.edu.unal.add_update";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        addCompanyButton = (Button) findViewById(R.id.button_add_company);
        editCompanyButton = (Button) findViewById(R.id.button_edit_company);
        deleteCompanyButton = (Button) findViewById(R.id.button_delete_company);
        viewAllCompanyButton = (Button)findViewById(R.id.button_view_companies);



        addCompanyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,AddUpdateCompany.class);
                i.putExtra(EXTRA_ADD_UPDATE, "Add");
                startActivity(i);
            }
        });
        editCompanyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCmpIdAndUpdateCmp();
            }
        });
        deleteCompanyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCmpIdAndRemoveCmp();
            }
        });
        viewAllCompanyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ViewAllCompanies.class);
                startActivity(i);
            }
        });

    }





    public void getCmpIdAndUpdateCmp(){

        LayoutInflater li = LayoutInflater.from(this);
        View getCmpIdView = li.inflate(R.layout.dialog_get_company_id, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set dialog_get_emp_id.xml to alertdialog builder
        alertDialogBuilder.setView(getCmpIdView);

        final EditText companyInput = (EditText) getCmpIdView.findViewById(R.id.text_edit_company_id);
        System.out.println("sjdlskjdskjdskdsjkdsjdsk");
        System.out.println(companyInput);
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // get user input and set it to result
                        // edit text
                        Intent i = new Intent(MainActivity.this,AddUpdateCompany.class);
                        i.putExtra(EXTRA_ADD_UPDATE, "Update");
                        i.putExtra(EXTRA_CMP_ID, Long.parseLong(companyInput.getText().toString()));
                        startActivity(i);
                    }
                }).create()
                .show();

    }


    public void getCmpIdAndRemoveCmp(){

        LayoutInflater li = LayoutInflater.from(this);
        View getCmpIdView = li.inflate(R.layout.dialog_get_company_id, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set dialog_get_emp_id.xml to alertdialog builder
        alertDialogBuilder.setView(getCmpIdView);

        final EditText companyInput = (EditText) getCmpIdView.findViewById(R.id.text_edit_company_id);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int companyId) {
                        // get user input and set it to result
                        // edit text
                        companyOperations = new CompanyOperations(MainActivity.this);
                        companyOperations.removeCompany(companyOperations.getCompany(Long.parseLong(companyInput.getText().toString())));
                        Toast t = Toast.makeText(MainActivity.this,"Company removed successfully!",Toast.LENGTH_SHORT);
                        t.show();
                    }
                }).create()
                .show();

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        companyOperations.open();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        companyOperations.close();
//
//    }
}