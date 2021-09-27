package com.project.iknowwhatnonedoes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.DomainCombiner;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ValueEventListener {

    ListView listView;
    Toolbar toolbar;
    ArrayAdapter<Upload> adapter;
    ArrayList<Upload> arrayList;
    FirebaseAnalytics firebaseAnalytics;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String name, country, exp, date;
    ConnectivityManager connectivityManager;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list);
        toolbar = findViewById(R.id.toolbar);
        toolbar.getOverflowIcon().setColorFilter(getResources().getColor(R.color.accent_color), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Experiences");
        sharedPreferences = getSharedPreferences("IKWND", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
        arrayList = new ArrayList<Upload>();
        name = "";
        country = "";
        exp = "";
        date = "";
        listView.setAdapter(adapter);
        connectivityManager = (ConnectivityManager)getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if(activeNetwork!=null)
            onStart();
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            View v = getLayoutInflater().inflate(R.layout.noconnection, null);
            builder.setView(v)
                    .setPositiveButton("OK", null);
            builder.create().show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.post:
            {
                open();
                break;
            }
            case R.id.signout:
            {
                signout();
                break;
            }
            case R.id.settings:
            {
                settings();
                break;
            }
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(this);
    }

    public void open() {
        Intent intent = new Intent(MainActivity.this, Post.class);
        startActivity(intent);
    }
    public void settings(){
        Intent intent = new Intent(MainActivity.this, Settings.class);
        startActivity(intent);
    }

    public void signout() {
        editor.remove("NAME");
        editor.remove("COUNTRY");
        editor.remove("EMAIL");
        editor.apply();

        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        arrayList.clear();
        for(DataSnapshot dataSnapshot : snapshot.getChildren())
        {
            Upload data = new Upload();
            data.setUID(dataSnapshot.getKey());
            for (DataSnapshot snapshot1 : dataSnapshot.getChildren())
            {
                switch (snapshot1.getKey()){
                    case "name": data.setName(snapshot1.getValue().toString()); break;
                    case "experience": data.setExperience(snapshot1.getValue().toString()); break;
                    case "country": data.setCountry(snapshot1.getValue().toString()); break;
                    case "email": data.setEmail(snapshot1.getValue().toString()); break;
                    case "date": data.setDate(snapshot1.getValue().toString()); break;
                }
            }
            arrayList.add(data);
        }
        try {
            adapter = new ArrayAdapter<Upload>(MainActivity.this, R.layout.view, arrayList){
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    if (convertView == null){
                        convertView = getLayoutInflater().inflate(R.layout.view, null);
                    }
                    TextView name = convertView.findViewById(R.id.name);
                    TextView country = convertView.findViewById(R.id.country);
                    TextView experience = convertView.findViewById(R.id.exp);
                    TextView date = convertView.findViewById(R.id.date);
                    name.setText(arrayList.get(position).getName());
                    country.setText(arrayList.get(position).getCountry());
                    experience.setText(arrayList.get(position).getExperience());
                    date.setText(arrayList.get(position).getDate());
                    return convertView;
                }
            };
        }
        catch (Exception e)
        {
            Toast.makeText(MainActivity.this, "Contact the Developer", Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Toast.makeText(MainActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
    }
}