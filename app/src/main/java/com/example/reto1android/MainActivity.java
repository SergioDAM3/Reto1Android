package com.example.reto1android;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import connSQLite.SQLiteOpenHelper;
import connSqlServer.ConnSqlServer;
import funcionesGenerales.FuncionesGenerales;

public class MainActivity extends AppCompatActivity {
    //Attribs
    protected String SqlServerConnUrl = FuncionesGenerales.cadConnSqlServer("10.0.2.2" , "1433" , "PruebaAndroid" , "JHERRERO-P\\JAVISQL" , "admin" , "Admin1234");
    protected ConnSqlServer adminSQLServer = null;

    //Generamos SQLite
    SQLiteOpenHelper adminSQLite = new SQLiteOpenHelper(this, "pda", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Creamos la cinexión a la db
        SQLiteDatabase db = adminSQLite.getWritableDatabase();
    }

    public void execAsyncTask(View view){
        adminSQLServer = (ConnSqlServer) new ConnSqlServer(this.SqlServerConnUrl , 1 , "select * from menus where id_menu = 1").execute();
        //admin.execSelect("select * from menus where id_menu = 1");
        //admin.closeConn();
        adminSQLServer = null;
    }

    public void toast(View view){
        System.out.println("HOLA");
    }
}