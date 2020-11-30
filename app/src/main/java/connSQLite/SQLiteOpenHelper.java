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
        db.execSQL("create table productos(id_prod integer primary key autoincrement , nombre text not null , cant_total integer not null, cant_objetivo integer not null ,   p_v_p numeric(10,2) not null , p_prov numeric(10,2) not null , id_categoria integer not null , id_menu integer not null  , foreign key(id_categoria) references cat_prod(id_categoria), foreign key(id_menu) references menus(id_menu)) ");
        //Proveedores
        db.execSQL("create table proveedores(id_prov integer primary key autoincrement , nombre text not null)");
        //Incidencias Proveedores
        db.execSQL("create table incidencias_proveedores(id_inci integer primary key autoincrement , descripcion text not null , tipo integer  not null, fecha_hora datetime  , id_prov integer not null , foreign key (id_prov) references proveedores(id_prov))");
        //Reg_caja
        db.execSQL("create table reg_caja(id_reg integer primary key autoincrement , descripcion text not null , fecha datetime not null , cantidad_total numeric(10,2) not null , tipo integer not null , cantidad_cambio numeric(10,2) not null)");
        //Pedidos
        db.execSQL("create table pedidos(id_pedido integer primary key autoincrement , fecha_pedido datetime not null , costo_total numeric(10,2) not null , id_disp integer not null  , id_reg integer not null , foreign key (id_reg) references reg_caja(id_reg))");
        //LineaPedidos
        db.execSQL("create table linea_pedidos(id_lineaped integer primary key autoincrement , coste numeric(10,2) not null , cantidad integer not null , id_prod integer not null , id_pedido integer not null ,  foreign key(id_prod) references productos(id_prod) , foreign key (id_pedido) references pedidos(id_pedido))");
        //Clientes
        db.execSQL("create table clientes(id_cliente integer primary key autoincrement , nombre text not null , correo text not null , telefono varchar not null , visitas integer not null)");
        //Empleados
        db.execSQL("create table empleados(id_emp integer primary key autoincrement , nom text not null , ape text not null , permiso_chat integer not null  , password text not null , id_jefe integer , foreign key (id_jefe) references empleados(id_emp))");
        //Dispositivos
        db.execSQL("create table dispositivos(id_disp integer primary key autoincrement , disponible integer not null , ip text not null)");
        //rel_disp_empl
        db.execSQL("create table rel_disp_emp(id_rel integer primary key autoincrement , h_inicio datetime , h_fin datetime , id_dispositivo integer not null , id_emp integer not null , foreign key (id_dispositivo) references dispositivos(id_disp) , foreign key (id_emp) references empleados(id_emp))");

        //TABLAS DE PDA
        //tabla infoDevice
        db.execSQL("create table thisDeviceInfo(id_disp integer primary key , idEmpRecordado integer)");
        //Nos asignamos una id de device
        db.execSQL("insert into thisDeviceInfo (id_disp , idEmpRecordado) values (1 , null)");
    }

    //Se ejecuta para actualizar
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
