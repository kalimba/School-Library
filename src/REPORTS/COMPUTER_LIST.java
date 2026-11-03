
package REPORTS;

import ISSUINGITEMS.*;
import Authentication.Login;
import LIBRARY.DATABASE.DatabaseHandler;
import LIBRARY.HOME.HOME;
import LIBRARY.HOME.config;
import LIBRARY.HOME.config;
import java.awt.HeadlessException;
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
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import net.proteanit.sql.DbUtils;


public class COMPUTER_LIST extends javax.swing.JFrame {

    Connection conn=null;
    ResultSet rs=null;
    PreparedStatement pst=null;
    DefaultTableModel dm;
    DefaultTableModel model;
    TableRowSorter<DefaultTableModel> sorter;
    public COMPUTER_LIST() {
        initComponents();
        conn=DatabaseHandler.ConnectDb(); 
        allcomputers();
        enableLiveSearch(jtxtsearchCOMP);
        config.openFile();
        config.readFile();        
        config.closeFile();
    }
    public void close(){
WindowEvent winClosingEvent=new WindowEvent(this,WindowEvent.WINDOW_CLOSING);
Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(winClosingEvent);
}
   /* private void filter(String query){
        TableRowSorter<DefaultTableModel> tr=new TableRowSorter<DefaultTableModel>(dm);
        jtblmemberlist.setRowSorter(tr);
       tr.setRowFilter(RowFilter.regexFilter(query));
    }  */
private void allcomputers(){

    String sql =
        "SELECT C.COMPUTER_ID, C.SERIAL_NUMBER, C.MARK_NAME, " +
        "C.SPECIFICATIONS, C.STATUS, C.COMPUTER_DESCRIPTION " +
        "FROM COMPUTER C";

    try{

        pst = conn.prepareStatement(sql);
        rs = pst.executeQuery();

        DefaultTableModel model =
            (DefaultTableModel) net.proteanit.sql.DbUtils
            .resultSetToTableModel(rs);

        // Set table model
        jtblCOMPUTER_LIST.setModel(model);
        
        // Disable editing
        jtblCOMPUTER_LIST.setDefaultEditor(Object.class, null);

        // ===== SORTER =====
        sorter = new TableRowSorter<>(model);
        jtblCOMPUTER_LIST.setRowSorter(sorter);

        // ===== ADD DYNAMIC NUMBER COLUMN =====
        model.addColumn("No");
        jtblCOMPUTER_LIST.moveColumn(model.getColumnCount()-1, 0);

        jtblCOMPUTER_LIST.getColumnModel()
                .getColumn(0)
                .setCellRenderer(new RowNumberRenderer());

        // ⭐ PROFESSIONAL FEATURES
        jtblCOMPUTER_LIST.setRowHeight(25);

    }catch(Exception e){
        JOptionPane.showMessageDialog(null, e + " COMP TABLE");
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jBTNbORROW_COMP = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jtxtsearchCOMP = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtblCOMPUTER_LIST = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        jButton2.setText("CLOSE");

        jBTNbORROW_COMP.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jBTNbORROW_COMP.setText("EXPORT TO EXCEL");
        jBTNbORROW_COMP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBTNbORROW_COMPActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Search", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 10), new java.awt.Color(0, 102, 102))); // NOI18N

        jtxtsearchCOMP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtsearchCOMPActionPerformed(evt);
            }
        });
        jtxtsearchCOMP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtxtsearchCOMPKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jtxtsearchCOMP, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jtxtsearchCOMP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jtblCOMPUTER_LIST.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5"
            }
        ));
        jtblCOMPUTER_LIST.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtblCOMPUTER_LISTMouseClicked(evt);
            }
        });
        jtblCOMPUTER_LIST.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtblCOMPUTER_LISTKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jtblCOMPUTER_LIST);

        jButton3.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jButton3.setForeground(new java.awt.Color(0, 153, 153));
        jButton3.setText("BACK");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("LIST OF AVAILABLE COMPUTERS");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jBTNbORROW_COMP)
                .addGap(18, 18, 18)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 77, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(416, 416, 416)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(549, 549, 549)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2)
                            .addComponent(jButton3)
                            .addComponent(jBTNbORROW_COMP))
                        .addContainerGap(25, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1)
                        .addContainerGap())))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jtxtsearchCOMPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtsearchCOMPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtsearchCOMPActionPerformed

    private void jtblCOMPUTER_LISTMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtblCOMPUTER_LISTMouseClicked
        /* String computer_id,mark;
        int row=jtblCOMPUTER_LIST.getSelectedRow();
        computer_id=(jtblCOMPUTER_LIST.getModel().getValueAt(row,0).toString());
        mark=(jtblCOMPUTER_LIST.getModel().getValueAt(row,1).toString());
        jLabelCOMP_ID.setText(computer_id);
        jLabelMARK.setText(mark);*/
    }//GEN-LAST:event_jtblCOMPUTER_LISTMouseClicked

    private void jtblCOMPUTER_LISTKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtblCOMPUTER_LISTKeyReleased
        /*if(evt.getKeyCode()==KeyEvent.VK_DOWN||evt.getKeyCode()==KeyEvent.VK_UP)
        {  String computer_id,mark;
        int row=jtblCOMPUTER_LIST.getSelectedRow();
        computer_id=(jtblCOMPUTER_LIST.getModel().getValueAt(row,0).toString());
        mark=(jtblCOMPUTER_LIST.getModel().getValueAt(row,1).toString());
        jLabelCOMP_ID.setText(computer_id);
        jLabelMARK.setText(mark);
        }*/
    }//GEN-LAST:event_jtblCOMPUTER_LISTKeyReleased

    private void jtxtsearchCOMPKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtsearchCOMPKeyReleased
        /*if(!jtxtsearchCOMP.getText().trim().isEmpty()){
        try{
        String sql="select COMPUTER_ID, SERIAL_NUMBER, MARK_NAME,SPECIFICATIONS, STATUS "
        + "from COMPUTER WHERE  STATUS='AVAILABLE' AND "
        + "(COMPUTER_ID=? OR UPPER(MARK_NAME) LIKE ? OR UPPER(SERIAL_NUMBER) LIKE ? "
        + "OR UPPER(SPECIFICATIONS) LIKE ? OR UPPER(STATUS) LIKE ?)";
        String sql2="select COMPUTER_ID, SERIAL_NUMBER, MARK_NAME,SPECIFICATIONS, STATUS "
        + "from COMPUTER WHERE  STATUS='AVAILABLE' AND "
        + "(UPPER(MARK_NAME) LIKE ? OR UPPER(SERIAL_NUMBER) LIKE ? "
        + "OR UPPER(SPECIFICATIONS) LIKE ? OR UPPER(STATUS) LIKE ?)";
        String keyword = jtxtsearchCOMP.getText().trim().toUpperCase();
        String search = "%" + keyword + "%";
        if(keyword.matches("\\d+")) {
        pst=conn.prepareStatement(sql);
        pst.setLong(1, Long.parseLong(keyword));
        pst.setString(2, search);
        pst.setString(3, search);
        pst.setString(4, search);
        pst.setString(5, search);
        } else {
        pst=conn.prepareStatement(sql2);
        pst.setString(1, search);
        pst.setString(2, search);
        pst.setString(3, search);
        pst.setString(4, search);
        }
        rs=pst.executeQuery();
        jtblCOMPUTER_LIST.setModel(DbUtils.resultSetToTableModel(rs));
        }catch(Exception e){JOptionPane.showMessageDialog(null,e+"SEARCH_COMP!");}
        finally {
        try{
        rs.close();
        pst.close();
        //conn.close();
        }
        catch(Exception e){ }
        }
        }else{ computersList(); }
        
        */
    }//GEN-LAST:event_jtxtsearchCOMPKeyReleased

    private void jBTNbORROW_COMPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBTNbORROW_COMPActionPerformed
          exportUtil.exportTableToExcel(jtblCOMPUTER_LIST);       
    }//GEN-LAST:event_jBTNbORROW_COMPActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
       close();
          HOME hm= new HOME();
          hm.setTitle(config.getUserFN()+"     "+config.textline[5]);
          hm.setVisible(true); 
    }//GEN-LAST:event_jButton3ActionPerformed

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
            java.util.logging.Logger.getLogger(COMPUTER_LIST.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(COMPUTER_LIST.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(COMPUTER_LIST.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(COMPUTER_LIST.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new COMPUTER_LIST().setVisible(true);
            }
        });
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBTNbORROW_COMP;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jtblCOMPUTER_LIST;
    private javax.swing.JTextField jtxtsearchCOMP;
    // End of variables declaration//GEN-END:variables
}
