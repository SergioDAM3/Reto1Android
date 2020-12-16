package connSqlServer;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.reto1android.MainActivity;

import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import activities.Cesta;
import connSQLite.SQLiteOpenHelper;
import funcionesJava.FuncionesDB;

public class ConnSqlServer extends AsyncTask<Integer , Integer , Integer> {
    //Attributes
    protected String connUrl;
    protected Connection conn;
    protected int tipo; //0-UpdateBD/1-Select
    protected String sql;
    protected  SQLiteDatabase dbSQLite;

    boolean SQLiteActualizado = false;

    //Overrided Methods
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //Dependiendo del tipo,ejecutamos una acción u otra
        switch(this.tipo){
            case 0:
                //Actualizar SQL Server
                break;
            case 1:
                //Actualizar SQLite
                break;
            case 2:
                //Hacer select
                break;
            case 3:
                //Actualizar Cesta
                Cesta.actualizandoCesta(true);
                break;
        }

    }
    @Override
    protected Integer doInBackground(Integer... integers) {
        //Abrimos conexion
        this.openConn();

        //Dependiendo del tipo,ejecutamos una acción u otra
        switch(this.tipo){
            case 0:
                this.actualizarSqlServer();
                break;
            case 1:
                SQLiteActualizado = this.actualizarSQLite();
                break;
            case 2:
                //Hacer select
                this.execSelect(this.sql);
                break;
            case 3:
                //actualizar Cesta
                this.actualizarCesta();
                break;
        }

        //Cerramos conexion
        this.closeConn();
        return 2;
    }
    @Override
    protected void onPostExecute(Integer s) {
        super.onPostExecute(s);

        //Dependiendo del tipo,ejecutamos una acción u otra
        switch(this.tipo){
            case 0:
                //Actualizar SQL Server
                break;
            case 1:
                //Actualizar SQLite
                //Una vez actualizada, habilitamos el botón de entrada
                MainActivity.SQLiteActualizado(SQLiteActualizado);
                break;
            case 2:
                //Hacer select
                break;
            case 3:
                //actualizar Cesta
                Cesta.actualizandoCesta(false);
                break;
        }
    }

    //Methods
    //Conexión a la DB
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

    //Sacar el nombre de las columnas de la tabla especificada (SQL Server)
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

    //Insert Simple
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
    //Insert Simple II
    public void execInsertSimpleSqlServer(String _nomTabla , String[] _nomCols , String[][] _valReg , int[] _tipo){
        try{
            // create a Statement from the connection
            Statement statement = conn.createStatement();

            //Sacamos el número de registros a introducir
            int cantReg = _valReg.length ;

            //Creamos la variable con la sentencia Insert
            String sql;

            for(int i = 0 ; i<1 ; i++){
                //Hacemos una insert por cada registro
                sql = "insert into " + _nomTabla + " (";

                //Metemos las columnas
                for(int j = 0 ; j < _nomCols.length ; j++){
                    if(j != 0){
                        sql += _nomCols[j];
                        if(j != _nomCols.length - 1){
                            sql += ",";
                        }
                    }
                }
                sql += ") values (";

                //Metemos los registros
                for(int k = 0 ; k < _nomCols.length ; k++){
                    if(k != 0){
                        //Miramos si el registro tiene que ir entre comillas simples
                        switch (_tipo[k]){
                            case 1:
                                //si es un int
                                break;
                            case 3:
                                //Si es varchar o date
                                sql += "'";
                                break;
                            default:
                        }

                        sql += _valReg[i][k];

                        switch (_tipo[k]){
                            case 1:
                                //si es un int
                                break;
                            case 3:
                                //Si es varchar o date
                                sql += "'";
                                break;
                            default:
                        }
                        if(k != _nomCols.length - 1){
                            sql += ",";
                        }
                    }
                }
                sql += ");";

                System.out.println("SQL INSERT: " + sql);

                // insert the data
                statement.executeUpdate(sql);
            }
        }catch(Exception e){
            Log.e("Error al ejecutar insert" , e.getMessage());
        }
    }
    //Insert múltiple
    public void execInsertMultipleSqlServer(String _nomTabla , String[] _nomCols , String[][] _valReg , int[] _tipo){
        try{
            // create a Statement from the connection
            Statement statement = conn.createStatement();

            //Sacamos el número de registros a introducir
            int cantReg = _valReg.length ;

            //Creamos la variable con la sentencia Insert
            String sql;

            for(int i = 0 ; i<cantReg ; i++){
                //Hacemos una insert por cada registro
                sql = "insert into " + _nomTabla + " (";

                //Metemos las columnas
                for(int j = 0 ; j < _nomCols.length ; j++){
                    if(j != 0){
                        sql += _nomCols[j];
                        if(j != _nomCols.length - 1){
                            sql += ",";
                        }
                    }
                }
                sql += ") values (";

                //Metemos los registros
                for(int k = 0 ; k < _nomCols.length ; k++){
                    if(k != 0){
                        //Miramos si el registro tiene que ir entre comillas simples
                        switch (_tipo[k]){
                            case 1:
                                //si es un int
                                break;
                            case 3:
                                //Si es varchar o date
                                sql += "'";
                                break;
                            default:
                        }

                        sql += _valReg[i][k];

                        switch (_tipo[k]){
                            case 1:
                                //si es un int
                                break;
                            case 3:
                                //Si es varchar o date
                                sql += "'";
                                break;
                            default:
                        }
                        if(k != _nomCols.length - 1){
                            sql += ",";
                        }
                    }
                }
                sql += ");";

                System.out.println("SQL INSERT: " + sql);

                // insert the data
                statement.executeUpdate(sql);
            }
        }catch(Exception e){
            Log.e("Error al ejecutar insert" , e.getMessage());
        }
    }

    //Insertar tabla a SQL Server
    protected boolean insertarTablaSqlServer(String _nomTabla){
        try{
            String[] nomCols;
            String[][] valReg;
            int[] tipoCols;
            //Sacamos el nombre de las columnas de la DB
            nomCols = FuncionesDB.sacarNomCols(this.dbSQLite , _nomTabla); //Comprobado que funciona

            //Sacamos los datos
            valReg = FuncionesDB.sacarValReg(this.dbSQLite , _nomTabla);

            //Sacamos el tipo de las columnas
            tipoCols = FuncionesDB.sacarTipoCols(this.dbSQLite , _nomTabla);

            //Ejecutamos la insert
            this.execInsertMultipleSqlServer(_nomTabla , nomCols , valReg , tipoCols);

            nomCols = null;
            valReg = null;
            tipoCols = null;

            return true;

        }catch (Exception e){
            return false;
        }
    }

    //Insertar tabla a SQL Server sin la primera col
    //NO IMPLEMENTADO!!!!!
    protected boolean insertarTablaSqlServerNoId(String _nomTabla){
        try{
            String[] nomCols;
            String[][] valReg;
            int[] tipoCols;
            //Sacamos el nombre de las columnas de la DB
            nomCols = FuncionesDB.sacarNomCols(this.dbSQLite , _nomTabla); //Comprobado que funciona

            //Sacamos los datos
            valReg = FuncionesDB.sacarValReg(this.dbSQLite , _nomTabla);

            //Sacamos el tipo de las columnas
            tipoCols = FuncionesDB.sacarTipoCols(this.dbSQLite , _nomTabla);

            //Ejecutamos la insert
            this.execInsertMultipleSqlServer(_nomTabla , nomCols , valReg , tipoCols);

            nomCols = null;
            valReg = null;
            tipoCols = null;

            return true;

        }catch (Exception e){
            return false;
        }
    }

    //actualizarSQLServer
    protected void actualizarSqlServer(){
        //Actualizar BD SQL Server con SQLite
        //No Implementado
        try{
            //Sacamos la información de la base de datos y la subimos
            this.insertarTablaSqlServer("productos");

            //Limpiamos SQLite
            this.dbSQLite.execSQL("delete from productos");
        }catch(Exception e){
            Log.e("Error a la hora de actualizar la BD SQL Server con los datos de SQLite: " , e.getMessage());
        }
    }

    //Función para actualizar las tablas de SQLite con las de SQL Server
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

    //Función para actualizar la tabla de historial con los pedidos del dispositivo
    protected void actualizarMiHistorialSQLite(int _idDisp){
        try{
            String[][] dataMenus = this.execSelectMultiple("select * from pedidos where id_disp = " + _idDisp + ";");
            String[] colNamesMenus = this.execGetColumnNames("pedidos");
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
                this.dbSQLite.insert("miHistorial", null, registro);
            }
            System.out.println("Tabla " + "miHistorial" + " actualizada correctamente. (SQL Server -> SQLite)");
        }catch(Exception e){
            Log.e("Error a la hora de actualizar Tabla SQLite" , e.getMessage());
        }
    }

    //actualizarSQLite
    protected boolean actualizarSQLite(){
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
            //this.actualizarTablaSQLite("reg_caja");
            this.actualizarTablaSQLite("pedidos");
            this.actualizarTablaSQLite("linea_pedidos");
            this.actualizarTablaSQLite("clientes");
            this.actualizarTablaSQLite("empleados");
            this.actualizarTablaSQLite("dispositivos");
            this.actualizarTablaSQLite("rel_disp_emp");

            //Actualizamos tabla de mi historial
            //Sacamos la id deldispositivo atual
            Cursor filaIdDisp = dbSQLite.rawQuery("select id_disp from thisDeviceInfo limit 1" , null);
            filaIdDisp.moveToFirst();
            //Limpiamos historial
            dbSQLite.execSQL("delete from miHistorial");
            //Actualizamos historial
            this.actualizarMiHistorialSQLite(filaIdDisp.getInt(0));

            System.out.println("Tablas actualizadas correctamente.\nBD SQLite actualizada correctamente.");

            return true;
        }catch(Exception e){
            Log.e("Error a la hora de actualizar la BD SQLite con los datos de SQL Server: " , e.getMessage());
            return false;
        }
    }

    //actualizarSQLServer
    protected void actualizarCesta(){
        //Actualizar BD SQL Server con SQLite
        try{
            //Creamos nuevo registro de caja en SQL Server
            //Sacamos la info no relevante de SQLite
            Cursor filaRegCaja = dbSQLite.rawQuery("select descripcion , fecha , cantidad_total , tipo , cantidad_cambio from reg_caja order by id_reg desc limit 1" , null);
            filaRegCaja.moveToFirst();
            Cursor filaUltPedido = dbSQLite.rawQuery("select costo_total from pedidos order by id_pedido desc limit 1" , null);
            filaUltPedido.moveToFirst();
            //Sacamos la cantidad total que va a haber después del cambio
            String[][] cantTotalCaja = this.execSelectMultiple("select top 1 cantidad_total from reg_caja order by fecha desc");
            System.out.println("A ver puto tonto, que tiene que haber como mínimo un registro en reg_caja (SQL Server), tonto que eres tonto");
            double cantTotalCajaActual = Double.valueOf(cantTotalCaja[0][0]) + Double.valueOf(filaUltPedido.getString(0));
            //Hacemos la insert con los datos obtenidos
            System.out.println("Exec insert en RegCaja SQLSERVER: " + "insert into reg_caja (descripcion , cantidad_total , tipo , cantidad_cambio) values ('"+filaRegCaja.getString(0)+"','"+ filaRegCaja.getString(1)+"' , "+cantTotalCajaActual+" , 1 , "+ filaUltPedido.getString(0)+")");
            this.execInsertSqlServer("insert into reg_caja (descripcion , fecha , cantidad_total , tipo , cantidad_cambio) values ('"+filaRegCaja.getString(0)+"','"+ filaRegCaja.getString(1)+"' , "+cantTotalCajaActual+" , 1 , "+ filaUltPedido.getString(0)+")");

            //Sacamos la id del último registro de caja
            String[][] idUltRegCaja = this.execSelectMultiple("select top 1 id_reg from reg_caja order by id_reg desc");

            //Actualizamos la id_reg de pedidos
            this.dbSQLite.execSQL("update pedidos set id_reg = " + idUltRegCaja[0][0]);

            //Sacamos la información de la base de datos y la subimos
            this.insertarTablaSqlServer("pedidos");

            //sacamos la id del pedido insertado
            String[][] idUltPedido = this.execSelectMultiple("select top 1 id_pedido from pedidos order by id_pedido desc");

            //Actualizamos la FK de linea_pedidos(SQLite)
            this.dbSQLite.execSQL("update linea_pedidos set id_pedido = " + idUltPedido[0][0]);

            //Insertamos los pedidos
            String[] colsPedidos =  FuncionesDB.sacarNomCols(this.dbSQLite , "pedidos"); //Comprobado que funciona
            String[][] valRegPedidos = FuncionesDB.sacarValReg(this.dbSQLite , "pedidos");
            int[] tipoPedidos = FuncionesDB.sacarTipoCols(this.dbSQLite , "pedidos");
            //this.execInsertSimpleSqlServer("pedidos" , colsPedidos , valRegPedidos , tipoPedidos );
            System.out.println("Insert pedido correcto");

            //Preparamos lin pedido
            //Actualizamos id_pedido de linea_pedidos
            this.dbSQLite.execSQL("update linea_pedidos set id_pedido = " + idUltPedido[0][0]);

            //insertamos los linea_pedidos
            String[] colsLinPedidos =  FuncionesDB.sacarNomCols(this.dbSQLite , "linea_pedidos"); //Comprobado que funciona
            String[][] valRegLinPedidos = FuncionesDB.sacarValReg(this.dbSQLite , "linea_pedidos");
            int[] tipoLinPedidos = FuncionesDB.sacarTipoCols(this.dbSQLite , "linea_pedidos");
            this.execInsertMultipleSqlServer("linea_pedidos" , colsLinPedidos , valRegLinPedidos , tipoLinPedidos );
            System.out.println("Insert linea_pedidos correcto");

            //Limpiamos SQLite
            this.dbSQLite.execSQL("delete from linea_pedidos");
            this.dbSQLite.execSQL("delete from pedidos");
            this.dbSQLite.execSQL("delete from reg_caja");

        }catch (Exception e){
            Log.e("Error al actualizar cesta" , e.getMessage());
        }


    }

    //Constructores
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
    public ConnSqlServer(String _connUrl , int _tipo , SQLiteDatabase _db , String _sql){
        this.connUrl = _connUrl;
        this.conn = null;
        this.tipo = _tipo;
        this.sql = _sql;
        this.dbSQLite = _db;
    }
}
