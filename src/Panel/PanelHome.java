/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Panel;

import Form.Dashboard;
import Koneksi.Koneksi;
import Model.StatusType;
import Panel.ScrollBar;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import Panel.TambahPengaduan;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author FARHAN
 */
public class PanelHome extends javax.swing.JPanel {

    JPopupMenu contextMenu = new JPopupMenu();
    JMenuItem menuItemDetail = new JMenuItem("Detail");
    JMenuItem menuItemDelete = new JMenuItem("Delete");
    JMenuItem menuItemTanggapan = new JMenuItem("Beri Tanggapan");
    JMenuItem menuItemLihatTanggapan = new JMenuItem("Lihat Tanggapan");
    public Statement st;
    public ResultSet rs;
    public DefaultTableModel tabModel;
    private TambahPengaduan form1;
    private DetaiPengaduan FR_DETAILPENGADUAN;
    /**
     * Creates new form PanelHome
     */
    private int level;
    private int nik = 0;
    private int selectedIdPengaduan;
    private int idpetugas = 0;

    public PanelHome(int level, int nik, int idpetugas) {

        this.level = level;
        this.nik = nik;
        this.idpetugas = idpetugas;

        initComponents();
        if (level == 1 || level == 2) {
            BTN_PENGADUAN.setEnabled(false);
            contextMenu.add(menuItemTanggapan);
            contextMenu.add(menuItemDelete);
        } else if (level == 3) {
            contextMenu.add(menuItemDetail);
            contextMenu.add(menuItemDelete);
            contextMenu.add(menuItemLihatTanggapan);
        }
        form1 = new TambahPengaduan(level, nik, idpetugas);

        menuItemDetail.addActionListener(e -> {
            int selectedRow = DG_TAMPIL.getSelectedRow();
            if (selectedRow != -1) {
                // Mendapatkan Status Pengaduan
                Object statusObject = DG_TAMPIL.getValueAt(selectedRow, 4);

                // Memastikan bahwa nilai status tidak null dan merupakan instance dari StatusType
                if (statusObject instanceof StatusType) {
                    StatusType statusPengaduan = (StatusType) statusObject;

                    // Jika status 'proses' atau 'selesai', tampilkan pesan
                    if (statusPengaduan == StatusType.PROSES || statusPengaduan == StatusType.SELESAI) {
                        JOptionPane.showMessageDialog(this, "Data tidak bisa di edit karena status 'proses' atau 'selesai'.");
                    } else {
                        // Mendapatkan ID Pengaduan
                        String idPengaduanString = DG_TAMPIL.getValueAt(selectedRow, 0).toString();
                        int idPengaduan = Integer.parseInt(idPengaduanString);

                        // Membuat objek DetailPanel dengan ID Pengaduan yang telah didapatkan
                        DetaiPengaduan detailPanel = new DetaiPengaduan(level, nik, DG_TAMPIL, idPengaduan, idpetugas);
                        System.out.println("id_pengaduan: " + idPengaduan);

                        // Mendapatkan objek Dashboard yang merupakan parent dari PanelHome
                        Dashboard dashboard = (Dashboard) SwingUtilities.getWindowAncestor(this);

                        // Jika Dashboard tidak null, set form detailPanel
                        if (dashboard != null) {
                            dashboard.setForm(detailPanel);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Status pengaduan tidak valid.");
                }
            }
        });

        menuItemDelete.addActionListener(e -> {
            // Mendapatkan indeks baris terpilih
            int selectedRow = DG_TAMPIL.getSelectedRow();

            // Memastikan bahwa baris terpilih valid
            if (selectedRow != -1) {
                // Mendapatkan nilai status_pengaduan dari baris terpilih
                Object statusObject = DG_TAMPIL.getValueAt(selectedRow, 4);

                // Memastikan bahwa nilai status tidak null dan merupakan instance dari StatusType
                if (statusObject instanceof StatusType) {
                    StatusType statusPengaduan = (StatusType) statusObject;

                    // Jika pengguna adalah masyarakat (level 3) dan status 'selesai', tampilkan pesan
                    if (level == 3 && statusPengaduan == StatusType.PROSES) {
                        JOptionPane.showMessageDialog(this, "Data tidak bisa dihapus karena status 'proses'.");
                    } else {
                        // Jika pengguna adalah admin/petugas (level 1/2) dan status '0' (Belum), tampilkan pesan
                        if ((level == 1 || level == 2) && (statusPengaduan == StatusType.SELESAI || statusPengaduan == StatusType.PROSES)) {
                            JOptionPane.showMessageDialog(this, "Data tidak bisa dihapus karena anda bukan penulisnya.");
                        } else {
                            // Mendapatkan nilai id_pengaduan dari baris terpilih
                            String idPengaduanStr = (String) DG_TAMPIL.getValueAt(selectedRow, 0);
                            int idPengaduan = Integer.parseInt(idPengaduanStr);

                            // Konfirmasi penghapusan
                            int option = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menghapus pengaduan ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
                            if (option == JOptionPane.YES_OPTION) {
                                // Panggil metode untuk menghapus data berdasarkan id_pengaduan
                                deleteDataPengaduan(idPengaduan);

                                // Refresh tampilan atau lakukan operasi lain setelah penghapusan
                                // Misalnya, tampilkan ulang data pada tabel
                                tampilDataFromDatabase();
                            }
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Status pengaduan tidak valid.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Pilih baris terlebih dahulu.");
            }
        });

        menuItemTanggapan.addActionListener(e -> {
            int selectedRow = DG_TAMPIL.getSelectedRow();
            if (selectedRow != -1) {
                // Mendapatkan status Pengaduan
                StatusType statusPengaduan = (StatusType) DG_TAMPIL.getValueAt(selectedRow, 4); // Ganti dengan indeks kolom Status

                // Pemeriksaan status pengaduan
                if (statusPengaduan == StatusType.PROSES || statusPengaduan == StatusType.SELESAI) {
                    JOptionPane.showMessageDialog(this, "Pengaduan sudah memiliki tanggapan.");
                } else {
                    // Mendapatkan ID Pengaduan
                    String idPengaduanString = (String) DG_TAMPIL.getValueAt(selectedRow, 0);
                    int idPengaduan = Integer.parseInt(idPengaduanString);

                    // Membuat objek TanggapiLaporan dengan ID Pengaduan yang telah didapatkan
                    TanggapiLaporan detailPanel = new TanggapiLaporan(level, nik, DG_TAMPIL, idPengaduan, idpetugas);

                    // Mendapatkan objek Dashboard yang merupakan parent dari PanelHome
                    Dashboard dashboard = (Dashboard) SwingUtilities.getWindowAncestor(this);

                    // Jika Dashboard tidak null, set form detailPanel
                    if (dashboard != null) {
                        dashboard.setForm(detailPanel);
                    }
                }
            }
        });

        menuItemLihatTanggapan.addActionListener(e -> {
            int selectedRow = DG_TAMPIL.getSelectedRow();
            if (selectedRow != -1) {
                // Mendapatkan ID Pengaduan
                String idPengaduanString = DG_TAMPIL.getValueAt(selectedRow, 0).toString();
                int idPengaduan = Integer.parseInt(idPengaduanString);

                // Mendapatkan ID Tanggapan dari tbl_tanggapan
                int idTanggapan = getIdTanggapanByPengaduanId(idPengaduan);

                // Check if tanggapan is found
                if (idTanggapan != 0) {
                    // Membuat objek DetailTanggapan dengan ID Pengaduan dan ID Tanggapan yang telah didapatkan
                    DetailTanggapan tanggapanPanel = new DetailTanggapan(level, nik, DG_TAMPIL, idTanggapan, idPengaduan, idpetugas);

                    // Mendapatkan objek Dashboard yang merupakan parent dari PanelHome
                    Dashboard dashboard = (Dashboard) SwingUtilities.getWindowAncestor(this);

                    // Jika Dashboard tidak null, set form tanggapanPanel
                    if (dashboard != null) {
                        dashboard.setForm(tanggapanPanel);
                    }
                } else {
                    // Display message if tanggapan is not found
                    JOptionPane.showMessageDialog(this, "Tanggapan Tidak Ditemukan");
                }
            }
        });

        DG_TAMPIL.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showContextMenu(e);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showContextMenu(e);
                }
            }
        });

        judul();
        tampilDataFromDatabase();
        spTable.setVerticalScrollBar(new ScrollBar());
        spTable.getVerticalScrollBar().setBackground(Color.WHITE);
        spTable.getViewport().setBackground(Color.WHITE);
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        spTable.setCorner(JScrollPane.UPPER_RIGHT_CORNER, p);

        BTN_PENGADUAN.addActionListener(e -> {
            TambahPengaduan form1 = new TambahPengaduan(level, nik, idpetugas);
            Dashboard dashboard = (Dashboard) SwingUtilities.getWindowAncestor(this);

            if (dashboard != null) {
                dashboard.setForm(form1);
            }
        });
    }

    private int getIdTanggapanByPengaduanId(int idPengaduan) {
        int idTanggapan = 0; // Default value jika tidak ditemukan

        Connection kon = Koneksi.KoneksiDb();
        String sql = "SELECT id_tanggapan FROM tbl_tanggapan WHERE id_pengaduan = ?";

        try ( PreparedStatement ps = kon.prepareStatement(sql)) {
            ps.setInt(1, idPengaduan);

            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    idTanggapan = rs.getInt("id_tanggapan");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return idTanggapan;
    }

    private void deleteDataPengaduan(int idPengaduan) {
        Connection kon = Koneksi.KoneksiDb();
        String sql = "DELETE FROM tbl_pengaduan WHERE id_pengaduan = ?";

        try ( PreparedStatement ps = kon.prepareStatement(sql)) {
            // Set nilai parameter
            ps.setInt(1, idPengaduan);

            // Eksekusi pernyataan SQL
            int result = ps.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(null, "Data berhasil dihapus.");
                // Refresh tampilan atau lakukan operasi lain setelah penghapusan
            } else {
                JOptionPane.showMessageDialog(null, "Gagal melakukan penghapusan data.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void showContextMenu(MouseEvent e) {
        // Menentukan baris yang dipilih
        int row = DG_TAMPIL.rowAtPoint(e.getPoint());
        DG_TAMPIL.getSelectionModel().setSelectionInterval(row, row);

        // Menampilkan menu pop-up di lokasi klik
        contextMenu.show(DG_TAMPIL, e.getX(), e.getY());
    }

    private void judul() {
        Object[] judul = {"ID Pengaduan", "Tanggal", "Nama", "Isi Laporan", "Status"};
        tabModel = new DefaultTableModel(null, judul);
        DG_TAMPIL.setModel(tabModel);

        // Menambahkan TableRowSorter ke tabel
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(DG_TAMPIL.getModel());
        DG_TAMPIL.setRowSorter(sorter);

        // Mengatur pengurutan berdasarkan kolom "ID Pengaduan" secara default
        sorter.setSortKeys(List.of(new RowSorter.SortKey(0, SortOrder.ASCENDING)));
    }

    private void tampilDataFromDatabase() {
        try {
            Connection kon = Koneksi.KoneksiDb();
            Statement st = kon.createStatement();
            String query = "SELECT P.id_pengaduan, P.tgl_pengaduan, M.nama, P.isi_laporan, P.status "
                    + "FROM tbl_pengaduan P "
                    + "INNER JOIN tbl_masyarakat M ON P.nik = M.nik";
            ResultSet rs = st.executeQuery(query);

            // Membersihkan model tabel sebelum menambahkan data baru
            DefaultTableModel tabModel = (DefaultTableModel) DG_TAMPIL.getModel();
            tabModel.setRowCount(0);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");

            while (rs.next()) {
                String statusString = rs.getString("status");
                StatusType status = StatusType.bacaStatusDariDatabase(statusString);

                // Format ulang tanggal menggunakan SimpleDateFormat
                String formattedDate = dateFormat.format(rs.getDate("tgl_pengaduan"));

                Object[] data = {
                    rs.getString("id_pengaduan"),
                    formattedDate, // Menggunakan tanggal yang diformat ulang
                    rs.getString("nama"), // Mengambil data nama dari tbl_masyarakat
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
        BTN_PENGADUAN = new javax.swing.JButton();
        spTable = new javax.swing.JScrollPane();
        DG_TAMPIL = new Panel.Table();
        panelHeader2 = new Panel.PanelHeader();

        panelBorder1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(127, 127, 127));
        jLabel1.setText("Tabel Pengaduan");

        BTN_PENGADUAN.setText("Buat Pengaduan");
        BTN_PENGADUAN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BTN_PENGADUANMouseClicked(evt);
            }
        });
        BTN_PENGADUAN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTN_PENGADUANActionPerformed(evt);
            }
        });

        spTable.setBorder(null);

        DG_TAMPIL.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        spTable.setViewportView(DG_TAMPIL);

        panelHeader2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                panelHeader2KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout panelBorder1Layout = new javax.swing.GroupLayout(panelBorder1);
        panelBorder1.setLayout(panelBorder1Layout);
        panelBorder1Layout.setHorizontalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBorder1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(20, 20, 20)
                        .addComponent(BTN_PENGADUAN, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(panelHeader2, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(spTable, javax.swing.GroupLayout.DEFAULT_SIZE, 905, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );
        panelBorder1Layout.setVerticalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(BTN_PENGADUAN, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelHeader2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addComponent(spTable, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
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

    private void BTN_PENGADUANMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BTN_PENGADUANMouseClicked

    }//GEN-LAST:event_BTN_PENGADUANMouseClicked

    private void BTN_PENGADUANActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTN_PENGADUANActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BTN_PENGADUANActionPerformed

    private void panelHeader2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_panelHeader2KeyReleased


    }//GEN-LAST:event_panelHeader2KeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BTN_PENGADUAN;
    private Panel.Table DG_TAMPIL;
    private javax.swing.JLabel jLabel1;
    private Panel.PanelBorder panelBorder1;
    private Panel.PanelHeader panelHeader2;
    private javax.swing.JScrollPane spTable;
    // End of variables declaration//GEN-END:variables
}
