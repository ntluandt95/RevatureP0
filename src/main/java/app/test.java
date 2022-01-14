package app;

import utilities.ConnectionUtility;

import java.sql.Connection;
import java.sql.SQLException;

public class test {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        final Connection connection = ConnectionUtility.getConnection();
    }
}
