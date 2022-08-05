package com.assassino.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText txtInEmail = findViewById(R.id.txtInEmail);
        EditText txtInPassword = findViewById(R.id.txtInPassword);
        Button btnSignIn = findViewById(R.id.btnSignIn);
        Button btnSignUp = findViewById(R.id.btnSignUp);

        txtInEmail.setText(PreferenceManager.getDefaultSharedPreferences(this).getString("keyEmail", ""));
        txtInPassword.setText(PreferenceManager.getDefaultSharedPreferences(this).getString("keyPassword", ""));

        mAuth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(view -> {
            if (TextUtils.isEmpty(txtInEmail.getText().toString()) || TextUtils.isEmpty(txtInPassword.getText().toString())) {
                Toast.makeText(LoginActivity.this, "Please fill both fields", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.createUserWithEmailAndPassword(txtInEmail.getText().toString(), txtInPassword.getText().toString())
                    .addOnSuccessListener(result -> {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
                        editor.putString("keyEmail",  txtInEmail.getText().toString());
                        editor.putString("keyPassword",  txtInPassword.getText().toString());
                        editor.apply();
                        Toast.makeText(LoginActivity.this, "Successfully signed up", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });

        btnSignIn.setOnClickListener(view -> {
            if (TextUtils.isEmpty(txtInEmail.getText().toString()) || TextUtils.isEmpty(txtInPassword.getText().toString())) {
                Toast.makeText(LoginActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.signInWithEmailAndPassword(txtInEmail.getText().toString(), txtInPassword.getText().toString())
                    .addOnSuccessListener(result -> {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
                        editor.putString("keyEmail",  txtInEmail.getText().toString());
                        editor.putString("keyPassword",  txtInPassword.getText().toString());
                        editor.apply();
                        Toast.makeText(LoginActivity.this, "Successfully signed in", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    })
                    .addOnFailureListener(e -> Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });

    }
}