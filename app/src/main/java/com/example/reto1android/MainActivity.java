package com.example.reto1android;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import connSQLite.SQLiteOpenHelper;
import connSqlServer.ConnSqlServer;
import funcionesJava.FuncionesDB;
import funcionesJava.FuncionesGenerales;

public class MainActivity extends AppCompatActivity {
    //Attribs
    EditText etNom;
    EditText etPasswd;
    Switch swRecuerdame;

    //DB Stuff
    protected String sqlServerConnUrl = FuncionesGenerales.cadConnSqlServer("10.0.2.2" , "1433" , "Restaurante" , "JHERRERO-P\\JAVISQL" , "admin" , "Admin1234");
    protected ConnSqlServer adminSQLServer = null;

    //Generamos SQLite
    SQLiteOpenHelper adminSQLite = new SQLiteOpenHelper(this, "pda", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Definimos los elementos
        etNom =(EditText)findViewById(R.id.etIdEmp);
        etPasswd =(EditText)findViewById(R.id.etPasswd);
        swRecuerdame =(Switch) findViewById(R.id.swRecuerdame);

        //Creamos la conexión a la db SQLite
        SQLiteDatabase db = adminSQLite.getWritableDatabase();

        //Actualizamos SQLite con la base de datos SQL Server central mediante un AsyncTask
        //FuncionesDB.actualizarSQLite(adminSQLServer , sqlServerConnUrl , db);
    }

    public void tempActualizarSqlServer(View view){

        try {
            //Creamos la conexión a la db SQLite
            SQLiteDatabase db = adminSQLite.getWritableDatabase();

            FuncionesDB.actualizarSqlServer(adminSQLServer , sqlServerConnUrl , db);
        }catch (Exception e){
            Log.e("Erro al actualizar SQL Server" , e.getMessage());
        }
    }


    //Función para entrar en la Aplicacion
    public void entrarApp(View view){
        if(swRecuerdame.isChecked()){
            System.out.println("HOLA");
        }
    }

    //Función para salir de la aplicación
    public void salirApp(View view){
        //Creamos la conexión a la db SQLite
        SQLiteDatabase db = adminSQLite.getWritableDatabase();

        //Subimos la información a SQL Server
        FuncionesDB.actualizarSqlServer(adminSQLServer , sqlServerConnUrl , db);

        //Cerramos ventana
        finish();

        //Mostramos mensaje
        System.out.println("El usuario Sale de la aplicación\n");

        //Cerramos app con exit status 0
        System.exit(0);
    }
}