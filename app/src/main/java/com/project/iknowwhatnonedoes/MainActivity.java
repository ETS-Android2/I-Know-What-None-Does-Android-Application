package com.project.iknowwhatnonedoes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ValueEventListener {

    ListView listView;
    ArrayAdapter<String> adapter;
    ArrayList<String> arrayList;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String name, country, exp;
    ConnectivityManager connectivityManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Experiences");
        arrayList = new ArrayList<String>();
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
    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(this);
    }

    public void open(View view) {
        Intent intent = new Intent(MainActivity.this, Post.class);
        startActivity(intent);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        arrayList.clear();
        for(DataSnapshot dataSnapshot : snapshot.getChildren())
        {
            loadData(dataSnapshot);
        }
        load();
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
        }
        arrayList.add(data);
    }
    void load()
    {
        adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.view, arrayList){
            @SuppressLint("ViewHolder")
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                convertView = getLayoutInflater().inflate(R.layout.view, null);
                TextView textView = convertView.findViewById(R.id.view);
                String data = arrayList.get(position);
                textView.setText(data);
                return convertView;
            }
        };
        listView.setAdapter(adapter);
    }
}