package activities;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.reto1android.R;

import connSQLite.SQLiteOpenHelper;

public class TomarNota extends AppCompatActivity {

    //Attrib
    Spinner spMenus;
    LinearLayout linLytPlato1;
    LinearLayout linLytPlato2;
    LinearLayout linLytPlato3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomar_nota);

        //Generamos SQLite
        SQLiteOpenHelper adminSQLite = new SQLiteOpenHelper(this, "pda", null, 1);

        spMenus = (Spinner)findViewById(R.id.spMenus);

        //Preparamos el adaptador de menús
        //Preparamos el array de datos
        String[] infoMenus;

        //Creamos la conexión a la db SQLite
        SQLiteDatabase db = adminSQLite.getWritableDatabase();

        //Buscamos la info de los menus
        Cursor filaMenus = db.rawQuery("select nombre from menus where activo = 1 order by id_menu asc", null);
        int cantMenus = filaMenus.getCount();
        filaMenus.moveToFirst();
        if (cantMenus > 0){
            infoMenus = new String[cantMenus];
            infoMenus[0] = filaMenus.getString(0);
            for(int i = 1 ; i < cantMenus ; i ++){
                filaMenus.moveToNext();
                infoMenus[i] = filaMenus.getString(0);
            }
        }else{
            infoMenus = new String[]{"No hay menús existentes. Hable con el administrador para que meta menús nuevos."};
        }

        ArrayAdapter<String> adaptadorSpMenus = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_item , infoMenus);
        adaptadorSpMenus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMenus.setAdapter(adaptadorSpMenus);
    }

}