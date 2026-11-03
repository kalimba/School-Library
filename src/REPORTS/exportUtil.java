/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package REPORTS;

/**
 *
 * @author Kalimba
 */
import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.FileOutputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public  class exportUtil {


public static void exportTableToExcel(JTable table) {

    try {

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save Excel File");

        if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION)
            return;

        String path = chooser.getSelectedFile().getAbsolutePath() + ".xlsx";

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Books");

        int columnCount = table.getColumnCount();

        // ===== CREATE BOLD STYLE =====
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);

        // ===== HEADER ROW =====
        Row headerRow = sheet.createRow(0);

        // Number column header
        Cell noCell = headerRow.createCell(0);
        noCell.setCellValue("No");
        noCell.setCellStyle(headerStyle);

        for (int col = 0; col < columnCount; col++) {

            Cell cell = headerRow.createCell(col + 1);
            cell.setCellValue(table.getColumnName(col));
            cell.setCellStyle(headerStyle);   // ⭐ APPLY BOLD
        }

        // ===== DATA ROWS =====
        for (int row = 0; row < table.getRowCount(); row++) {

            Row excelRow = sheet.createRow(row + 1);

            // numbering
            excelRow.createCell(0).setCellValue(row + 1);

            for (int col = 0; col < columnCount; col++) {

                Object value = table.getValueAt(row, col);

                if (value != null) {
                    excelRow.createCell(col + 1)
                            .setCellValue(value.toString());
                }
            }
        }

        // Auto size columns
        for (int i = 0; i <= columnCount; i++) {
            sheet.autoSizeColumn(i);
        }

        FileOutputStream fos = new FileOutputStream(path);
        workbook.write(fos);

        fos.close();
        workbook.close();

        JOptionPane.showMessageDialog(null, "Export Successful!");

    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
