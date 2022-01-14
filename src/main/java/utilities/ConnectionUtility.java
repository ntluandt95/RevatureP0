package utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConnectionUtility {
    public static Connection getConnection() throws SQLException, ClassNotFoundException{
        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://sparkp0.c2g6uufmaxwt.us-east-1.rds.amazonaws.com:5432/postgres";
        String user = "thanhadmin";
        String pass = "password";
        Connection conn = DriverManager.getConnection(url,user,pass);
        return conn;
    }
}
