/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Panel;

import Form.Dashboard;
import Koneksi.Koneksi;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author FARHAN
 */
public class InfoAkun extends javax.swing.JPanel {

    /**
     * Creates new form InfoAkun
     */
    private int LevelLogin;
    private int nik;
    private int idPetugas;

    public InfoAkun(int LevelLogin, int nik, int idPetugas) {
        initComponents();
        this.LevelLogin = LevelLogin;
        this.nik = nik;
        this.idPetugas = idPetugas;
        if (LevelLogin == 3) {
            RBPetugas.setVisible(false);
            RBADMIN.setVisible(false);

            getDataForMasyarakat(nik);
        }
        if (LevelLogin == 2) {
            getDataForPetugas(idPetugas);

        }
        if (LevelLogin == 1) {
            getDataForPetugas(idPetugas);
            RBPetugas.setEnabled(false);
            RBADMIN.setEnabled(false);
        }
    }

// Method untuk pengguna level masyarakat (level == 3)
    private void getDataForMasyarakat(int nik) {
        try {
            Connection kon = Koneksi.KoneksiDb();
            String query = "SELECT nik, nama, username, password, no_telepon FROM tbl_masyarakat WHERE nik = ?";
            try ( PreparedStatement ps = kon.prepareStatement(query)) {
                ps.setInt(1, nik);

                try ( ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        // Mengisi kolom-kolom sesuai dengan data yang diambil
                        TXT_NIK_PETUGAS.setText("NIK Masyarakat = " + String.valueOf(rs.getInt("nik")));
                        TXT_NAMA.setText(rs.getString("nama"));
                        TXT_USERNAME.setText(rs.getString("username"));
                        TXT_PASSWORD.setText(rs.getString("password"));
                        TXT_NOTELEPON.setText(rs.getString("no_telepon"));

                    } else {
                        // Handle jika data tidak ditemukan
                        // Misalnya, menampilkan pesan kesalahan atau membersihkan kolom
                        clearPetugasFields();
                        JOptionPane.showMessageDialog(null, "Data masyarakat tidak ditemukan.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

// Method untuk pengguna level petugas/admin (level == 1 || level == 2)
    private void getDataForPetugas(int idPetugas) {
        try {
            Connection kon = Koneksi.KoneksiDb();
            String query = "SELECT id_petugas, nama_petugas, username, password, no_telepon, level FROM tbl_petugas WHERE id_petugas = ?";
            try ( PreparedStatement ps = kon.prepareStatement(query)) {
                ps.setInt(1, idPetugas);

                try ( ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        // Mengisi kolom-kolom sesuai dengan data yang diambil
//                        TXT_NIK_PETUGAS.setText(String.valueOf(rs.getInt("id_petugas")));
                        TXT_NAMA.setText(rs.getString("nama_petugas"));
                        TXT_USERNAME.setText(rs.getString("username"));
                        TXT_PASSWORD.setText(rs.getString("password"));
                        TXT_NOTELEPON.setText(rs.getString("no_telepon"));
                        // Mengisi kolom level untuk petugas/admin
                        // Mengisi kolom level untuk petugas/admin
                        LevelType levelType = LevelType.fromString(rs.getString("level"));
                        String fullInfo = "Selamat Datang, " + levelType.toString() + " Id Anda  = " + rs.getString("id_petugas");

                        TXT_NIK_PETUGAS.setText(fullInfo);

                        String statusLevel = rs.getString("level");

                        if ("petugas".equalsIgnoreCase(statusLevel)) {
                            RBPetugas.setSelected(true);
                            RBADMIN.setSelected(false);
                        } else if ("administrator".equalsIgnoreCase(statusLevel)) {
                            RBPetugas.setSelected(false);
                            RBADMIN.setSelected(true);
                        } else {
                            System.out.println("Status tidak sesuai: " + statusLevel);
                        }

                    } else {
                        // Handle jika data tidak ditemukan
                        // Misalnya, menampilkan pesan kesalahan atau membersihkan kolom
                        clearPetugasFields();
                        JOptionPane.showMessageDialog(null, "Data petugas tidak ditemukan.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    private void editDataMasyarakat(int nik) {
        try {
            Connection kon = Koneksi.KoneksiDb();
            String query = "UPDATE tbl_masyarakat SET nama = ?, username = ?, password = ?, no_telepon = ? WHERE nik = ?";
            try ( PreparedStatement ps = kon.prepareStatement(query)) {
                ps.setString(1, TXT_NAMA.getText());
                ps.setString(2, TXT_USERNAME.getText());
                ps.setString(3, TXT_PASSWORD.getText());
                ps.setString(4, TXT_NOTELEPON.getText());
                ps.setInt(5, nik);

                int result = ps.executeUpdate();
                if (result > 0) {
                    JOptionPane.showMessageDialog(null, "Data masyarakat berhasil diupdate.");

                    // Kode tambahan setelah penyimpanan sukses
                    PanelHome form1 = new PanelHome(LevelLogin, nik, idPetugas);
                    Dashboard dashboard = (Dashboard) SwingUtilities.getWindowAncestor(this);

                    if (dashboard != null) {
                        dashboard.setForm(form1);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Gagal melakukan update data masyarakat.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

// Method untuk mengedit data petugas
    private void editDataPetugas(int idPetugas) {
        try {
            Connection kon = Koneksi.KoneksiDb();
            String query = "UPDATE tbl_petugas SET nama_petugas = ?, username = ?, password = ?, no_telepon = ?, level = ? WHERE id_petugas = ?";
            try ( PreparedStatement ps = kon.prepareStatement(query)) {
                ps.setString(1, TXT_NAMA.getText());
                ps.setString(2, TXT_USERNAME.getText());
                ps.setString(3, TXT_PASSWORD.getText());
                ps.setString(4, TXT_NOTELEPON.getText());

                // Mengambil nilai level dari RadioButton yang dipilih
                String level = RBPetugas.isSelected() ? "petugas" : "administrator";
                ps.setString(5, level);

                ps.setInt(6, idPetugas);

                int result = ps.executeUpdate();
                if (result > 0) {
                    JOptionPane.showMessageDialog(null, "Data petugas berhasil diupdate.");

                    // Kode tambahan setelah penyimpanan sukses
                    PanelHome form1 = new PanelHome(LevelLogin, nik, idPetugas);
                    Dashboard dashboard = (Dashboard) SwingUtilities.getWindowAncestor(this);

                    if (dashboard != null) {
                        dashboard.setForm(form1);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Gagal melakukan update data petugas.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }


private void editData() {
        if (LevelLogin == 3) {
            editDataMasyarakat(nik);
        } else if (LevelLogin == 1 || LevelLogin == 2) {
            editDataPetugas(idPetugas);
        }
    }

// Method untuk membersihkan kolom-kolom pada form petugas
    private void clearPetugasFields() {
        TXT_NIK_PETUGAS.setText("");
        TXT_NAMA.setText("");
        TXT_USERNAME.setText("");
        TXT_PASSWORD.setText("");
        TXT_NOTELEPON.setText("");

}

    public enum LevelType {
    PETUGAS("petugas"),
    ADMINISTRATOR("administrator");

    private final String level;

    LevelType(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }

    public static LevelType fromString(String level) {
        for (LevelType lt : LevelType.values()) {
            if (lt.level.equalsIgnoreCase(level)) {
                return lt;
            }
        }
        // Jika tidak ada enum yang cocok, Anda dapat mengembalikan nilai default atau null sesuai kebutuhan.
        // Sebagai contoh, kembalikan null jika tidak ada enum yang cocok.
        return null;
    }
}

/**
 * This method is called from within the constructor to initialize the form.
 * WARNING: Do NOT modify this code. The content of this method is always
 * regenerated by the Form Editor.
 */
@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        BG1 = new javax.swing.ButtonGroup();
        panelBorder1 = new Panel.PanelBorder();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        TXT_NAMA = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        TXT_USERNAME = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        TXT_PASSWORD = new javax.swing.JPasswordField();
        jPanel6 = new javax.swing.JPanel();
        TXT_NIK_PETUGAS = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        TXT_NOTELEPON = new javax.swing.JTextField();
        BTN_CANCEL = new javax.swing.JButton();
        BTN_TAMBAH = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        RBPetugas = new javax.swing.JRadioButton();
        RBADMIN = new javax.swing.JRadioButton();

        panelBorder1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(127, 127, 127));
        jLabel1.setText("Informasi Akun");

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setPreferredSize(new java.awt.Dimension(400, 350));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setPreferredSize(new java.awt.Dimension(250, 50));
        jPanel7.setLayout(new java.awt.BorderLayout(4, 4));

        jLabel2.setText("Nama");
        jPanel7.add(jLabel2, java.awt.BorderLayout.PAGE_START);
        jPanel7.add(TXT_NAMA, java.awt.BorderLayout.CENTER);

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setPreferredSize(new java.awt.Dimension(250, 50));
        jPanel10.setLayout(new java.awt.BorderLayout(4, 4));

        jLabel3.setText("Username");
        jPanel10.add(jLabel3, java.awt.BorderLayout.PAGE_START);
        jPanel10.add(TXT_USERNAME, java.awt.BorderLayout.CENTER);

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setPreferredSize(new java.awt.Dimension(250, 50));
        jPanel11.setLayout(new java.awt.BorderLayout(4, 4));

        jLabel6.setText("Password");
        jPanel11.add(jLabel6, java.awt.BorderLayout.PAGE_START);
        jPanel11.add(TXT_PASSWORD, java.awt.BorderLayout.CENTER);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setPreferredSize(new java.awt.Dimension(250, 50));

        TXT_NIK_PETUGAS.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        TXT_NIK_PETUGAS.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TXT_NIK_PETUGAS, javax.swing.GroupLayout.PREFERRED_SIZE, 459, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(TXT_NIK_PETUGAS, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setPreferredSize(new java.awt.Dimension(250, 50));
        jPanel12.setLayout(new java.awt.BorderLayout(4, 4));

        jLabel4.setText("No Telepon");
        jPanel12.add(jLabel4, java.awt.BorderLayout.PAGE_START);
        jPanel12.add(TXT_NOTELEPON, java.awt.BorderLayout.CENTER);

        BTN_CANCEL.setText("Kembali");
        BTN_CANCEL.setPreferredSize(new java.awt.Dimension(120, 30));
        BTN_CANCEL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTN_CANCELActionPerformed(evt);
            }
        });

        BTN_TAMBAH.setText("Simpan");
        BTN_TAMBAH.setPreferredSize(new java.awt.Dimension(120, 30));
        BTN_TAMBAH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTN_TAMBAHActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        BG1.add(RBPetugas);
        RBPetugas.setText("Petugas");

        BG1.add(RBADMIN);
        RBADMIN.setText("Administrator");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(RBPetugas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(RBADMIN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(RBPetugas, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
            .addComponent(RBADMIN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(BTN_CANCEL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(BTN_TAMBAH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(BTN_CANCEL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(BTN_TAMBAH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout panelBorder1Layout = new javax.swing.GroupLayout(panelBorder1);
        panelBorder1.setLayout(panelBorder1Layout);
        panelBorder1Layout.setHorizontalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBorder1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addContainerGap(726, Short.MAX_VALUE))
                    .addGroup(panelBorder1Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 465, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        panelBorder1Layout.setVerticalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelBorder1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(panelBorder1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(20, 20, 20))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void BTN_CANCELActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTN_CANCELActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BTN_CANCELActionPerformed

    private void BTN_TAMBAHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTN_TAMBAHActionPerformed
        editData();
        
    }//GEN-LAST:event_BTN_TAMBAHActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup BG1;
    private javax.swing.JButton BTN_CANCEL;
    private javax.swing.JButton BTN_TAMBAH;
    private javax.swing.JRadioButton RBADMIN;
    private javax.swing.JRadioButton RBPetugas;
    private javax.swing.JTextField TXT_NAMA;
    private javax.swing.JLabel TXT_NIK_PETUGAS;
    private javax.swing.JTextField TXT_NOTELEPON;
    private javax.swing.JPasswordField TXT_PASSWORD;
    private javax.swing.JTextField TXT_USERNAME;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private Panel.PanelBorder panelBorder1;
    // End of variables declaration//GEN-END:variables
}
