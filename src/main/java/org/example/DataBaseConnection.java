package org.example;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataBaseConnection {

    static Connection connection = null;
    public  static Connection getConnection(){

        if(connection != null){
            return  connection;
        }

        String db = "searchengine";
        String user = "Projects";
        String pass = "Santhosh@007";
        return  getConnection(db , user, pass);
    }

    private static Connection getConnection(String db, String user, String pass){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1/"+db+"?user="+user+"&password="+pass);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return connection;
    }
}
