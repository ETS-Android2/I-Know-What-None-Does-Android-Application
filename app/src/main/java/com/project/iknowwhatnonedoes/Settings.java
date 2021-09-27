package com.project.iknowwhatnonedoes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;



public class Settings extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        options = findViewById(R.id.options);
        options.setBackgroundColor(getResources().getColor(R.color.accent_color));
        options.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        switch (position)
        {
            case 0:
            {
                about();
                break;
            }
            case 1:
            {
                how_to_use();
                break;
            }
        }
    }

    private void how_to_use()
    {
        View usage = getLayoutInflater().inflate(R.layout.how_to_use, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
        builder.setView(usage)
                .setCancelable(true)
                .setNegativeButton("Cancel", null);
        builder.create().show();
    }

    private void about()
    {
        View about = getLayoutInflater().inflate(R.layout.about, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
        builder.setView(about)
                .setCancelable(true)
                .setNegativeButton("Cancel", null);
        builder.create().show();
    }
}