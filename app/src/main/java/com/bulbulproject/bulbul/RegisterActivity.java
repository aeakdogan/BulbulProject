package com.bulbulproject.bulbul;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    EditText nameInput;
    Spinner genderInput;
    Spinner birthYearInput;
    EditText emailInput;
    EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameInput = (EditText) findViewById(R.id.name);
        genderInput = (Spinner) findViewById(R.id.spinner_gender);
        birthYearInput = (Spinner) findViewById(R.id.spinner_birth_year);
        emailInput = (EditText) findViewById(R.id.email);
        passwordInput = (EditText) findViewById(R.id.password);


        List<String> yearList = new ArrayList<String>();
        for(int i = 2010; i >= 1930; i--)
            yearList.add(Integer.toString(i));
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearList);
        birthYearInput.setAdapter(spinnerAdapter);

        birthYearInput.setSelection(spinnerAdapter.getPosition("1994"));


        Button registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });
    }
    public void attemptRegister(){

        String name = nameInput.getText().toString();
        String gender = genderInput.getSelectedItem().toString();
        String birthYear = birthYearInput.getSelectedItem().toString();
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        if(name.length() < 3){
            nameInput.setError(getString(R.string.error_field_required));
            nameInput.requestFocus();
            return;
        }
        if(!email.contains("@") || email.length() < 3){
            emailInput.setError(getString(R.string.error_no_email_account));
            emailInput.requestFocus();
            return;
        }
        if(password.length() < 5){
            passwordInput.setError(getString(R.string.error_invalid_password));
            passwordInput.requestFocus();
            return;
        }

        try {
            // Simulate network access.
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }
}
