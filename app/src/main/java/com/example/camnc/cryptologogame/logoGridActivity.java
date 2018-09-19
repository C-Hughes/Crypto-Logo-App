package com.example.camnc.cryptologogame;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.camnc.cryptologogame.Adapter.ImageAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class logoGridActivity extends AppCompatActivity {
    int level;
    GridView gridView;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo_grid);

        Intent i = getIntent();
        Bundle data = i.getExtras();
        level = data.getInt("LevelSelected");

        //Load the grid
        loadGrid();
    }

    @Override
    public void onResume(){
        super.onResume();
        loadGrid();
    }

    private void loadGrid(){
        //Show loading message...
        pd = new ProgressDialog(logoGridActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();

        gridView = (GridView) this.findViewById(R.id.gridView_logo);
        gridView.setAdapter(new ImageAdapter(this, level));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // set an Intent to Another Activity
                Intent intent = new Intent(logoGridActivity.this, guessLogoActivity.class);
                intent.putExtra("ImageID", position); // put image data in Intent
                intent.putExtra("LevelSelected", level); // put image data in Intent
                startActivity(intent); // start Intent
            }
        });

        //Remove loading message
        if (pd.isShowing()) pd.dismiss();
    }
}
