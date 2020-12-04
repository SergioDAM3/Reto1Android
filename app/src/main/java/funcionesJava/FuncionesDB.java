package funcionesJava;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import connSQLite.SQLiteOpenHelper;
import connSqlServer.ConnSqlServer;

public class FuncionesDB {
    ///////////////////////////////////////////////////////////////////////////////////////////////////////-

    //EN ESTA CLASE SE GUARDARÁN FUNCIONES QUE SERÁN USADAS POR OTROS OBJETOS

    ///////////////////////////////////////////////////////////////////////////////////////////////////////-

    //Función para actualizar SQL Server con la SQLite
    public static void actualizarSqlServer(ConnSqlServer adminSQLServer , String _sqlServerConnUrl , SQLiteDatabase _db){
        //Ejecutamos el AsyncTask
        adminSQLServer = (ConnSqlServer) new ConnSqlServer(_sqlServerConnUrl , 0 , _db).execute();
        adminSQLServer = null;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////-

    //Función para actualizar SQLite con SQL Server
    public static boolean actualizarSQLite(ConnSqlServer adminSQLServer , String _sqlServerConnUrl , SQLiteDatabase _db){
        try{
            //Ejecutamos el AsyncTask para recoger la información
            adminSQLServer = (ConnSqlServer) new ConnSqlServer(_sqlServerConnUrl , 1 , _db).execute();
            adminSQLServer = null;
            return true;
        }catch (Exception e){
            Log.e("Error al actualizar SQLite" , e.getMessage());
            return false;
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////-

    //Función para ejecutar select
    //TODAVÍA NO IMPLEMENTADO
    public static void execAsyncTask(ConnSqlServer adminSQLServer, int _tipo , String _sqlServerConnUrl){
        //Ejecutamos el AsyncTask
        adminSQLServer = (ConnSqlServer) new ConnSqlServer(_sqlServerConnUrl , _tipo , "select * from menus where id_menu = 1").execute();
        adminSQLServer = null;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////-

    //Función para borrar todos los registros de la bd SQLite
    public static void borrarTodoSQLite(SQLiteDatabase _db){
        _db.execSQL("delete from menus");
        _db.execSQL("delete from cat_prod");
        _db.execSQL("delete from productos");
        _db.execSQL("delete from proveedores");
        _db.execSQL("delete from incidencias_proveedores");
        _db.execSQL("delete from reg_caja");
        _db.execSQL("delete from pedidos");
        _db.execSQL("delete from linea_pedidos");
        _db.execSQL("delete from clientes");
        _db.execSQL("delete from empleados");
        _db.execSQL("delete from dispositivos");
        _db.execSQL("delete from rel_disp_emp");
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////-

    //Función sacamos los nombres de los campos de la tabla introducida por parámetros (SQLite)
    public static String[] sacarNomCols(SQLiteDatabase _db , String _nomTabla){
        try{
            Cursor fila = _db.rawQuery("select * from " + _nomTabla + " limit 1", null);
            fila.moveToFirst();
            return fila.getColumnNames();
        }catch (Exception e){
            Log.e("Error al sacar el nombre de los campos" , e.getMessage());
        }
        return null;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////-

    //Función sacamos el tipo de cada columna
    public static int[] sacarTipoCols(SQLiteDatabase _db , String _nomTabla){
        try{
            Cursor fila = _db.rawQuery("select * from " + _nomTabla + " limit 1", null);
            fila.moveToFirst();
            int cantCol = fila.getColumnCount();
            int[] dev = new int[cantCol];
            for(int i = 0 ; i < cantCol ; i ++){
                //System.out.println("COLUMNA: " + fila.getColumnName(i) + "TIPO COLUMNA: " + fila.getType(i));
                //Integer es getType = 1
                //String es getType = 3
                dev[i] = fila.getType(i);
            }
            return dev;
        }catch (Exception e){
            Log.e("Error al sacar el nombre de los campos" , e.getMessage());
        }
        return null;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////-

    //Función sacamos los nombres de los campos de la tabla introducida por parámetros (SQLite)
    public static String[][] sacarValReg(SQLiteDatabase _db , String _nomTabla){
        try{
            Cursor fila = _db.rawQuery("select * from " + _nomTabla, null);
            fila.moveToFirst();
            int cantReg = fila.getCount();
            int cantCols = fila.getColumnCount();
            String[][] data = new String[cantReg][cantCols];
            for(int i = 0 ; i < cantReg ; i++){
                for(int j = 0 ; j < cantCols ; j ++){
                    data[i][j] = fila.getString(j);
                }
                fila.moveToNext();
            }
            return data;
        }catch (Exception e){
            Log.e("Error al sacar los registross" , e.getMessage());
        }
        return null;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////-

    //Funciones para actualizar los spinners de platos en tomar nota, devuelve un string con la info de los platos
    public static String[] sacarPlatosMenu(int _idMenu , int _tipoPlato , SQLiteOpenHelper _adminDb){
        //Creamos la conexión a la db SQLite
        SQLiteDatabase db = _adminDb.getWritableDatabase();

        //ejecutamos la sentencia
        Cursor filaMenuSelec = db.rawQuery("select p.nombre , p.p_v_p from productos p join cat_prod cp on (p.id_categoria = cp.id_categoria) where p.id_menu = " + _idMenu + " and cp.tipo= " +  _tipoPlato+ " order by nombre asc", null);
        filaMenuSelec.moveToFirst();
        int cantPlatos = filaMenuSelec.getCount();
        String[] dev = new String[cantPlatos];
        if (cantPlatos > 0){
            dev = new String[cantPlatos];
            dev[0] = filaMenuSelec.getString(0) + ", " + filaMenuSelec.getString(1) + "EUR";
            for(int i = 1 ; i < cantPlatos ; i ++){
                filaMenuSelec.moveToNext();
                dev[i] = filaMenuSelec.getString(0) + ", " + filaMenuSelec.getString(1) + "EUR";
            }
        }else{
            dev = new String[]{"No hay platos asignados a este menú."};
        }

        return dev;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////-

    //Función para devolver el id de un plato según su nombre, tipo y menú
    public static int sacarIdPlato(int _idMenu , int _tipoPlato , String _nomPlato , SQLiteOpenHelper _adminDb){
        int dev;
        //Creamos la conexión a la db SQLite
        SQLiteDatabase db = _adminDb.getWritableDatabase();

        //ejecutamos la sentencia
        Cursor filaPlatoSelec = db.rawQuery("select p.id_prod from productos p join cat_prod cp on (p.id_categoria = cp.id_categoria) where p.id_menu = "+_idMenu+" and  cp.tipo = "+_tipoPlato+" and nombre = '"+_nomPlato+"'", null);
        filaPlatoSelec.moveToFirst();
        dev = Integer.parseInt(filaPlatoSelec.getString(0));

        return dev;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////-

    //Función para saber la cantidad de productos disponibles
    public static int cantProdDisp(int _idProd , SQLiteOpenHelper _adminDb){
        int dev;
        //Creamos la conexión a la db SQLite
        SQLiteDatabase db = _adminDb.getWritableDatabase();

        try {
            //ejecutamos la sentencia
            Cursor filaCantProd = db.rawQuery("select cant_total from productos where id_prod  = "+_idProd, null);
            filaCantProd.moveToFirst();
            dev = Integer.parseInt(filaCantProd.getString(0));

            return dev;
        }catch (Exception e){
            Log.e("Errora al sacar la cantidad de productos totales" , e.getMessage());
        }
        return -1;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////-

    //Función para actualizar cesta a la hora de subir un pedido
    public static boolean actualizarCesta(ConnSqlServer adminSQLServer , String _sqlServerConnUrl , SQLiteDatabase _db){
        try{
            //Ejecutamos el AsyncTask para recoger la información
            adminSQLServer = (ConnSqlServer) new ConnSqlServer(_sqlServerConnUrl , 3 , _db).execute();
            adminSQLServer = null;
            return true;
        }catch (Exception e){
            Log.e("Error al actualizar SQLite" , e.getMessage());
            return false;
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////-

}
