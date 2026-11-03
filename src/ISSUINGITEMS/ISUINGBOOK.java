
package ISSUINGITEMS;

import Authentication.Login;
import LIBRARY.DATABASE.DatabaseHandler;
import LIBRARY.HOME.HOME;
import LIBRARY.HOME.config;
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


public class ISUINGBOOK extends javax.swing.JFrame {

    Connection conn=null;
    ResultSet rs=null;
    PreparedStatement pst=null;
    DefaultTableModel dm;
    DefaultTableModel model;
    TableRowSorter<DefaultTableModel> sorter;
    public ISUINGBOOK() {
        initComponents();
        conn=DatabaseHandler.ConnectDb(); 
        booksList(); 
        StudentsList();
        enableLiveSearch(jtxtsearchB);
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
    
    private void booksList(){

            String sql =
                "SELECT B.BOOK_ID, B.BOOK_CODE, B.TITLE, C.CATEGORY_NAME, " +
                "B.STATUS, A.AUTHOR_NAME " +
                "FROM BOOK B " +
                "JOIN AUTHOR A ON B.AUTHOR_ID = A.AUTHOR_ID " +
                "JOIN CATEGORY C ON B.CATEGORY_ID = C.CATEGORY_ID " +
                "WHERE B.STATUS = 'AVAILABLE'";

            try{

                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                jtblbooklist.setModel(
                    net.proteanit.sql.DbUtils.resultSetToTableModel(rs)
                );
                // Disable editing
                jtblbooklist.setDefaultEditor(Object.class, null);
                
                jtblbooklist.setRowHeight(25);   // professional look
            }catch(Exception e){
                JOptionPane.showMessageDialog(null,e+" BOOK TABLE");
            }
            finally{
                try{if(rs!=null) rs.close();
                    if(pst!=null) pst.close();
                }catch(Exception ex){}
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
        
        //jtblstudentlist.setModel(model);
        // ⭐ 2. INITIALIZE SORTER HERE (THIS IS THE KEY)
        sorter = new TableRowSorter<>(model);
        jtblstudentlist.setRowSorter(sorter);
        jtblstudentlist.setAutoCreateRowSorter(true);
        // optional UI improvement
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
                RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(text)));
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
        jScrollPane2 = new javax.swing.JScrollPane();
        jtblstudentlist = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jtxtsearchSt = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextBORROWSTATUS = new javax.swing.JTextField();
        jLabelCODE = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabelTITLE = new javax.swing.JLabel();
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

        jBTNBORROW_BOOK.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jBTNBORROW_BOOK.setText("BORROW");
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
        jLabel1.setText("LIST OF AVAILABLE BOOKS");

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "SELECTED BOOK", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 3, 12))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("BOOK CODE:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("BOOK TITLE:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("BOOK CONDTION");

        jTextBORROWSTATUS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextBORROWSTATUSActionPerformed(evt);
            }
        });

        jLabelCODE.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelCODE.setText("  ");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setText("ISSUED DATE");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setText("DUE DATE");

        jLabelTITLE.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

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
                            .addComponent(jTextBORROWSTATUS, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jDateChooser2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3))
                        .addGap(44, 44, 44)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabelCODE, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
                            .addComponent(jLabelTITLE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabelCODE, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabelTITLE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jTextBORROWSTATUS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                        .addComponent(jBTNBORROW_BOOK)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 87, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(70, 70, 70)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel2)
                                .addGap(484, 484, 484)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 866, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGap(33, 33, 33)
                            .addComponent(jLabel1)
                            .addGap(434, 434, 434)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                            .addGap(18, 18, 18)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 866, javax.swing.GroupLayout.PREFERRED_SIZE))))
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
                            .addComponent(jBTNBORROW_BOOK)))
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

    private void jtxtsearchBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtsearchBActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtsearchBActionPerformed

    private void jtblbooklistMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtblbooklistMouseClicked
       String code,title;
                int row=jtblbooklist.getSelectedRow();
                code=(jtblbooklist.getModel().getValueAt(row,1).toString());
                title=(jtblbooklist.getModel().getValueAt(row,2).toString());
                 jLabelCODE.setText(code);
                 jLabelTITLE.setText(title);
    }//GEN-LAST:event_jtblbooklistMouseClicked

    private void jtblbooklistKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtblbooklistKeyReleased
       if(evt.getKeyCode()==KeyEvent.VK_DOWN||evt.getKeyCode()==KeyEvent.VK_UP)
         {  String code,title;
                int row=jtblbooklist.getSelectedRow();
                code=(jtblbooklist.getModel().getValueAt(row,1).toString());
                title=(jtblbooklist.getModel().getValueAt(row,2).toString());
                 jLabelCODE.setText(code);
                 jLabelTITLE.setText(title);
            }
    }//GEN-LAST:event_jtblbooklistKeyReleased

    private void jtxtsearchBKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtsearchBKeyReleased
        /*if(!jtxtsearchB.getText().trim().isEmpty()){
        try{
        String sql="select BOOK.BOOK_ID, BOOK.BOOK_CODE, BOOK.TITLE, CATEGORY.CATEGORY_NAME, "
        + "BOOK.STATUS, AUTHOR.AUTHOR_NAME "
        + "from BOOK, AUTHOR, CATEGORY "
        + "WHERE BOOK.STATUS='AVAILABLE' AND BOOK.AUTHOR_ID=AUTHOR.AUTHOR_ID AND "
        + "BOOK.CATEGORY_ID=CATEGORY.CATEGORY_ID AND"
        + "(UPPER(BOOK.BOOK_CODE) LIKE ? "
        + "OR UPPER(BOOK.TITLE) LIKE ? "
        + "OR UPPER(CATEGORY.CATEGORY_NAME) LIKE ? "
        + "OR UPPER(AUTHOR.AUTHOR_NAME) LIKE ? "
        + "OR UPPER(BOOK.STATUS) LIKE ?)";
        pst=conn.prepareStatement(sql);
        String search = "%" + jtxtsearchB.getText().toUpperCase().trim() + "%";
        pst.setString(1, search);
        pst.setString(2, search);
        pst.setString(3, search);
        pst.setString(4, search);
        pst.setString(5, search);
        rs=pst.executeQuery();
        jtblbooklist.setModel(DbUtils.resultSetToTableModel(rs));
        }catch(Exception e){JOptionPane.showMessageDialog(null,e+"SEARCH!");}
        finally {
        try{
        rs.close();
        pst.close();
        //conn.close();
        }
        catch(Exception e){ }
        }
        }else{ booksList(); }
        */
    }//GEN-LAST:event_jtxtsearchBKeyReleased

    private void jBTNBORROW_BOOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBTNBORROW_BOOKActionPerformed
       // Calendar cal = Calendar.getInstance();
        //Date today = cal.getTime();
       // jDateChooser2.setDate(today);
        
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        String bookcode = jLabelCODE.getText().trim();
        String booktitle = jLabelTITLE.getText().trim();
        String borrowcondition = jTextBORROWSTATUS.getText().trim();
        String ST_NAME = jLabelSTUDENT_NAMES.getText().trim();
        String st_id = jLabelSTUDENT_ID.getText().trim();

        int book_id = 0;

        try {

            /* ==========================
               1. GET BOOK_ID SAFELY
               ========================== */
            String sql = "SELECT BOOK_ID FROM BOOK WHERE BOOK_CODE=?";

            pst = conn.prepareStatement(sql);
            pst.setString(1, bookcode);
            rs = pst.executeQuery();

            if (rs.next()) {
                book_id = rs.getInt("BOOK_ID");
            } else {
                JOptionPane.showMessageDialog(null, "BOOK NOT FOUND");
                return;
            }

            rs.close();
            pst.close();

            /* ==========================
               2. GET LIBRARIAN ID
               ========================== */
            int librarian_id = Login.lib_id;

            /* ==========================
               3. INSERT ISSUED BOOK
               ========================== */
            String sql1 =
                    "INSERT INTO ISSUEDBOOK "
                  + "(STUDENT_ID,BOOK_ID,LIBRARIAN_ID,ISSUE_DATE,DUE_DATE,STATUS,BORROW_STATUS) "
                  + "VALUES(?,?,?,?,?,?,?)";

            pst = conn.prepareStatement(sql1);

            pst.setString(1, st_id);
            pst.setInt(2, book_id);
            pst.setInt(3, librarian_id);

            pst.setString(4, df.format(jDateChooser1.getDate()));
            pst.setString(5, df.format(jDateChooser2.getDate()));

            pst.setString(6, "NOT_RETURNED");
            pst.setString(7, borrowcondition);

            pst.executeUpdate();
            pst.close();

            /* ==========================
               4. UPDATE BOOK STATUS
               ========================== */
            String query = "UPDATE BOOK SET STATUS=? WHERE BOOK_ID=?";

            pst = conn.prepareStatement(query);
            pst.setString(1, "NOT_AVAILABLE");
            pst.setInt(2, book_id);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(null,
                    ST_NAME + " BORROWED A BOOK SUCCESSFULLY.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

        /* ==========================
           5. CLEAN RESOURCES
           ========================== */
        finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (pst != null) pst.close(); } catch (Exception e) {}
        }

        /* ==========================
           6. RESET FORM
           ========================== */
        jLabelCODE.setText("");
        jLabelTITLE.setText("");
        jTextBORROWSTATUS.setText("");
        jLabelSTUDENT_NAMES.setText("");
        jLabelSTUDENT_ID.setText("");

        jDateChooser1.setDate(null);
        jDateChooser2.setDate(null);

        jtxtsearchB.setText("");
        jtxtsearchSt.setText("");

        booksList();
        StudentsList();
    }//GEN-LAST:event_jBTNBORROW_BOOKActionPerformed

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
                ln=(jtblstudentlist.getModel().getValueAt(row,2).toString());
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
        }else{ StudentsList(); }        */
    }//GEN-LAST:event_jtxtsearchStKeyReleased

    private void jTextBORROWSTATUSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextBORROWSTATUSActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextBORROWSTATUSActionPerformed

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
            java.util.logging.Logger.getLogger(ISUINGBOOK.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ISUINGBOOK.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ISUINGBOOK.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ISUINGBOOK.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ISUINGBOOK().setVisible(true);
            }
        });
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBTNBORROW_BOOK;
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
    private javax.swing.JLabel jLabelCODE;
    private javax.swing.JLabel jLabelSTUDENT_ID;
    private javax.swing.JLabel jLabelSTUDENT_NAMES;
    private javax.swing.JLabel jLabelTITLE;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextBORROWSTATUS;
    private javax.swing.JTable jtblbooklist;
    private javax.swing.JTable jtblstudentlist;
    private javax.swing.JTextField jtxtsearchB;
    private javax.swing.JTextField jtxtsearchSt;
    // End of variables declaration//GEN-END:variables
}
