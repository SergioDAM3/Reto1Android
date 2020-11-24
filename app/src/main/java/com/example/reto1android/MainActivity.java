package com.example.reto1android;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import connSQLite.SQLiteOpenHelper;
import connSqlServer.ConnSqlServer;
import funcionesJava.FuncionesDB;
import funcionesJava.FuncionesGenerales;

public class MainActivity extends AppCompatActivity {
    //Attribs
    protected String sqlServerConnUrl = FuncionesGenerales.cadConnSqlServer("10.0.2.2" , "1433" , "PruebaAndroid" , "JHERRERO-P\\JAVISQL" , "admin" , "Admin1234");
    protected ConnSqlServer adminSQLServer = null;

    //Generamos SQLite
    SQLiteOpenHelper adminSQLite = new SQLiteOpenHelper(this, "pda", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Creamos la cinexión a la db SQLite
        SQLiteDatabase db = adminSQLite.getWritableDatabase();

        //Actualizamos SQLite con la base de datos SQL Server central mediante un AsyncTask
        FuncionesDB.execAsyncTask(adminSQLServer , sqlServerConnUrl);
    }

    //Función para salir de la aplicación
    public void salirApp(View view){
        finish();
        System.out.println("El usuario Sale de la aplicación");
        System.exit(0);
    }
}