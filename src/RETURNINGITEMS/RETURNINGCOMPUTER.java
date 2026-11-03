
package RETURNINGITEMS;

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


public class RETURNINGCOMPUTER extends javax.swing.JFrame {

    Connection conn=null;
    ResultSet rs=null;
    PreparedStatement pst=null;
    DefaultTableModel dm;
    
    DefaultTableModel model;
    TableRowSorter<DefaultTableModel> sorter;
    
    String computer_id,SERIALNO,DUE_DATE;
    String fn="",ln="",ST_FN="",ST_LN="",librarianID;
    String issued_comp_id,studentid,status="";
    
    public RETURNINGCOMPUTER() {
        initComponents();
        conn=DatabaseHandler.ConnectDb(); 
        borrowedcomputers(); 
        enableLiveSearch(jtxtsearchCOMP);
       // StudentsList();
        config.openFile();
        config.readFile();        
        config.closeFile();
        jreturn_date.setDateFormatString("yyyy-MM-dd HH:mm:ss");
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
    
    private void borrowedcomputers(){

    String sql =
        "SELECT I.COMPUTER_ID, C.SERIAL_NUMBER, C.MARK_NAME, " +
        "I.ISSUE_DATE, I.DUE_DATE, I.STATUS " +
        "FROM ISSUEDCOMPUTER I " +
        "JOIN COMPUTER C ON I.COMPUTER_ID = C.COMPUTER_ID " +
        "WHERE I.STATUS = 'NOT_RETURNED'";

    try{

        pst = conn.prepareStatement(sql);
        rs = pst.executeQuery();

        jtblborrowed_comp.setModel(
            net.proteanit.sql.DbUtils.resultSetToTableModel(rs)
        );
        // Disable editing
        jtblborrowed_comp.setDefaultEditor(Object.class, null);
        
        //jtblborrowed_comp.setModel(model);
        jtblborrowed_comp.setAutoCreateRowSorter(true);
        jtblborrowed_comp.setRowHeight(25);

    }catch(Exception e){
        JOptionPane.showMessageDialog(null, e + " BORROWED COMP.");
    }
    finally{
        try{
            if(rs != null) rs.close();
            if(pst != null) pst.close();
        }catch(Exception e){}
    }
}
    private void Selectedtransaction(){

    int row = jtblborrowed_comp.getSelectedRow();

    if(row == -1){
        JOptionPane.showMessageDialog(null, "Please select a row first!");
        return;
    }

    computer_id = jtblborrowed_comp.getValueAt(row, 0).toString();
    SERIALNO = jtblborrowed_comp.getValueAt(row, 1).toString();
    DUE_DATE = jtblborrowed_comp.getValueAt(row, 4).toString();

    try{

        String sql =
            "SELECT S.FIRST_NAME, S.LAST_NAME, " +
            "I.LIBRARIAN_ID, I.ISSUED_COMPUTER_ID, " +
            "I.STUDENT_ID, I.BORROW_STATUS " +
            "FROM ISSUEDCOMPUTER I " +
            "JOIN STUDENT S ON I.STUDENT_ID = S.STUDENT_ID " +
            "WHERE I.COMPUTER_ID = ? AND I.STATUS = 'NOT_RETURNED'";

        pst = conn.prepareStatement(sql);
        pst.setString(1, computer_id);
        rs = pst.executeQuery();

        if(rs.next()){

            fn = rs.getString("FIRST_NAME");
            ln = rs.getString("LAST_NAME");
            librarianID = rs.getString("LIBRARIAN_ID");
            issued_comp_id = rs.getString("ISSUED_COMPUTER_ID");
            studentid = rs.getString("STUDENT_ID");
            status = rs.getString("BORROW_STATUS");
            pst.close(); rs.close();
            // Get librarian info
            String sql2 =
                "SELECT FIRST_NAME, LAST_NAME FROM LIBRARIAN WHERE LIBRARIAN_ID=?";

            pst = conn.prepareStatement(sql2);
            pst.setString(1, librarianID);
            ResultSet rs2 = pst.executeQuery();

            if(rs2.next()){
                ST_FN = rs2.getString("FIRST_NAME");
                ST_LN = rs2.getString("LAST_NAME");
            }

            rs2.close();
        }

    }catch(Exception e){
        JOptionPane.showMessageDialog(null, e + " SELECTION ERROR");
    }
    finally{
        try{
            if(rs != null) rs.close();
            if(pst != null) pst.close();
        }catch(Exception e){}
    }

    // ================= UI UPDATE =================

    jLabelCOMP_ID.setText(computer_id);
    jLabelSERIALNO.setText(SERIALNO);
    jLabelSTUDENT_NAMES.setText(fn + " " + ln);
    jLabelOFFER.setText(ST_FN + " " + ST_LN);
    jLabelDUE_DATE.setText(DUE_DATE);
    jLabelISSUED_COND.setText(status);
}
    /*    private void search(JTextField  textField ){
    
    String text = textField.getText();
    
    if(text.trim().length()==0){
    sorter.setRowFilter(null);
    }else{
    sorter.setRowFilter(
    RowFilter.regexFilter("(?i)" + text)
    );
    //sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 2, 5));
    }
    }*/
private void search(JTextField field){

    if(sorter == null || field == null){
        return;
    }

    String text = field.getText();

    if(text == null || text.trim().isEmpty()){
        sorter.setRowFilter(null);
    }else{

        // ⭐ ESCAPE SPECIAL CHARACTERS
        String safeText = java.util.regex.Pattern.quote(text.trim());

        sorter.setRowFilter(
            RowFilter.regexFilter("(?i)" + safeText)
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
        jBtnReturn_Comp = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jtxtsearchCOMP = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtblborrowed_comp = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabelCOMP_ID = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabelSERIALNO = new javax.swing.JLabel();
        jreturn_date = new com.toedter.calendar.JDateChooser();
        jLabel11 = new javax.swing.JLabel();
        jTextRUTURNED_COND = new javax.swing.JTextField();
        jLabelDUE_DATE = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabelSTUDENT_NAMES = new javax.swing.JLabel();
        jLabelISSUED_COND = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabelOFFER = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        jButton2.setText("CLOSE");

        jBtnReturn_Comp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jBtnReturn_Comp.setText("RETURN COMPUTER");
        jBtnReturn_Comp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnReturn_CompActionPerformed(evt);
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

        jtblborrowed_comp.setModel(new javax.swing.table.DefaultTableModel(
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
        jtblborrowed_comp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtblborrowed_compMouseClicked(evt);
            }
        });
        jtblborrowed_comp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtblborrowed_compKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jtblborrowed_comp);

        jButton3.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jButton3.setForeground(new java.awt.Color(0, 153, 153));
        jButton3.setText("BACK");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("LIST OF  BORROWED COMPUTERS");

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "RETURNED COMPUTER", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 3, 12))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("COMPUTER ID:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("SERIAL NUMBER");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("ISSUED CONDITION");

        jLabelCOMP_ID.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelCOMP_ID.setText("  ");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setText("DUE DATE");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setText("RETURNED DATE");

        jLabelSERIALNO.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setText("RETURNED CONDITION");

        jTextRUTURNED_COND.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextRUTURNED_CONDActionPerformed(evt);
            }
        });

        jLabelDUE_DATE.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setText("BORROWED STUDENT");

        jLabelSTUDENT_NAMES.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelSTUDENT_NAMES.setText("           ");

        jLabelISSUED_COND.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setText("OFFEERED BY");

        jLabelOFFER.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(18, 18, 18)
                        .addComponent(jTextRUTURNED_COND))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel8)
                            .addComponent(jLabel13))
                        .addGap(54, 54, 54)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelDUE_DATE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelOFFER, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jreturn_date, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5))
                        .addGap(54, 54, 54)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelCOMP_ID, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelSERIALNO, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel12))
                        .addGap(24, 24, 24)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelSTUDENT_NAMES, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelISSUED_COND, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(51, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelCOMP_ID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelSERIALNO, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabelSTUDENT_NAMES, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(jLabelOFFER, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jLabelDUE_DATE, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabelISSUED_COND, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextRUTURNED_COND, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10)
                    .addComponent(jreturn_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jBtnReturn_Comp)
                        .addGap(31, 31, 31)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(416, 416, 416)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 866, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 205, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBtnReturn_Comp)
                    .addComponent(jButton3)
                    .addComponent(jButton2))
                .addGap(25, 25, 25))
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

    private void jtblborrowed_compMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtblborrowed_compMouseClicked
       Selectedtransaction();
    }//GEN-LAST:event_jtblborrowed_compMouseClicked

    private void jtblborrowed_compKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtblborrowed_compKeyReleased
       if(evt.getKeyCode()==KeyEvent.VK_DOWN||evt.getKeyCode()==KeyEvent.VK_UP)
         { Selectedtransaction();}
    }//GEN-LAST:event_jtblborrowed_compKeyReleased

    private void jtxtsearchCOMPKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtsearchCOMPKeyReleased
        /*if(!jtxtsearchCOMP.getText().trim().isEmpty()){
        try{
        String sql="select I.COMPUTER_ID, C.SERIAL_NUMBER, C.MARK_NAME,I.ISSUE_DATE, I.DUE_DATE, I.STATUS "
        + "from COMPUTER C, ISSUEDCOMPUTER I WHERE  I.STATUS='NOT_RETURNED' AND "
        + "(C.COMPUTER_ID=? OR UPPER(C.MARK_NAME) LIKE ? OR UPPER(C.SERIAL_NUMBER) LIKE ? "
        + "OR UPPER(C.SPECIFICATIONS) LIKE ? OR UPPER(I.STATUS) LIKE ?)";
        String sql2="select I.COMPUTER_ID, C.SERIAL_NUMBER, C.MARK_NAME,I.ISSUE_DATE, I.DUE_DATE, I.STATUS "
        + "from COMPUTER C, ISSUEDCOMPUTER I WHERE  I.STATUS='NOT_RETURNED' AND "
        + "(UPPER(C.MARK_NAME) LIKE ? OR UPPER(C.SERIAL_NUMBER) LIKE ? "
        + "OR UPPER(C.SPECIFICATIONS) LIKE ? OR UPPER(I.STATUS) LIKE ?)";
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
        jtblborrowed_comp.setModel(DbUtils.resultSetToTableModel(rs));
        }catch(Exception e){JOptionPane.showMessageDialog(null,e+"SEARCH_COMP!");}
        finally {
        try{
        rs.close();
        pst.close();
        //conn.close();
        }
        catch(Exception e){ }
        }
        }else{ borrowedcomputers(); } */
        
    }//GEN-LAST:event_jtxtsearchCOMPKeyReleased

    private void jBtnReturn_CompActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnReturn_CompActionPerformed
      DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int librarian_id = Login.lib_id;
        try {
            // ===== UPDATE ISSUED COMPUTER =====
            String sql = "UPDATE ISSUEDCOMPUTER SET "
                    + "RECEIVER_ID=?, RETURN_DESCRIPTION=?, RETURN_DATE=?, STATUS=? "
                    + "WHERE ISSUED_COMPUTER_ID=?";

            pst = conn.prepareStatement(sql);
            pst.setInt(1, librarian_id);
            pst.setString(2, jTextRUTURNED_COND.getText().trim());
            pst.setString(3, df.format(jreturn_date.getDate()));
            pst.setString(4, "RETURNED");
            pst.setInt(5, Integer.parseInt(issued_comp_id));

            pst.executeUpdate();
            pst.close();

            // ===== UPDATE COMPUTER STATUS =====
            String query = "UPDATE COMPUTER SET STATUS=? WHERE COMPUTER_ID=?";

            pst = conn.prepareStatement(query);
            pst.setString(1, "AVAILABLE");
            pst.setInt(2, Integer.parseInt(computer_id));

            pst.executeUpdate();

            JOptionPane.showMessageDialog(null,
                    fn + " " + ln + " RETURNED COMPUTER SUCCESSFULLY.");

        }
        catch (SQLException | HeadlessException e) {

            JOptionPane.showMessageDialog(null, e + " NOT UPDATED!");

        }
        finally {

            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
            } catch (Exception ex) { }
        }
        jLabelCOMP_ID.setText("");
        jLabelSERIALNO.setText("");
        jLabelSTUDENT_NAMES.setText("");
        jLabelOFFER.setText("");
        jLabelDUE_DATE.setText("");
        jLabelISSUED_COND.setText("");
        jTextRUTURNED_COND.setText("");
        jreturn_date.cleanup();
        borrowedcomputers();
    }//GEN-LAST:event_jBtnReturn_CompActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
       close();
          HOME hm= new HOME();
          hm.setTitle(config.getUserFN()+"     "+config.textline[5]);
          hm.setVisible(true); 
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTextRUTURNED_CONDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextRUTURNED_CONDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextRUTURNED_CONDActionPerformed

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
            java.util.logging.Logger.getLogger(RETURNINGCOMPUTER.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RETURNINGCOMPUTER.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RETURNINGCOMPUTER.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RETURNINGCOMPUTER.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
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
                new RETURNINGCOMPUTER().setVisible(true);
            }
        });
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnReturn_Comp;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabelCOMP_ID;
    private javax.swing.JLabel jLabelDUE_DATE;
    private javax.swing.JLabel jLabelISSUED_COND;
    private javax.swing.JLabel jLabelOFFER;
    private javax.swing.JLabel jLabelSERIALNO;
    private javax.swing.JLabel jLabelSTUDENT_NAMES;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextRUTURNED_COND;
    private com.toedter.calendar.JDateChooser jreturn_date;
    private javax.swing.JTable jtblborrowed_comp;
    private javax.swing.JTextField jtxtsearchCOMP;
    // End of variables declaration//GEN-END:variables
}
//OK