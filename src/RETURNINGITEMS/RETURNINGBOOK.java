
package RETURNINGITEMS;

import ISSUINGITEMS.*;
import Authentication.Login;
import LIBRARY.DATABASE.DatabaseHandler;
import LIBRARY.HOME.HOME;
import LIBRARY.HOME.config;
import LIBRARY.HOME.config;
import java.awt.Color;
import java.awt.Component;
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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import net.proteanit.sql.DbUtils;


public class RETURNINGBOOK extends javax.swing.JFrame {

    Connection conn=null;
    ResultSet rs=null;
    PreparedStatement pst=null;
    DefaultTableModel dm;
    
    String book_id,Book_Code,DUE_DATE,title;
    String fn="",ln="",ST_FN="",ST_LN="",librarianID;
    String issued_book_id,studentid,status="";
    
    DefaultTableModel model;
    TableRowSorter<DefaultTableModel> sorter;

    public RETURNINGBOOK() {
        initComponents();
        conn=DatabaseHandler.ConnectDb(); 
        //borrowedbooks(); 
        //StudentsList();
        borrowedbooks();        
        enableLiveSearch(jtxtsearchBOOK);
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
    
   /* private void borrowedbooks(){     
    try{
        String sql1="select B.BOOK_ID, B.TITLE, I.ISSUE_DATE, I.DUE_DATE, C.CATEGORY_NAME, "
                + " A.AUTHOR_NAME from BOOK B, ISSUEDBOOK I, AUTHOR A, CATEGORY C WHERE "
                + "I.STATUS='NOT_RETURNED' AND B.STATUS='NOT_AVAILABLE' ";
         pst=conn.prepareStatement(sql1);
         rs=pst.executeQuery();
         jtblborrowed_book.setModel(DbUtils.resultSetToTableModel(rs));

    }catch(Exception e){JOptionPane.showMessageDialog(null,e+" BOOK TABLE");}
    finally {try{rs.close(); pst.close();} catch(Exception e){ }}
      } */
    private void Selectedtransaction(){
            int row=jtblborrowed_book.getSelectedRow();
            book_id=(jtblborrowed_book.getModel().getValueAt(row,0).toString());                
    try{
        String sql1 =
        "SELECT S.FIRST_NAME, S.LAST_NAME, " +
        "I.LIBRARIAN_ID, I.ISSUED_BOOK_ID, " +
        "I.STUDENT_ID, I.BORROW_STATUS " +
        "FROM ISSUEDBOOK I " +
        "JOIN STUDENT S ON I.STUDENT_ID = S.STUDENT_ID " +
        "JOIN BOOK B ON I.BOOK_ID = B.BOOK_ID " +
        "WHERE I.STATUS = 'NOT_RETURNED' " +
        "AND B.STATUS = 'NOT_AVAILABLE' " +
        "AND I.BOOK_ID = ?";
        pst=conn.prepareStatement(sql1);
        pst.setString(1, book_id);
        rs=pst.executeQuery();
        if(rs.next()){            
            ST_LN=rs.getString("LAST_NAME");
            ST_FN=rs.getString("FIRST_NAME");
            librarianID=rs.getString("LIBRARIAN_ID");
            issued_book_id=rs.getString("ISSUED_BOOK_ID");
            studentid=rs.getString("STUDENT_ID");
            status=rs.getString("BORROW_STATUS");
            try {
                String sql2 = "select FIRST_NAME, LAST_NAME from LIBRARIAN WHERE LIBRARIAN_ID=?";
                pst = conn.prepareStatement(sql2);
                pst.setString(1, librarianID);
                rs = pst.executeQuery();
                if (rs.next()) {                    
                    ln = rs.getString("LAST_NAME");
                    fn= rs.getString("FIRST_NAME");
                }
            } catch (SQLException sQLException) 
            {JOptionPane.showMessageDialog(null,sQLException+" FAILED TO GET LIBRALIAN ID.");}
            
        }
        }catch(Exception e){JOptionPane.showMessageDialog(null,e+" SOME PROBLEM IN SELECTION ");}
        finally { try{ rs.close(); pst.close(); } catch(Exception e){ } }
    
                title=(jtblborrowed_book.getModel().getValueAt(row,1).toString());
                DUE_DATE=(jtblborrowed_book.getModel().getValueAt(row,3).toString());
                jLabelBOOK_CODE.setText(book_id);
                jLabelBOOK_TITLE.setText(title);
                jLabelSTUDENT_NAMES.setText(ST_FN+" ,,"+ST_LN);
                jLabelOFFER.setText(fn+",, "+ln);
                jLabelDUE_DATE.setText(DUE_DATE);
                jLabelISSUED_COND.setText(status);
      }
      public void borrowedbooks(){

    try{
        String sql =
                    "SELECT B.BOOK_ID, B.BOOK_CODE, B.TITLE, I.ISSUE_DATE, I.DUE_DATE, " +
                    "C.CATEGORY_NAME, A.AUTHOR_NAME, I.STATUS " +
                    "FROM ISSUEDBOOK I " +
                    "JOIN BOOK B ON I.BOOK_ID = B.BOOK_ID " +
                    "JOIN AUTHOR A ON B.AUTHOR_ID = A.AUTHOR_ID " +
                    "JOIN CATEGORY C ON B.CATEGORY_ID = C.CATEGORY_ID " +
                    "WHERE I.STATUS='NOT_RETURNED' " +
                    "AND B.STATUS='NOT_AVAILABLE'";
       
        pst = conn.prepareStatement(sql);
        rs = pst.executeQuery();

        model =
        (DefaultTableModel) net.proteanit.sql.DbUtils
        .resultSetToTableModel(rs);

        jtblborrowed_book.setModel(model);
        // Disable editing
        jtblborrowed_book.setDefaultEditor(Object.class, null);

        sorter = new TableRowSorter<>(model);
        jtblborrowed_book.setRowSorter(sorter);
        jtblborrowed_book.setRowHeight(25);
        
        jtblborrowed_book.setDefaultRenderer(Object.class,
        new DefaultTableCellRenderer(){

    @Override
    public Component getTableCellRendererComponent(
        JTable table,Object value,boolean isSelected,
        boolean hasFocus,int row,int column){

        Component c =
        super.getTableCellRendererComponent(
        table,value,isSelected,hasFocus,row,column);

        try{

            String dueDate =
            table.getValueAt(row,4).toString(); // DUE_DATE column

            java.sql.Date due =
            java.sql.Date.valueOf(dueDate);

            java.sql.Date today =
            new java.sql.Date(System.currentTimeMillis());

            if(due.before(today)){
                c.setBackground(Color.PINK);   // overdue
            }else{
                c.setBackground(Color.WHITE);
            }

        }catch(Exception e){
            c.setBackground(Color.WHITE);
            JOptionPane.showMessageDialog(null,e+" FAILED TO SET COLORS");
        }

        return c;
    }
});

        }catch(Exception e){
        JOptionPane.showMessageDialog(null,e+" FAILED TO LIST BOOKS");
    }
}
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
        jtxtsearchBOOK = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtblborrowed_book = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabelBOOK_CODE = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabelBOOK_TITLE = new javax.swing.JLabel();
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
        jBtnReturn_Comp.setText("RETURN BOOK");
        jBtnReturn_Comp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnReturn_CompActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Search", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 10), new java.awt.Color(0, 102, 102))); // NOI18N

        jtxtsearchBOOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtsearchBOOKActionPerformed(evt);
            }
        });
        jtxtsearchBOOK.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtxtsearchBOOKKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jtxtsearchBOOK, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jtxtsearchBOOK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jtblborrowed_book.setModel(new javax.swing.table.DefaultTableModel(
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
        jtblborrowed_book.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtblborrowed_bookMouseClicked(evt);
            }
        });
        jtblborrowed_book.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtblborrowed_bookKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jtblborrowed_book);

        jButton3.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jButton3.setForeground(new java.awt.Color(0, 153, 153));
        jButton3.setText("BACK");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("LIST OF  BORROWED BOOKS");

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "RETURNED BOOK", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 3, 12))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("BOOK CODE");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("BOOK TITLE");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("ISSUED CONDITION");

        jLabelBOOK_CODE.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelBOOK_CODE.setText("  ");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setText("DUE DATE");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setText("RETURNED DATE");

        jLabelBOOK_TITLE.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

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
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel10)
                                .addComponent(jLabel8)
                                .addComponent(jLabel13))
                            .addGap(54, 54, 54)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabelDUE_DATE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jreturn_date, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
                                .addComponent(jLabelOFFER, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel3)
                                .addComponent(jLabel5))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabelBOOK_TITLE, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabelBOOK_CODE, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                            .addComponent(jLabel12)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelSTUDENT_NAMES, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel6))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabelISSUED_COND, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextRUTURNED_COND, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE))))
                .addContainerGap(69, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelBOOK_CODE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelBOOK_TITLE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10)
                    .addComponent(jreturn_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabelISSUED_COND, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextRUTURNED_COND, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(86, 86, 86))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(416, 416, 416)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 866, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jBtnReturn_Comp)
                        .addGap(33, 33, 33)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
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
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 135, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBtnReturn_Comp)
                    .addComponent(jButton3)
                    .addComponent(jButton2))
                .addGap(59, 59, 59))
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

    private void jtxtsearchBOOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtsearchBOOKActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtsearchBOOKActionPerformed

    private void jtblborrowed_bookMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtblborrowed_bookMouseClicked
            Selectedtransaction();
    }//GEN-LAST:event_jtblborrowed_bookMouseClicked

    private void jtblborrowed_bookKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtblborrowed_bookKeyReleased
       if(evt.getKeyCode()==KeyEvent.VK_DOWN||evt.getKeyCode()==KeyEvent.VK_UP)
         {  Selectedtransaction();
            }
    }//GEN-LAST:event_jtblborrowed_bookKeyReleased

    private void jtxtsearchBOOKKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtsearchBOOKKeyReleased
   /* if(!jtxtsearchBOOK.getText().trim().isEmpty()){
        try{ 
        String sql="select B.BOOK_ID, B.TITLE, I.ISSUE_DATE, I.DUE_DATE, C.CATEGORY_NAME, "
                + " A.AUTHOR_NAME, I.STATUS "
                + "from BOOK B, AUTHOR A, CATEGORY C, ISSUEDBOOK I WHERE "
                + "I.STATUS='NOT_RETURNED' AND B.STATUS='NOT_AVAILABLE' AND (I.BOOK_ID=? OR UPPER(B.TITLE) LIKE ? OR "
                + "UPPER(C.CATEGORY_NAME) LIKE ? OR UPPER(A.AUTHOR_NAME) LIKE ? OR "
                + "UPPER(I.ISSUE_DATE) LIKE ? OR UPPER(I.DUE_DATE) LIKE ? OR UPPER(I.STATUS) LIKE ?)";
        String sql2="select B.BOOK_ID, B.TITLE, I.ISSUE_DATE, I.DUE_DATE, C.CATEGORY_NAME, "
                + " A.AUTHOR_NAME, I.STATUS "
                + "from BOOK B, AUTHOR A, CATEGORY C, ISSUEDBOOK I WHERE  "
                + "I.STATUS='NOT_RETURNED' AND B.STATUS='NOT_AVAILABLE' AND (UPPER(B.TITLE) LIKE ? OR "
                + "UPPER(C.CATEGORY_NAME) LIKE ? OR UPPER(A.AUTHOR_NAME) LIKE ? OR "
                + "UPPER(I.ISSUE_DATE) LIKE ? OR UPPER(I.DUE_DATE) LIKE ? OR UPPER(I.STATUS) LIKE ?)";
        
        String keyword = jtxtsearchBOOK.getText().trim().toUpperCase();
        String search = "%" + keyword + "%";
        if(keyword.matches("\\d+")) {
                pst=conn.prepareStatement(sql);
                pst.setLong(1, Long.parseLong(keyword));
                pst.setString(2, search);
                pst.setString(3, search);
                pst.setString(4, search);
                pst.setString(5, search);
                pst.setString(6, search);
                pst.setString(7, search);
        } else {
                pst=conn.prepareStatement(sql2);
                pst.setString(1, search);
                pst.setString(2, search);
                pst.setString(3, search);
                pst.setString(4, search);
                pst.setString(5, search);
                pst.setString(6, search);
        }        
         rs=pst.executeQuery();
         jtblborrowed_book.setModel(DbUtils.resultSetToTableModel(rs));
    }catch(Exception e){JOptionPane.showMessageDialog(null,e+"SEARCH_BOOK!");}
        finally { try{ rs.close(); pst.close(); } catch(Exception e){ } }
    }else{ borrowedbooks(); } 
        */
    }//GEN-LAST:event_jtxtsearchBOOKKeyReleased

    private void jBtnReturn_CompActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnReturn_CompActionPerformed
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        //jDateChooser2.setDate(today);
        
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        //String bookcode=jLabelBOOK_CODE.getText().trim();
        //String booktile=jLabelBOOK_TITLE.getText().trim();
       String returnDate=df.format(jreturn_date.getDate());
        String returnStatus=jTextRUTURNED_COND.getText().trim();
        
        Login l= new Login();
        int librrian_id;
        librrian_id=l.lib_id;
        
                   try {
         String SQL ="UPDATE ISSUEDBOOK SET "
                 + "RECEIVER_ID=?, RETURN_DESCRIPTION=?, RETURN_DATE=?, STATUS=? "
                 + "WHERE ISSUED_BOOK_ID=? ";

                    pst = conn.prepareStatement(SQL);
                    pst.setInt(1, librrian_id);
                    pst.setString(2, returnStatus);
                    pst.setString(3, returnDate);
                    pst.setString(4, "RETURNED");
                    pst.setString(5, issued_book_id);                    
                    pst.execute();
                    pst.close();
                    try {
            String query = "UPDATE BOOK SET STATUS=? WHERE BOOK_ID=" +book_id+ "";
            pst = conn.prepareStatement(query);
            pst.setString(1, "AVAILABLE");
            pst.execute();
            } catch (SQLException e){JOptionPane.showMessageDialog(null, e+" BOOK NOT UPDATED IN THE SYSTEM ");}
            
            JOptionPane.showMessageDialog(null, ST_FN+" "+ST_LN+" RUTURNED BOOK SUCCESSFULLY.");
            } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e+" NOT UPDATED!"); }  
            
            finally { try{ rs.close(); pst.close(); } catch(Exception e){ } }
       
        jLabelBOOK_CODE.setText("");
        jLabelBOOK_TITLE.setText("");
        jTextRUTURNED_COND.setText("");
        jLabelSTUDENT_NAMES.setText("");
        jLabelOFFER.setText("");        
        jLabelDUE_DATE.setText("");
        //jreturn_date.setDate(today);
        jreturn_date.cleanup();
        jtxtsearchBOOK.setText("");
        borrowedbooks();
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
            java.util.logging.Logger.getLogger(RETURNINGBOOK.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RETURNINGBOOK.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RETURNINGBOOK.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RETURNINGBOOK.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new RETURNINGBOOK().setVisible(true);
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
    private javax.swing.JLabel jLabelBOOK_CODE;
    private javax.swing.JLabel jLabelBOOK_TITLE;
    private javax.swing.JLabel jLabelDUE_DATE;
    private javax.swing.JLabel jLabelISSUED_COND;
    private javax.swing.JLabel jLabelOFFER;
    private javax.swing.JLabel jLabelSTUDENT_NAMES;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextRUTURNED_COND;
    private com.toedter.calendar.JDateChooser jreturn_date;
    private javax.swing.JTable jtblborrowed_book;
    private javax.swing.JTextField jtxtsearchBOOK;
    // End of variables declaration//GEN-END:variables
}
