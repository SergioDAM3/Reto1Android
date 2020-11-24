package funcionesJava;

import connSqlServer.ConnSqlServer;

public class FuncionesDB {
    ///////////////////////////////////////////////////////////////////////////////////////////////////////-

    //EN ESTA CLASE SE GUARDARÁN FUNCIONES QUE SERÁN USADAS POR OTROS OBJETOS

    ///////////////////////////////////////////////////////////////////////////////////////////////////////-

    //Función para actualizar SQLite con la SQL Server
    //TODAVÍA NO IMPLEMENTADO
    public static void execAsyncTask(ConnSqlServer adminSQLServer, String _sqlServerConnUrl){
        adminSQLServer = (ConnSqlServer) new ConnSqlServer(_sqlServerConnUrl , 1 , "select * from menus where id_menu = 1").execute();
        //admin.execSelect("select * from menus where id_menu = 1");
        //admin.closeConn();
        adminSQLServer = null;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////-
}
