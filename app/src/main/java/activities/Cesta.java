package activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.reto1android.MainActivity;
import com.example.reto1android.R;

import java.util.ArrayList;

import connSQLite.SQLiteOpenHelper;
import connSqlServer.ConnSqlServer;
import funcionesJava.FuncionesDB;
import funcionesJava.FuncionesGenerales;

public class Cesta extends AppCompatActivity {

    static Cesta activityCesta;

    //Creamos la variable tipo ListView
    private ListView listPlato1;
    private TextView tvCostoTotal;
    private static TextView tvEstadoSubida;


    //DB SQL SERVER Stuff
    public String sqlServerConnUrl = MainActivity.sqlServerConnUrl;
    protected ConnSqlServer adminSQLServer = null;

    //Generamos SQLite
    connSQLite.SQLiteOpenHelper adminSQLite = new SQLiteOpenHelper(this, "pda", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cesta);

        //Creamos la conexión a la db SQLite
        SQLiteDatabase db = adminSQLite.getWritableDatabase();

        activityCesta = this;

        //Asignamos listas
        listPlato1 = findViewById(R.id.listPlato1);
        tvCostoTotal = findViewById(R.id.tvCostoTotal);
        tvEstadoSubida = findViewById(R.id.tvEstadoSubida);

        //Actualizamos el costo total
        this.actualizarCostoTotal();

        //Actualizamos listas
        System.out.println("Actualizamso lista");
        this.actualizarListas();
        System.out.println("Fin actualizar lista");
    }

    public Cesta getInstance(){
        return   activityCesta;
    }

    public void volverInicio(View view){
        TomarNota.getInstance().finish();
        finish();
    }

    private void actualizarListas(){
        //Creamos la conexión a la db SQLite
        SQLiteDatabase db = adminSQLite.getWritableDatabase();

        //Variagles gen
        String texto;
        int contador = 0;

        try{
            //Creamos los arraylists
            ArrayList <String> contenidoPlato1 = new ArrayList<String>();
            ArrayList <String> contenidoPlato2 = new ArrayList<String>();
            ArrayList <String> contenidoPlato3 = new ArrayList<String>();

            //Metemos los datos
            //Primeros
            contenidoPlato1.add("Primeros:");
            Cursor filaLinPed1 = db.rawQuery("select p.nombre , p.p_v_p , m.nombre from linea_pedidos lp join productos p on (lp.id_prod = p.id_prod) join cat_prod cp on (p.id_categoria = cp.id_categoria) join menus m on (p.id_menu = m.id_menu) where cp.tipo=1 order by lp.id_lineaped asc" , null);
            filaLinPed1.moveToFirst();
            if (filaLinPed1.getCount() > 0){
                contenidoPlato1.add("\t" + contador + " - Menú: " + filaLinPed1.getString(2) + "\t\tPlato: " + filaLinPed1.getString(0) + "\t\t" + filaLinPed1.getString(1) + "€");
                for(int i = 1 ; i < filaLinPed1.getCount() ; i ++){
                    filaLinPed1.moveToNext();
                    contador++;
                    contenidoPlato1.add("\t" + contador + " - Menú: " + filaLinPed1.getString(2) + "\t\tPlato: " + filaLinPed1.getString(0) + "\t\t" + filaLinPed1.getString(1) + "€");
                }
            }else{
                contenidoPlato1.add("No hay primer plato");
            }

            contador = 0;

            //Segundos
            contenidoPlato1.add("Segundos:");
            Cursor filaLinPed2 = db.rawQuery("select p.nombre , p.p_v_p , m.nombre from linea_pedidos lp join productos p on (lp.id_prod = p.id_prod) join cat_prod cp on (p.id_categoria = cp.id_categoria) join menus m on (p.id_menu = m.id_menu) where cp.tipo=2 order by lp.id_lineaped asc" , null);
            filaLinPed2.moveToFirst();
            if (filaLinPed2.getCount() > 0){
                contenidoPlato1.add("\t" + contador + " - Menú: " + filaLinPed2.getString(2) + "\t\tPlato: " + filaLinPed2.getString(0) + "\t\t" + filaLinPed2.getString(1) + "€");
                for(int i = 1 ; i < filaLinPed2.getCount() ; i ++){
                    filaLinPed2.moveToNext();
                    contador++;
                    contenidoPlato1.add("\t" + contador + " - Menú: " + filaLinPed2.getString(2) + "\t\tPlato: " + filaLinPed2.getString(0) + "\t\t" + filaLinPed2.getString(1) + "€");
                }
            }else{
                contenidoPlato1.add("No hay primer plato");
            }

            contador = 0;
            //Postres
            contenidoPlato1.add("Postres:");
            Cursor filaLinPed3 = db.rawQuery("select p.nombre , p.p_v_p , m.nombre from linea_pedidos lp join productos p on (lp.id_prod = p.id_prod) join cat_prod cp on (p.id_categoria = cp.id_categoria) join menus m on (p.id_menu = m.id_menu) where cp.tipo=3 order by lp.id_lineaped asc" , null);
            filaLinPed3.moveToFirst();
            if (filaLinPed3.getCount() > 0){
                contenidoPlato1.add("\t" + contador + " - Menú: " + filaLinPed3.getString(2) + "\t\tPlato: " + filaLinPed3.getString(0) + "\t\t" + filaLinPed3.getString(1) + "€");
                for(int i = 1 ; i < filaLinPed3.getCount() ; i ++){
                    filaLinPed3.moveToNext();
                    contador++;
                    contenidoPlato1.add("\t" + contador + " - Menú: " + filaLinPed3.getString(2) + "\t\tPlato: " + filaLinPed3.getString(0) + "\t\t" + filaLinPed3.getString(1) + "€");
                }
            }else{
                contenidoPlato1.add("No hay primer plato");
            }

            //Liberamos hueco
            contenidoPlato1.add("");
            contenidoPlato1.add("");

            //Preparamos los adaptadores
            ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contenidoPlato1);

            //Asignamos los adaptadores a las listas
            listPlato1.setAdapter(adaptador);

        }catch (Exception e){
            Log.e("Error a la hora de actulizar listas" , e.getMessage());
        }
    }

    public void vaciarCesta(View view){
        //Creamos la conexión a la db SQLite
        SQLiteDatabase db = adminSQLite.getWritableDatabase();

        //Vaciamos la cesta
        db.execSQL("delete from linea_pedidos");

        //Actualizamos el listView
        this.actualizarListas();

        //Actualizamos el costo total
        this.actualizarCostoTotal();
    }

    public void  actualizarCostoTotal(){
        //Creamos la conexión a la db SQLite
        SQLiteDatabase db = adminSQLite.getWritableDatabase();
        try {
            //Recogemos el costo total de la activity Tomar nota
            double costoTotal = 0.0;
            Cursor filaCostoTotal = db.rawQuery("select sum(coste) from linea_pedidos" , null);
            if(filaCostoTotal.getCount() > 0){
                filaCostoTotal.moveToFirst();
                costoTotal = Double.valueOf(filaCostoTotal.getString(0));
            }

            tvCostoTotal.setText(costoTotal+"");
        }catch (Exception e){
            System.out.println("Error al actualizar costo total (Se ha puesto el valor 0.0 al valor total)" + e.getMessage());

            double costoTotal = 0.0;

            tvCostoTotal.setText(costoTotal+"");
        }
    }

    public  void confirmarCesta(View view){
        //Creamos la conexión a la db SQLite
        SQLiteDatabase db = adminSQLite.getWritableDatabase();

        //Subimos los registros de pedidos y lineapedidos a SQL Server
        FuncionesDB.actualizarCesta(adminSQLServer , sqlServerConnUrl , db);
    }

    public static void actualizandoCesta(boolean _result){
        if(_result){
            tvEstadoSubida.setVisibility(View.VISIBLE);
            tvEstadoSubida.setText("Confirmando Cesta ...");
        }else{
            tvEstadoSubida.setText("Cesta confirmada correctamente.");
        }
    }
}