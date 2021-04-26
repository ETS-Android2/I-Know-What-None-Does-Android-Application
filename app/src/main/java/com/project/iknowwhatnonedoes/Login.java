package com.project.iknowwhatnonedoes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Login extends AppCompatActivity {

    EditText name, country, email;
    String Name, Country, Email;
    SharedPreferences sharedPreference;
    SharedPreferences.Editor editor;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        name = findViewById(R.id.nametxt);
        country = findViewById(R.id.countrytxt);
        email = findViewById(R.id.emailtxt);

        sharedPreference = getSharedPreferences("IKWND", MODE_PRIVATE);
        editor = sharedPreference.edit();
        editor.apply();

        connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo==null)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(R.layout.noconnection)
                    .setPositiveButton("Ok", null)
                    .setCancelable(false);
            builder.create().show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(sharedPreference.contains("NAME")&&sharedPreference.contains("EMAIL")&&sharedPreference.contains("COUNTRY"))
        {
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void enter(View view) {
        Name = name.getText().toString();
        Country = country.getText().toString();
        Email = email.getText().toString();

        editor.putString("NAME", Name);
        editor.putString("COUNTRY", Country);
        editor.putString("EMAIL", Email);
        editor.apply();

        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
    }
}