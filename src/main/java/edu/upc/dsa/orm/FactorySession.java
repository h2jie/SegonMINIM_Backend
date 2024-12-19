package edu.upc.dsa.orm;

import edu.upc.dsa.DB.DBUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static edu.upc.dsa.DB.DBUtils.getConnection;

public class FactorySession {

    public static Session openSession() {
        Connection conn = getConnection();
        Session session = new SessionImpl(conn);
        return session;
    }

    public static Connection getConnection()  {
        String db = DBUtils.getDb();
        String host = DBUtils.getHost();
        String port = DBUtils.getPort();
        String user = DBUtils.getUser();
        String pass = DBUtils.getPassword();


        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mariadb://"+host+":"+port+"/"+
                    db+"?user="+user+"&password="+pass);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }

    public static Session openSession(String url, String user, String password) {
        return null;
    }

}
