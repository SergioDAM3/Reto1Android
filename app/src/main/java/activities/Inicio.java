package activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.example.reto1android.R;

import connSQLite.SQLiteOpenHelper;
import connSqlServer.ConnSqlServer;
import funcionesJava.FuncionesDB;
import funcionesJava.FuncionesGenerales;

public class Inicio extends AppCompatActivity {



    //DB SQL SERVER Stuff
    protected String sqlServerConnUrl = FuncionesGenerales.cadConnSqlServer("10.0.2.2" , "1433" , "Restaurante" , "JHERRERO-P\\JAVISQL" , "admin" , "Admin1234");
    protected ConnSqlServer adminSQLServer = null;

    //Generamos SQLite
    connSQLite.SQLiteOpenHelper adminSQLite = new SQLiteOpenHelper(this, "pda", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        //Creamos la conexión a la db SQLite
        SQLiteDatabase db = adminSQLite.getWritableDatabase();
    }

    //Función para ir a la activity de tomar nota
    public void tomarNota(View view){
        //Vamos a la página Tomar Nota
        Intent i = new Intent(this, TomarNota.class );
        startActivity(i);
    }

    //Función para ir a la activity de tomar nota
    public void miHistorial(View view){
        //Vamos a la página Tomar Nota
        Intent i = new Intent(this, MiHistorial.class );
        startActivity(i);
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