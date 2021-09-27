package com.project.iknowwhatnonedoes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;

public class Post extends AppCompatActivity {

    EditText exp, name, country, email;
    Button postbtn;
    String Name, Country, Email, Experience, Date;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbRef;
    ConnectivityManager connectivityManager;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        exp = findViewById(R.id.postText);
        name = findViewById(R.id.postName);
        country = findViewById(R.id.postCountry);
        email = findViewById(R.id.postEmail);
        postbtn = findViewById(R.id.postButton);
        exp.requestFocus();
        firebaseDatabase = FirebaseDatabase.getInstance();
        dbRef = firebaseDatabase.getReference("Experiences").push();
        connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        sharedPreferences = getSharedPreferences("IKWND", MODE_PRIVATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(sharedPreferences.contains("NAME")&&sharedPreferences.contains("EMAIL")&&sharedPreferences.contains("COUNTRY"))
        {
            name.setText(sharedPreferences.getString("NAME", ""));
            country.setText(sharedPreferences.getString("COUNTRY", ""));
            email.setText(sharedPreferences.getString("EMAIL", ""));
        }
    }

    public void post(View view) {
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if(activeNetwork!=null)
        {
            Name = name.getText().toString();
            Country = country.getText().toString();
            Email = email.getText().toString();
            Experience = exp.getText().toString();
            Date = String.valueOf(LocalDate.now());
            if(!Name.equals("")&&!Country.equals("")&&!Email.equals("")&&!Experience.equals(""))
            {
                Upload data = new Upload();
                data.setName(Name);
                data.setCountry(Country);
                data.setEmail(Email);
                data.setExperience(Experience);
                data.setDate(Date);
                try {
                    dbRef.setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isComplete() && task.isSuccessful())
                                Toast.makeText(Post.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Intent intent = new Intent(Post.this, MainActivity.class);
                    startActivity(intent);
                }
                catch (Exception e)
                {
                    System.out.println(e);
                    Toast.makeText(Post.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(Post.this);
                builder.setMessage("Please fill all the fields")
                        .setPositiveButton("Ok", null);
                builder.create().show();
            }
        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(Post.this);
            View v = getLayoutInflater().inflate(R.layout.noconnection, null);
            builder.setView(v)
                    .setPositiveButton("OK", null);
            builder.create().show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Post.this, MainActivity.class);
        startActivity(intent);
    }
}