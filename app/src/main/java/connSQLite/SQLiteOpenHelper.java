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
        db.execSQL("create table productos(id_producto integer primary key autoincrement , nombre text not null , cant_total integer not null , id_menu integer not null , id_categoria integer not null , cant_objetivo integer not null , p_v_p numeric(10,2) not null , p_prov numeric(10,2) not null , foreign key(id_categoria) references cat_prod(id_categoria), foreign key(id_menu) references menus(id_menu))");
        //Proveedores
        db.execSQL("create table proveedores(id_prov integer primary key autoincrement , nombre text not null)");
        //Incidencias Proveedores
        db.execSQL("create table incidencias_proveedores(id_inci integer primary key autoincrement , id_prov integer not null , descripcion text not null , tipo integer  not null, fecha_hora datetime , foreign key (id_prov) references proveedores(id_prov))");
        //Reg_caja
        db.execSQL("create table reg_caja(id_reg integer primary key autoincrement , descripcion text not null , fecha datetime not null , cantidad_total numeric(10,2) not null , tipo integer not null , cantidad_cambio numeric(10,2) not null)");
        //Pedidos
        db.execSQL("create table pedidos(id_pedido integer primary key autoincrement , fecha_pedido datetime not null , id_dispositivo integer not null , coste_total numeric(10,2) not null , id_reg_caja integer not null , foreign key (id_reg_caja) references reg_caja(id_reg))");
        //LineaPedidos
        db.execSQL("create table linea_pedidos(id_linped integer primary key autoincrement , id_producto integer not null , id_pedido integer not null , coste numeric(10,2) not null , cantidad integer not null , foreign key(id_producto) references productos(id_producto) , foreign key (id_pedido) references pedidos(id_pedido))");
        //Clientes
        db.execSQL("create table clientes(id_cliente integer primary key autoincrement , nombre text not null , correo text not null , telefono varchar not null , visitas integer not null)");
        //Empleados
        db.execSQL("create table empleados(id_empleado integer primary key autoincrement , nom text not null , ape text not null , permiso_chat integer not null , id_jefe integer)");
        //Dispositivos
        db.execSQL("create table dispositivos(id_dispositivo integer primary key autoincrement , disponible integer not null , ip text not null)");
        //rel_disp_empl
        db.execSQL("create table rel_disp_emp(id_rel integer primary key autoincrement , id_dispositivo integer not null , id_empleado integer not null , h_inicio datetime , h_fin datetime)");

    }

    //Se ejecuta para actualizar
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
