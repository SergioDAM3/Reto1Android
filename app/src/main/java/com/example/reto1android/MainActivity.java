package com.example.reto1android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Trace;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import activities.Inicio;
import connSQLite.SQLiteOpenHelper;
import connSqlServer.ConnSqlServer;
import funcionesJava.FuncionesDB;
import funcionesJava.FuncionesGenerales;

public class MainActivity extends AppCompatActivity {
    //Attribs
    EditText etIdEmp;
    EditText etPasswd;
    Switch swRecuerdame;
    static TextView tvMsgCargandoDB;
    static Button btnEntrar;

    //DB sQL SERVER Stuff
    protected String sqlServerConnUrl = FuncionesGenerales.cadConnSqlServer("192.168.43.15" , "1433" , "Restaurante" , "DESKTOP-MHOH6P7\\SQLEXPRES" , "admin" , "Admin1234");
    protected ConnSqlServer adminSQLServer = null;

    //Generamos SQLite
    SQLiteOpenHelper adminSQLite = new SQLiteOpenHelper(this, "pda", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Definimos los elementos
        etIdEmp =(EditText)findViewById(R.id.etIdEmp);
        etPasswd =(EditText)findViewById(R.id.etPasswd);
        swRecuerdame =(Switch) findViewById(R.id.swRecuerdame);
        btnEntrar = (Button)findViewById(R.id.btnEntrar);
        tvMsgCargandoDB = (TextView)findViewById(R.id.tvMsgCargandoDB);

        //Creamos la conexión a la db SQLite
        SQLiteDatabase db = adminSQLite.getWritableDatabase();

        //Actualizamos SQLite con la base de datos SQL Server central mediante un AsyncTask
        FuncionesDB.actualizarSQLite(adminSQLServer , sqlServerConnUrl , db);

        //Mstramos el Id Empleado recordado
        Cursor filaDeviceInfo = db.rawQuery("select idEmpRecordado from thisDeviceInfo", null);
        filaDeviceInfo.moveToFirst();
        filaDeviceInfo.getCount();
        if(filaDeviceInfo.getCount() != 0 && filaDeviceInfo.getString(0) != ""){
            etIdEmp.setText(filaDeviceInfo.getString(0));
        }else{
        }
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
        //Creamos la conexión a la db SQLite
        SQLiteDatabase db = adminSQLite.getWritableDatabase();

        //Comprobamos que el idEmp y la Contraseña son correctos
        Cursor fila = db.rawQuery("select id_emp , password , permiso_chat from empleados order by id_emp asc", null);
        fila.moveToFirst();

        //Variable de control
        boolean encontrado = false;

        do{
            //System.out.println("1-" + this.etIdEmp.getText().toString() + "-" + fila.getString(0));
            //System.out.println("2-" + this.etPasswd.getText().toString() + "-" + fila.getString(1));

            if((this.etIdEmp.getText().toString().equals(fila.getString(0))) && (this.etPasswd.getText().toString().equals(fila.getString(1)))){
                //Marcamos que lo hemos encontrado
                encontrado = true;

                if(this.swRecuerdame.isChecked()){

                    System.out.println("Usuario encontrado. Recuérdame:ON");
                    try {
                        //Hacemos la update en la BD
                        ContentValues registroUpdate = new ContentValues();
                        registroUpdate.put("idEmpRecordado", this.etIdEmp.getText().toString());
                        db.update("thisDeviceInfo", registroUpdate, "", null);
                    }catch (Exception e){
                        Log.e("Error al actualizar la ID del Empleado recordado" , e.getMessage());
                    }
                }else{
                    System.out.println("Usuario encontrado. Recuérdame:OFF");
                    //Hacemos la update en la BD
                    ContentValues registroUpdate = new ContentValues();
                    registroUpdate.put("idEmpRecordado", "");
                    db.update("thisDeviceInfo", registroUpdate, "", null);
                }

                //Cerramos la activity de Login
                finish();

                //Vamos a la página Inicio
                Intent i = new Intent(this, Inicio.class );
                startActivity(i);

                break;
            }
        }while(fila.moveToNext());

        if(encontrado != true){
            Toast.makeText(this , "Usuario no encontrado." , Toast.LENGTH_LONG).show();
            System.out.println("Usuario no encontrado." );
            this.etPasswd.setText("");
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

    //Para habilitar btnEnabled
    public static void SQLiteActualizado(boolean result){
        if(result){
            tvMsgCargandoDB.setText("Datos recibidos correctamente.");
            btnEntrar.setVisibility(View.VISIBLE);
            btnEntrar.setEnabled(true);
        }
    }
}