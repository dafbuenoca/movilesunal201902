package co.edu.unal.company;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import co.edu.unal.company.Db.CompanyOperations;
import co.edu.unal.company.Model.Company;
import co.edu.unal.company.Model.CompanyType;

public class AddUpdateCompany extends AppCompatActivity {

    private static final String EXTRA_ENT_ID = "co.edu.unal.entId";
    private static final String EXTRA_ADD_UPDATE = "co.edu.unal.add_update";

    private EditText name;
    private EditText url;
    private EditText phone;
    private EditText email;
    private EditText productsAndServices;
    private RadioGroup classification;
    private RadioButton consulting, tailoredDevelopment, softwareFactory;
    private Button addUpdateCompanyButton;

    private Company newCompany, oldCompany;
    private long id;
    private CompanyOperations companyOperations;
    private String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_company);
        newCompany = new Company();
        oldCompany = new Company();
        companyOperations = new CompanyOperations(this);

        name = (EditText) findViewById(R.id.edit_text_name);
        url = (EditText) findViewById(R.id.edit_text_url);
        phone = (EditText) findViewById(R.id.edit_text_phone);
        email = (EditText) findViewById(R.id.edit_text_email);
        productsAndServices = (EditText) findViewById(R.id.edit_products_and_services);
        classification = (RadioGroup) findViewById(R.id.radio_classification);
        consulting = (RadioButton) findViewById(R.id.radio_consulting);
        tailoredDevelopment = (RadioButton) findViewById(R.id.radio_tailored_development);
        softwareFactory = (RadioButton) findViewById(R.id.radio_software_factory);
        addUpdateCompanyButton = (Button) findViewById(R.id.button_add_update_company);

        mode = getIntent().getStringExtra(EXTRA_ADD_UPDATE);
        if (mode.equals("Update")) {
            addUpdateCompanyButton.setText(R.string.update_company);
            id = getIntent().getLongExtra(EXTRA_ENT_ID, 0);
            initializeCompany(id);
        }

        classification.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_tailored_development:
                        newCompany.setType(CompanyType.TAILORED_DEVELOPMENT);
                        if (mode.equals("Update")) oldCompany.setType(CompanyType.TAILORED_DEVELOPMENT);
                        break;
                    case R.id.radio_software_factory:
                        newCompany.setType(CompanyType.SOFTWARE_FACTORY);
                        if (mode.equals("Update")) oldCompany.setType(CompanyType.SOFTWARE_FACTORY);
                        break;
                    default:
                        newCompany.setType(CompanyType.CONSULTING);
                        if (mode.equals("Update")) oldCompany.setType(CompanyType.CONSULTING);
                }
            }
        });

        addUpdateCompanyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text;
                if (mode.equals("Add")) {
                    newCompany.setName(name.getText().toString());
                    newCompany.setUrl(url.getText().toString());
                    newCompany.setPhone(phone.getText().toString());
                    newCompany.setEmail(email.getText().toString());
                    newCompany.setProductsAndServices(productsAndServices.getText().toString());
                    companyOperations.addCompany(newCompany);
                    text = "La empresa " + newCompany.getName() + " se ha agredado correctamente";
                } else {
                    oldCompany.setName(name.getText().toString());
                    oldCompany.setUrl(url.getText().toString());
                    oldCompany.setPhone(phone.getText().toString());
                    oldCompany.setEmail(email.getText().toString());
                    oldCompany.setProductsAndServices(productsAndServices.getText().toString());
                    companyOperations.updateCompany(oldCompany);
                    text = "La empresa " + oldCompany.getName() + " se ha actualizado correctamente";
                }
                Toast toast = Toast.makeText(AddUpdateCompany.this, text, Toast.LENGTH_SHORT);
                toast.show();
                Intent intent = new Intent(AddUpdateCompany.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initializeCompany(long id) {
        oldCompany = companyOperations.getCompany(id);
        name.setText(oldCompany.getName());
        url.setText(oldCompany.getUrl());
        phone.setText(oldCompany.getPhone());
        email.setText(oldCompany.getEmail());
        productsAndServices.setText(oldCompany.getProductsAndServices());
        switch (oldCompany.getType()) {
            case CompanyType.TAILORED_DEVELOPMENT:
                classification.check(R.id.radio_tailored_development);
                break;
            case CompanyType.SOFTWARE_FACTORY:
                classification.check(R.id.radio_software_factory);
                break;
            default:
                classification.check(R.id.radio_consulting);
        }
    }
}
