
package LIBRARY.HOME;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


public class config {
    public static String userName,userfn,permition;
    public static Scanner conf;
    public static String[] textline=new String[20];
    
   public static void openFile(){
        try{
          conf=new Scanner(new File("config.txt"));  
        }catch(Exception e){JOptionPane.showMessageDialog(null,"No config file");}        
    }
    public static void readFile(){
        int i = 0;
        while (conf != null && conf.hasNextLine() && i < textline.length) {
            textline[i++] = conf.nextLine().trim();
        }
    }
    public static void closeFile(){
        conf.close();
    }

    public static String getUserName() {
        return userName;
    }

    public static String getUserFN() {
        return userfn;
    }

    public static void setUserName(String userName) {
        config.userName = userName;
    }

    public static void setUserFN(String userfn) {
        config.userfn = userfn;
    }

    public static void setPermition(String permition) {
        config.permition = permition;
    }

    public static String getPermition() {
        return permition;
    }
    public static void imageLabel(String path,String imagename,JLabel jlbl){
        BufferedImage image = null;
        File f = null;
        try{
        f = new File(path+"\\"+imagename); //image file path
        image = ImageIO.read(f);
        ImageIcon imageIcon=new ImageIcon(new ImageIcon(image).getImage().getScaledInstance(jlbl.getWidth(), jlbl.getHeight(), Image.SCALE_SMOOTH));                
        jlbl.setIcon(imageIcon); 
        }catch(IOException e){
        JOptionPane.showMessageDialog(null,e+"imageLabel");
            }
    }
    public static void imageJButton(String path,String imagename,int width,int heignht,JButton jbtn){
        BufferedImage image = null;
        File f = null;
        try{
        f = new File(path+"\\"+imagename); //image file path
        image = ImageIO.read(f);
        ImageIcon imageIcon=new ImageIcon(new ImageIcon(image).getImage().getScaledInstance(width, jbtn.getHeight(), Image.SCALE_SMOOTH));                
        jbtn.setIcon(imageIcon); 
        }catch(IOException e){
        JOptionPane.showMessageDialog(null,e+"imageJButton");
            }
    }
}
