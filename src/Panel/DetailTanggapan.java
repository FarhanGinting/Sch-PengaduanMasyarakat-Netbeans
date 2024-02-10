/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Panel;

import Form.Dashboard;
import Koneksi.Koneksi;
import Model.StatusType;
import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
public class DetailTanggapan extends javax.swing.JPanel {

    /**
     * Creates new form DetailTanggapan
     */
    private int nik = 0;
    private final int level;
    private byte[] fotoByteArray;
    private JTable DG_TAMPIL;
    private int selectedIdPengaduan;
    private DefaultTableModel tableModel;
    private int idTanggapan;
    private int idPengaduan;
    private int idpetugas;

    public DetailTanggapan(int level, int nik, JTable dgTampil, int idTanggapan, int idPengaduan, int idpetugas) {
        initComponents();
        this.DG_TAMPIL = dgTampil;
        this.tableModel = new DefaultTableModel();
        this.idTanggapan = idTanggapan;
        this.idPengaduan = idPengaduan;

        this.idpetugas = idpetugas;
        this.level = level;
        tampilDataFromDatabase();
        boolean isLevel3 = (level == 3);

// Mengatur properti komponen-komponen sesuai dengan level
        if (isLevel3) {
            TGL_PENGADUAN.setEnabled(!isLevel3); // Jika level bukan 3, maka aktifkan komponen
            TGL_TANGGAPAN.setEnabled(!isLevel3);
            TXT_LAPORAN.setEditable(!isLevel3); // Jika level bukan 3, maka bisa diedit
            TXT_TANGGAPAN.setEditable(!isLevel3);
            JRadioButtonProses.setEnabled(false);
            JRadioButtonSelesai.setEnabled(false);
            BTN_EDIT.setEnabled(!isLevel3);

        }

        tampilkanDetailPengaduan();
        System.out.println("idPengaduan =   " + idPengaduan);
        System.out.println("idPetugas =   " + idpetugas);
        System.out.println("Level = " + level);
        spTable.setVerticalScrollBar(new ScrollBar());
        spTable.getVerticalScrollBar().setBackground(Color.WHITE);
        spTable.getViewport().setBackground(Color.WHITE);
        spTable1.setVerticalScrollBar(new ScrollBar());
        spTable1.getVerticalScrollBar().setBackground(Color.WHITE);
        spTable1.getViewport().setBackground(Color.WHITE);

        BTN_CANCEL.addActionListener(e -> {
            if (isLevel3) {
                PanelHome form1 = new PanelHome(level, nik, idpetugas);
                Dashboard dashboard = (Dashboard) SwingUtilities.getWindowAncestor(this);

                if (dashboard != null) {
                    dashboard.setForm(form1);
                }
            } else {
                    TabelTanggapan form1 = new TabelTanggapan(level, nik, idpetugas);
                    Dashboard dashboard = (Dashboard) SwingUtilities.getWindowAncestor(this);

                    if (dashboard != null) {
                        dashboard.setForm(form1);
                    }
            }
        });

    }

    public void tampilkanDetailPengaduan() {
        Connection kon = Koneksi.KoneksiDb();

        try {
            // Query untuk mendapatkan data dari tbl_tanggapan
            String sqlTanggapan = "SELECT t.id_tanggapan, t.id_pengaduan, t.tgl_tanggapan, t.tanggapan, p.nama_petugas "
                    + "FROM tbl_tanggapan t "
                    + "INNER JOIN tbl_petugas p ON t.id_petugas = p.id_petugas "
                    + "WHERE t.id_tanggapan = ?";

            try ( PreparedStatement psTanggapan = kon.prepareStatement(sqlTanggapan)) {
                psTanggapan.setInt(1, idTanggapan);

                try ( ResultSet rsTanggapan = psTanggapan.executeQuery()) {
                    if (rsTanggapan.next()) {
                        // Ambil data dari hasil query tbl_tanggapan
                        int idTanggapan = rsTanggapan.getInt("id_tanggapan");
                        int idPengaduan = rsTanggapan.getInt("id_pengaduan");
                        String tglTanggapan = rsTanggapan.getString("tgl_tanggapan");
                        String tanggapan = rsTanggapan.getString("tanggapan");
                        String namaPetugas = rsTanggapan.getString("nama_petugas");

                        // Tampilkan data tbl_tanggapan
                        TXT_IDTANGGAPAN.setText("ID Tanggapan        : " + idTanggapan);
                        TGL_TANGGAPAN.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(tglTanggapan));
                        TXT_TANGGAPAN.setText(tanggapan);
                        TXT_IDPETUGAS.setText("Nama Petugas       : " + namaPetugas);

                        // Query untuk mendapatkan data dari tbl_pengaduan
                        String sqlPengaduan = "SELECT p.id_pengaduan, m.nama, p.tgl_pengaduan, p.isi_laporan, p.foto, p.status "
                                + "FROM tbl_pengaduan p "
                                + "INNER JOIN tbl_masyarakat m ON p.nik = m.nik "
                                + "WHERE p.id_pengaduan = ?";

                        try ( PreparedStatement psPengaduan = kon.prepareStatement(sqlPengaduan)) {
                            psPengaduan.setInt(1, idPengaduan);

                            try ( ResultSet rsPengaduan = psPengaduan.executeQuery()) {
                                if (rsPengaduan.next()) {
                                    // Ambil data dari hasil query tbl_pengaduan
                                    int idPengaduanP = rsPengaduan.getInt("id_pengaduan");
                                    String namaMasyarakat = rsPengaduan.getString("nama");
                                    String tglPengaduan = rsPengaduan.getString("tgl_pengaduan");
                                    String isiLaporan = rsPengaduan.getString("isi_laporan");
                                    byte[] fotoByteArray = rsPengaduan.getBytes("foto");
                                    String statusPengaduan = rsPengaduan.getString("status");

                                    // Set data ke komponen-komponen di Panel
                                    TXT_IDPENGADUAN.setText("ID Pengaduan        : " + idPengaduan);
                                    TXT_NIK.setText("Nama Masyarakat : " + namaMasyarakat);
                                    TGL_PENGADUAN.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(tglPengaduan));
                                    TXT_LAPORAN.setText(isiLaporan);
                                    ImageIcon imageIcon = new ImageIcon(fotoByteArray);
                                    lb_gambar.setIcon(imageIcon);

                                    // Set status pada JRadioButton
                                    if ("proses".equalsIgnoreCase(statusPengaduan)) {
                                        JRadioButtonProses.setSelected(true);
                                        JRadioButtonSelesai.setSelected(false);
                                    } else if ("selesai".equalsIgnoreCase(statusPengaduan)) {
                                        JRadioButtonProses.setSelected(false);
                                        JRadioButtonSelesai.setSelected(true);
                                    } else {
                                        System.out.println("Status tidak sesuai: " + statusPengaduan);
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "Pengaduan tidak ditemukan.");
                                }
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Tanggapan tidak ditemukan.");
                    }
                }
            }
        } catch (SQLException | ParseException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public static String getFormattedDateFromChooser(JDateChooser dateChooser) {
        // Mendapatkan tanggal dari komponen JDateChooser
        Date selectedDate = dateChooser.getDate();

        // Mengatur format tanggal yang diinginkan
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // Mengonversi tanggal ke string dengan format yang diinginkan
        String formattedDate = dateFormat.format(selectedDate);

        return formattedDate;
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
        BTN_EDIT = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        TGL_TANGGAPAN = new com.toedter.calendar.JDateChooser();
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
        JRadioButtonSelesai = new javax.swing.JRadioButton();
        TXT_IDPETUGAS = new javax.swing.JLabel();
        lb_gambar = new javax.swing.JLabel();
        TXT_IDTANGGAPAN = new javax.swing.JLabel();

        panelBorder1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(127, 127, 127));
        jLabel1.setText("Detail Laporan");

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

        BTN_EDIT.setText("Simpan");
        BTN_EDIT.setPreferredSize(new java.awt.Dimension(120, 30));
        BTN_EDIT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTN_EDITActionPerformed(evt);
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

        buttonGroup.add(JRadioButtonSelesai);
        JRadioButtonSelesai.setText("Selesai");

        TXT_IDPETUGAS.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        TXT_IDPETUGAS.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

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
                                .addComponent(JRadioButtonSelesai)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(BTN_CANCEL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)
                                .addComponent(BTN_EDIT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
                            .addComponent(TXT_IDPETUGAS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(TXT_IDPENGADUAN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(TXT_NIK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(TXT_IDPETUGAS, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(TXT_IDPENGADUAN, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(TXT_NIK, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BTN_CANCEL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BTN_EDIT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JRadioButtonSelesai)
                    .addComponent(JRadioButtonProses))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lb_gambar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_gambar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        TXT_IDTANGGAPAN.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        TXT_IDTANGGAPAN.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout panelBorder1Layout = new javax.swing.GroupLayout(panelBorder1);
        panelBorder1.setLayout(panelBorder1Layout);
        panelBorder1Layout.setHorizontalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBorder1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelBorder1Layout.createSequentialGroup()
                        .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 465, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TXT_IDTANGGAPAN, javax.swing.GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE))
                        .addGap(70, 70, 70)
                        .addComponent(lb_gambar, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(71, 71, 71))))
        );
        panelBorder1Layout.setVerticalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(TXT_IDTANGGAPAN, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBorder1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lb_gambar, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(114, 114, 114))
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
                .addComponent(panelBorder1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void BTN_CANCELActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTN_CANCELActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BTN_CANCELActionPerformed

    private void BTN_EDITActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTN_EDITActionPerformed
        int updatedLevel = BTN_EDITActionPerformed(evt, level);

        TabelTanggapan form1 = new TabelTanggapan(updatedLevel, nik, idpetugas);
        Dashboard dashboard = (Dashboard) SwingUtilities.getWindowAncestor(this);

        java.util.Date tgltTanggapanBaru = TGL_TANGGAPAN.getDate();
        String isiTanggapanBaru = TXT_TANGGAPAN.getText();

        // Konversi java.util.Date ke java.sql.Date
        java.sql.Date sqlDate = new java.sql.Date(tgltTanggapanBaru.getTime());

        // Panggil metode untuk melakukan update ke database
        updateDataTanggapan(idTanggapan, idPengaduan, sqlDate, isiTanggapanBaru, idpetugas);
    }//GEN-LAST:event_BTN_EDITActionPerformed

    private void updateDataTanggapan(int idTanggapan, int idPengaduan, java.sql.Date tglTanggapanBaru, String tanggapanBaru, int idPetugas) {
        Connection kon = Koneksi.KoneksiDb();
        String sql;

        boolean updateSuccess = false; // Tandai apakah proses update berhasil

        sql = "UPDATE tbl_tanggapan SET id_pengaduan = ?, tgl_tanggapan = ?, tanggapan = ?, id_petugas = ? WHERE id_tanggapan = ?";

        try ( PreparedStatement ps = kon.prepareStatement(sql)) {
            // Set nilai parameter
            ps.setInt(1, idPengaduan);
            ps.setDate(2, tglTanggapanBaru);
            ps.setString(3, tanggapanBaru);
            ps.setInt(4, idPetugas);
            ps.setInt(5, idTanggapan);

            // Eksekusi pernyataan SQL
            int result = ps.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(null, "Data tanggapan berhasil diupdate.");
                updateSuccess = true; // Update berhasil

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
                        TabelTanggapan form1 = new TabelTanggapan(level, nik, idpetugas);
                        Dashboard dashboard = (Dashboard) SwingUtilities.getWindowAncestor(this);

                        if (dashboard != null) {
                            dashboard.setForm(form1);

                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Gagal memperbarui status pengaduan.");
                    }
                }
            }
        } catch (SQLException e) {
            // Log the exception for debugging
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal melakukan update data tanggapan. Error: " + e.getMessage());
        }
    }

//    private void updateDataStatusPengaduan(int idPengaduan, String statusPengaduanBaru) {
//        Connection kon = Koneksi.KoneksiDb();
//        String sql = "UPDATE tbl_pengaduan SET status = ? WHERE id_pengaduan = ?";
//
//        try ( PreparedStatement ps = kon.prepareStatement(sql)) {
//            // Set nilai parameter
//            ps.setString(1, statusPengaduanBaru);
//            ps.setInt(2, idPengaduan);
//
//            // Eksekusi pernyataan SQL
//            int result = ps.executeUpdate();
//
//            if (result > 0) {
//                JOptionPane.showMessageDialog(null, "Status pengaduan berhasil diupdate.");
//            } else {
//                JOptionPane.showMessageDialog(null, "Gagal melakukan update status pengaduan.");
//            }
//        } catch (SQLException e) {
//            // Log the exception for debugging
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(null, "Gagal melakukan update status pengaduan. Error: " + e.getMessage());
//        }
//    }
    private int BTN_EDITActionPerformed(java.awt.event.ActionEvent evt, int level) {
        // Ambil nilai dari komponen-komponen yang ingin diupdate
        java.util.Date tgltTanggapanBaru = TGL_TANGGAPAN.getDate();
        String isiTanggapanBaru = TXT_TANGGAPAN.getText();

        // Konversi java.util.Date ke java.sql.Date
        java.sql.Date sqlDate = new java.sql.Date(tgltTanggapanBaru.getTime());

        // Mendapatkan nilai idPengaduan (sesuai dengan logika aplikasi Anda)
        int idpengaduan = idPengaduan; // Anda perlu menggantinya sesuai dengan logika aplikasi Anda

        // Menampilkan ID petugas sebelum melakukan pembaruan
        System.out.println("ID Petugas: " + idpetugas);

        // Kembalikan nilai idPengaduan
        return idpengaduan;
    }

    private void tampilDataFromDatabase() {
        try {
            Connection kon = Koneksi.KoneksiDb();
            Statement st = kon.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM tbl_pengaduan");

            // Membersihkan model tabel sebelum menambahkan data baru
            DefaultTableModel tabModel = (DefaultTableModel) DG_TAMPIL.getModel();
            tabModel.setRowCount(0);

            while (rs.next()) {
                String statusString = rs.getString("status");
                StatusType status = StatusType.bacaStatusDariDatabase(statusString);

                Object[] data = {
                    rs.getString("id_pengaduan"),
                    rs.getString("tgl_pengaduan"),
                    rs.getString("nik"),
                    rs.getString("isi_laporan"),
                    status
                };

                tabModel.addRow(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BTN_CANCEL;
    private javax.swing.JButton BTN_EDIT;
    private javax.swing.JRadioButton JRadioButtonProses;
    private javax.swing.JRadioButton JRadioButtonSelesai;
    private com.toedter.calendar.JDateChooser TGL_PENGADUAN;
    private com.toedter.calendar.JDateChooser TGL_TANGGAPAN;
    private javax.swing.JLabel TXT_IDPENGADUAN;
    private javax.swing.JLabel TXT_IDPETUGAS;
    private javax.swing.JLabel TXT_IDTANGGAPAN;
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
    private javax.swing.JLabel lb_gambar;
    private Panel.PanelBorder panelBorder1;
    private javax.swing.JScrollPane spTable;
    private javax.swing.JScrollPane spTable1;
    // End of variables declaration//GEN-END:variables
}
