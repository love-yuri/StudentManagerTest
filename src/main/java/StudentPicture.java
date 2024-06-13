import javax.swing.*;
import java.io.*;
import java.awt.*;
public class StudentPicture extends JPanel{
   File imageFile;
   Toolkit tool;
   public StudentPicture(){
      tool=getToolkit();
   }
   public void setImage(File imageFile){
       this.imageFile=imageFile;
       repaint(); 
   }
   public void paintComponent(Graphics g){
      super.paintComponent(g);  
      int w=getBounds().width;
      int h=getBounds().height;
      if(imageFile!=null){
        Image image=tool.getImage(imageFile.getAbsolutePath()); 
        g.drawImage(image,0,0,w,h,this);
      }
      else
        g.drawString("没有选择照片图像",20,30); 
   } 
}
