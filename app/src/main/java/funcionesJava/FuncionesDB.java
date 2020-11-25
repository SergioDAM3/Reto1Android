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
    //TODAVÍA NO IMPLEMENTADO
    public static void actualizarSQLite(ConnSqlServer adminSQLServer , String _sqlServerConnUrl , SQLiteDatabase _db){
        //Ejecutamos el AsyncTask para recoger la información
        adminSQLServer = (ConnSqlServer) new ConnSqlServer(_sqlServerConnUrl , 1 , _db).execute();
        adminSQLServer = null;
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
}
