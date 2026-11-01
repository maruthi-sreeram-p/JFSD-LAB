package com.jfsd.servlets;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Opens JDBC connections. H2 is used so the program runs without installing a
 * database server; the data is saved in a file in your home folder.
 *
 * To use MySQL instead, add the mysql-connector-j dependency and change the
 * three constants below:
 *   URL      = "jdbc:mysql://localhost:3306/jfsd"
 *   USER     = "root"
 *   PASSWORD = "your password"
 */
public class DbUtil {

    private static final String URL = "jdbc:h2:file:" + System.getProperty("user.home") + "/jfsd-students";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    static {
        try {
            Class.forName("org.h2.Driver");
            createTable();
        } catch (ClassNotFoundException | SQLException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS students ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "name VARCHAR(100) NOT NULL, "
                + "email VARCHAR(100) NOT NULL, "
                + "course VARCHAR(50))";
        try (Connection con = getConnection(); Statement st = con.createStatement()) {
            st.execute(sql);
        }
    }
}
