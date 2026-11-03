/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LIBRARY.HOME;

import Authentication.Login;
import REPORTS.BOOK_LIST;
import REPORTS.COMPUTER_LIST;
import ISSUINGITEMS.ISUINGBOOK;
import ISSUINGITEMS.ISUINGCOMPUTER;
import REGISTER.BOOK_REG;
import REGISTER.CLASS_AND_DEPT;
import REGISTER.COMPUTER_REG;
import REGISTER.STUDENT_REG;
import REPORTS.AVAILABLE_BOOK;
import REPORTS.AVAILABLE_COMPUTER;
import REPORTS.ISSUED_BOOK;
import REPORTS.ISSUED_COMPUTER;
import REPORTS.OVERDUE_BOOK;
import REPORTS.OVERDUE_COMPUTER;
import REPORTS.STUDENT_LIST;
import RETURNINGITEMS.RETURNINGBOOK;
import RETURNINGITEMS.RETURNINGCOMPUTER;
import VIEW.CurrentStudents;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

/**
 *
 * @author Kalimba
 */
public class HOME extends javax.swing.JFrame {

    private ArrayList<String> filelist;
    private static  String OUTPUT_ZIP_FILE;
    private static String SOURCE_FOLDER;
    public HOME() {
        initComponents();
        config.openFile();
        config.readFile();
        config.closeFile(); 
        OUTPUT_ZIP_FILE=config.textline[2];
        SOURCE_FOLDER=config.textline[1];
        
        config.imageLabel(config.textline[0],config.textline[6],jlblbackgrnd);
        config.imageJButton(config.textline[0],config.textline[7], 40, 20, jButtonregBook);
        config.imageJButton(config.textline[0],config.textline[8], 40, 20, jButtonissueBook); 
        config.imageJButton(config.textline[0],config.textline[9], 40, 20, jButtonIssueComp);
        config.imageJButton(config.textline[0],config.textline[10], 40, 20, jButtonreturnBook); 
        config.imageJButton(config.textline[0],config.textline[11], 40, 20, jButtonreturnComp);
        config.imageJButton(config.textline[0],config.textline[12], 40, 20, jButtonsettings); 
        config.imageJButton(config.textline[0],config.textline[13], 40, 20, jButtonsaveBackup);
        config.imageJButton(config.textline[0],config.textline[14], 40, 20, jButtonregStudent);        
        
        settingMenu();
    }
    public void test(){
        Login l= new Login();
        int librrian_id=l.lib_id;
    }
public void close(){
WindowEvent winClosingEvent=new WindowEvent(this,WindowEvent.WINDOW_CLOSING);
Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(winClosingEvent);
}

    public void generateFileList(File node){
        if(node.isFile()){
            filelist.add(generateZipEntry(node.getAbsoluteFile().toString()));
        }
        if(node.isDirectory()){
            String[] subNote=node.list();
            for(String filename: subNote){
                generateFileList(new File(node,filename));
            }
        }        
    }
    private String generateZipEntry(String file){
        return file.substring(SOURCE_FOLDER.length()+1, file.length());
    }
    public void ZipIt(String ZipFile) throws IOException{
       byte[] buffer=new byte[1024];
       try(
               FileOutputStream fos=new FileOutputStream(ZipFile);
               ZipOutputStream zos=new ZipOutputStream(fos)){
           System.out.println("Output to Zip :"+ ZipFile);
           for(String file:this.filelist){
             System.out.println("File added :"+ file);  
               ZipEntry ze=new ZipEntry(file);
               zos.putNextEntry(ze);
               
               FileInputStream in=new FileInputStream(SOURCE_FOLDER+File.separator+file);
               int len;
               while((len=in.read(buffer))>0){
                   zos.write(buffer, 0, len);
               }
              zos.closeEntry();
           }
       }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpnlAcount = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButtonregBook = new javax.swing.JButton();
        jButtonsettings = new javax.swing.JButton();
        jButtonsaveBackup = new javax.swing.JButton();
        jButtonregStudent = new javax.swing.JButton();
        jButtonissueBook = new javax.swing.JButton();
        jButtonIssueComp = new javax.swing.JButton();
        jButtonreturnBook = new javax.swing.JButton();
        jButtonreturnComp = new javax.swing.JButton();
        jlblbackgrnd = new javax.swing.JLabel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenuFILE = new javax.swing.JMenu();
        jMenuItemSAVE_BACKUP = new javax.swing.JMenuItem();
        jMenuItemSETTINGS = new javax.swing.JMenuItem();
        jMenuItemMINIMIZE = new javax.swing.JMenuItem();
        jMenuItemMAXMIZE = new javax.swing.JMenuItem();
        jMenuItemLOGOUT = new javax.swing.JMenuItem();
        jMenuItemEXIT = new javax.swing.JMenuItem();
        jMenuCIRCURATIONS = new javax.swing.JMenu();
        jMenuItemISSUE_BOOKS = new javax.swing.JMenuItem();
        jMenuItemRETURN_BOOKS = new javax.swing.JMenuItem();
        jMenuItemISSUE_COMPUTERS = new javax.swing.JMenuItem();
        jMenuItemRETURN_COMPUTERS = new javax.swing.JMenuItem();
        jMenuBOOKS = new javax.swing.JMenu();
        jMenuItemREGISTER_BOOK = new javax.swing.JMenuItem();
        jMenuItemBOOK_LIST = new javax.swing.JMenuItem();
        jMenuItemCATEGORIES = new javax.swing.JMenuItem();
        jMenuItemAUTHORS = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItemcomp_list = new javax.swing.JMenuItem();
        jMenuSTUDENTS = new javax.swing.JMenu();
        jMenuItemREGISTER_STUDENT = new javax.swing.JMenuItem();
        jMenuItemSTUDENT_LIST = new javax.swing.JMenuItem();
        jMenuPENAELTIES = new javax.swing.JMenu();
        jMenuItemPENAELTIES_ON_BOOKS = new javax.swing.JMenuItem();
        jMenuItemPENALTIES_ON_COMPUTER = new javax.swing.JMenuItem();
        jMenuREPORTS = new javax.swing.JMenu();
        jMenuBOOK = new javax.swing.JMenu();
        jMenuItemAVAILABLE_BOOKS = new javax.swing.JMenuItem();
        jMenuItemISUED_BOOKS = new javax.swing.JMenuItem();
        jMenuItemOVERDUE_BOOKS = new javax.swing.JMenuItem();
        jMenuItemALL_BOOKS = new javax.swing.JMenuItem();
        jMenuCOMPUTERS = new javax.swing.JMenu();
        jMenuItemREPORT_COMP = new javax.swing.JMenuItem();
        jMenuItemISSUED_COMP = new javax.swing.JMenuItem();
        jMenuItemOVER_COMP = new javax.swing.JMenuItem();
        jMenuItemALLCOMP = new javax.swing.JMenuItem();
        jMenuSETTINGS = new javax.swing.JMenu();
        jMenuItemSAVEBAKUP_SETTING = new javax.swing.JMenuItem();
        jMenuBOOK_SETTING = new javax.swing.JMenu();
        jMenuItemUPDATE_BOOK = new javax.swing.JMenuItem();
        jMenuItemDELETE_BOOK = new javax.swing.JMenuItem();
        jMenuCOMPUTER_SETTING = new javax.swing.JMenu();
        jMenuItemUPDATE_COMPUTER = new javax.swing.JMenuItem();
        jMenuItemDELETE_COMPUTER = new javax.swing.JMenuItem();
        jMenuSTUDENT_SETTINGS = new javax.swing.JMenu();
        jMenuItemUPDATE_STUDENT = new javax.swing.JMenuItem();
        jMenuItemREMOVE_STUDENT = new javax.swing.JMenuItem();
        jMenuHELP = new javax.swing.JMenu();
        jMenuItemABOUT_SOFTWARE = new javax.swing.JMenuItem();
        jMenuItemUSER_MANUAL = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jpnlAcount.setBackground(new java.awt.Color(204, 204, 204));

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        jButtonregBook.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonregBook.setText("REGISTER BOOK");
        jButtonregBook.setToolTipText("");
        jButtonregBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonregBookActionPerformed(evt);
            }
        });

        jButtonsettings.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonsettings.setText("SETTINGS");
        jButtonsettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonsettingsActionPerformed(evt);
            }
        });

        jButtonsaveBackup.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonsaveBackup.setText("SAVE BACKUP         ");
        jButtonsaveBackup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonsaveBackupActionPerformed(evt);
            }
        });

        jButtonregStudent.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonregStudent.setText("REGISTER STUDENT");
        jButtonregStudent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonregStudentActionPerformed(evt);
            }
        });

        jButtonissueBook.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonissueBook.setText("ISSUE BOOKS");
        jButtonissueBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonissueBookActionPerformed(evt);
            }
        });

        jButtonIssueComp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonIssueComp.setText("ISSUE COMPUTER");
        jButtonIssueComp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonIssueCompActionPerformed(evt);
            }
        });

        jButtonreturnBook.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonreturnBook.setText("RETURN BOOK");
        jButtonreturnBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonreturnBookActionPerformed(evt);
            }
        });

        jButtonreturnComp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonreturnComp.setText("RETURN COMPUTER");
        jButtonreturnComp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonreturnCompActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButtonregBook, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonissueBook, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonIssueComp, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonsaveBackup, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonsettings, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonreturnComp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonreturnBook, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonregStudent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonissueBook, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonreturnBook, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonIssueComp, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(jButtonreturnComp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonregBook, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonregStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonsettings, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonsaveBackup, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jToggleButton1.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jToggleButton1.setText("About software");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpnlAcountLayout = new javax.swing.GroupLayout(jpnlAcount);
        jpnlAcount.setLayout(jpnlAcountLayout);
        jpnlAcountLayout.setHorizontalGroup(
            jpnlAcountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlAcountLayout.createSequentialGroup()
                .addGroup(jpnlAcountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpnlAcountLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jToggleButton1))
                    .addGroup(jpnlAcountLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
                .addComponent(jlblbackgrnd, javax.swing.GroupLayout.PREFERRED_SIZE, 726, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jpnlAcountLayout.setVerticalGroup(
            jpnlAcountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jlblbackgrnd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jpnlAcountLayout.createSequentialGroup()
                .addGap(0, 67, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(164, 164, 164)
                .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jMenuFILE.setText("FILE");
        jMenuFILE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuFILEActionPerformed(evt);
            }
        });

        jMenuItemSAVE_BACKUP.setText("SAVE BACKUP");
        jMenuFILE.add(jMenuItemSAVE_BACKUP);

        jMenuItemSETTINGS.setText("SETTINGS");
        jMenuItemSETTINGS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSETTINGSActionPerformed(evt);
            }
        });
        jMenuFILE.add(jMenuItemSETTINGS);

        jMenuItemMINIMIZE.setText("MINIMIZE");
        jMenuItemMINIMIZE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemMINIMIZEActionPerformed(evt);
            }
        });
        jMenuFILE.add(jMenuItemMINIMIZE);

        jMenuItemMAXMIZE.setText("MAXMIZE");
        jMenuItemMAXMIZE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemMAXMIZEActionPerformed(evt);
            }
        });
        jMenuFILE.add(jMenuItemMAXMIZE);

        jMenuItemLOGOUT.setText("LOGOUT");
        jMenuItemLOGOUT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemLOGOUTActionPerformed(evt);
            }
        });
        jMenuFILE.add(jMenuItemLOGOUT);

        jMenuItemEXIT.setText("EXIT");
        jMenuItemEXIT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemEXITActionPerformed(evt);
            }
        });
        jMenuFILE.add(jMenuItemEXIT);

        jMenuBar1.add(jMenuFILE);

        jMenuCIRCURATIONS.setText("CIRCULATIONS");
        jMenuCIRCURATIONS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuCIRCURATIONSActionPerformed(evt);
            }
        });

        jMenuItemISSUE_BOOKS.setText("ISSUE BOOKS");
        jMenuItemISSUE_BOOKS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemISSUE_BOOKSActionPerformed(evt);
            }
        });
        jMenuCIRCURATIONS.add(jMenuItemISSUE_BOOKS);

        jMenuItemRETURN_BOOKS.setText("RETURN BOOKS");
        jMenuItemRETURN_BOOKS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemRETURN_BOOKSActionPerformed(evt);
            }
        });
        jMenuCIRCURATIONS.add(jMenuItemRETURN_BOOKS);

        jMenuItemISSUE_COMPUTERS.setText("ISSUE COMPUTERS");
        jMenuItemISSUE_COMPUTERS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemISSUE_COMPUTERSActionPerformed(evt);
            }
        });
        jMenuCIRCURATIONS.add(jMenuItemISSUE_COMPUTERS);

        jMenuItemRETURN_COMPUTERS.setText("RETURN COMPUTERS");
        jMenuItemRETURN_COMPUTERS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemRETURN_COMPUTERSActionPerformed(evt);
            }
        });
        jMenuCIRCURATIONS.add(jMenuItemRETURN_COMPUTERS);

        jMenuBar1.add(jMenuCIRCURATIONS);

        jMenuBOOKS.setText("BOOKS");

        jMenuItemREGISTER_BOOK.setText("REGISTER BOOK");
        jMenuItemREGISTER_BOOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemREGISTER_BOOKActionPerformed(evt);
            }
        });
        jMenuBOOKS.add(jMenuItemREGISTER_BOOK);

        jMenuItemBOOK_LIST.setText("BOOK LIST");
        jMenuItemBOOK_LIST.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemBOOK_LISTActionPerformed(evt);
            }
        });
        jMenuBOOKS.add(jMenuItemBOOK_LIST);

        jMenuItemCATEGORIES.setText("CATEGORIES");
        jMenuItemCATEGORIES.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCATEGORIESActionPerformed(evt);
            }
        });
        jMenuBOOKS.add(jMenuItemCATEGORIES);

        jMenuItemAUTHORS.setText("AUTHORS");
        jMenuItemAUTHORS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAUTHORSActionPerformed(evt);
            }
        });
        jMenuBOOKS.add(jMenuItemAUTHORS);

        jMenuBar1.add(jMenuBOOKS);

        jMenu6.setText("COMPUTERS");

        jMenuItem7.setText("ADD COMPUTER");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem7);

        jMenuItemcomp_list.setText("COMPUTER LIST");
        jMenuItemcomp_list.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemcomp_listActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItemcomp_list);

        jMenuBar1.add(jMenu6);

        jMenuSTUDENTS.setText("STUDENTS ");

        jMenuItemREGISTER_STUDENT.setText("REGISTER STUDENT");
        jMenuItemREGISTER_STUDENT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemREGISTER_STUDENTActionPerformed(evt);
            }
        });
        jMenuSTUDENTS.add(jMenuItemREGISTER_STUDENT);

        jMenuItemSTUDENT_LIST.setText("STUDENT LIST");
        jMenuItemSTUDENT_LIST.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSTUDENT_LISTActionPerformed(evt);
            }
        });
        jMenuSTUDENTS.add(jMenuItemSTUDENT_LIST);

        jMenuBar1.add(jMenuSTUDENTS);

        jMenuPENAELTIES.setText("PENAELTIES");

        jMenuItemPENAELTIES_ON_BOOKS.setText("PENALTIES ON BOOKS");
        jMenuPENAELTIES.add(jMenuItemPENAELTIES_ON_BOOKS);

        jMenuItemPENALTIES_ON_COMPUTER.setText("PENALTIES ON COMPUTER");
        jMenuItemPENALTIES_ON_COMPUTER.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPENALTIES_ON_COMPUTERActionPerformed(evt);
            }
        });
        jMenuPENAELTIES.add(jMenuItemPENALTIES_ON_COMPUTER);

        jMenuBar1.add(jMenuPENAELTIES);

        jMenuREPORTS.setText("REPORTS");

        jMenuBOOK.setText("BOOKS");

        jMenuItemAVAILABLE_BOOKS.setText("AVAILABLE BOOKS");
        jMenuItemAVAILABLE_BOOKS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAVAILABLE_BOOKSActionPerformed(evt);
            }
        });
        jMenuBOOK.add(jMenuItemAVAILABLE_BOOKS);

        jMenuItemISUED_BOOKS.setText("ISUED BOOKS");
        jMenuItemISUED_BOOKS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemISUED_BOOKSActionPerformed(evt);
            }
        });
        jMenuBOOK.add(jMenuItemISUED_BOOKS);

        jMenuItemOVERDUE_BOOKS.setText("OVERDUE BOOKS");
        jMenuItemOVERDUE_BOOKS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOVERDUE_BOOKSActionPerformed(evt);
            }
        });
        jMenuBOOK.add(jMenuItemOVERDUE_BOOKS);

        jMenuItemALL_BOOKS.setText("ALL BOOKS");
        jMenuItemALL_BOOKS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemALL_BOOKSActionPerformed(evt);
            }
        });
        jMenuBOOK.add(jMenuItemALL_BOOKS);

        jMenuREPORTS.add(jMenuBOOK);

        jMenuCOMPUTERS.setText("COMPUTERS");

        jMenuItemREPORT_COMP.setText("AVAILABLE COMPUTERS");
        jMenuItemREPORT_COMP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemREPORT_COMPActionPerformed(evt);
            }
        });
        jMenuCOMPUTERS.add(jMenuItemREPORT_COMP);

        jMenuItemISSUED_COMP.setText("ISUED COMPUTERS");
        jMenuItemISSUED_COMP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemISSUED_COMPActionPerformed(evt);
            }
        });
        jMenuCOMPUTERS.add(jMenuItemISSUED_COMP);

        jMenuItemOVER_COMP.setText("OVERDUE COMPUTERS");
        jMenuItemOVER_COMP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOVER_COMPActionPerformed(evt);
            }
        });
        jMenuCOMPUTERS.add(jMenuItemOVER_COMP);

        jMenuItemALLCOMP.setText("ALL COMPUTERS");
        jMenuItemALLCOMP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemALLCOMPActionPerformed(evt);
            }
        });
        jMenuCOMPUTERS.add(jMenuItemALLCOMP);

        jMenuREPORTS.add(jMenuCOMPUTERS);

        jMenuBar1.add(jMenuREPORTS);

        jMenuSETTINGS.setText("SETTINGS");

        jMenuItemSAVEBAKUP_SETTING.setText("SAVE BACKUP");
        jMenuSETTINGS.add(jMenuItemSAVEBAKUP_SETTING);

        jMenuBOOK_SETTING.setText("BOOKS");

        jMenuItemUPDATE_BOOK.setText("UPDATE BOOK");
        jMenuBOOK_SETTING.add(jMenuItemUPDATE_BOOK);

        jMenuItemDELETE_BOOK.setText("DELETE BOOK");
        jMenuBOOK_SETTING.add(jMenuItemDELETE_BOOK);

        jMenuSETTINGS.add(jMenuBOOK_SETTING);

        jMenuCOMPUTER_SETTING.setText("COMPUTERS");

        jMenuItemUPDATE_COMPUTER.setText("UPDATE COMPUTER");
        jMenuCOMPUTER_SETTING.add(jMenuItemUPDATE_COMPUTER);

        jMenuItemDELETE_COMPUTER.setText("DELETE COMPUTER");
        jMenuCOMPUTER_SETTING.add(jMenuItemDELETE_COMPUTER);

        jMenuSETTINGS.add(jMenuCOMPUTER_SETTING);

        jMenuSTUDENT_SETTINGS.setText("STUDENTS");

        jMenuItemUPDATE_STUDENT.setText("UPDATE STUDENT");
        jMenuItemUPDATE_STUDENT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemUPDATE_STUDENTActionPerformed(evt);
            }
        });
        jMenuSTUDENT_SETTINGS.add(jMenuItemUPDATE_STUDENT);

        jMenuItemREMOVE_STUDENT.setText("REMOVE STUDENT");
        jMenuSTUDENT_SETTINGS.add(jMenuItemREMOVE_STUDENT);

        jMenuSETTINGS.add(jMenuSTUDENT_SETTINGS);

        jMenuBar1.add(jMenuSETTINGS);

        jMenuHELP.setText("HELP");

        jMenuItemABOUT_SOFTWARE.setText("ABOUT SOFTWARE");
        jMenuItemABOUT_SOFTWARE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemABOUT_SOFTWAREActionPerformed(evt);
            }
        });
        jMenuHELP.add(jMenuItemABOUT_SOFTWARE);

        jMenuItemUSER_MANUAL.setText("USER  MANUAL");
        jMenuHELP.add(jMenuItemUSER_MANUAL);

        jMenuBar1.add(jMenuHELP);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jpnlAcount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpnlAcount, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonregBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonregBookActionPerformed
        close();
        BOOK_REG bk =new BOOK_REG();     
        //a.setTitle(config.getUserTitle()+" "+config.getUserName()+" - "+config.textline[5]);
        bk.setVisible(true);
    }//GEN-LAST:event_jButtonregBookActionPerformed

    private void jButtonissueBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonissueBookActionPerformed
        close();
        ISUINGBOOK i= new ISUINGBOOK();
        i.setTitle(config.getUserFN()+"     "+config.textline[5]);
        i.setVisible(true);
    }//GEN-LAST:event_jButtonissueBookActionPerformed
   int i=0;
    private void jButtonsettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonsettingsActionPerformed
       if(config.getPermition().equals("1")){
        close();
        /*Leaders l=new Leaders();
        l.setTitle(config.getUserFN()+"     "+config.textline[5]);
        l.setLocationRelativeTo(null);
        l.setVisible(true);*/
       }else { if(i++<2)
           JOptionPane.showMessageDialog(null,"YOU DON'T HAVE PERMITION HERE.");
       else if(i++<6) JOptionPane.showMessageDialog(null,"ASK FOR A HELP PLEASE.");
       else if(i++<9) JOptionPane.showMessageDialog(null,"ASK FOR A HELP PLEASE, If you click again button is going to be Disabled");
       else{ jButtonsettings.setEnabled(false);
           JOptionPane.showMessageDialog(null,"Contact administrator");}       
       }
    }//GEN-LAST:event_jButtonsettingsActionPerformed

    private void jButtonsaveBackupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonsaveBackupActionPerformed
        try {
        filelist=new ArrayList<String>();
        generateFileList(new File(SOURCE_FOLDER));        
        ZipIt(OUTPUT_ZIP_FILE);
        } catch (IOException ex) {
            Logger.getLogger(HOME.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonsaveBackupActionPerformed

    private void jButtonIssueCompActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonIssueCompActionPerformed
        close();
        ISUINGCOMPUTER I=new ISUINGCOMPUTER();
        I.setTitle(config.getUserFN()+"     "+config.textline[5]);
        I.setVisible(true);
    }//GEN-LAST:event_jButtonIssueCompActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        ABOUTSOFTWARE l=new ABOUTSOFTWARE();
        l.setTitle("ABOUT SOFTWARE");
        l.setLocationRelativeTo(null);
        l.setAlwaysOnTop(true);
        l.setVisible(true); 
        
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jButtonregStudentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonregStudentActionPerformed
        close();
        //STUDENT_REG st= new STUDENT_REG();
        new STUDENT_REG().setVisible(true);
        
                
    }//GEN-LAST:event_jButtonregStudentActionPerformed

    private void jButtonreturnBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonreturnBookActionPerformed
         close();
        RETURNINGBOOK RB= new RETURNINGBOOK();
        RB.setVisible(true);
        
    }//GEN-LAST:event_jButtonreturnBookActionPerformed

    private void jButtonreturnCompActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonreturnCompActionPerformed
       close();
        RETURNINGCOMPUTER RC= new RETURNINGCOMPUTER();
       RC.setVisible(true);
    }//GEN-LAST:event_jButtonreturnCompActionPerformed

    private void jMenuItemMINIMIZEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemMINIMIZEActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItemMINIMIZEActionPerformed

    private void jMenuItemSETTINGSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSETTINGSActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItemSETTINGSActionPerformed

    private void jMenuItemcomp_listActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemcomp_listActionPerformed
        COMPUTER_LIST cl= new COMPUTER_LIST();
        cl.setVisible(true);
    }//GEN-LAST:event_jMenuItemcomp_listActionPerformed

    private void jMenuItemSTUDENT_LISTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSTUDENT_LISTActionPerformed
       STUDENT_LIST SL= new STUDENT_LIST();
       SL.setVisible(true);
    }//GEN-LAST:event_jMenuItemSTUDENT_LISTActionPerformed

    private void jMenuItemREGISTER_STUDENTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemREGISTER_STUDENTActionPerformed
       close();
       STUDENT_REG ST= new STUDENT_REG();
       ST.setTitle("  STUDENT REGISTRATION");
       ST.setVisible(true);
    }//GEN-LAST:event_jMenuItemREGISTER_STUDENTActionPerformed

    private void jMenuItemPENALTIES_ON_COMPUTERActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPENALTIES_ON_COMPUTERActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItemPENALTIES_ON_COMPUTERActionPerformed

    private void jMenuItemALL_BOOKSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemALL_BOOKSActionPerformed
       close();
        BOOK_LIST bl= new BOOK_LIST();
       bl.setVisible(true);
    }//GEN-LAST:event_jMenuItemALL_BOOKSActionPerformed

    private void jMenuItemALLCOMPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemALLCOMPActionPerformed
       close();
       COMPUTER_LIST CL= new COMPUTER_LIST();
       CL.setVisible(true);
    }//GEN-LAST:event_jMenuItemALLCOMPActionPerformed

    private void jMenuItemMAXMIZEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemMAXMIZEActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItemMAXMIZEActionPerformed

    private void jMenuItemLOGOUTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemLOGOUTActionPerformed
        new Login().setVisible(true);
    }//GEN-LAST:event_jMenuItemLOGOUTActionPerformed

    private void jMenuItemEXITActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemEXITActionPerformed
        close();
    }//GEN-LAST:event_jMenuItemEXITActionPerformed

    private void jMenuItemRETURN_BOOKSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemRETURN_BOOKSActionPerformed
  close();
        RETURNINGBOOK RB= new RETURNINGBOOK();
        RB.setVisible(true);
    }//GEN-LAST:event_jMenuItemRETURN_BOOKSActionPerformed

    private void jMenuItemRETURN_COMPUTERSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemRETURN_COMPUTERSActionPerformed
        close();
        RETURNINGCOMPUTER RC= new RETURNINGCOMPUTER();
       RC.setVisible(true);
    }//GEN-LAST:event_jMenuItemRETURN_COMPUTERSActionPerformed

    private void jMenuCIRCURATIONSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuCIRCURATIONSActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuCIRCURATIONSActionPerformed

    private void jMenuItemISSUE_BOOKSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemISSUE_BOOKSActionPerformed
   close();
        ISUINGBOOK i= new ISUINGBOOK();
        i.setVisible(true);
    }//GEN-LAST:event_jMenuItemISSUE_BOOKSActionPerformed

    private void jMenuItemISSUE_COMPUTERSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemISSUE_COMPUTERSActionPerformed
        close();
        ISUINGCOMPUTER I=new ISUINGCOMPUTER();
        I.setTitle(config.getUserFN()+"     "+config.textline[5]);
        I.setVisible(true);
    }//GEN-LAST:event_jMenuItemISSUE_COMPUTERSActionPerformed

    private void jMenuFILEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuFILEActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuFILEActionPerformed

    private void jMenuItemREGISTER_BOOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemREGISTER_BOOKActionPerformed
        close();
        BOOK_REG rb= new BOOK_REG();
        rb.setVisible(true);
    }//GEN-LAST:event_jMenuItemREGISTER_BOOKActionPerformed

    private void jMenuItemCATEGORIESActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCATEGORIESActionPerformed
        close();
        CLASS_AND_DEPT UT= new CLASS_AND_DEPT();
        UT.setVisible(true);
    }//GEN-LAST:event_jMenuItemCATEGORIESActionPerformed

    private void jMenuItemAUTHORSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAUTHORSActionPerformed
        close();
        CLASS_AND_DEPT UT= new CLASS_AND_DEPT();
        UT.setVisible(true);
    }//GEN-LAST:event_jMenuItemAUTHORSActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
       close();
       COMPUTER_REG CR= new COMPUTER_REG();
       CR.setVisible(true);
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItemBOOK_LISTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemBOOK_LISTActionPerformed
       close();
        BOOK_LIST BL= new BOOK_LIST();
       BL.setVisible(true);
    }//GEN-LAST:event_jMenuItemBOOK_LISTActionPerformed

    private void jMenuItemAVAILABLE_BOOKSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAVAILABLE_BOOKSActionPerformed
        close();
        AVAILABLE_BOOK AV= new AVAILABLE_BOOK();
        AV.setVisible(true);
        
    }//GEN-LAST:event_jMenuItemAVAILABLE_BOOKSActionPerformed

    private void jMenuItemREPORT_COMPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemREPORT_COMPActionPerformed
       AVAILABLE_COMPUTER av= new AVAILABLE_COMPUTER();
       av.setVisible(true);
    }//GEN-LAST:event_jMenuItemREPORT_COMPActionPerformed

    private void jMenuItemISUED_BOOKSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemISUED_BOOKSActionPerformed
      close();
      ISSUED_BOOK IB=new ISSUED_BOOK();
      IB.setVisible(true);
    }//GEN-LAST:event_jMenuItemISUED_BOOKSActionPerformed

    private void jMenuItemOVERDUE_BOOKSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOVERDUE_BOOKSActionPerformed
       close();
       OVERDUE_BOOK ov= new OVERDUE_BOOK();
      ov.setVisible(true);
    }//GEN-LAST:event_jMenuItemOVERDUE_BOOKSActionPerformed

    private void jMenuItemISSUED_COMPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemISSUED_COMPActionPerformed
     close();
     ISSUED_COMPUTER ov= new ISSUED_COMPUTER();
     ov.setVisible(true);
    }//GEN-LAST:event_jMenuItemISSUED_COMPActionPerformed

    private void jMenuItemOVER_COMPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOVER_COMPActionPerformed
     close();
     OVERDUE_COMPUTER ov= new OVERDUE_COMPUTER();
     ov.setVisible(true);
    }//GEN-LAST:event_jMenuItemOVER_COMPActionPerformed

    private void jMenuItemABOUT_SOFTWAREActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemABOUT_SOFTWAREActionPerformed
        ABOUTSOFTWARE l=new ABOUTSOFTWARE();
        l.setTitle("ABOUT SOFTWARE");
        l.setLocationRelativeTo(null);
        l.setAlwaysOnTop(true);
        l.setVisible(true); 
    }//GEN-LAST:event_jMenuItemABOUT_SOFTWAREActionPerformed

    private void jMenuItemUPDATE_STUDENTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemUPDATE_STUDENTActionPerformed
       close();
     CurrentStudents cs= new CurrentStudents();
     cs.setTitle(config.getUserFN()+"     "+config.textline[5]);
     cs.setVisible(true);
    }//GEN-LAST:event_jMenuItemUPDATE_STUDENTActionPerformed

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
            java.util.logging.Logger.getLogger(HOME.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HOME.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HOME.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HOME.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               HOME a= new HOME();
               a.setVisible(true);               
            }
        });
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonIssueComp;
    private javax.swing.JButton jButtonissueBook;
    private javax.swing.JButton jButtonregBook;
    private javax.swing.JButton jButtonregStudent;
    private javax.swing.JButton jButtonreturnBook;
    private javax.swing.JButton jButtonreturnComp;
    private javax.swing.JButton jButtonsaveBackup;
    private javax.swing.JButton jButtonsettings;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenuBOOK;
    private javax.swing.JMenu jMenuBOOKS;
    private javax.swing.JMenu jMenuBOOK_SETTING;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu jMenuCIRCURATIONS;
    private javax.swing.JMenu jMenuCOMPUTERS;
    private javax.swing.JMenu jMenuCOMPUTER_SETTING;
    private javax.swing.JMenu jMenuFILE;
    private javax.swing.JMenu jMenuHELP;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItemABOUT_SOFTWARE;
    private javax.swing.JMenuItem jMenuItemALLCOMP;
    private javax.swing.JMenuItem jMenuItemALL_BOOKS;
    private javax.swing.JMenuItem jMenuItemAUTHORS;
    private javax.swing.JMenuItem jMenuItemAVAILABLE_BOOKS;
    private javax.swing.JMenuItem jMenuItemBOOK_LIST;
    private javax.swing.JMenuItem jMenuItemCATEGORIES;
    private javax.swing.JMenuItem jMenuItemDELETE_BOOK;
    private javax.swing.JMenuItem jMenuItemDELETE_COMPUTER;
    private javax.swing.JMenuItem jMenuItemEXIT;
    private javax.swing.JMenuItem jMenuItemISSUED_COMP;
    private javax.swing.JMenuItem jMenuItemISSUE_BOOKS;
    private javax.swing.JMenuItem jMenuItemISSUE_COMPUTERS;
    private javax.swing.JMenuItem jMenuItemISUED_BOOKS;
    private javax.swing.JMenuItem jMenuItemLOGOUT;
    private javax.swing.JMenuItem jMenuItemMAXMIZE;
    private javax.swing.JMenuItem jMenuItemMINIMIZE;
    private javax.swing.JMenuItem jMenuItemOVERDUE_BOOKS;
    private javax.swing.JMenuItem jMenuItemOVER_COMP;
    private javax.swing.JMenuItem jMenuItemPENAELTIES_ON_BOOKS;
    private javax.swing.JMenuItem jMenuItemPENALTIES_ON_COMPUTER;
    private javax.swing.JMenuItem jMenuItemREGISTER_BOOK;
    private javax.swing.JMenuItem jMenuItemREGISTER_STUDENT;
    private javax.swing.JMenuItem jMenuItemREMOVE_STUDENT;
    private javax.swing.JMenuItem jMenuItemREPORT_COMP;
    private javax.swing.JMenuItem jMenuItemRETURN_BOOKS;
    private javax.swing.JMenuItem jMenuItemRETURN_COMPUTERS;
    private javax.swing.JMenuItem jMenuItemSAVEBAKUP_SETTING;
    private javax.swing.JMenuItem jMenuItemSAVE_BACKUP;
    private javax.swing.JMenuItem jMenuItemSETTINGS;
    private javax.swing.JMenuItem jMenuItemSTUDENT_LIST;
    private javax.swing.JMenuItem jMenuItemUPDATE_BOOK;
    private javax.swing.JMenuItem jMenuItemUPDATE_COMPUTER;
    private javax.swing.JMenuItem jMenuItemUPDATE_STUDENT;
    private javax.swing.JMenuItem jMenuItemUSER_MANUAL;
    private javax.swing.JMenuItem jMenuItemcomp_list;
    private javax.swing.JMenu jMenuPENAELTIES;
    private javax.swing.JMenu jMenuREPORTS;
    private javax.swing.JMenu jMenuSETTINGS;
    private javax.swing.JMenu jMenuSTUDENTS;
    private javax.swing.JMenu jMenuSTUDENT_SETTINGS;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JLabel jlblbackgrnd;
    private javax.swing.JPanel jpnlAcount;
    // End of variables declaration//GEN-END:variables

    public void settingMenu(){
       // JMenu menu = new JMenu("File");

        jMenuSETTINGS.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
               if(!"Admin".equals(config.getPermition())){                
                jMenuSETTINGS.setEnabled(false);
                }
            }

            @Override
            public void menuDeselected(MenuEvent e) {
                // Optional: Action when the menu closes
                jMenuSETTINGS.setEnabled(true);
            }

            @Override
            public void menuCanceled(MenuEvent e) {
                // Optional: Action when the menu is canceled
            }
        });
    }
}
