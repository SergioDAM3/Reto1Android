package com.example.reto1android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import connSqlServer.ConnSqlServer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void connDb(View view){
        ConnSqlServer conn = new ConnSqlServer();
        conn.connDb();
    }
}