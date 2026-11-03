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
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Kalimba
 */
public class STUDENT_REG extends javax.swing.JFrame {

    Connection conn=null;
    ResultSet rs=null;
    PreparedStatement pst=null;
    
    public STUDENT_REG() {
        initComponents();
        config.openFile();
        config.readFile();        
        config.closeFile();
        conn=DatabaseHandler.ConnectDb();
        setupAutoClear();
        //SwingUtilities.invokeLater(() -> {
        loadComboBox(jComboclass,
             "CLASS_NAME",
             "CLASS_NAME_ID",
             "CLASS_NAME");

        loadComboBox(jCombodept,
             "DEPARTMENT",
             "DEPARTMENT_ID",
             "DEPARTMENT_NAME");
      //  });
    }
    public void close(){
WindowEvent winClosingEvent=new WindowEvent(this,WindowEvent.WINDOW_CLOSING);
Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(winClosingEvent);
}
    private void showError(String message, Component comp){

    JOptionPane.showMessageDialog(this, message);

    comp.requestFocus();

    // Optional visual highlight
    comp.setBackground(new Color(255,220,220));
}
    private boolean validateForm() {

    // TEXTFIELDS VALIDATION
    if(jTextfn.getText().trim().isEmpty()){
        showError("Enter First Name", jTextfn);
        return false;
    }

    if(jTextln.getText().trim().isEmpty()){
        showError("Enter Last Name", jTextln);
        return false;
    }

    if(jTextemail.getText().trim().isEmpty()){
        showError("Enter Email", jTextemail);
        return false;
    }

    if(jTextphone.getText().trim().isEmpty()){
        showError("Enter Phone", jTextphone);
        return false;
    }

    // COMBOBOX VALIDATION
    ComboItem classItem =
            (ComboItem) jComboclass.getSelectedItem();

    if(classItem == null || classItem.getId().equals("0")){
        showError("Select Class", jComboclass);
        return false;
    }

    ComboItem deptItem =
            (ComboItem) jCombodept.getSelectedItem();

    if(deptItem == null || deptItem.getId().equals("0")){
        showError("Select Department", jCombodept);
        return false;
    } 
    if(jReg_date.getDate() == null){
        showError("Select Date", jReg_date);
        return false;
    }
    return true; // ✅ ALL VALID
}
    private void loadComboBox(JComboBox combo,
                          String table,
                          String idColumn,
                          String nameColumn) {

    combo.removeAllItems();
    // ✅ DEFAULT OPTION (PUT HERE)
    combo.addItem(new ComboItem("0", "-- Select --"));

    String sql =
        "SELECT " + idColumn + ", " + nameColumn +
        " FROM " + table+"";
    try (PreparedStatement pst = conn.prepareStatement(sql);
         ResultSet rs = pst.executeQuery()) {
       
        while(rs.next()) {

            combo.addItem(
                new ComboItem(
                    rs.getString(idColumn),
                    rs.getString(nameColumn)
                )
            );
        }

    } catch(Exception e){
        JOptionPane.showMessageDialog(null,
                e + " Failed to load " + table);
    }
}
    public void saveStudentForm() {

    ComboItem classItem =
        (ComboItem) jComboclass.getSelectedItem();
    String classID = classItem.getId();

    ComboItem deptItem =
        (ComboItem) jCombodept.getSelectedItem();
    String deptID = deptItem.getId(); 
    // ⭐ SMART VALIDATION
    if(!validateForm()){
        return;
    }

    // ✅ VALIDATION HERE
    if(classItem.getId().equals("0")){
    JOptionPane.showMessageDialog(null,"Select Class");
    jComboclass.requestFocus();
    return;
    }
    if(deptItem.getId().equals("0")){
    JOptionPane.showMessageDialog(null,"Select Department");
    jComboclass.requestFocus();
    return;
    }
    // continue saving...
    String sql =
        "INSERT INTO STUDENT "
      + "(FIRST_NAME,LAST_NAME,EMAIL,PHONE,"
      + "CLASS_NAME_ID,DEPARTMENT_ID,REGISTRATION_DATE,STATUS) "
      + "VALUES(?,?,?,?,?,?,?,?)";
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    try (PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setString(1, jTextfn.getText().trim());
        pst.setString(2, jTextln.getText().trim());
        pst.setString(3, jTextemail.getText().trim());
        pst.setString(4, jTextphone.getText().trim());
        pst.setString(5, classID);
        pst.setString(6, deptID);
        pst.setString(7, df.format(jReg_date.getDate()));
        pst.setString(8, "AT_SCHOOL");

        pst.executeUpdate();

        setToEmpty();

        JOptionPane.showMessageDialog(null,
                "STUDENT SAVED WELL.");

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null,
                e + " STUDENT NOT SAVED");
    }
}
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextfn = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextln = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextemail = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jButsave = new javax.swing.JButton();
        jButcancel = new javax.swing.JButton();
        jTextphone = new javax.swing.JTextField();
        jComboclass = new javax.swing.JComboBox();
        jCombodept = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        jButtonBACKHM = new javax.swing.JButton();
        jReg_date = new com.toedter.calendar.JDateChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLocation(new java.awt.Point(450, 100));

        jLabel1.setText("FIRSTNAME");

        jLabel2.setText("LASTNAME");

        jLabel3.setText("EMAL");

        jLabel5.setText("PHONE");

        jLabel6.setText("CLASS");

        jLabel7.setText("REG DATE");

        jButsave.setText("Save");
        jButsave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButsaveActionPerformed(evt);
            }
        });

        jButcancel.setText("CENCEL");
        jButcancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButcancelActionPerformed(evt);
            }
        });

        jComboclass.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SELECT", "SCIENCE", "DICTIONARY", "THESIS", "TEXTBOOK" }));
        jComboclass.setName(""); // NOI18N
        jComboclass.setOpaque(false);

        jCombodept.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SELECT", "SCIENCE", "DICTIONARY", "THESIS", "TEXTBOOK" }));
        jCombodept.setName(""); // NOI18N
        jCombodept.setOpaque(false);

        jLabel8.setText("DEPARTMANT");

        jButtonBACKHM.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jButtonBACKHM.setForeground(new java.awt.Color(0, 153, 153));
        jButtonBACKHM.setText("BACK");
        jButtonBACKHM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBACKHMActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jButsave, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(jButtonBACKHM)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                .addComponent(jButcancel)
                .addGap(26, 26, 26))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel8)
                    .addComponent(jLabel7)
                    .addComponent(jLabel1))
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jTextfn)
                    .addComponent(jTextln)
                    .addComponent(jTextemail)
                    .addComponent(jTextphone)
                    .addComponent(jComboclass, 0, 205, Short.MAX_VALUE)
                    .addComponent(jCombodept, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jReg_date, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextfn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextln, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextemail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextphone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboclass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCombodept, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jReg_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButsave)
                    .addComponent(jButcancel)
                    .addComponent(jButtonBACKHM))
                .addContainerGap(43, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButsaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButsaveActionPerformed
        saveStudentForm();
    }//GEN-LAST:event_jButsaveActionPerformed

    private void jButcancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButcancelActionPerformed
      close();
    }//GEN-LAST:event_jButcancelActionPerformed

    private void jButtonBACKHMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBACKHMActionPerformed
        close();
        HOME hm= new HOME();
        hm.setTitle(config.getUserFN()+"     "+config.textline[5]);
        hm.setVisible(true);
    }//GEN-LAST:event_jButtonBACKHMActionPerformed

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
            java.util.logging.Logger.getLogger(STUDENT_REG.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(STUDENT_REG.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(STUDENT_REG.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(STUDENT_REG.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
  
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
              STUDENT_REG SR=  new STUDENT_REG();
                SR.setTitle("     REGISTER NEW STUDENT");
                SR.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButcancel;
    private javax.swing.JButton jButsave;
    private javax.swing.JButton jButtonBACKHM;
    private javax.swing.JComboBox jComboclass;
    private javax.swing.JComboBox jCombodept;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private com.toedter.calendar.JDateChooser jReg_date;
    private javax.swing.JTextField jTextemail;
    private javax.swing.JTextField jTextfn;
    private javax.swing.JTextField jTextln;
    private javax.swing.JTextField jTextphone;
    // End of variables declaration//GEN-END:variables


private void setupAutoClear(){

    enableAutoClear(jTextfn);
    enableAutoClear(jTextln);
    enableAutoClear(jTextemail);
    enableAutoClear(jTextphone);
    enableAutoClear(jComboclass);
    enableAutoClear(jCombodept);
    enableAutoClear(jReg_date); 
}
private void enableAutoClear(Component comp){

    // JTextField / JTextArea
    if(comp instanceof JTextComponent){

        ((JTextComponent) comp).getDocument()
        .addDocumentListener(new javax.swing.event.DocumentListener(){

            private void clear(){
                comp.setBackground(Color.WHITE);
            }

            public void insertUpdate(javax.swing.event.DocumentEvent e){ clear(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e){ clear(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e){ clear(); }
        });
    }

    // JComboBox
    if(comp instanceof JComboBox){

        ((JComboBox<?>) comp).addActionListener(e ->
            comp.setBackground(Color.WHITE)
        );
    }
    // ✅ JDateChooser / JCalendar
    else if(comp instanceof com.toedter.calendar.JDateChooser){

    com.toedter.calendar.JDateChooser dc =
        (com.toedter.calendar.JDateChooser) comp;

    dc.addPropertyChangeListener("date", evt ->
        dc.getDateEditor()
          .getUiComponent()
          .setBackground(Color.WHITE)
    );
}
}   
private void setToEmpty() {

    // Reset ComboBoxes
    if (jCombodept.getItemCount() > 0)
        jCombodept.setSelectedIndex(0);

    if (jComboclass.getItemCount() > 0)
        jComboclass.setSelectedIndex(0);

    // Clear TextFields
    jTextfn.setText("");
    jTextln.setText("");
    jTextemail.setText("");
    jTextphone.setText("");

    // Clear DateChooser
    jReg_date.setDate(null);

    // Optional: return focus to first field
    jTextfn.requestFocus();
}
}
