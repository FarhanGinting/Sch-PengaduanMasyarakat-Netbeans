package Panel;

import java.awt.*;
import java.awt.print.*;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class PrintTable implements Printable {

    private final JTable table;

    public PrintTable(JTable table) {
        this.table = table;
    }

    @Override
    public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        // Menambahkan judul tabel
        printTableHeader(g2d);

        // Mengatur font untuk isi tabel
        g2d.setFont(table.getFont());

        // Mendapatkan informasi tentang lebar kolom
        int[] columnWidths = getColumnWidths();

        // Mendapatkan informasi tentang tinggi baris
        int rowHeight = table.getRowHeight();

        // Menentukan jumlah baris yang dapat ditampilkan pada halaman
        int rowsPerPage = (int) (pageFormat.getImageableHeight() / rowHeight);
        int totalRows = table.getRowCount();
        int startRow = pageIndex * rowsPerPage;
        int endRow = Math.min(startRow + rowsPerPage, totalRows);

        // Menggambar data tabel pada halaman
        for (int row = startRow; row < endRow; row++) {
            int y = (row - startRow + 1) * rowHeight;
            for (int col = 0; col < table.getColumnCount(); col++) {
                int x = getColumnX(col, columnWidths);
                String cellValue = table.getValueAt(row, col).toString();
                g2d.drawString(cellValue, x, y);
            }
        }

        return PAGE_EXISTS;
    }

    private void printTableHeader(Graphics2D g2d) {
        JTableHeader tableHeader = table.getTableHeader();
        Font originalFont = g2d.getFont();
        g2d.setFont(tableHeader.getFont());

        // Menentukan tinggi judul
        int headerHeight = tableHeader.getHeight();

        // Menggambar judul di halaman
        for (int col = 0; col < table.getColumnCount(); col++) {
            int x = getColumnX(col, getColumnWidths());
            int y = headerHeight;
            String headerValue = table.getColumnName(col);
            g2d.drawString(headerValue, x, y);
        }

        // Mengembalikan font ke font asli
        g2d.setFont(originalFont);
    }

    private int[] getColumnWidths() {
        int columnCount = table.getColumnCount();
        int[] columnWidths = new int[columnCount];

        for (int col = 0; col < columnCount; col++) {
            TableColumnModel columnModel = table.getColumnModel();
            TableColumn column = columnModel.getColumn(col);
            columnWidths[col] = column.getWidth();
        }

        return columnWidths;
    }

    private int getColumnX(int col, int[] columnWidths) {
        int x = 0;
        for (int i = 0; i < col; i++) {
            x += columnWidths[i];
        }
        return x;
    }

    public static void printTable(JTable table) {
        PrinterJob job = PrinterJob.getPrinterJob();
        PageFormat pageFormat = job.defaultPage();
        pageFormat.setOrientation(PageFormat.PORTRAIT); // Mengatur orientasi ke lanskap
        job.setPrintable(new PrintTable(table), pageFormat);

        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException ex) {
                ex.printStackTrace();
            }
        }
    }
}
