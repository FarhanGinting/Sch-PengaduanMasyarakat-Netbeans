/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Panel;

import Form.Dashboard;
import Koneksi.Koneksi;
import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author FARHAN
 */
public class TanggapiLaporan extends javax.swing.JPanel {

    /**
     * Creates new form TanggapiLaporan
     */
    private int nik = 0;
    private final int level;
    private byte[] fotoByteArray;
    private JTable DG_TAMPIL;
    private int selectedIdPengaduan;
    private DefaultTableModel tableModel;
    private int idPengaduan;
    private int idpetugas = 0;

    int maxLabelLength = 15;

    public TanggapiLaporan(int level, int nik, JTable dgTampil, int idPengaduan, int idpetugas) {
        initComponents();
        this.nik = nik;
        this.DG_TAMPIL = dgTampil;
        this.tableModel = new DefaultTableModel();
        this.idPengaduan = idPengaduan;
        this.idpetugas = idpetugas;
        this.level = level;
        System.out.println("Level: " + level);

        try {
            Connection kon = Koneksi.KoneksiDb();
            String query = "SELECT tbl_tanggapan.id_tanggapan, tbl_tanggapan.tgl_tanggapan, tbl_petugas.nama_petugas, tbl_tanggapan.tanggapan FROM tbl_tanggapan INNER JOIN tbl_petugas ON tbl_tanggapan.id_petugas = tbl_petugas.id_petugas WHERE tbl_tanggapan.id_petugas = ?";
            try ( PreparedStatement pstmt = kon.prepareStatement(query)) {
                pstmt.setInt(1, idpetugas); // Gantilah sesuai dengan nilai idPetugas yang Anda miliki
                try ( ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String namaPetugas = rs.getString("nama_petugas");
                        TXT_IDPETUGAS.setText("Nama Petugas       : " + namaPetugas);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        BTN_CANCEL.addActionListener(e -> {
            PanelHome form1 = new PanelHome(level, nik, idpetugas);
            Dashboard dashboard = (Dashboard) SwingUtilities.getWindowAncestor(this);

            if (dashboard != null) {
                dashboard.setForm(form1);
            }
        });

        spTable.setVerticalScrollBar(new ScrollBar());
        spTable.getVerticalScrollBar().setBackground(Color.WHITE);
        spTable.getViewport().setBackground(Color.WHITE);
        spTable1.setVerticalScrollBar(new ScrollBar());
        spTable1.getVerticalScrollBar().setBackground(Color.WHITE);
        spTable1.getViewport().setBackground(Color.WHITE);
        tampilkanDetailPengaduan();
    }

    public void tampilkanDetailPengaduan() {
        Connection kon = Koneksi.KoneksiDb();

        try {
            String sql = "SELECT p.id_pengaduan, m.nama, p.tgl_pengaduan, p.isi_laporan, p.foto "
                    + "FROM tbl_pengaduan p "
                    + "INNER JOIN tbl_masyarakat m ON p.nik = m.nik "
                    + "WHERE p.id_pengaduan = ?";
            try ( PreparedStatement ps = kon.prepareStatement(sql)) {
                ps.setInt(1, idPengaduan);

                try ( ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        // Ambil data dari hasil query
                        int idPengaduan = rs.getInt("id_pengaduan");
                        String namaMasyarakat = rs.getString("nama");
                        String tglPengaduan = rs.getString("tgl_pengaduan");
                        String isiLaporan = rs.getString("isi_laporan");
                        byte[] fotoByteArray = rs.getBytes("foto");

                        // Set data ke komponen-komponen di Panel
                        TXT_IDPENGADUAN.setText("ID Pengaduan        : " + idPengaduan);
                        TXT_NIK.setText("Nama Masyarakat : " + namaMasyarakat);
                        TGL_PENGADUAN.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(tglPengaduan));
                        TXT_LAPORAN.setText(isiLaporan);

                        // Menampilkan foto
                        ImageIcon imageIcon = new ImageIcon(fotoByteArray);
                        lb_gambar.setIcon(imageIcon);

                    } else {
                        JOptionPane.showMessageDialog(null, "Pengaduan tidak ditemukan.");
                    }
                }
            }
        } catch (SQLException | ParseException e) {
            JOptionPane.showMessageDialog(null, e);
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

        buttonGroup = new javax.swing.ButtonGroup();
        panelBorder1 = new Panel.PanelBorder();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        spTable = new javax.swing.JScrollPane();
        TXT_TANGGAPAN = new javax.swing.JTextArea();
        BTN_CANCEL = new javax.swing.JButton();
        BTN_TAMBAH = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        TGL_TANGGAPAN = new com.toedter.calendar.JDateChooser();
        TXT_IDPETUGAS = new javax.swing.JLabel();
        TXT_IDPENGADUAN = new javax.swing.JLabel();
        TXT_NIK = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        TGL_PENGADUAN = new com.toedter.calendar.JDateChooser();
        jPanel17 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        spTable1 = new javax.swing.JScrollPane();
        TXT_LAPORAN = new javax.swing.JTextArea();
        JRadioButtonProses = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        lb_gambar = new javax.swing.JLabel();

        panelBorder1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(127, 127, 127));
        jLabel1.setText("Tanggapi Laporan");

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setPreferredSize(new java.awt.Dimension(400, 350));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setPreferredSize(new java.awt.Dimension(250, 110));
        jPanel8.setLayout(new java.awt.BorderLayout(4, 0));

        jLabel4.setText("Tanggapan");
        jPanel8.add(jLabel4, java.awt.BorderLayout.CENTER);

        spTable.setPreferredSize(new java.awt.Dimension(234, 80));

        TXT_TANGGAPAN.setColumns(20);
        TXT_TANGGAPAN.setRows(5);
        spTable.setViewportView(TXT_TANGGAPAN);

        jPanel8.add(spTable, java.awt.BorderLayout.PAGE_END);

        BTN_CANCEL.setText("Kembali");
        BTN_CANCEL.setPreferredSize(new java.awt.Dimension(120, 30));
        BTN_CANCEL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTN_CANCELActionPerformed(evt);
            }
        });

        BTN_TAMBAH.setText("Tambah");
        BTN_TAMBAH.setPreferredSize(new java.awt.Dimension(120, 30));
        BTN_TAMBAH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTN_TAMBAHActionPerformed(evt);
            }
        });

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setPreferredSize(new java.awt.Dimension(250, 50));
        jPanel7.setLayout(new java.awt.BorderLayout(4, 4));

        jLabel2.setText("Tanggal Tanggapan");
        jPanel7.add(jLabel2, java.awt.BorderLayout.PAGE_START);

        TGL_TANGGAPAN.setBackground(new java.awt.Color(255, 255, 255));
        TGL_TANGGAPAN.setPreferredSize(new java.awt.Dimension(90, 30));
        jPanel7.add(TGL_TANGGAPAN, java.awt.BorderLayout.CENTER);

        TXT_IDPETUGAS.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        TXT_IDPETUGAS.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        TXT_IDPENGADUAN.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        TXT_IDPENGADUAN.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        TXT_NIK.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        TXT_NIK.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));
        jPanel15.setPreferredSize(new java.awt.Dimension(250, 50));
        jPanel15.setLayout(new java.awt.BorderLayout(4, 4));

        jLabel5.setText("Tanggal Pengaduan");
        jPanel15.add(jLabel5, java.awt.BorderLayout.PAGE_START);

        TGL_PENGADUAN.setBackground(new java.awt.Color(255, 255, 255));
        TGL_PENGADUAN.setPreferredSize(new java.awt.Dimension(90, 30));
        jPanel15.add(TGL_PENGADUAN, java.awt.BorderLayout.CENTER);

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));
        jPanel17.setPreferredSize(new java.awt.Dimension(250, 110));
        jPanel17.setLayout(new java.awt.BorderLayout(4, 0));

        jLabel6.setText("Isi Laporan");
        jPanel17.add(jLabel6, java.awt.BorderLayout.CENTER);

        spTable1.setPreferredSize(new java.awt.Dimension(234, 80));

        TXT_LAPORAN.setColumns(20);
        TXT_LAPORAN.setRows(5);
        spTable1.setViewportView(TXT_LAPORAN);

        jPanel17.add(spTable1, java.awt.BorderLayout.PAGE_END);

        buttonGroup.add(JRadioButtonProses);
        JRadioButtonProses.setText("Proses");

        buttonGroup.add(jRadioButton3);
        jRadioButton3.setText("Selesai");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(JRadioButtonProses)
                                .addGap(18, 18, 18)
                                .addComponent(jRadioButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(BTN_CANCEL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)
                                .addComponent(BTN_TAMBAH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(TXT_IDPETUGAS, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TXT_IDPENGADUAN, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 459, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TXT_NIK, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 459, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 459, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(TXT_IDPETUGAS, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(TXT_IDPENGADUAN, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(TXT_NIK, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BTN_CANCEL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BTN_TAMBAH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadioButton3)
                    .addComponent(JRadioButtonProses))
                .addGap(26, 26, 26))
        );

        lb_gambar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_gambar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout panelBorder1Layout = new javax.swing.GroupLayout(panelBorder1);
        panelBorder1.setLayout(panelBorder1Layout);
        panelBorder1Layout.setHorizontalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBorder1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addContainerGap(764, Short.MAX_VALUE))
                    .addGroup(panelBorder1Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 465, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                        .addComponent(lb_gambar, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(70, 70, 70))))
        );
        panelBorder1Layout.setVerticalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBorder1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 522, Short.MAX_VALUE)
                        .addGap(20, 20, 20))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBorder1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lb_gambar, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(112, 112, 112))))
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

    private void BTN_TAMBAHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTN_TAMBAHActionPerformed
        Connection kon = Koneksi.KoneksiDb();
        String tglTanggapan = getFormattedDateFromChooser(TGL_TANGGAPAN); // Mengasumsikan Anda memiliki metode untuk mendapatkan tanggal saat ini yang diformat
        String tanggapan = TXT_TANGGAPAN.getText(); // Mengasumsikan Anda memiliki kolom teks untuk tanggapan

// Tambahkan kolom 'id_petugas' ke query dan gunakan "RETURN_GENERATED_KEYS"
        String sqlTanggapan = "INSERT INTO tbl_tanggapan (id_tanggapan, id_pengaduan, tgl_tanggapan, tanggapan, id_petugas) VALUES (DEFAULT, ?, ?, ?, ?)";

        try ( PreparedStatement psTanggapan = kon.prepareStatement(sqlTanggapan, PreparedStatement.RETURN_GENERATED_KEYS)) {
            psTanggapan.setInt(1, idPengaduan); // Mengasumsikan Anda memiliki nilai 'idPengaduan' yang tersedia
            psTanggapan.setString(2, tglTanggapan);
            psTanggapan.setString(3, tanggapan);
            psTanggapan.setInt(4, idpetugas); // Mengasumsikan Anda memiliki nilai 'idPetugas' yang tersedia

            int resultTanggapan = psTanggapan.executeUpdate();

            if (resultTanggapan > 0) {
                // Dapatkan nilai auto-increment yang dihasilkan
                try ( ResultSet generatedKeysTanggapan = psTanggapan.getGeneratedKeys()) {
                    if (generatedKeysTanggapan.next()) {
                        int generatedIdTanggapan = generatedKeysTanggapan.getInt(1);
                        JOptionPane.showMessageDialog(null, "Tanggapan berhasil disimpan dengan ID: " + generatedIdTanggapan);

                        // Lakukan update status di tbl_pengaduan
                        String statusPengaduan = "SELESAI"; // Atur status berdasarkan JRadioButton yang dipilih
                        if (JRadioButtonProses.isSelected()) {
                            statusPengaduan = "PROSES";
                        }

                        String sqlUpdateStatus = "UPDATE tbl_pengaduan SET status = ? WHERE id_pengaduan = ?";
                        try ( PreparedStatement psUpdateStatus = kon.prepareStatement(sqlUpdateStatus)) {
                            psUpdateStatus.setString(1, statusPengaduan);
                            psUpdateStatus.setInt(2, idPengaduan);

                            int resultUpdateStatus = psUpdateStatus.executeUpdate();

                            if (resultUpdateStatus > 0) {

                                // Lakukan hal-hal lain setelah penyimpanan sukses
                                PanelHome form1 = new PanelHome(level, nik, idpetugas);
                                Dashboard dashboard = (Dashboard) SwingUtilities.getWindowAncestor(this);

                                if (dashboard != null) {
                                    dashboard.setForm(form1);
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Gagal memperbarui status pengaduan.");
                            }
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Tanggapan gagal disimpan. Silakan coba lagi.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }//GEN-LAST:event_BTN_TAMBAHActionPerformed

    private void BTN_CANCELActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTN_CANCELActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BTN_CANCELActionPerformed

    public static String getFormattedDateFromChooser(JDateChooser dateChooser) {
        // Mendapatkan tanggal dari komponen JDateChooser
        Date selectedDate = dateChooser.getDate();

        // Mengatur format tanggal yang diinginkan
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // Mengonversi tanggal ke string dengan format yang diinginkan
        String formattedDate = dateFormat.format(selectedDate);

        return formattedDate;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BTN_CANCEL;
    private javax.swing.JButton BTN_TAMBAH;
    private javax.swing.JRadioButton JRadioButtonProses;
    private com.toedter.calendar.JDateChooser TGL_PENGADUAN;
    private com.toedter.calendar.JDateChooser TGL_TANGGAPAN;
    private javax.swing.JLabel TXT_IDPENGADUAN;
    private javax.swing.JLabel TXT_IDPETUGAS;
    private javax.swing.JTextArea TXT_LAPORAN;
    private javax.swing.JLabel TXT_NIK;
    private javax.swing.JTextArea TXT_TANGGAPAN;
    private javax.swing.ButtonGroup buttonGroup;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JLabel lb_gambar;
    private Panel.PanelBorder panelBorder1;
    private javax.swing.JScrollPane spTable;
    private javax.swing.JScrollPane spTable1;
    // End of variables declaration//GEN-END:variables
}
