package activities;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.reto1android.R;

import java.util.ArrayList;

import connSQLite.SQLiteOpenHelper;

public class MiHistorial extends AppCompatActivity {

    //Attrib
    private TextView tvPedTotales2;
    private ListView listHistorial;
    private ArrayList<String> contenido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_historial);

        listHistorial = findViewById(R.id.listHistorial);
        tvPedTotales2 = findViewById(R.id.tvPedTotales2);

        //SQLite
        connSQLite.SQLiteOpenHelper adminSQLite = new SQLiteOpenHelper(this, "pda", null, 1);

        //Creamos la conexión a la db SQLite
        SQLiteDatabase db = adminSQLite.getWritableDatabase();

        //Sacamos los datos del Historial
        Cursor filaHistorial = db.rawQuery("select id_pedido , fecha_pedido , costo_total from miHistorial" , null);
        tvPedTotales2.setText(""+filaHistorial.getCount());
        filaHistorial.moveToFirst();

        int contador = 1;
        contenido = new ArrayList<String>();

        contenido.add("Count \t - \t ID PED \t - \t Fecha PED \t - \t Coste PED");
        contenido.add(contador + "\t - \t" + filaHistorial.getString(0) + "\t - \t" + filaHistorial.getString(1) + "\t - \t" + filaHistorial.getString(2) + "€");

        try{

            while(filaHistorial.moveToNext()){
                contador ++;
                contenido.add(contador + "\t - \t" + filaHistorial.getString(0) + "\t - \t" + filaHistorial.getString(1) + "\t - \t" + filaHistorial.getString(2) + "€");
            }

            ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contenido);
            listHistorial.setAdapter(adaptador);

        }catch (Exception e){
            Log.e("Error a la hora de extraer la info de la tabla miHistorial (SQLite)" , e.getMessage());
        }
    }

    public void volverInicio(View view){
        finish();
    }
}