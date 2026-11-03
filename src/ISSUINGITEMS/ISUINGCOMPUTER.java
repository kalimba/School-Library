
package ISSUINGITEMS;

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


public class ISUINGCOMPUTER extends javax.swing.JFrame {

    Connection conn=null;
    ResultSet rs=null;
    PreparedStatement pst=null;
    DefaultTableModel dm;
    DefaultTableModel model;
    TableRowSorter<DefaultTableModel> sorter;
    public ISUINGCOMPUTER() {
        initComponents();
        conn=DatabaseHandler.ConnectDb(); 
        computersList(); 
        StudentsList();
        enableLiveSearch(jtxtsearchCOMP);
        enableLiveSearch(jtxtsearchSt);
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
    private void computersList(){

    String sql =
        "SELECT C.COMPUTER_ID, C.SERIAL_NUMBER, C.MARK_NAME, " +
        "C.SPECIFICATIONS, C.STATUS " +
        "FROM COMPUTER C " +
        "WHERE C.STATUS = 'AVAILABLE'";

    try{

        pst = conn.prepareStatement(sql);
        rs = pst.executeQuery();

        jtblCOMPUTER_LIST.setModel(
            net.proteanit.sql.DbUtils.resultSetToTableModel(rs)
        );
        // Disable editing
        jtblCOMPUTER_LIST.setDefaultEditor(Object.class, null);
        //jtblCOMPUTER_LIST.setModel(model);
        // ⭐ PROFESSIONAL FEATURES
        jtblCOMPUTER_LIST.setAutoCreateRowSorter(true);
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
    private void StudentsList(){

    String sql =
        "SELECT S.STUDENT_ID, S.FIRST_NAME, S.LAST_NAME, " +
        "C.CLASS_NAME, D.DEPARTMENT_NAME " +
        "FROM STUDENT S " +
        "JOIN CLASS_NAME C ON S.CLASS_NAME_ID = C.CLASS_NAME_ID " +
        "JOIN DEPARTMENT D ON S.DEPARTMENT_ID = D.DEPARTMENT_ID";

    try{

        pst = conn.prepareStatement(sql);
        rs = pst.executeQuery();

        jtblstudentlist.setModel(
            net.proteanit.sql.DbUtils.resultSetToTableModel(rs)
        );

        // Disable editing
        jtblstudentlist.setDefaultEditor(Object.class, null);
        
        // ⭐ PROFESSIONAL FEATURES
        jtblstudentlist.setAutoCreateRowSorter(true);
        jtblstudentlist.setRowHeight(25);

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
      private void search(JTextField  textField ){

        String text = textField.getText();

        if(text.trim().length()==0){
            sorter.setRowFilter(null);
        }else{
            sorter.setRowFilter(
                RowFilter.regexFilter("(?i)" + text)
            );
        //sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 2, 5));
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
    private void issueComputer() {

    //Calendar cal = Calendar.getInstance();
    //Date today = cal.getTime();

    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    String comp_id = jLabelCOMP_ID.getText().trim();
    String mark = jLabelMARK.getText().trim();
    String compCurrentStatus = jTextBORROW_STATUS.getText().trim();
    String ST_NAME = jLabelSTUDENT_NAMES.getText().trim();
    String st_id = jLabelSTUDENT_ID.getText().trim();

    // ✅ get librarian id correctly
    int librarian_id = Login.lib_id;
       
    // ✅ validation (VERY IMPORTANT)
    if (comp_id.isEmpty() || st_id.isEmpty()
            || jDateChooser1.getDate() == null
            || jDateChooser2.getDate() == null) {

        JOptionPane.showMessageDialog(null, "Complete all fields!");
        return;
    }

    try {

        /* ===============================
           INSERT ISSUED COMPUTER
        =============================== */

        String insertSQL =
                "INSERT INTO ISSUEDCOMPUTER "
                + "(STUDENT_ID, COMPUTER_ID, LIBRARIAN_ID, ISSUE_DATE, DUE_DATE, STATUS, BORROW_STATUS) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pst1 = conn.prepareStatement(insertSQL)) {

            pst1.setString(1, st_id);
            pst1.setString(2, comp_id);
            pst1.setInt(3, librarian_id);
            pst1.setString(4, df.format(jDateChooser1.getDate().getTime()));
            pst1.setString(5, df.format(jDateChooser2.getDate().getTime()));
            pst1.setString(6, "NOT_RETURNED");
            pst1.setString(7, compCurrentStatus);

            pst1.executeUpdate();
        }

        /* ===============================
           UPDATE COMPUTER STATUS
        =============================== */

        String updateSQL =
                "UPDATE COMPUTER SET STATUS=? WHERE COMPUTER_ID=?";

        try (PreparedStatement pst2 = conn.prepareStatement(updateSQL)) {

            pst2.setString(1, "NOT_AVAILABLE");
            pst2.setString(2, comp_id);

            pst2.executeUpdate();
        }

        JOptionPane.showMessageDialog(null,
                ST_NAME + " BORROWED A COMPUTER.");

    } catch (SQLException | HeadlessException e) {

        JOptionPane.showMessageDialog(null,
                e + " NOT BORROWED");

    }

    /* ===============================
       RESET FORM
    =============================== */

    jLabelCOMP_ID.setText("");
    jLabelMARK.setText("");
    jTextBORROW_STATUS.setText("");
    jLabelSTUDENT_NAMES.setText("");
    jLabelSTUDENT_ID.setText("");

    jDateChooser1.setDate(null);
    jDateChooser2.setDate(null);

    jtxtsearchCOMP.setText("");
    jtxtsearchSt.setText("");

    computersList();
    StudentsList();
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
        jScrollPane2 = new javax.swing.JScrollPane();
        jtblstudentlist = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jtxtsearchSt = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextBORROW_STATUS = new javax.swing.JTextField();
        jLabelCOMP_ID = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabelMARK = new javax.swing.JLabel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabelSTUDENT_NAMES = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabelSTUDENT_ID = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        jButton2.setText("CLOSE");

        jBTNbORROW_COMP.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jBTNbORROW_COMP.setText("BORROW");
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
        jScrollPane2.setViewportView(jtblstudentlist);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Search", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 10), new java.awt.Color(0, 102, 102))); // NOI18N

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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jtxtsearchSt, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jtxtsearchSt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("LIST OF AVAILABLE COMPUTERS");

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "SELECTED BOOK", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 3, 12))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("COMPUTER ID:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("MARK NAME");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("CURRENT STATUS");

        jTextBORROW_STATUS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextBORROW_STATUSActionPerformed(evt);
            }
        });

        jLabelCOMP_ID.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelCOMP_ID.setText("  ");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setText("ISSUED DATE");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setText("DUE DATE");

        jLabelMARK.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel8)
                            .addComponent(jLabel10))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jTextBORROW_STATUS, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jDateChooser2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5))
                        .addGap(44, 44, 44)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabelCOMP_ID, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
                            .addComponent(jLabelMARK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(75, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabelCOMP_ID, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabelMARK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jTextBORROW_STATUS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addComponent(jLabel8))
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10)
                    .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(34, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("LIST OF STUDENTS");

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "SELECTED STUDENT", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 3, 12))); // NOI18N

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setText("NAMES:");

        jLabelSTUDENT_NAMES.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelSTUDENT_NAMES.setText("           ");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setText("REG NUMBER:");

        jLabelSTUDENT_ID.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelSTUDENT_ID.setText("      ");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelSTUDENT_NAMES, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelSTUDENT_ID, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelSTUDENT_NAMES, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelSTUDENT_ID, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(45, Short.MAX_VALUE))
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
                        .addComponent(jBTNbORROW_COMP)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 87, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(70, 70, 70)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGap(18, 18, 18)
                            .addComponent(jLabel2)
                            .addGap(484, 484, 484)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 866, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addGap(416, 416, 416)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 866, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(57, 57, 57)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2)
                            .addComponent(jButton3)
                            .addComponent(jBTNbORROW_COMP)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
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
       String computer_id,mark;
                int row=jtblCOMPUTER_LIST.getSelectedRow();
                computer_id=(jtblCOMPUTER_LIST.getModel().getValueAt(row,0).toString());
                mark=(jtblCOMPUTER_LIST.getModel().getValueAt(row,1).toString());
                 jLabelCOMP_ID.setText(computer_id);
                 jLabelMARK.setText(mark);
    }//GEN-LAST:event_jtblCOMPUTER_LISTMouseClicked

    private void jtblCOMPUTER_LISTKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtblCOMPUTER_LISTKeyReleased
       if(evt.getKeyCode()==KeyEvent.VK_DOWN||evt.getKeyCode()==KeyEvent.VK_UP)
         {  String computer_id,mark;
                int row=jtblCOMPUTER_LIST.getSelectedRow();
                computer_id=(jtblCOMPUTER_LIST.getModel().getValueAt(row,0).toString());
                mark=(jtblCOMPUTER_LIST.getModel().getValueAt(row,1).toString());
                 jLabelCOMP_ID.setText(computer_id);
                 jLabelMARK.setText(mark);
            }
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
       issueComputer();
    }//GEN-LAST:event_jBTNbORROW_COMPActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
       close();
          HOME hm= new HOME();
          hm.setTitle(config.getUserFN()+"     "+config.textline[5]);
          hm.setVisible(true); 
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jtblstudentlistMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtblstudentlistMouseClicked
       String fn,ln,studentid;
                int row=jtblstudentlist.getSelectedRow();
                studentid=(jtblstudentlist.getModel().getValueAt(row,0).toString());
                fn=(jtblstudentlist.getModel().getValueAt(row,1).toString());                
                ln=(jtblstudentlist.getModel().getValueAt(row,1).toString());
                 jLabelSTUDENT_ID.setText(studentid);
                 jLabelSTUDENT_NAMES.setText(fn+" "+ln);
    }//GEN-LAST:event_jtblstudentlistMouseClicked

    private void jtblstudentlistKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtblstudentlistKeyReleased
     if(evt.getKeyCode()==KeyEvent.VK_DOWN||evt.getKeyCode()==KeyEvent.VK_UP)
         {  String fn,ln,studentid;
                int row=jtblstudentlist.getSelectedRow();
                studentid=(jtblstudentlist.getModel().getValueAt(row,0).toString());
                fn=(jtblstudentlist.getModel().getValueAt(row,1).toString());                
                ln=(jtblstudentlist.getModel().getValueAt(row,2).toString());
                 jLabelSTUDENT_ID.setText(studentid);
                 jLabelSTUDENT_NAMES.setText(fn+" "+ln);
            }
    }//GEN-LAST:event_jtblstudentlistKeyReleased

    private void jtxtsearchStActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtsearchStActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtsearchStActionPerformed

    private void jtxtsearchStKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtsearchStKeyReleased
        /*if(!jtxtsearchSt.getText().trim().isEmpty()){
        try{
        String sql="select S.STUDENT_ID, S.FIRST_NAME, S.LAST_NAME, "
        + "CL.CLASS_NAME, D.DEPARTMENT_NAME from "
        + "STUDENT S, CLASS_NAME CL ,DEPARTMENT D WHERE "
        + "S.CLASS_NAME_ID=CL.CLASS_NAME_ID AND "
        + "S.DEPARTMENT_ID=D.DEPARTMENT_ID AND"
        + "(UPPER(S.FIRST_NAME) LIKE ? OR UPPER(S.LAST_NAME) LIKE ? OR "
        + "UPPER(CL.CLASS_NAME) LIKE ? OR UPPER(D.DEPARTMENT_NAME) LIKE ?)";
        pst=conn.prepareStatement(sql);
        String search = "%" + jtxtsearchSt.getText().toUpperCase().trim() + "%";
        pst.setString(1, search);
        pst.setString(2, search);
        pst.setString(3, search);
        pst.setString(4, search);
        rs=pst.executeQuery();
        jtblstudentlist.setModel(DbUtils.resultSetToTableModel(rs));
        }catch(Exception e){JOptionPane.showMessageDialog(null,e+"SEARCH 2!");}
        finally {
        try{
        rs.close();
        pst.close();
        //conn.close();
        }
        catch(Exception e){ }
        }
        }else{ StudentsList(); }     */   
    }//GEN-LAST:event_jtxtsearchStKeyReleased

    private void jTextBORROW_STATUSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextBORROW_STATUSActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextBORROW_STATUSActionPerformed

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
            java.util.logging.Logger.getLogger(ISUINGCOMPUTER.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ISUINGCOMPUTER.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ISUINGCOMPUTER.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ISUINGCOMPUTER.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new ISUINGCOMPUTER().setVisible(true);
            }
        });
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBTNbORROW_COMP;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelCOMP_ID;
    private javax.swing.JLabel jLabelMARK;
    private javax.swing.JLabel jLabelSTUDENT_ID;
    private javax.swing.JLabel jLabelSTUDENT_NAMES;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextBORROW_STATUS;
    private javax.swing.JTable jtblCOMPUTER_LIST;
    private javax.swing.JTable jtblstudentlist;
    private javax.swing.JTextField jtxtsearchCOMP;
    private javax.swing.JTextField jtxtsearchSt;
    // End of variables declaration//GEN-END:variables
}
