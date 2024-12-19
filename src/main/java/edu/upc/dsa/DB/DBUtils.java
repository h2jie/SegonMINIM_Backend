package edu.upc.dsa.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtils {

    public static final String DB_NAME = "Projecte_DSA";
    public static final String DB_HOST = "localhost";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "123456";
    public static final String DB_PORT = "3306";

    public static String getDb(){
        return DB_NAME;
    }

    public static String getHost(){
        return DB_HOST;
    }

    public static String getUser(){
        return DB_USER;
    }

    public static String getPassword(){
        return DB_PASSWORD;
    }

    public static String getPort(){
        return DB_PORT;
    }

    public static Connection getConnection() throws SQLException{
        String db = DBUtils.getDb();
        String host = DBUtils.getHost();
        String port = DBUtils.getPort();
        String user = DBUtils.getUser();
        String password = DBUtils.getPassword();

        Connection connection = DriverManager.getConnection("jdbc:mariadb://"+host+":"+port+"/"+
                db+"?user="+user+"&password="+password);

        return connection;
    }

}
