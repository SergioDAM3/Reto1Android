package activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.reto1android.R;

import connSQLite.SQLiteOpenHelper;
import connSqlServer.ConnSqlServer;
import funcionesJava.FuncionesDB;
import funcionesJava.FuncionesGenerales;

public class Inicio extends AppCompatActivity {

    static Inicio activityInicio;

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

        activityInicio = this;
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

    //Funcion para abrir la url de twitter
    public void twitterLink(View view){
        String url = "https://www.twitter.com/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    //Funcion para abrir la url de youtube
    public void youtubeLink(View view){
        String url = "https://www.youtube.com/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    //Funcion para abrir la url de instagram
    public void instagramLink(View view){
        String url = "https://www.instagram.com/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    //Funcion para abrir la url de facebook
    public void facebookLink(View view){
        String url = "https://www.facebook.com/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    //Funcion para abrir la url de facebook
    public void mapsLink(View view){
        String url = "https://www.google.com/maps/@43.3214803,-3.1256713,700m";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public static Inicio getInstance(){
        return   activityInicio;
    }
}