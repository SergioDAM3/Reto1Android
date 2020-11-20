package connSQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;

public class SQLiteOpenHelper extends android.database.sqlite.SQLiteOpenHelper implements Serializable {
    public SQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //Se ejecuta al crear el objeto únicamente
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creamos las tablas
        //Tabla menus
        db.execSQL("create table menus(id_menu integer primary key autoincrement , nombre text not null, activo integer not null)");
        //Tabla Categoría de productos
        db.execSQL("create table cat_prod(id_categoria integer primary key autoincrement , tipo integer not null , descripcion text not null)");
        //Tabla Productos
        db.execSQL("create table productos(id_producto integer primary key autoincrement , nombre text not null , cant_total integer not null , id_menu integer not null , id_categoria integer not null , cant_objetivo integer not null , p_v_p numeric(10,2) not null , p_prov numeric(10,2) not null)");
    }

    //Se ejecuta para actualizar
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
