/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Panel;

import Form.Dashboard;
import Koneksi.Koneksi;
import Model.StatusType;
import static Panel.TambahPengaduan.getFormattedCurrentDate;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author FARHAN
 */
public class DetaiPengaduan extends javax.swing.JPanel {

    private int nik = 0;
    private final int level;
    private byte[] fotoByteArray;
    private JTable DG_TAMPIL;
    private int selectedIdPengaduan;
    private DefaultTableModel tableModel;
    private int idPengaduan;
    private int idpetugas = 0;
    

    /**
     * Creates new form DetaiPengaduan
     */
    public DetaiPengaduan(int level, int nik, JTable dgTampil, int idPengaduan, int idpetugas) {
        this.nik = nik;
        this.idpetugas = idpetugas;
        this.DG_TAMPIL = dgTampil;
        this.tableModel = new DefaultTableModel();
        this.idPengaduan = idPengaduan;
        this.level = level;
        
        System.out.println("Level: " + level);

        DG_TAMPIL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        initComponents();
        TXT_NIK.setText("MYRK               :   " + nik);

        BTN_CANCEL.addActionListener(e -> {
            PanelHome form1 = new PanelHome(level, nik, idpetugas);
            Dashboard dashboard = (Dashboard) SwingUtilities.getWindowAncestor(this);

            if (dashboard != null) {
                dashboard.setForm(form1);
            }
        });

        TXT_PATHFOTO.setEditable(false);
        spTable.setVerticalScrollBar(new ScrollBar());
        spTable.getVerticalScrollBar().setBackground(Color.WHITE);
        spTable.getViewport().setBackground(Color.WHITE);
        tampilkanDetailPengaduan();
    }

    public void tampilkanDetailPengaduan() {
        Connection kon = Koneksi.KoneksiDb();

        try {
            String sql = "SELECT P.nik, P.tgl_pengaduan, P.isi_laporan, P.foto, M.nama "
                    + "FROM tbl_pengaduan P "
                    + "INNER JOIN tbl_masyarakat M ON P.nik = M.nik "
                    + "WHERE P.id_pengaduan = ?";
            try ( PreparedStatement ps = kon.prepareStatement(sql)) {
                ps.setInt(1, idPengaduan);

                try ( ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        // Ambil data dari hasil query
                        String nik = rs.getString("nik");
                        String tglPengaduan = rs.getString("tgl_pengaduan");
                        String isiLaporan = rs.getString("isi_laporan");
                        byte[] fotoByteArray = rs.getBytes("foto");
                        String nama = rs.getString("nama");

                        // Set data ke komponen-komponen di Panel
                        TXT_NIK.setText("MYRK               :   " + nama);
                        TGL_PENGADUAN.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(tglPengaduan));
                        TXT_LAPORAN.setText(isiLaporan);

                        int labelWidth = 320;
                        int labelHeight = 320;

                        // UKURAN GAMBAR ASLI
                        ImageIcon imageIcon = new ImageIcon(fotoByteArray);
                        int imageWidth = imageIcon.getIconWidth();
                        int imageHeight = imageIcon.getIconHeight();

                        // HITUNG SKALA UNTUK GAMBAR YANG BARU
                        double scaleX = (double) labelWidth / (double) imageWidth;
                        double scaleY = (double) labelHeight / (double) imageHeight;
                        double scale = Math.min(scaleX, scaleY);

                        //UBAH UKURAN GAMBAR DENGAN SKALA YANG SUDAH DI HITUNG
                        Image scaledImage = imageIcon.getImage().getScaledInstance((int) (scale * imageWidth), (int) (scale * imageHeight), Image.SCALE_SMOOTH);

                        // MENAMPILKAN GAMBAR KE LABEL
                        lb_gambar.setIcon(new ImageIcon(new ImageIcon(scaledImage).getImage().getScaledInstance(320, 320, Image.SCALE_SMOOTH)));
                    } else {
                        JOptionPane.showMessageDialog(null, "Pengaduan tidak ditemukan.");
                    }
                }
            }
        } catch (SQLException | ParseException e) {
            JOptionPane.showMessageDialog(null, e);
        }
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelBorder1 = new Panel.PanelBorder();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        TGL_PENGADUAN = new com.toedter.calendar.JDateChooser();
        jPanel6 = new javax.swing.JPanel();
        TXT_NIK = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        spTable = new javax.swing.JScrollPane();
        TXT_LAPORAN = new javax.swing.JTextArea();
        jPanel9 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        TXT_PATHFOTO = new javax.swing.JTextField();
        BTN_FOTO = new javax.swing.JButton();
        BTN_CANCEL = new javax.swing.JButton();
        BTN_EDIT = new javax.swing.JButton();
        lb_gambar = new javax.swing.JLabel();

        panelBorder1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(127, 127, 127));
        jLabel1.setText("Detail Pengaduan");

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setPreferredSize(new java.awt.Dimension(400, 350));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setPreferredSize(new java.awt.Dimension(250, 50));
        jPanel7.setLayout(new java.awt.BorderLayout(4, 4));

        jLabel2.setText("Tanggal Pengaduan");
        jPanel7.add(jLabel2, java.awt.BorderLayout.PAGE_START);

        TGL_PENGADUAN.setBackground(new java.awt.Color(255, 255, 255));
        TGL_PENGADUAN.setPreferredSize(new java.awt.Dimension(90, 30));
        jPanel7.add(TGL_PENGADUAN, java.awt.BorderLayout.CENTER);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setPreferredSize(new java.awt.Dimension(250, 50));

        TXT_NIK.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        TXT_NIK.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TXT_NIK, javax.swing.GroupLayout.PREFERRED_SIZE, 459, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(TXT_NIK, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setPreferredSize(new java.awt.Dimension(250, 110));
        jPanel8.setLayout(new java.awt.BorderLayout(4, 4));

        jLabel4.setText("Isi Laporan");
        jPanel8.add(jLabel4, java.awt.BorderLayout.CENTER);

        spTable.setPreferredSize(new java.awt.Dimension(234, 80));

        TXT_LAPORAN.setColumns(20);
        TXT_LAPORAN.setRows(5);
        spTable.setViewportView(TXT_LAPORAN);

        jPanel8.add(spTable, java.awt.BorderLayout.PAGE_END);

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setPreferredSize(new java.awt.Dimension(275, 50));
        jPanel9.setLayout(new java.awt.BorderLayout(0, 10));

        jLabel5.setText("Foto");
        jPanel9.add(jLabel5, java.awt.BorderLayout.CENTER);

        TXT_PATHFOTO.setPreferredSize(new java.awt.Dimension(90, 30));
        jPanel9.add(TXT_PATHFOTO, java.awt.BorderLayout.PAGE_END);

        BTN_FOTO.setText("Ganti Foto");
        BTN_FOTO.setPreferredSize(new java.awt.Dimension(120, 20));
        BTN_FOTO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTN_FOTOActionPerformed(evt);
            }
        });
        jPanel9.add(BTN_FOTO, java.awt.BorderLayout.LINE_END);

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

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(BTN_CANCEL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(BTN_EDIT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BTN_CANCEL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BTN_EDIT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
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
                    .addComponent(jLabel1)
                    .addGroup(panelBorder1Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 465, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(70, 70, 70)
                        .addComponent(lb_gambar, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(70, 70, 70))
        );
        panelBorder1Layout.setVerticalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBorder1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelBorder1Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(lb_gambar, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(92, 92, 92))))
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

    private void BTN_EDITActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTN_EDITActionPerformed
        // Ambil nilai dari komponen-komponen yang ingin diupdate
        int updatedLevel = BTN_EDITActionPerformed(evt, level);

        PanelHome form1 = new PanelHome(updatedLevel, nik, idpetugas);
        Dashboard dashboard = (Dashboard) SwingUtilities.getWindowAncestor(this);

        java.util.Date tglPengaduanBaru = TGL_PENGADUAN.getDate();
        String isiLaporanBaru = TXT_LAPORAN.getText();
        byte[] fotoBaru = choosePhoto(); // Menggunakan metode baru

        // Konversi java.util.Date ke java.sql.Date
        java.sql.Date sqlDate = new java.sql.Date(tglPengaduanBaru.getTime());

        // Panggil metode untuk melakukan update ke database
        updateDataPengaduan(idPengaduan, sqlDate, isiLaporanBaru, fotoBaru);


    }//GEN-LAST:event_BTN_EDITActionPerformed

    private void BTN_FOTOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTN_FOTOActionPerformed
        setFotoFromFileChooser();
    }//GEN-LAST:event_BTN_FOTOActionPerformed

    private int BTN_EDITActionPerformed(java.awt.event.ActionEvent evt, int level) {
        // Ambil nilai dari komponen-komponen yang ingin diupdate
        java.util.Date tglPengaduanBaru = TGL_PENGADUAN.getDate();
        String isiLaporanBaru = TXT_LAPORAN.getText();
        byte[] fotoBaru = choosePhoto();

        // Konversi java.util.Date ke java.sql.Date
        java.sql.Date sqlDate = new java.sql.Date(tglPengaduanBaru.getTime());

        System.out.println("Level: " + level);

        // Kembalikan nilai level
        return level;

    }

    public byte[] setFotoFromFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("'.IMAGE", "jpg", "gif", "png");
        fileChooser.addChoosableFileFilter(filter);
        int result = fileChooser.showSaveDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String path = selectedFile.getAbsolutePath();

            try {
                // Baca gambar dari file
                byte[] img = Files.readAllBytes(selectedFile.toPath());

                // UKURAN YANG AKAN DI TAMPILKAN
                int labelWidth = 320;
                int labelHeight = 320;

                // UKURAN GAMBAR ASLI
                ImageIcon imageIcon = new ImageIcon(img);
                int imageWidth = imageIcon.getIconWidth();
                int imageHeight = imageIcon.getIconHeight();

                // HITUNG SKALA UNTUK GAMBAR YANG BARU
                double scaleX = (double) labelWidth / (double) imageWidth;
                double scaleY = (double) labelHeight / (double) imageHeight;
                double scale = Math.min(scaleX, scaleY);

                //UBAH UKURAN GAMBAR DENGAN SKALA YANG SUDAH DI HITUNG
                Image scaledImage = imageIcon.getImage().getScaledInstance((int) (scale * imageWidth), (int) (scale * imageHeight), Image.SCALE_SMOOTH);

                // MENAMPILKAN GAMBAR KE LABEL
                lb_gambar.setIcon(new ImageIcon(new ImageIcon(scaledImage).getImage().getScaledInstance(320, 320, Image.SCALE_SMOOTH)));
                TXT_PATHFOTO.setText(path);
                TXT_PATHFOTO.revalidate();

                // Konversi gambar yang telah diubah ukurannya ke dalam array byte
                return choosePhoto();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return null;
    }
//error saat edit gambar ukurannya

    private byte[] choosePhoto() {
        // Ambil foto dari lb_gambar dan konversi ke dalam array byte
        ImageIcon imageIcon = (ImageIcon) lb_gambar.getIcon();
        if (imageIcon != null) {
            // UKURAN YANG AKAN DI TAMPILKAN
            int labelWidth = 320;
            int labelHeight = 320;

            // UKURAN GAMBAR ASLI
            int imageWidth = imageIcon.getIconWidth();
            int imageHeight = imageIcon.getIconHeight();

            // HITUNG SKALA UNTUK GAMBAR YANG BARU
            double scaleX = (double) labelWidth / (double) imageWidth;
            double scaleY = (double) labelHeight / (double) imageHeight;
            double scale = Math.min(scaleX, scaleY);

            // UBAH UKURAN GAMBAR DENGAN SKALA YANG SUDAH DI HITUNG
            Image scaledImage = imageIcon.getImage().getScaledInstance((int) (scale * imageWidth), (int) (scale * imageHeight), Image.SCALE_SMOOTH);

            // MENAMPILKAN GAMBAR KE LABEL
            lb_gambar.setIcon(new ImageIcon(new ImageIcon(scaledImage).getImage().getScaledInstance(320, 320, Image.SCALE_SMOOTH)));

            // Konversi gambar yang sudah diubah ukurannya ke dalam array byte
            BufferedImage bufferedImage = new BufferedImage(scaledImage.getWidth(null), scaledImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics g = bufferedImage.createGraphics();
            g.drawImage(scaledImage, 0, 0, null);
            g.dispose();

            try ( ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                ImageIO.write(bufferedImage, "png", baos);
                return baos.toByteArray();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return null;
    }

    private void updateDataPengaduan(int idPengaduan, Date tglPengaduanBaru, String isiLaporanBaru, byte[] fotoBaru) {
        Connection kon = Koneksi.KoneksiDb();
        String sql;

        boolean updateSuccess = false; // Tandai apakah proses update berhasil

        if (fotoBaru == null) {
            // Jika fotoBaru null, update tanpa mengubah kolom foto
            sql = "UPDATE tbl_pengaduan SET tgl_pengaduan = ?, isi_laporan = ? WHERE id_pengaduan = ?";
        } else {
            // Jika fotoBaru tidak null, update dengan mengubah kolom foto
            sql = "UPDATE tbl_pengaduan SET tgl_pengaduan = ?, isi_laporan = ?, foto = ? WHERE id_pengaduan = ?";
        }

        try ( PreparedStatement ps = kon.prepareStatement(sql)) {
            // Set nilai parameter
            ps.setDate(1, tglPengaduanBaru);
            ps.setString(2, isiLaporanBaru);

            if (fotoBaru != null) {
                ps.setBytes(3, fotoBaru);
                ps.setInt(4, idPengaduan);
            } else {
                ps.setInt(3, idPengaduan);
            }

            // Eksekusi pernyataan SQL
            int result = ps.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(null, "Data berhasil diupdate.");
                updateSuccess = true; // Update berhasil
            } else {
                JOptionPane.showMessageDialog(null, "Gagal melakukan update data.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

        // Refresh tampilan atau lakukan operasi lain setelah update
        if (updateSuccess) {
            PanelHome form1 = new PanelHome(level, nik, idpetugas);
            Dashboard dashboard = (Dashboard) SwingUtilities.getWindowAncestor(this);

            if (dashboard != null) {
                dashboard.setForm(form1);
                tampilDataFromDatabase();
            }
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BTN_CANCEL;
    private javax.swing.JButton BTN_EDIT;
    private javax.swing.JButton BTN_FOTO;
    private com.toedter.calendar.JDateChooser TGL_PENGADUAN;
    private javax.swing.JTextArea TXT_LAPORAN;
    private javax.swing.JLabel TXT_NIK;
    private javax.swing.JTextField TXT_PATHFOTO;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel lb_gambar;
    private Panel.PanelBorder panelBorder1;
    private javax.swing.JScrollPane spTable;
    // End of variables declaration//GEN-END:variables
}
