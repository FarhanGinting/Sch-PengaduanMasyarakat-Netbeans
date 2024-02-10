/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Koneksi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Koneksi {

    Connection koneksi = null;

    public static Connection KoneksiDb() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection koneksiDb = DriverManager.getConnection("jdbc:mysql://localhost/db_pengaduan_masyarakat", "root", "");
            return koneksiDb;
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            return null;
        }
    }
}
