import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
public class Inquest extends JDialog implements ActionListener{
   StudentPicture pic;
   PrintJob print; 
   Graphics g=null;
   HashMap<String,Student> 基本信息表; 
   JTextField 学号,姓名,专业,年级,出生;  
   JRadioButton 男,女;
   JButton 查询,打印文本及照片,打印文本,打印照片;
   ButtonGroup group=null;
   FileInputStream inOne=null;
   ObjectInputStream inTwo=null;
   File systemFile=null;
   JPanel messPanel;
   Student stu; 
   public Inquest(File file){
      setTitle("查询对话框"); 
      setModal(false); 
      systemFile=file;
      pic=new StudentPicture();
      学号=new JTextField(6);
      查询=new JButton("查询");
      学号.addActionListener(this);
      查询.addActionListener(this);
      打印文本及照片=new JButton("打印文本及照片");
      打印文本=new JButton("打印文本");
      打印照片=new JButton("打印照片");
      打印文本及照片.addActionListener(this);
      打印文本.addActionListener(this);
      打印照片.addActionListener(this);
      打印文本及照片.setEnabled(false);
      打印文本.setEnabled(false);
      打印照片.setEnabled(false);     
      姓名=new JTextField(5); 
      姓名.setEditable(false);
      专业=new JTextField(5);
      专业.setEditable(false);
      年级=new JTextField(5);
      年级.setEditable(false);
      出生=new JTextField(5);
      出生.setEditable(false);
      男=new JRadioButton("男",false);
      女=new JRadioButton("女",false);
      group=new ButtonGroup();
      group.add(男);
      group.add(女);
      Box box1=Box.createHorizontalBox(); 
      box1.add(new JLabel("学号:",JLabel.CENTER));
      box1.add(学号);
      box1.add(查询);
      Box box2=Box.createHorizontalBox();  
      box2.add(new JLabel("姓名:",JLabel.CENTER));
      box2.add(姓名);
      Box box3=Box.createHorizontalBox(); 
      box3.add(new JLabel("性别:",JLabel.CENTER));
      box3.add(男);
      box3.add(女);
      Box box4=Box.createHorizontalBox(); 
      box4.add(new JLabel("专业:",JLabel.CENTER));
      box4.add(专业); 
      Box box5=Box.createHorizontalBox(); 
      box5.add(new JLabel("年级:",JLabel.CENTER));
      box5.add(年级);
      Box box6=Box.createHorizontalBox();              
      box6.add(new JLabel("出生:",JLabel.CENTER));
      box6.add(出生);
      Box boxH=Box.createVerticalBox();  
      boxH.add(box1);
      boxH.add(box2);
      boxH.add(box3);
      boxH.add(box4);
      boxH.add(box5);
      boxH.add(box6);
      boxH.add(Box.createVerticalGlue()); 
      messPanel=new JPanel();
      messPanel.add(boxH);
      JSplitPane splitH=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,messPanel,pic);
      add(splitH,BorderLayout.CENTER); 
      JPanel pSouth=new JPanel();
      pSouth.add(打印文本及照片);
      pSouth.add(打印文本);
      pSouth.add(打印照片);
      add(pSouth,BorderLayout.SOUTH);  
      validate();
      setVisible(false);
      setSize(360,230);
      addWindowListener(new WindowAdapter(){
                    public void windowClosing(WindowEvent e){
                         setVisible(false);
                    }});
   }
   public void actionPerformed(ActionEvent e){
      if(e.getSource()==查询||e.getSource()==学号){
         String number="";
         number=学号.getText();
         if(number.length()>0){
              try {
                    inOne=new FileInputStream(systemFile);
                    inTwo=new ObjectInputStream(inOne);
                    基本信息表=(HashMap<String,Student>)inTwo.readObject();
                    inOne.close();
                    inTwo.close();
               }
               catch(Exception ee){}
              if(基本信息表.containsKey(number)){          
                   stu=基本信息表.get(number);
                   姓名.setText(stu.getName());
                   专业.setText(stu.getDisciping());
                   年级.setText(stu.getGrade());
                   出生.setText(stu.getBorth()); 
                   if(stu.getSex().equals("男"))
                        男.setSelected(true);
                   else
                        女.setSelected(true);
                   pic.setImage(stu.getImagePic());
                   pic.repaint();
                   打印文本及照片.setEnabled(true);
                   打印文本.setEnabled(true);
                   打印照片.setEnabled(true);
              }
              else{
                  打印文本及照片.setEnabled(false);
                  打印文本.setEnabled(false);
                  打印照片.setEnabled(false);
                  String warning="该学号不存在!";
                  JOptionPane.showMessageDialog(this,warning,"警告",
                                               JOptionPane.WARNING_MESSAGE);
                  clearMess();
              }
         }
         else{
              打印文本及照片.setEnabled(false);
              打印文本.setEnabled(false);
              打印照片.setEnabled(false);
              String warning="必须要输入学号!";
              JOptionPane.showMessageDialog(this,warning,"警告",
                                            JOptionPane.WARNING_MESSAGE);
             }
      }
      else if(e.getSource()==打印文本及照片){
         try{
            print=getToolkit().getPrintJob(new JFrame(),"打印",new Properties());
            g=print.getGraphics();
            g.translate(120,200);//在打印页的坐标(120,200)开始打印文本内容
            int w=messPanel.getBounds().width;
            messPanel.printAll(g);
            g.translate(w,0);   //在打印页的坐标(120+w,200)处打印照片
            pic.printAll(g);
            姓名.setText(stu.getName());
            g.dispose();          
            print.end();
         }
         catch(Exception exp){}    
      }
      else if(e.getSource()==打印文本){
         try{ 
           print=getToolkit().getPrintJob(new JFrame(),"打印",new Properties());
           g=print.getGraphics();
           g.translate(120,200);//在打印页的坐标(120,200)开始打印文本内容
           messPanel.printAll(g);
           g.dispose();          
           print.end();
         }
         catch(Exception exp){} 
      }  
      else if(e.getSource()==打印照片){
         try{ 
           print=getToolkit().getPrintJob(new JFrame(),"打印",new Properties());
           g=print.getGraphics();
           int w=messPanel.getBounds().width;
           g.translate(120+w,200);//在打印页的坐标(120+w,200)处打印照片
           pic.printAll(g);
           g.dispose();          
           print.end();
         }
         catch(Exception exp){}
      } 
   }
   public void clearMess(){
       学号.setText(null);
       姓名.setText(null);
       年级.setText(null);
       出生.setText(null);
       专业.setText(null);
       pic.setImage(null);
       pic.repaint();
   }
}
