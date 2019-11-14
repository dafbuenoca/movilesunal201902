package co.edu.unal.company.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import co.edu.unal.company.Model.Company;

import java.util.ArrayList;
import java.util.List;

public class CompanyOperations {
    public static final String LOGTAG = "ENT_MANAGEMENT_SYS";

    SQLiteOpenHelper dbHandler;
    SQLiteDatabase database;

    private static final String[] allColumns = {
            CompanyDbHandler.COLUMN_ID,
            CompanyDbHandler.COLUMN_NAME,
            CompanyDbHandler.COLUMN_URL,
            CompanyDbHandler.COLUMN_PHONE,
            CompanyDbHandler.COLUMN_EMAIL,
            CompanyDbHandler.COLUMN_PRODUCTS_AND_SERVICES,
            CompanyDbHandler.COLUMN_TYPE,
    };

    public CompanyOperations(Context context) {
        dbHandler = new CompanyDbHandler(context);
    }

    public Company addCompany(Company company) {
        ContentValues values = new ContentValues();
        values.put(CompanyDbHandler.COLUMN_NAME, company.getName());
        values.put(CompanyDbHandler.COLUMN_URL, company.getUrl());
        values.put(CompanyDbHandler.COLUMN_PHONE, company.getPhone());
        values.put(CompanyDbHandler.COLUMN_EMAIL, company.getEmail());
        values.put(CompanyDbHandler.COLUMN_PRODUCTS_AND_SERVICES, company.getProductsAndServices());
        values.put(CompanyDbHandler.COLUMN_TYPE, company.getType());
        database = dbHandler.getWritableDatabase();
        long insertId = database.insert(CompanyDbHandler.TABLE_COMPANIES, null, values);
        database.close();
        company.setId(insertId);
        return company;
    }

    public Company getCompany(long id) {
        database = dbHandler.getReadableDatabase();
        Cursor cursor = database.query(
                CompanyDbHandler.TABLE_COMPANIES,
                allColumns,
                CompanyDbHandler.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );
        if (cursor != null)
            cursor.moveToFirst();
        Company company = new Company(
                Long.parseLong(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6)
        );
        database.close();
        return company;
    }

    public List<Company> getAllCompanies() {
        database = dbHandler.getReadableDatabase();
        Cursor cursor = database.query(
                CompanyDbHandler.TABLE_COMPANIES,
                allColumns,
                null,
                null,
                null,
                null,
                null
        );
        List<Company> companies = new ArrayList<>();
        if (cursor.getCount() > 0)
            while (cursor.moveToNext()) {
                Company company = new Company(
                        cursor.getLong(cursor.getColumnIndex(CompanyDbHandler.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(CompanyDbHandler.COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(CompanyDbHandler.COLUMN_URL)),
                        cursor.getString(cursor.getColumnIndex(CompanyDbHandler.COLUMN_PHONE)),
                        cursor.getString(cursor.getColumnIndex(CompanyDbHandler.COLUMN_EMAIL)),
                        cursor.getString(cursor.getColumnIndex(CompanyDbHandler.COLUMN_PRODUCTS_AND_SERVICES)),
                        cursor.getString(cursor.getColumnIndex(CompanyDbHandler.COLUMN_TYPE))
                );
                companies.add(company);
            }
        database.close();
        return companies;
    }

    public void updateCompany(Company company) {
        ContentValues values = new ContentValues();
        values.put(CompanyDbHandler.COLUMN_NAME, company.getName());
        values.put(CompanyDbHandler.COLUMN_URL, company.getUrl());
        values.put(CompanyDbHandler.COLUMN_PHONE, company.getPhone());
        values.put(CompanyDbHandler.COLUMN_EMAIL, company.getEmail());
        values.put(CompanyDbHandler.COLUMN_PRODUCTS_AND_SERVICES, company.getProductsAndServices());
        values.put(CompanyDbHandler.COLUMN_TYPE, company.getType());

        database = dbHandler.getReadableDatabase();
        database.update(
                CompanyDbHandler.TABLE_COMPANIES,
                values,
                CompanyDbHandler.COLUMN_ID + "=?",
                new String[]{String.valueOf(company.getId())}
        );
        database.close();
    }

    public void removeCompany(Company company) {
        database = dbHandler.getWritableDatabase();
        database.delete(
                CompanyDbHandler.TABLE_COMPANIES,
                CompanyDbHandler.COLUMN_ID + "=" + company.getId(),
                null
        );
        database.close();
    }
}
