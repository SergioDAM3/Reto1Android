package connSqlServer;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnSqlServer {
    public void connDb(){
        System.out.println("Conectando a la BD ....");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            //old: "jdbc:jtds:sqlserver://127.0.0.1:1433/PruebaAndroid;instance=JAVISQL;user=admin;password=Admin1234"
            String url  = "jdbc:jtds:sqlserver://10.0.2.4:1433;databaseName=PruebaAndroid;instance=JHERRERO-P\\JAVISQL;user=admin;password=Admin1234";
            Connection conn = DriverManager.getConnection(url);
            System.out.println("Conexión establecida.");
        }catch (Exception e){
            Log.e("Error al intentar conectar con SQL Server -> ", ""+e);
            System.out.println("No se ha establecido conexión con la BD.");
        }
    }
}
