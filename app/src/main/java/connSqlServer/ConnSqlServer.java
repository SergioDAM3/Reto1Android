package connSqlServer;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.example.reto1android.MainActivity;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnSqlServer extends AsyncTask<Integer , Integer , Connection> {
    //Attribs
    protected  String server;
    protected  String port;
    protected  String db;
    protected  String instance;
    protected  String user;
    protected  String passwd;
    protected  String connUrl;
    protected  Connection conn;

    //Getters & Setters
    public Connection getConn(){
        return this.conn;
    }

    //Methods
    @Override
    protected Connection doInBackground(Integer... integers) {
        this.conn = this.connDb();
        return this.conn;
    }

    @Override
    protected void onPostExecute(Connection s) {
        super.onPostExecute(s);
    }

    public Connection connDb(){
        System.out.println("Conectando a la BD ....");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            //old: "jdbc:jtds:sqlserver://127.0.0.1:1433/PruebaAndroid;instance=JAVISQL;user=admin;password=Admin1234" //10.0.2.2
            //String url  = "jdbc:jtds:sqlserver://"+_server+":"+_port+";databaseName="+_db+";instance="+_instance+";user="+_user+";password="+_passwd+";";
            System.out.println(this.connUrl);
            Connection conn = DriverManager.getConnection(this.connUrl);
            System.out.println("Conexión establecida.");
            return conn;
        }catch (Exception e){
            Log.e("Error al intentar conectar con SQL Server -> ", ""+e);
            System.out.println("No se ha establecido conexión con la BD.");
        }
        return null;
    }

    //Constructores
    public ConnSqlServer(String _server , String _port , String _db , String _instance , String _user , String _passwd){
        this.server = _server;
        this.port = _port;
        this.db = _db;
        this.instance = _instance;
        this.user = _user;
        this.passwd = _passwd;
        this.connUrl = "jdbc:jtds:sqlserver://"+_server+":"+_port+";databaseName="+_db+";instance="+_instance+";user="+_user+";password="+_passwd+";";
        this.conn = null;
    }
}