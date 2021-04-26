package com.project.iknowwhatnonedoes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
    ArrayAdapter adapter;
    ArrayList<String> arrayList;
    FirebaseAnalytics firebaseAnalytics;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String name, country, exp;
    ConnectivityManager connectivityManager;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Experiences");
        sharedPreferences = getSharedPreferences("IKWND", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        name = "";
        country = "";
        exp = "";
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
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(this);
        arrayList.clear();
    }

    public void open() {
        Intent intent = new Intent(MainActivity.this, Post.class);
        startActivity(intent);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        arrayList.clear();
        for(DataSnapshot dataSnapshot : snapshot.getChildren())
        {
            loadData(dataSnapshot);
//            Upload data = dataSnapshot.getValue(Upload.class);
//            arrayList.add(data);
        }
        // load();
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    void loadData(DataSnapshot snapshot)
    {
        String data = "";
        for(DataSnapshot dataSnapshot : snapshot.getChildren())
        {
            String key = dataSnapshot.getKey();
            assert key != null;
            switch (key)
            {
                case "Name":
                {
                    name = dataSnapshot.getValue().toString();
                    break;
                }
                case "Experience":
                {
                    exp = dataSnapshot.getValue().toString();
                    break;
                }
                case "Country":
                {
                    country = dataSnapshot.getValue().toString();
                    break;
                }
            }
            data = name +"\t" + country + "\n \n" + exp;
            arrayList.add(data);
        }
    }
//    void load()
//    {
//        adapter = new ArrayAdapter<Upload>(MainActivity.this, R.layout.view, arrayList){
//            @SuppressLint("ViewHolder")
//            @NonNull
//            @Override
//            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//                Upload data = arrayList.get(position);
//                convertView = getLayoutInflater().inflate(R.layout.view, null);
//                TextView name = convertView.findViewById(R.id.name);
//                TextView place = convertView.findViewById(R.id.country);
//                TextView exp = convertView.findViewById(R.id.exp);
//                name.setText(data.getName());
//                place.setText(data.getCountry());
//                exp.setText(data.getExperience());
//                return convertView;
//            }
//        };
//        adapter.notifyDataSetChanged();
//        listView.setAdapter(adapter);
//    }

    public void signout() {
        editor.remove("NAME");
        editor.remove("COUNTRY");
        editor.remove("EMAIL");
        editor.apply();

        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
    }
}