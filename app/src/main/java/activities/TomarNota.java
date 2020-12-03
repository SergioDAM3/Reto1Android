package activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reto1android.R;

import java.time.LocalDate;

import connSQLite.SQLiteOpenHelper;
import funcionesJava.FuncionesDB;

public class TomarNota extends AppCompatActivity {

    //Attrib
    Spinner spMenus;
    Spinner spPlato1;
    Spinner spPlato2;
    Spinner spPlato3;
    LinearLayout linLytSelectedMenu;
    TextView lblMenuSelec;
    LinearLayout linLytPlato1;
    LinearLayout linLytPlato2;
    LinearLayout linLytPlato3;

    TableLayout tblLytResult;
    TextView lblResultPlt1;
    TextView lblResultPlt2;
    TextView lblResultPlt3;
    TextView lblSumPrecioTotal;

    static TomarNota activityTomarNota;

    //Id del menu y platos seleccionados
    int idMenuSelec;
    int[] idPlatosSelected = new int[3];
    double[] precioProd = new double[3];
    int[] cantProd = new int[3];
    double costoTotal = 0;

    //Generamos SQLite
    SQLiteOpenHelper adminSQLite = new SQLiteOpenHelper(this, "pda", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomar_nota);

        spMenus = findViewById(R.id.spMenus);
        spPlato1 = findViewById(R.id.spPlato1);
        spPlato2 = findViewById(R.id.spPlato2);
        spPlato3 = findViewById(R.id.spPlato3);

        linLytSelectedMenu = findViewById(R.id.linLytSelectedMenu);
        lblMenuSelec = findViewById(R.id.lblMenuSelec);
        linLytPlato1 = findViewById(R.id.linLytPlato1);
        linLytPlato2 = findViewById(R.id.linLytPlato2);
        linLytPlato3 = findViewById(R.id.linLytPlato3);
        tblLytResult = findViewById(R.id.tblLytResult);
        lblResultPlt1 = findViewById(R.id.lblResultPlt1);
        lblResultPlt2 = findViewById(R.id.lblResultPlt2);
        lblResultPlt3 = findViewById(R.id.lblResultPlt3);
        lblSumPrecioTotal = findViewById(R.id.lblSumPrecioTotal);

        activityTomarNota = this;


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

    public void actualizarPlatos(View view){
        //Actualizamos los platos dependiendo del menú que esté seleccionado
        try{
            //Creamos la conexión a la db SQLite
            SQLiteDatabase db = adminSQLite.getWritableDatabase();

            //Sacamos el id del item seleccionado
            Cursor filaMenuSelec = db.rawQuery("select id_menu , nombre from menus where nombre = '" + spMenus.getSelectedItem() + "' order by id_menu asc", null);
            filaMenuSelec.moveToFirst();

            //Guardamos indice en SQLite del menú para futuras búsquedas
            idMenuSelec = filaMenuSelec.getInt(0);

            //Preparamos la UI
            lblMenuSelec.setText(filaMenuSelec.getString(1));
            linLytSelectedMenu.setVisibility(View.VISIBLE);
            linLytPlato1.setVisibility(View.VISIBLE);
            linLytPlato2.setVisibility(View.VISIBLE);
            linLytPlato3.setVisibility(View.VISIBLE);
            tblLytResult.setVisibility(View.VISIBLE);

            //Mostramos mensaje
            Toast.makeText(this,"Menú seleccionado" , Toast.LENGTH_SHORT).show();

            //Actualizamos los spinners de platos
            //Primero
            ArrayAdapter<String> adaptadorSpPlato1 = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_item , FuncionesDB.sacarPlatosMenu(this.idMenuSelec , 1 , this.adminSQLite));
            adaptadorSpPlato1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spPlato1.setAdapter(adaptadorSpPlato1);
            //Segundo
            ArrayAdapter<String> adaptadorSpPlato2 = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_item , FuncionesDB.sacarPlatosMenu(this.idMenuSelec , 2 , this.adminSQLite));
            adaptadorSpPlato2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spPlato2.setAdapter(adaptadorSpPlato2);
            //Postre
            ArrayAdapter<String> adaptadorSpPlato3 = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_item , FuncionesDB.sacarPlatosMenu(this.idMenuSelec , 3 , this.adminSQLite));
            adaptadorSpPlato3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spPlato3.setAdapter(adaptadorSpPlato3);
        }catch (Exception e){
            Log.e("Error al actualizar platos" , e.getMessage());
        }
    }

    public void seleccionarPlato(View view){
        //Al hacer clic en el botón correspondiente, marcamos como seleccionado el plato del spinner
        try {
            Button btnSelec = findViewById(view.getId()) ;
            switch (btnSelec.getId()){
                case R.id.btnPlato1:
                    String spItem1 = spPlato1.getSelectedItem().toString();
                    String[] nomPlato1 = spItem1.split(",");
                    String[] precio1 = nomPlato1[1].split("EUR");
                    this.precioProd[0] = Double.parseDouble(precio1[0]);
                    this.idPlatosSelected[0] = FuncionesDB.sacarIdPlato(this.idMenuSelec , 1 , nomPlato1[0] , this.adminSQLite);
                    this.cantProd[0] = FuncionesDB.cantProdDisp(this.idPlatosSelected[0] , this.adminSQLite);
                    this.lblResultPlt1.setText(nomPlato1[0] + "(" + this.cantProd[0] + ")");

                    break;
                case R.id.btnPlato2:
                    String spItem2 = spPlato2.getSelectedItem().toString();
                    String[] nomPlato2 = spItem2.split(",");
                    String[] precio2 = nomPlato2[1].split("EUR");
                    this.precioProd[1] = Double.parseDouble(precio2[0]);
                    this.idPlatosSelected[1] = FuncionesDB.sacarIdPlato(this.idMenuSelec , 2 , nomPlato2[0] , this.adminSQLite);
                    this.cantProd[1] = FuncionesDB.cantProdDisp(this.idPlatosSelected[1] , this.adminSQLite);
                    this.lblResultPlt2.setText(nomPlato2[0] + "(" + this.cantProd[1] + ")");
                    break;
                case R.id.btnPlato3:
                    String spItem3 = spPlato3.getSelectedItem().toString();
                    String[] nomPlato3 = spItem3.split(",");
                    String[] precio3 = nomPlato3[1].split("EUR");
                    this.precioProd[2] = Double.parseDouble(precio3[0]);
                    this.idPlatosSelected[2] = FuncionesDB.sacarIdPlato(this.idMenuSelec , 3 , nomPlato3[0] , this.adminSQLite);
                    this.cantProd[2] = FuncionesDB.cantProdDisp(this.idPlatosSelected[2] , this.adminSQLite);
                    this.lblResultPlt3.setText(nomPlato3[0] + "(" + this.cantProd[2] + ")");
                    break;
            }

            //Comprobamos el precio
            if (this.cantProd[0] > 0 && this.cantProd[1] > 0 && this.cantProd[2] > 0){
                lblSumPrecioTotal.setText(  "" + (this.precioProd[0] + this.precioProd[1] + this.precioProd[2]) + "€" );
                this.costoTotal = this.precioProd[0] + this.precioProd[1] + this.precioProd[2];
            }
        }catch (Exception e){
            Log.e("Error al asignar plato" , e.getMessage());
        }

    }

    //Función para añadir los platos a la cesta
    public void anadirACesta(View view){
        //Creamos la conexión a la db SQLite
        SQLiteDatabase db = this.adminSQLite.getWritableDatabase();

        int idPedido = -1;

        LocalDate fechaHoy = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            fechaHoy = LocalDate.now();
        }

        try{
            //Comprobamos que haya un pedido creado, en caso contrario, creamos uno nuevo
            Cursor filaPedidos = db.rawQuery("select id_pedido from pedidos", null);
            System.out.println("Buscamos el últipo medido: select id_pedido from pedidos");
            if (filaPedidos.getCount() > 0){
                //Sacamos la info del pedido
                filaPedidos.moveToFirst();
                idPedido = filaPedidos.getInt(0);
            }else {
                //Sacamos la id del dispositivo actual
                Cursor filaIdDisp = db.rawQuery("select id_disp from thisDeviceInfo", null);
                System.out.println("Sacamos el id del dispositivo actual: select id_disp from thisDeviceInfo");
                filaIdDisp.moveToFirst();

                //Buscamos registro de caja
                int idCaja;
                Cursor filaCajas = db.rawQuery("select id_reg from reg_caja",null);
                if(filaCajas.getCount() > 0){
                    filaCajas.moveToLast();
                    idCaja = filaCajas.getInt(0);
                }else{
                    db.execSQL("insert into reg_caja (id_reg , descripcion , fecha , cantidad_total , tipo , cantidad_cambio) values (1,'Pedido Cliente','"+fechaHoy+"',-1,1,"+this.costoTotal+")");
                    System.out.println("Insertamos caja: insert into reg_caja (id_reg , descripcion , fecha , cantidad_total , tipo , cantidad_cambio) values (1,'Pedido Cliente','"+fechaHoy+"',1,1,"+this.costoTotal+")");
                    idCaja = 1;
                }

                //Metemos un pedido nuevo
                db.execSQL("insert into pedidos (id_pedido , fecha_pedido , costo_total , id_disp , id_reg) values (1 , '"+fechaHoy+"' , "+ this.costoTotal + " , " + filaIdDisp.getString(0) + ","+idCaja+")");
                System.out.println("Insertamos pedido nuevo: insert into pedidos (id_pedido , fecha_pedido , costo_total , id_disp , id_reg) values (1 , '"+fechaHoy+"' , "+ this.costoTotal + " , " + filaIdDisp.getString(0) + ","+idCaja+")");
                idPedido = 1;
            }

            //Añadimos los platos a lineapedidos asignandolo al pedido
            filaPedidos = db.rawQuery("select id_pedido from pedidos", null);
            filaPedidos.moveToFirst();
            //Plato 1
            db.execSQL("insert into linea_pedidos ( coste , cantidad , id_prod , id_pedido) values ("+precioProd[0]+",1,"+idPlatosSelected[0]+","+idPedido+")");
            System.out.println("Insertamos linea_epdidos: " +"insert into linea_pedidos ( coste , cantidad , id_prod , id_pedido) values ("+precioProd[0]+",1,"+idPlatosSelected[0]+","+idPedido+")");
            //Plato 2
            db.execSQL("insert into linea_pedidos ( coste , cantidad , id_prod , id_pedido) values ("+precioProd[1]+",1,"+idPlatosSelected[1]+","+idPedido+")");
            //Plato 3
            db.execSQL("insert into linea_pedidos ( coste , cantidad , id_prod , id_pedido) values ("+precioProd[2]+",1,"+idPlatosSelected[2]+","+idPedido+")");

            Toast.makeText(this , "Pedido añadido Correctamente." , Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            Log.e("Error al añadir Cesta" , e.getMessage());

            Toast.makeText(this , "Error al añadir el pedido." , Toast.LENGTH_SHORT).show();
        }
    }

    public  void irCesta(View view){
        //Vamos a la página Cesta
        Intent i = new Intent(this, Cesta.class );
        i.putExtra("costoTotal" , this.costoTotal+"");
        startActivity(i);
    }

    public static TomarNota getInstance(){
        return   activityTomarNota;
    }
}