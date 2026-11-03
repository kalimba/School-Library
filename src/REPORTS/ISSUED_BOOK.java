package REPORTS;

import Authentication.Login;
import LIBRARY.DATABASE.DatabaseHandler;
import LIBRARY.HOME.HOME;
import LIBRARY.HOME.config;
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


public class ISSUED_BOOK extends javax.swing.JFrame {

    Connection conn=null;
    ResultSet rs=null;
    PreparedStatement pst=null;
    DefaultTableModel dm;
    DefaultTableModel model;
    TableRowSorter<DefaultTableModel> sorter;
    public ISSUED_BOOK() {
        initComponents();
        conn=DatabaseHandler.ConnectDb(); 
        allbooks(); 
        enableLiveSearch(jtxtsearchB);
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
    
private void allbooks() {

    String sql =
        "SELECT B.BOOK_ID, B.BOOK_CODE, B.TITLE, " +
        "C.CATEGORY_NAME, A.AUTHOR_NAME, " +
        "I.ISSUE_DATE, I.DUE_DATE, " +
        "COALESCE(S.FIRST_NAME,'') || ' ' || COALESCE(S.LAST_NAME,'') AS STUDENT_NAME " +
        "FROM BOOK B " +
        "JOIN AUTHOR A ON B.AUTHOR_ID = A.AUTHOR_ID " +
        "JOIN CATEGORY C ON B.CATEGORY_ID = C.CATEGORY_ID " +
        "JOIN ISSUEDBOOK I ON B.BOOK_ID = I.BOOK_ID " +
        "JOIN STUDENT S ON S.STUDENT_ID = I.STUDENT_ID " +
        "WHERE I.STATUS = 'NOT_RETURNED'";

    try {

        pst = conn.prepareStatement(sql);
        rs = pst.executeQuery();

        DefaultTableModel model =
            (DefaultTableModel) net.proteanit.sql.DbUtils
            .resultSetToTableModel(rs);

        // ===== ADD DAYS_USED COLUMN (JAVA SIDE) =====
        model.addColumn("DAYS_USED");

        for (int i = 0; i < model.getRowCount(); i++) {

            java.sql.Date issueDate = (java.sql.Date) model.getValueAt(i, 7); // ISSUE_DATE
            java.sql.Date dueDate = (java.sql.Date) model.getValueAt(i, 8);   // DUE_DATE

            long diff = dueDate.getTime() - issueDate.getTime();
            long daysUsed = diff / (1000 * 60 * 60 * 24);

            model.setValueAt(daysUsed, i, model.getColumnCount() - 1);
        }

        // SET MODEL FIRST
        jtblbooklist.setModel(model);
        
        // Disable editing
        jtblbooklist.setDefaultEditor(Object.class, null);

        // ===== SORTER =====
        sorter = new TableRowSorter<>(model);
        jtblbooklist.setRowSorter(sorter);

        // ===== DYNAMIC NUMBER COLUMN =====
        model.addColumn("No");
        jtblbooklist.moveColumn(model.getColumnCount() - 1, 0);

        jtblbooklist.getColumnModel()
                .getColumn(0)
                .setCellRenderer(new RowNumberRenderer());

        // ⭐ PROFESSIONAL STYLE
        jtblbooklist.setRowHeight(25);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e + " BOOK LIST");
    } finally {
        try {
            if (rs != null) rs.close();
            if (pst != null) pst.close();
        } catch (Exception ex) {}
    }
}

private void search(JTextField textField){

    String text = textField.getText();

    if(text.trim().length()==0){
        sorter.setRowFilter(null);
    }else{
        sorter.setRowFilter(
      RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(text)));
//sorter.setRowFilter(RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(text, 2, 5)));
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
        jBTNBORROW_BOOK = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jtxtsearchB = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtblbooklist = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        jButton2.setText("CLOSE");

        jBTNBORROW_BOOK.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jBTNBORROW_BOOK.setText("EXPORT TO EXCEL");
        jBTNBORROW_BOOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBTNBORROW_BOOKActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Search", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 10), new java.awt.Color(0, 102, 102))); // NOI18N

        jtxtsearchB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtsearchBActionPerformed(evt);
            }
        });
        jtxtsearchB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtxtsearchBKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jtxtsearchB, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jtxtsearchB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jtblbooklist.setModel(new javax.swing.table.DefaultTableModel(
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
        jtblbooklist.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtblbooklistMouseClicked(evt);
            }
        });
        jtblbooklist.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtblbooklistKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jtblbooklist);

        jButton3.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jButton3.setForeground(new java.awt.Color(0, 153, 153));
        jButton3.setText("BACK");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("LIST OF ALL BOOKS");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jBTNBORROW_BOOK)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(434, 434, 434)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1012, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                            .addComponent(jBTNBORROW_BOOK))
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

    private void jtxtsearchBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtsearchBActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtsearchBActionPerformed

    private void jtblbooklistMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtblbooklistMouseClicked
        /*String code,title;
        int row=jtblbooklist.getSelectedRow();
        code=(jtblbooklist.getModel().getValueAt(row,1).toString());
        title=(jtblbooklist.getModel().getValueAt(row,2).toString());
        */
    }//GEN-LAST:event_jtblbooklistMouseClicked

    private void jtblbooklistKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtblbooklistKeyReleased
        /*if(evt.getKeyCode()==KeyEvent.VK_DOWN||evt.getKeyCode()==KeyEvent.VK_UP)
        {  String code,title;
        int row=jtblbooklist.getSelectedRow();
        code=(jtblbooklist.getModel().getValueAt(row,1).toString());
        title=(jtblbooklist.getModel().getValueAt(row,2).toString());
        jLabelCODE.setText(code);
        jLabelTITLE.setText(title);
        }*/
    }//GEN-LAST:event_jtblbooklistKeyReleased

    private void jtxtsearchBKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtsearchBKeyReleased
        
    }//GEN-LAST:event_jtxtsearchBKeyReleased

    private void jBTNBORROW_BOOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBTNBORROW_BOOKActionPerformed
     exportUtil.exportTableToExcel(jtblbooklist);
    }//GEN-LAST:event_jBTNBORROW_BOOKActionPerformed

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
            java.util.logging.Logger.getLogger(ISSUED_BOOK.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ISSUED_BOOK.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ISSUED_BOOK.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ISSUED_BOOK.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ISSUED_BOOK().setVisible(true);
            }
        });
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBTNBORROW_BOOK;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jtblbooklist;
    private javax.swing.JTextField jtxtsearchB;
    // End of variables declaration//GEN-END:variables
}
