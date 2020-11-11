package com.example.reto1android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.sql.Connection;

import connSqlServer.ConnSqlServer;

public class MainActivity extends AppCompatActivity {

    protected Connection conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //AsyncTask de
        ConnSqlServer admin = new ConnSqlServer("10.0.2.2" , "1433" , "PruebaAndroid" , "JHERRERO-P\\JAVISQL" ,  "admin" , "Admin1234");
        admin.execute();
    }

    public void sysoConn(View view){
        System.out.println("Hola2: " + conn);
    }

    public void connDb(View view){
        try {
            conn.close();
        }catch (Exception e){
            Log.e("Error en cierre de connexión a BD: " , ""+e);
        }
    }
}