package connSqlServer;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ConnSqlServer extends AsyncTask<Integer , Integer , String> {
    //Attributes
    protected String connUrl;
    protected Connection conn;
    protected int tipo; //0-UpdateBD/1-Select
    protected String sql;

    //Overrided Methods
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected String doInBackground(Integer... integers) {
        this.openConn();

        //Dependiendo del tipo,ejecutamos una acción u otra
        switch(this.tipo){
            case 0:
                //Actualizar BD con SQL Server
                break;
            case 1:
                //Hacer select
                this.execSelect(this.sql);
                break;
        }

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
            System.out.println("Conexión establecida.");
        }catch (Exception e){
            Log.e("Error a la hora de establecer conexión: ", ""+e);
        }
    }
    //Cerrar conn a la DB
    public void closeConn(){
        try{
            this.conn.close();
            this.conn = null;
            System.out.println("Conexión cerrada exitosamente.");
        }catch(Exception e){
            Log.e("Error en cerrar conn a SQL Server: " , e.getMessage());
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

    //Constructors
    public ConnSqlServer(String _connUrl , int _tipo , String _sql){
        this.connUrl = _connUrl;
        this.conn = null;
        this.tipo = _tipo;
        this.sql = _sql;
    }
    public ConnSqlServer(String _connUrl , int _tipo){
        this.connUrl = _connUrl;
        this.conn = null;
        this.tipo = _tipo;
        this.sql = "";
    }
}
