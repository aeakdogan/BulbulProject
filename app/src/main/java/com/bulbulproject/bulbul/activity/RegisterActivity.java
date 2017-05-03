package com.bulbulproject.bulbul.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.bulbulproject.RegisterMutation;
import com.bulbulproject.bulbul.App;
import com.bulbulproject.bulbul.R;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class RegisterActivity extends AppCompatActivity {

    private SharedPreferences sharedPref;
    private EditText nameInput;
    private Spinner genderInput;
    private Spinner birthYearInput;
    private EditText emailInput;
    private EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE);
        nameInput = (EditText) findViewById(R.id.name);
        genderInput = (Spinner) findViewById(R.id.spinner_gender);
        birthYearInput = (Spinner) findViewById(R.id.spinner_birth_year);
        emailInput = (EditText) findViewById(R.id.email);
        passwordInput = (EditText) findViewById(R.id.password);


        List<String> yearList = new ArrayList<String>();
        for (int i = 2010; i >= 1930; i--)
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

    public void attemptRegister() {

        String name = nameInput.getText().toString();
        String gender = genderInput.getSelectedItem().toString();
        String birthYear = birthYearInput.getSelectedItem().toString();
        final String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        if (name.length() < 3) {
            nameInput.setError(getString(R.string.error_field_required));
            nameInput.requestFocus();
            return;
        }
        if (!email.contains("@") || email.length() < 3) {
            emailInput.setError(getString(R.string.error_no_email_account));
            emailInput.requestFocus();
            return;
        }
        if (password.length() < 5) {
            passwordInput.setError(getString(R.string.error_invalid_password));
            passwordInput.requestFocus();
            return;
        }

        ((App) getApplication()).apolloClient().newCall(
                RegisterMutation.builder()
                        .email(emailInput.getText().toString())
                        .password(passwordInput.getText().toString())
                        .username(nameInput.getText().toString())
                        .build()).enqueue(new ApolloCall.Callback<RegisterMutation.Data>() {
            @Override
            public void onResponse(@Nonnull Response<RegisterMutation.Data> response) {
                if(response.isSuccessful()) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("AUTH_TOKEN", response.data().register());
                    editor.apply();
                    Intent intent = new Intent(getApplicationContext(), SpotifyConnectionActivity.class);
                    startActivity(intent);
                }
               else {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            emailInput.setError("");
                            nameInput.setError("");
                        }
                    });
                }
            }


            @Override
            public void onFailure(@Nonnull ApolloException e) {
                final String text = e.getMessage();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RegisterActivity.this, text, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
}
