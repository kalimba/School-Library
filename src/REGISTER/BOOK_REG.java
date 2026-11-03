/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package REGISTER;

import LIBRARY.DATABASE.DatabaseHandler;
import LIBRARY.HOME.HOME;
import LIBRARY.HOME.config;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Kalimba
 */
public class BOOK_REG extends javax.swing.JFrame {

    Connection conn=null;
    ResultSet rs=null;
    PreparedStatement pst=null;
    private javax.swing.Timer typingTimer;
    TableRowSorter<DefaultTableModel> sorter;
    
    public BOOK_REG() {
        initComponents();
        conn=DatabaseHandler.ConnectDb();
        loadBookList();
        setupAutoClearBookForm(); 
        loadAllCombos();
        enableLiveSearch(jTextbooktitle);
        typingTimer = new javax.swing.Timer(400, e -> {
        generate(); 
        System.out.println("generate irahamagawe");
        });
        typingTimer.setRepeats(false);
        setupBookCodeAuto();        
    }
    public void close(){
WindowEvent winClosingEvent=new WindowEvent(this,WindowEvent.WINDOW_CLOSING);
Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(winClosingEvent);
}
private void loadAllCombos() {

    loadComboBox(jComboauthor,
            "AUTHOR",
            "AUTHOR_ID",
            "AUTHOR_NAME");

    loadComboBox(jComboBookcat,
            "CATEGORY",
            "CATEGORY_ID",
            "CATEGORY_NAME");
}
private boolean validateBookForm() {

    // BOOK TITLE
    if (jTextbooktitle.getText().trim().isEmpty()) {
        showError("Enter Book Title", jTextbooktitle);
        return false;
    }

    // ISBN
    if (jTextisbn.getText().trim().isEmpty()) {
        showError("Enter ISBN", jTextisbn);
        return false;
    }

    // PUBLICATION YEAR
    String yearText = jTextpubyear.getText().trim();

    if (yearText.isEmpty()) {
        showError("Enter Publication Year", jTextpubyear);
        return false;
    }

    int year;

    try {
        year = Integer.parseInt(yearText);
    } catch (NumberFormatException e) {
        showError("Publication Year must be numeric", jTextpubyear);
        return false;
    }

    // ⭐ YEAR RANGE VALIDATION
    int currentYear = java.time.Year.now().getValue();

    if (year < 1500 || year > currentYear) {
        showError("Invalid Publication Year", jTextpubyear);
        return false;
    }

    // AUTHOR
    ComboItem author = (ComboItem) jComboauthor.getSelectedItem();
    if (author == null || author.getId().equals("0")) {
        showError("Select Author", jComboauthor);
        return false;
    }

    // CATEGORY
    ComboItem category = (ComboItem) jComboBookcat.getSelectedItem();
    if (category == null || category.getId().equals("0")) {
        showError("Select Category", jComboBookcat);
        return false;
    }

    // BOOK CODE
    if (jTextbookcode.getText().trim().isEmpty()) {
        showError("Book Code is not generated", jTextbookcode);
        return false;
    }
    if (jTextbkcomment.getText().trim().isEmpty()) {
        showError("Enter Book Description", jTextbookcode);
        return false;
    }
    return true;
}
private void showError(String message, Component comp) {

    // Show message to user
    JOptionPane.showMessageDialog(this, message);

    // Highlight field in RED
    comp.setBackground(new Color(255, 200, 200));

    // Move cursor to field
    comp.requestFocus();
}
    private void setupAutoClearBookForm() {

    enableAutoClear(jTextbookcode);
    enableAutoClear(jTextbooktitle);
    enableAutoClear(jTextisbn);
    enableAutoClear(jTextpubyear);
    enableAutoClear(jTextbkcomment);
    enableAutoClear(jComboauthor);
    enableAutoClear(jComboBookcat);
}
private void enableAutoClear(Component comp) {

    // ==========================
    // TEXT FIELD AUTO CLEAR
    // ==========================
    if (comp instanceof JTextComponent) {

        ((JTextComponent) comp).getDocument()
        .addDocumentListener(new javax.swing.event.DocumentListener() {

            private void clear() {
                comp.setBackground(Color.WHITE);
            }

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                clear();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                clear();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                clear();
            }
        });
    }

    // ==========================
    // COMBOBOX AUTO CLEAR
    // ==========================
    if (comp instanceof JComboBox) {

        ((JComboBox<?>) comp).addActionListener(e -> {
            comp.setBackground(Color.WHITE);
        });
    }
}
    private void loadComboBox(JComboBox combo,
                          String table,
                          String idColumn,
                          String nameColumn) {

    combo.removeAllItems();
    combo.addItem(new ComboItem("0", "-- Select --"));

    String sql =
        "SELECT " + idColumn + ", " + nameColumn +
        " FROM " + table;

    try (PreparedStatement pst = conn.prepareStatement(sql);
         ResultSet rs = pst.executeQuery()) {

        while (rs.next()) {
            combo.addItem(new ComboItem(
                rs.getString(idColumn),
                rs.getString(nameColumn)
            ));
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null,
                "Load Error: " + e);
    }
}
public void saveBookForm() {
   
    // 1. VALIDATION
    if (!validateBookForm())
        return;

    // 2. GET IDS FROM COMBO CLASS
    ComboItem authorItem = (ComboItem) jComboauthor.getSelectedItem();
    ComboItem categoryItem = (ComboItem) jComboBookcat.getSelectedItem();
    //setupBookCodeAuto();
    String authorID = authorItem.getId();
    String categoryID = categoryItem.getId();
    
    // 3. INSERT QUERY
    String sql =
        "INSERT INTO BOOK " +
        "(BOOK_TYPE,BOOK_CODE,TITLE,ISBN,PUBLICATION_YEAR," +
        "AUTHOR_ID,CATEGORY_ID,STATUS,COMMENT,REGISTRATION_DATE) " +
        "VALUES(?,?,?,?,?,?,?,?,?,?)";
    //setupBookCodeAuto();
    try (PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setString(1, jComboBooktype.getSelectedItem().toString().trim());
        pst.setString(2, jTextbookcode.getText().trim());
        pst.setString(3, jTextbooktitle.getText().trim().toUpperCase());
        pst.setString(4, jTextisbn.getText().trim());
        pst.setString(5, jTextpubyear.getText().trim());
        pst.setString(6, authorID);
        pst.setString(7, categoryID);
        pst.setString(8, "AVAILABLE");
        pst.setString(9, jTextbkcomment.getText().trim());
        pst.setDate(10, java.sql.Date.valueOf(LocalDate.now()));
        

        pst.executeUpdate();

        //setToEmpty();

        JOptionPane.showMessageDialog(null,
                "BOOK SAVED SUCCESSFULLY");
        setToEmpty();
        loadBookList();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null,
                "SAVE ERROR: " + e);
    }
}
private void loadBookList() {

    DefaultTableModel model = new DefaultTableModel(
        new Object[][]{},
        new String[]{
            "No","BOOK CODE","TITLE","ISBN","YEAR",
            "CATEGORY","AUTHOR","TOTAL COPIES","AVAILABLE COPIES"
        }
    );

      jtblbooklist.setModel(model);

      // ⭐ ADD THIS (IMPORTANT FOR SEARCH)
      sorter = new TableRowSorter<>(model);
      jtblbooklist.setRowSorter(sorter);

      formatBookTable();
    try {

        String sql =
            "SELECT MAX(BOOK.BOOK_CODE) AS BOOK_CODE, " +
            "BOOK.TITLE, " +
            "MAX(BOOK.ISBN) AS ISBN, " +
            "MAX(BOOK.PUBLICATION_YEAR) AS PUBLICATION_YEAR, " +
            "CATEGORY.CATEGORY_NAME, " +
            "AUTHOR.AUTHOR_NAME, " +
            "COUNT(*) AS TOTAL_COPIES, " +
            "SUM(CASE WHEN BOOK.STATUS='AVAILABLE' THEN 1 ELSE 0 END) AS AVAILABLE_COPIES " +
            "FROM BOOK " +
            "JOIN CATEGORY ON BOOK.CATEGORY_ID=CATEGORY.CATEGORY_ID " +
            "JOIN AUTHOR ON BOOK.AUTHOR_ID=AUTHOR.AUTHOR_ID " +
            "GROUP BY BOOK.TITLE, CATEGORY.CATEGORY_NAME, AUTHOR.AUTHOR_NAME " +
            "ORDER BY BOOK.TITLE";

        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        int number = 1;

        while (rs.next()) {

            model.addRow(new Object[]{
                number++,
                rs.getString("BOOK_CODE"),
                rs.getString("TITLE"),
                rs.getString("ISBN"),
                rs.getInt("PUBLICATION_YEAR"),
                rs.getString("CATEGORY_NAME"),
                rs.getString("AUTHOR_NAME"),
                rs.getInt("TOTAL_COPIES"),
                rs.getInt("AVAILABLE_COPIES")
            });
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}
private void formatBookTable() {

    JTableHeader header = jtblbooklist.getTableHeader();

    // ===== HEADER STYLE =====
    header.setFont(new Font("Segoe UI", Font.BOLD, 14));
    header.setBackground(new Color(30, 144, 255));
    header.setForeground(Color.WHITE);

    // ===== ROW HEIGHT =====
    jtblbooklist.setRowHeight(28);

    // ===== COLUMN WIDTHS =====
    TableColumnModel columnModel = jtblbooklist.getColumnModel();

    columnModel.getColumn(0).setPreferredWidth(40);   // No
    columnModel.getColumn(1).setPreferredWidth(110);  // Book Code
    columnModel.getColumn(2).setPreferredWidth(250);  // Title
    columnModel.getColumn(3).setPreferredWidth(100);  // ISBN
    columnModel.getColumn(4).setPreferredWidth(60);   // Year
    columnModel.getColumn(5).setPreferredWidth(130);  // Category
    columnModel.getColumn(6).setPreferredWidth(150);  // Author
    columnModel.getColumn(7).setPreferredWidth(110);  // Total
    columnModel.getColumn(8).setPreferredWidth(130);  // Available

    // ===== ALIGNMENT =====
    DefaultTableCellRenderer center =
            new DefaultTableCellRenderer();
    center.setHorizontalAlignment(SwingConstants.CENTER);

    DefaultTableCellRenderer right =
            new DefaultTableCellRenderer();
    right.setHorizontalAlignment(SwingConstants.RIGHT);

    // Center columns
    columnModel.getColumn(0).setCellRenderer(center); // No
    columnModel.getColumn(4).setCellRenderer(center); // Year

    // Right align numbers
    columnModel.getColumn(7).setCellRenderer(right); // Total
    columnModel.getColumn(8).setCellRenderer(right); // Available

    // ===== SELECTION COLOR =====
    jtblbooklist.setSelectionBackground(new Color(184,207,229));
    jtblbooklist.setSelectionForeground(Color.BLACK);

    // ===== AUTO RESIZE OFF =====
    jtblbooklist.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton5 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextbookcode = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextbooktitle = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jComboBooktype = new javax.swing.JComboBox();
        jTextisbn = new javax.swing.JTextField();
        jTextpubyear = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jComboBookcat = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        jComboauthor = new javax.swing.JComboBox();
        jButton6 = new javax.swing.JButton();
        jTextbkcomment = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtblbooklist = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();

        jButton5.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jButton5.setForeground(new java.awt.Color(0, 153, 153));
        jButton5.setText("BACK");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLocation(new java.awt.Point(30, 100));

        jLabel1.setText("BOOK CATEGORY");

        jLabel2.setText("BOOK CODE");

        jTextbookcode.setEditable(false);

        jLabel3.setText("BOOK TITLE");

        jLabel5.setText("ISBN");

        jLabel6.setText("BOOK TYPE");

        jLabel7.setText("COMMENT");

        jButton1.setText("SAVE");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("CENCEL");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jComboBooktype.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-- Select --", "HARD COPY", "SOFT COPY" }));
        jComboBooktype.setName(""); // NOI18N
        jComboBooktype.setOpaque(false);

        jLabel8.setText("PUBLICATION YEAR");

        jComboBookcat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SELECT", "MATHEMATICS", "CHEMISTRY", "BIOLOGY", "NOVEL" }));
        jComboBookcat.setName(""); // NOI18N
        jComboBookcat.setOpaque(false);

        jLabel9.setText("AUTHOR");

        jComboauthor.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-- Select --", "MATHEMATICS", "CHEMISTRY", "BIOLOGY", "NOVEL" }));
        jComboauthor.setName(""); // NOI18N
        jComboauthor.setOpaque(false);

        jButton6.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jButton6.setForeground(new java.awt.Color(0, 153, 153));
        jButton6.setText("BACK");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5)
                    .addComponent(jLabel8)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel9)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(jButton6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
                        .addComponent(jButton2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextbkcomment, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jComboauthor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextbookcode)
                            .addComponent(jComboBooktype, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextpubyear)
                            .addComponent(jComboBookcat, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextbooktitle)
                            .addComponent(jTextisbn, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextbooktitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextisbn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextpubyear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jComboBookcat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextbookcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBooktype, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboauthor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextbkcomment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton6))
                .addGap(46, 46, 46))
        );

        jtblbooklist.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jtblbooklist.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtblbooklistMouseClicked(evt);
            }
        });
        jtblbooklist.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtblbooklistKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(jtblbooklist);
        jtblbooklist.getAccessibleContext().setAccessibleParent(this);

        jButton4.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jButton4.setForeground(new java.awt.Color(0, 153, 153));
        jButton4.setText("BACK");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 895, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jButton4)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jButton4)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        getAccessibleContext().setAccessibleParent(this);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        saveBookForm();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
   close();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        close();
          HOME hm= new HOME();
          hm.setTitle(config.getUserFN()+"     "+config.textline[5]);
          hm.setVisible(true);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jtblbooklistKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtblbooklistKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_DOWN||evt.getKeyCode()==KeyEvent.VK_UP)
         { takeselectedrow();
            }
    }//GEN-LAST:event_jtblbooklistKeyPressed
    private void jtblbooklistMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtblbooklistMouseClicked
         takeselectedrow();
    }//GEN-LAST:event_jtblbooklistMouseClicked

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
            java.util.logging.Logger.getLogger(BOOK_REG.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BOOK_REG.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BOOK_REG.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BOOK_REG.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
              BOOK_REG BR=  new BOOK_REG();
                BR.setTitle("     REGISTER NEW BOOK");
                BR.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JComboBox jComboBookcat;
    private javax.swing.JComboBox jComboBooktype;
    private javax.swing.JComboBox jComboauthor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextbkcomment;
    private javax.swing.JTextField jTextbookcode;
    private javax.swing.JTextField jTextbooktitle;
    private javax.swing.JTextField jTextisbn;
    private javax.swing.JTextField jTextpubyear;
    private javax.swing.JTable jtblbooklist;
    // End of variables declaration//GEN-END:variables

private void setToEmpty() {
//jComboBooktype.setSelectedIndex(1);
jTextbookcode.setText("");
jTextbooktitle.setText("");
jTextisbn.setText("");
jTextpubyear.setText("");
jTextbkcomment.setText("");
jComboauthor.setSelectedIndex(0);
jComboBookcat.setSelectedIndex(0);
jTextbkcomment.requestFocus();
}

private String generateBookCode(String categoryName,
                                String bookTitle) {

    String prefix =
            categoryName.substring(0,2).toUpperCase();

    String year =
            String.valueOf(java.time.Year.now().getValue())
                    .substring(2);

    String base = prefix + year;
System.out.println(base);
    try {

        // =====================================================
        // 1️⃣ CHECK IF SAME TITLE EXISTS
        // =====================================================
        String sameTitleSQL =
            "SELECT BOOK_CODE FROM BOOK " +
            "WHERE UPPER(TITLE)=UPPER(?) " +
            "AND BOOK_CODE LIKE ? " +
            "ORDER BY BOOK_CODE DESC";

        PreparedStatement pst =
                conn.prepareStatement(sameTitleSQL);

        pst.setString(1, bookTitle);
        pst.setString(2, base + "%");

        ResultSet rs = pst.executeQuery();

        if (rs.next()) {

            String lastCode = rs.getString("BOOK_CODE");

            char letter = lastCode.charAt(4);
            int number =
                    Integer.parseInt(lastCode.substring(5));
   System.out.println(number+"nber "+letter+" letter / letter "+letter);
            number++;
   System.out.println(number);
            return base
                    + letter
                    + String.format("%04d", number);
        }else System.out.println("code ntibonetse");

        rs.close();
        pst.close();

        // =====================================================
        // 2️⃣ NEW TITLE → FIND LAST LETTER USED
        // =====================================================
        String letterSQL =
            "SELECT BOOK_CODE FROM BOOK " +
            "WHERE BOOK_CODE LIKE ? " +
            "ORDER BY BOOK_CODE DESC";

        pst = conn.prepareStatement(letterSQL);
        pst.setString(1, base + "%");

        rs = pst.executeQuery();

        char nextLetter = 'A';

        if (rs.next()) {

            String lastCode = rs.getString("BOOK_CODE");

            char lastLetter = lastCode.charAt(4);

            nextLetter = (char)(lastLetter + 1);
    System.out.println(lastCode+" "+lastLetter+" "+nextLetter+"lastCode/lastLetter/nextLetter");
            if(nextLetter > 'Z'){
                JOptionPane.showMessageDialog(null,
                        "Alphabet limit reached!");
                return null;
            }
        }

        rs.close();
        pst.close();
System.out.println(base+" base nextLetter "+nextLetter);
        // FIRST BOOK FOR THIS TITLE
        return base + nextLetter + "0001";

    } catch(Exception e){

        JOptionPane.showMessageDialog(null,
                "Code Generation Error: " + e);
    }

    return null;
}
    private void setupBookCodeAuto() {

    // TITLE typing → smart delayed generation
    jTextbooktitle.getDocument().addDocumentListener(
        new javax.swing.event.DocumentListener() {

        public void insertUpdate(javax.swing.event.DocumentEvent e) {
            smartGenerate();        }
        public void removeUpdate(javax.swing.event.DocumentEvent e) {
            smartGenerate();        }
        public void changedUpdate(javax.swing.event.DocumentEvent e) {
            smartGenerate();        }
    });

    // CATEGORY change → immediate generation
    jComboBookcat.addActionListener(e -> generate());
}
     private void generate() {

    // CATEGORY must be selected
    if (jComboBookcat.getSelectedIndex() <= 0){
        jTextbookcode.setText("");
        return;}

    // TITLE must exist
    String title = jTextbooktitle.getText().trim();
    if (title.isEmpty()){
        jTextbookcode.setText("");
        return;} 
 int i =1;
 System.out.println("tugiye muri database noneho"+i++);
    // Get category object safely
    ComboItem cat = (ComboItem) jComboBookcat.getSelectedItem();
    if (cat == null)
        return;

    // Generate KGL book code
    String code = generateBookCode(cat.toString(), title);

    // Avoid rewriting same value repeatedly
    if (!code.equals(jTextbookcode.getText())) {
        jTextbookcode.setText(code);
    }
}
     private void smartGenerate() {

    if (typingTimer.isRunning())
        typingTimer.restart();
    else
        typingTimer.start();
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
private void takeselectedrow() {

    int row = jtblbooklist.getSelectedRow();

    if(row == -1){
        JOptionPane.showMessageDialog(null, "Select a book first");
        return;
    }

    // IMPORTANT when sorter is used
    row = jtblbooklist.convertRowIndexToModel(row);

    DefaultTableModel model =
            (DefaultTableModel) jtblbooklist.getModel();

    jTextbookcode.setText(model.getValueAt(row, 1).toString());
    jTextbooktitle.setText(model.getValueAt(row, 2).toString());
    jTextisbn.setText(model.getValueAt(row, 3).toString());
    jTextpubyear.setText(model.getValueAt(row, 4).toString());

    // Optional fields reset
    jTextbkcomment.setText("");

    // Select CATEGORY in ComboBox
    selectComboItem(jComboBookcat,
            model.getValueAt(row, 5).toString());

    // Select AUTHOR in ComboBox
    selectComboItem(jComboauthor,
            model.getValueAt(row, 6).toString());
    
jComboBooktype.setSelectedIndex(1);    
jTextbkcomment.requestFocus();
}
private void selectComboItem(JComboBox<ComboItem> combo,
                             String value){

    for(int i=0;i<combo.getItemCount();i++){

        ComboItem item = combo.getItemAt(i);

        if(item.toString().equalsIgnoreCase(value)){
            combo.setSelectedIndex(i);
            break;
        }
    }
}
}