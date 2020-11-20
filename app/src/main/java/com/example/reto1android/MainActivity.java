package com.example.reto1android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.sql.Connection;

import connSqlServer.ConnSqlServer;

public class MainActivity extends AppCompatActivity {
    //Attribs
    protected String server = "10.0.2.2";
    protected String port = "1433";
    protected String db = "PruebaAndroid";
    protected String instance = "JHERRERO-P\\JAVISQL";
    protected String user = "admin";
    protected String passwd = "Admin1234";
    protected String SqlServerConnUrl = "jdbc:jtds:sqlserver://"+this.server+":"+this.port+";databaseName="+this.db+";instance="+this.instance+";user="+this.user+";password="+this.passwd+"";
    protected ConnSqlServer admin = null;


    //AsyncTask de
    //ConnSqlServer admin = new ConnSqlServer(this.SqlServerConnUrl);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void execAsyncTask(View view){
        admin = (ConnSqlServer) new ConnSqlServer(this.SqlServerConnUrl , 1 , "select * from menus where id_menu = 1").execute();
        admin.execSelect("select * from menus where id_menu = 1");
        admin.closeConn();
        //admin = null;
    }
}