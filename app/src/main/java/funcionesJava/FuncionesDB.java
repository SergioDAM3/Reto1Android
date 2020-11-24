package funcionesJava;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import connSQLite.SQLiteOpenHelper;
import connSqlServer.ConnSqlServer;

public class FuncionesDB {
    ///////////////////////////////////////////////////////////////////////////////////////////////////////-

    //EN ESTA CLASE SE GUARDARÁN FUNCIONES QUE SERÁN USADAS POR OTROS OBJETOS

    ///////////////////////////////////////////////////////////////////////////////////////////////////////-

    //Función para actualizar SQL Server con la SQLite
    //TODAVÍA NO IMPLEMENTADO
    public static void actualizarSqlServer(ConnSqlServer adminSQLServer , String _sqlServerConnUrl){
        //Ejecutamos el AsyncTask
        adminSQLServer = (ConnSqlServer) new ConnSqlServer(_sqlServerConnUrl , 0).execute();
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
}
