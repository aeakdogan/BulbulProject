package com.bulbulproject.bulbul;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private Spinner birthYearSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        birthYearSpinner = (Spinner) findViewById(R.id.spinner_birth_year);

        List<String> yearList = new ArrayList<String>();
        for(int i = 2010; i >= 1930; i--)
            yearList.add(Integer.toString(i));
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearList);
        birthYearSpinner.setAdapter(spinnerAdapter);

        birthYearSpinner.setSelection(spinnerAdapter.getPosition("1994"));
    }
}
