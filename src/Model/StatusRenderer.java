/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author FARHAN
 */
import javax.swing.table.DefaultTableCellRenderer;

public class StatusRenderer extends DefaultTableCellRenderer {

    @Override
    protected void setValue(Object value) {
        if (value instanceof StatusType) {
            StatusType status = (StatusType) value;
            setText(status.toString());
        } else {
            super.setValue(value);
        }
    }
}

