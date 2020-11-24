package connSqlServer;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.example.reto1android.MainActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import connSQLite.SQLiteOpenHelper;
import funcionesJava.FuncionesDB;

public class ConnSqlServer extends AsyncTask<Integer , Integer , String> {
    //Attributes
    protected String connUrl;
    protected Connection conn;
    protected int tipo; //0-UpdateBD/1-Select
    protected String sql;
    protected  SQLiteDatabase dbSQLite;

    //Overrided Methods
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected String doInBackground(Integer... integers) {
        //Abrimos conexion
        this.openConn();

        //Dependiendo del tipo,ejecutamos una acción u otra
        switch(this.tipo){
            case 0:
                this.actualizarSqlServer();
                break;
            case 1:
                this.actualizarSQLite();
                break;
            case 2:
                //Hacer select
                this.execSelect(this.sql);
                break;
        }

        //Cerramos conexion
        this.closeConn();
        return "2";
    }
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    //Methods
    //Conecxión a la DB
    public void openConn(){
        if(this.conn != null){
            try{
                this.conn.close();
            }catch(Exception e){
                Log.e("Error al cerrar conexion con BD anterior: " , ""+e);
            }
        }
        this.conn = null;

        System.out.println("Conectando con DB ....");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            this.conn = DriverManager.getConnection(this.connUrl);
            System.out.println("Conexión establecida(SQL Server).");
        }catch (Exception e){
            Log.e("Error a la hora de establecer conexión(SQL Server): ", ""+e);
        }
    }
    //Cerrar conn a la DB
    public void closeConn(){
        try{
            this.conn.close();
            this.conn = null;
            System.out.println("Conexión cerrada exitosamente(SQL Server).");
        }catch(Exception e){
            Log.e("Error en cerrar conn a SQL Server(SQL Server):" , e.getMessage());
        }
    }

    //Select
    public void execSelect(String _sql){
        try{
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(_sql);

            if(rs.next()){
                System.out.println("Result menu: " + rs.getString(2));
            }
        }catch(Exception e){
            Log.e("Error al ejecutar select: " , "" + e);
        }
    }
    //Select Múltiple
    public String[][] execSelectMultiple(String _sql){
        try{
            Statement stm = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); //stm que permite resetear el cursor
            ResultSet rs = stm.executeQuery(_sql);
            ResultSetMetaData rsmd = rs.getMetaData();

            //Creamos las variables que necesitaremos en la función
            int cantFilas = 0; //El número de filas que ha devuelto la sentencia
            int cantCols = rsmd.getColumnCount(); //El número de columnas que tiene la tabla buscada

            //Sacamos el número de filas que devuelve la sentencia
            while(rs.next()){
                cantFilas++;
            }

            //Creamos el String[][] que vamos a devolver con la información de la búsqueda
            String[][] dev = new String[cantFilas][cantCols];

            //Reseteamos el cursor
            rs.beforeFirst();

            //Variable contador
            int cont = 0;

            //Metemos los datos en el array que vamos a devolver
            while(rs.next()){
                for(int i = 0 ; i < cantCols ; i++){
                    dev[cont][i] = rs.getString(i+1);
                }
                cont++;
            }

            //Devolvemos el array de dos dimensiones
            return dev;
        }catch(Exception e){
            Log.e("Error al ejecutar select múltiple: " , "" + e);
        }

        return null;
    }

    //Sacar el nombre de las columnas de la tabla especificada
    protected String[] execGetColumnNames(String _tableName){
        try{
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("select top 1 * from " + _tableName);
            ResultSetMetaData rsmd = rs.getMetaData();

            //Sacamos el numero de columnas de la tabla
            int cantCols = rsmd.getColumnCount(); //El número de columnas que tiene la tabla buscada

            //creamos el array que vamos a devolver
            String[] dev = new String[cantCols];

            for(int i = 0 ; i < cantCols ; i++){
                dev[i] = rsmd.getColumnName(i+1);
            }

            //Devolvemos el arrya
            return dev;
        }catch(Exception e){
            Log.e("Error a la hora de sacar el nombre de columna de la tabla: " , e.getMessage());
        }
        return null;
    }

    //Insert
    public void execInsertSqlServer(String _sql){
        try{
            // create a Statement from the connection
            Statement statement = conn.createStatement();

            // insert the data
            statement.executeUpdate(_sql);
        }catch(Exception e){
            Log.e("Error al ejecutar insert: " , e.getMessage());
        }
    }

    //actualizarSQLServer
    protected void actualizarSqlServer(){
        //Actualizar BD SQL Server con SQLite
        //No Implementado
        try{
            this.execInsertSqlServer("insert into menus (nombre, activo) values ('Menú del Día2' , 1)");
        }catch(Exception e){
            Log.e("Error a la hora de actualizar la BD SQL Server con los datos de SQLite: " , e.getMessage());
        }
    }

    protected void actualizarTablaSQLite(String _nomTabla){
        try{
            String[][] dataMenus = this.execSelectMultiple("select * from "+_nomTabla+";");
            String[] colNamesMenus = this.execGetColumnNames(_nomTabla);
            //Preparamos la insert
            ContentValues registro = new ContentValues();
            for(int i = 0 ; i  < dataMenus.length ; i++){
                //System.out.println("Fila: " + i);
                for(int k = 0; k < dataMenus[0].length ; k++){
                    //System.out.print(dataMenus[i][k] + " ");
                    //System.out.println(colNamesMenus[k] + " -> " + dataMenus[i][k]);
                    registro.put(colNamesMenus[k] , dataMenus[i][k]);
                }
                //System.out.println("");
                //Ejecutamos la insert
                this.dbSQLite.insert(_nomTabla, null, registro);
            }
            System.out.println("Tabla " + _nomTabla + " actualizada correctamente. (SQL Server -> SQLite)");
        }catch(Exception e){
            Log.e("Error a la hora de actualizar Tabla SQLite" , e.getMessage());
        }
    }

    //actualizarSQLite
    protected void actualizarSQLite(){
        //Actualizar BD SQLite con SQL Server
        try{
            System.out.println("Actualizamos bd SQLite ....");
            //Comprobamos la conexión con SQLite
            try{
                this.dbSQLite.isOpen();
                System.out.println("Conn SQLite checked ....");
            }catch(Exception e){
                Log.e("No se ha establecido conexión con SQLite" , e.getMessage());
            }

            //Borramos toda la información de SQLite, para que no haya datos duplicados
            FuncionesDB.borrarTodoSQLite(this.dbSQLite);

            //Actualizamos las tablas
            System.out.println("Actualizando tablas ....");
            this.actualizarTablaSQLite("menus");
            this.actualizarTablaSQLite("cat_prod");
            this.actualizarTablaSQLite("productos");
            this.actualizarTablaSQLite("proveedores");
            this.actualizarTablaSQLite("incidencias_proveedores");
            this.actualizarTablaSQLite("reg_caja");
            this.actualizarTablaSQLite("pedidos");
            this.actualizarTablaSQLite("linea_pedidos");
            this.actualizarTablaSQLite("clientes");
            this.actualizarTablaSQLite("empleados");
            this.actualizarTablaSQLite("dispositivos");
            this.actualizarTablaSQLite("rel_disp_emp");

            System.out.println("Tablas actualizadas correctamente.\nBD SQLite actualizada correctamente.");
        }catch(Exception e){
            Log.e("Error a la hora de actualizar la BD SQLite con los datos de SQL Server: " , e.getMessage());
        }
    }

    //Constructors
    public ConnSqlServer(String _connUrl , int _tipo , String _sql){
        this.connUrl = _connUrl;
        this.conn = null;
        this.tipo = _tipo;
        this.sql = _sql;
        this.dbSQLite = null;
    }
    public ConnSqlServer(String _connUrl , int _tipo){
        this.connUrl = _connUrl;
        this.conn = null;
        this.tipo = _tipo;
        this.sql = "";
        this.dbSQLite = null;
    }
    public ConnSqlServer(String _connUrl , int _tipo , SQLiteDatabase _db){
        this.connUrl = _connUrl;
        this.conn = null;
        this.tipo = _tipo;
        this.sql = "";
        this.dbSQLite = _db;
    }
}
