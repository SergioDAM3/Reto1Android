package funcionesJava;

public class FuncionesGenerales {
    ///////////////////////////////////////////////////////////////////////////////////////////////////////-

    //EN ESTA CLASE SE GUARDARÁN FUNCIONES QUE SERÁN USADAS POR OTROS OBJETOS

    ///////////////////////////////////////////////////////////////////////////////////////////////////////-

    //Función para generar el strinf de conexión a la base de datos SQL Server
    public static String cadConnSqlServer(String _server , String _port , String _db , String _instance , String _user , String _passwd){
        return "jdbc:jtds:sqlserver://"+_server+":"+_port+";databaseName="+_db+";instance="+_instance+";user="+_user+";password="+_passwd+"";
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////-
}
