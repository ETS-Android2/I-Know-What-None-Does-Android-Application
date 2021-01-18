package com.project.iknowwhatnonedoes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Post extends AppCompatActivity implements DatabaseReference.CompletionListener {

    EditText exp, name, country, email;
    Button postbtn;
    String Name, Country, Email, Experience;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbRef;
    ConnectivityManager connectivityManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        exp = findViewById(R.id.postText);
        name = findViewById(R.id.postName);
        country = findViewById(R.id.postCountry);
        email = findViewById(R.id.postEmail);
        postbtn = findViewById(R.id.postButton);
        firebaseDatabase = FirebaseDatabase.getInstance();
        dbRef = firebaseDatabase.getReference("Experiences").push();
        connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
    }


    public void post(View view) {
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if(activeNetwork!=null)
        {
            Name = name.getText().toString();
            Country = country.getText().toString();
            Email = email.getText().toString();
            Experience = exp.getText().toString();
            if(!Name.equals("")&&!Country.equals("")&&!Email.equals("")&&!Experience.equals(""))
            {
                String ID = dbRef.push().toString();
                dbRef.child("Name").setValue(Name, this);
                dbRef.child("Experience").setValue(Experience, this);
                dbRef.child("Country").setValue(Country, this);
                dbRef.child("Email").setValue(Email, this);
                Intent intent = new Intent(Post.this, MainActivity.class);
                startActivity(intent);
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(Post.this);
                builder.setMessage("Please fill all the feilds")
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
    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
        Toast.makeText(Post.this, "Upload Successful", Toast.LENGTH_SHORT).show();
    }
}