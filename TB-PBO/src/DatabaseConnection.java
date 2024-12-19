// Kelas Pendukung: DatabaseConnection

import java.sql.*; // PENERAPAN NOMOR 8

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/db_dokumen"; // URL database
    private static final String USER = "root"; // username untuk database
    private static final String PASSWORD = ""; // password untuk database

    public static Connection connect() { // metode untuk koneksi ke database
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {  // melakukan try catch untuk menangkap Exception ketika kegagalan koneksi database
            e.printStackTrace();
            return null;
        }
    }

    public static void disconnect(Connection conn) {  // metode untuk diskonek ke database
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) { // melakukan try catch untuk menangkap Exception ketika kegagalan diskonek database
                e.printStackTrace();
            }
        }
    }
}
