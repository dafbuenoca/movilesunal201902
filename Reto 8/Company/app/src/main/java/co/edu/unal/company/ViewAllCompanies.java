package co.edu.unal.company;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.List;

import co.edu.unal.company.Db.CompanyOperations;
import co.edu.unal.company.Model.Company;

public class ViewAllCompanies extends ListActivity {

    private CompanyOperations companyOperations;
    List<Company> companies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_companies);
        companyOperations = new CompanyOperations(this);
        companies = companyOperations.getAllCompanies();
        ArrayAdapter<Company> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                companies
        );
        setListAdapter(adapter);
    }
}

