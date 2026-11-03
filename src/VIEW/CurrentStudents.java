
package VIEW;

import LIBRARY.DATABASE.DatabaseHandler;
import LIBRARY.HOME.HOME;
import LIBRARY.HOME.config;
import REPORTS.RowNumberRenderer;
import java.awt.Image;
import java.awt.List;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static java.io.ObjectStreamClass.lookup;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import net.proteanit.sql.DbUtils;


public class CurrentStudents extends javax.swing.JFrame {

    Connection conn=null;
    ResultSet rs=null;
    PreparedStatement pst=null;
    DefaultTableModel dm;
    TableRowSorter<DefaultTableModel> sorter;
    public CurrentStudents() {
        initComponents();
        conn=DatabaseHandler.ConnectDb(); 
        StudentsList(jtblstudentlist, "AT_SCHOOL"); 
        jTabbedPaneST.addChangeListener(e -> tabChanged());
        enableLiveSearch(jtxtsearchSt);
        enableLiveSearch(jtxtsearchSt2);
        config.openFile();
        config.readFile();        
        config.closeFile();
    }
    public void close(){
WindowEvent winClosingEvent=new WindowEvent(this,WindowEvent.WINDOW_CLOSING);
Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(winClosingEvent);
}
private void tabChanged() {

    int index = jTabbedPaneST.getSelectedIndex();

    if(index == 0){
        StudentsList(jtblstudentlist, "AT_SCHOOL"); 
    }
    else if(index == 1){
        StudentsList(jtblstudentlist2, "GRADUATE"); 
    }
    else if(index == 2){
        StudentsList(jtblstudentlist2, "AT_SCHOOL"); 
    }
}
private void StudentsList(JTable table, String statusValue){

    String sql =
        "SELECT S.STUDENT_ID, S.FIRST_NAME, S.LAST_NAME, " +
        "C.CLASS_NAME, D.DEPARTMENT_NAME " +
        "FROM STUDENT S " +
        "JOIN CLASS_NAME C ON S.CLASS_NAME_ID = C.CLASS_NAME_ID " +
        "JOIN DEPARTMENT D ON S.DEPARTMENT_ID = D.DEPARTMENT_ID " +
        "WHERE S.STATUS =?";

try{

    pst = conn.prepareStatement(sql);
    pst.setString(1, statusValue);
    rs = pst.executeQuery();

    DefaultTableModel model =
        (DefaultTableModel) net.proteanit.sql.DbUtils
        .resultSetToTableModel(rs);

    // Set model first
    table.setModel(model);
    
    // Disable editing
    table.setDefaultEditor(Object.class, null);

    // ===== CREATE SORTER =====
    sorter = new TableRowSorter<>(model);
    table.setRowSorter(sorter);

    // ===== ADD DYNAMIC NUMBER COLUMN =====
    model.addColumn("No");
    table.moveColumn(model.getColumnCount()-1, 0);

    table.getColumnModel()
            .getColumn(0)
            .setCellRenderer(new RowNumberRenderer());

    // ⭐ PROFESSIONAL FEATURES
    table.setRowHeight(25);

}catch(Exception e){
    JOptionPane.showMessageDialog(null, e + " STUDENT TABLE");
}
finally{
    try{
        if(rs != null) rs.close();
        if(pst != null) pst.close();
    }catch(Exception e){}
}
}
private void search(JTextField textField){

    String text = textField.getText();

    if(text.trim().length()==0){
        sorter.setRowFilter(null);
    }else{
        sorter.setRowFilter(
            RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(text))
        );
    }
}
    private void enableLiveSearch(JTextField textField){

    textField.getDocument().addDocumentListener(
        new javax.swing.event.DocumentListener(){

        @Override
        public void insertUpdate(javax.swing.event.DocumentEvent e){
            search(textField);
        }

        @Override
        public void removeUpdate(javax.swing.event.DocumentEvent e){
            search(textField);
        }

        @Override
        public void changedUpdate(javax.swing.event.DocumentEvent e){
            search(textField);
        }
    });
}
public void updateStatus(JTable table,
                         String dbTable,
                         String idColumn,
                         String statusColumn,
                         String newStatus) {

    int row = table.getSelectedRow();

    if (row == -1) {
        JOptionPane.showMessageDialog(null, "Please select a row first.");
        return;
    }

    String idValue = table.getModel()
                          .getValueAt(row, 0)
                          .toString();

    String sql = "UPDATE " + dbTable +
                 " SET " + statusColumn + "=? WHERE " +
                 idColumn + "=?";

    try (PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setString(1, newStatus);
        pst.setString(2, idValue);

        pst.executeUpdate();

        JOptionPane.showMessageDialog(null,
                "Status updated successfully.");

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null,
                e + " : Problem updating status");
    }
}
public void deleteRecord(JTable table,
                         String dbTable,
                         String idColumn){

    int row = table.getSelectedRow();

    if(row == -1){
        JOptionPane.showMessageDialog(null,
                "Select a record first.");
        return;
    }

    String idValue =
            table.getModel().getValueAt(row,0).toString();

    int confirm = JOptionPane.showConfirmDialog(
            null,
            "Delete selected record?",
            "Confirm",
            JOptionPane.YES_NO_OPTION);

    if(confirm != JOptionPane.YES_OPTION){
        return;
    }

    String sql =
            "DELETE FROM " + dbTable +
            " WHERE " + idColumn + "=?";

    try(PreparedStatement pst =
            conn.prepareStatement(sql)){

        pst.setString(1,idValue);
        pst.executeUpdate();

        JOptionPane.showMessageDialog(null,
                "Record deleted successfully.");

    }catch(Exception e){
        JOptionPane.showMessageDialog(null,e);
    }
}    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPaneST = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jtxtsearchSt = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtblstudentlist = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        jBtnTOGARADUATES = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jtxtsearchSt2 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtblstudentlist2 = new javax.swing.JTable();
        jButton7 = new javax.swing.JButton();
        jBtnTOSTUDENTS = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        jButton2.setText("CLOSE");

        jButton1.setText("DELETE");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Search", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 10), new java.awt.Color(0, 102, 102))); // NOI18N

        jtxtsearchSt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtsearchStActionPerformed(evt);
            }
        });
        jtxtsearchSt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtxtsearchStKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jtxtsearchSt, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jtxtsearchSt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jtblstudentlist.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Fresh Graduate"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jtblstudentlist.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtblstudentlistMouseClicked(evt);
            }
        });
        jtblstudentlist.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtblstudentlistKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jtblstudentlist);

        jButton3.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jButton3.setForeground(new java.awt.Color(0, 153, 153));
        jButton3.setText("BACK");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jBtnTOGARADUATES.setText("ADD TO GRADUATES");
        jBtnTOGARADUATES.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnTOGARADUATESActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2))
                    .addComponent(jBtnTOGARADUATES, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 960, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jBtnTOGARADUATES)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2)
                            .addComponent(jButton3)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 437, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40))
        );

        jTabbedPaneST.addTab("CURRENT STUDENTS", jPanel2);

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));

        jButton5.setText("CLOSE");

        jButton6.setText("DELETE");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Search", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 10), new java.awt.Color(0, 102, 102))); // NOI18N

        jtxtsearchSt2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtsearchSt2ActionPerformed(evt);
            }
        });
        jtxtsearchSt2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtxtsearchSt2KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jtxtsearchSt2, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jtxtsearchSt2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jtblstudentlist2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Fresh Graduate"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jtblstudentlist2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtblstudentlist2MouseClicked(evt);
            }
        });
        jtblstudentlist2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtblstudentlist2KeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(jtblstudentlist2);

        jButton7.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jButton7.setForeground(new java.awt.Color(0, 153, 153));
        jButton7.setText("BACK");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jBtnTOSTUDENTS.setText("ADD TO STUDENTS");
        jBtnTOSTUDENTS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnTOSTUDENTSActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5))
                    .addComponent(jBtnTOSTUDENTS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 960, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 437, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jBtnTOSTUDENTS)
                .addGap(18, 18, 18)
                .addComponent(jButton6)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5)
                    .addComponent(jButton7))
                .addGap(40, 40, 40))
        );

        jTabbedPaneST.addTab("GRADUATES", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneST, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneST, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jBtnTOSTUDENTSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnTOSTUDENTSActionPerformed
        updateStatus(jtblstudentlist2,"STUDENT","STUDENT_ID","STATUS","AT_SCHOOL");
    }//GEN-LAST:event_jBtnTOSTUDENTSActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        close();
        HOME hm= new HOME();
        hm.setTitle(config.getUserFN()+"     "+config.textline[5]);
        hm.setVisible(true);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jtblstudentlist2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtblstudentlist2KeyReleased

    }//GEN-LAST:event_jtblstudentlist2KeyReleased

    private void jtblstudentlist2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtblstudentlist2MouseClicked

    }//GEN-LAST:event_jtblstudentlist2MouseClicked

    private void jtxtsearchSt2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtsearchSt2KeyReleased

    }//GEN-LAST:event_jtxtsearchSt2KeyReleased

    private void jtxtsearchSt2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtsearchSt2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtsearchSt2ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        updateStatus(jtblstudentlist2,"STUDENT","STUDENT_ID","STATUS","DELETED");
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jBtnTOGARADUATESActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnTOGARADUATESActionPerformed
        updateStatus(jtblstudentlist,"STUDENT","STUDENT_ID","STATUS","GRADUATE");
    }//GEN-LAST:event_jBtnTOGARADUATESActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        close();
        HOME hm= new HOME();
        hm.setTitle(config.getUserFN()+"     "+config.textline[5]);
        hm.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jtblstudentlistKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtblstudentlistKeyReleased

    }//GEN-LAST:event_jtblstudentlistKeyReleased

    private void jtblstudentlistMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtblstudentlistMouseClicked

    }//GEN-LAST:event_jtblstudentlistMouseClicked

    private void jtxtsearchStKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtsearchStKeyReleased

    }//GEN-LAST:event_jtxtsearchStKeyReleased

    private void jtxtsearchStActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtsearchStActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtsearchStActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        updateStatus(jtblstudentlist,"STUDENT","STUDENT_ID","STATUS","DELETED");
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CurrentStudents.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CurrentStudents.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CurrentStudents.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CurrentStudents.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CurrentStudents().setVisible(true);
            }
        });
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnTOGARADUATES;
    private javax.swing.JButton jBtnTOSTUDENTS;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPaneST;
    private javax.swing.JTable jtblstudentlist;
    private javax.swing.JTable jtblstudentlist2;
    private javax.swing.JTextField jtxtsearchSt;
    private javax.swing.JTextField jtxtsearchSt2;
    // End of variables declaration//GEN-END:variables
}
