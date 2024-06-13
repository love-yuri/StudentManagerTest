import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import javax.swing.filechooser.*;
public class ModifySituation extends JPanel implements ActionListener{
    StudentPicture pic;
    HashMap<String,Student> 基本信息表=null;                           
    JTextField 学号,姓名,年级,出生; 
    JComboBox 专业;
    JButton 选择照片;                
    JRadioButton 男,女;
    ButtonGroup group=null;
    JButton 开始修改,录入修改,重置;
    FileInputStream inOne=null;
    ObjectInputStream inTwo=null;
    FileOutputStream outOne=null;
    ObjectOutputStream outTwo=null;
    File systemFile,imagePic;
    JComboBox oldMess;
    Student stu=null; 
    public ModifySituation(File file){
       systemFile=file;
       pic=new StudentPicture();
       学号=new JTextField(6);
       姓名=new JTextField(6);                                
       专业=new JComboBox();
       try{
           FileReader  inOne=new FileReader("专业.txt");
           BufferedReader inTwo= new BufferedReader(inOne);
           String s=null;
           int i=0;
           while((s=inTwo.readLine())!=null)
              专业.addItem(s);
           inOne.close();
           inTwo.close();
       }
       catch(IOException exp){
         专业.addItem("数学");
         专业.addItem("计算机科学与技术");
       } 
       年级=new JTextField(6);
       出生=new JTextField(6);
       选择照片=new JButton("选择");
       group=new ButtonGroup();
       男=new JRadioButton("男",true);
       女=new JRadioButton("女",false);
       group.add(男);
       group.add(女);
       oldMess=new JComboBox();
       开始修改=new JButton("开始修改");
       录入修改=new JButton("录入修改");
       录入修改.setEnabled(false);
       选择照片.setEnabled(false);
       重置=new JButton("重置");
       学号.addActionListener(this);
       开始修改.addActionListener(this);
       录入修改.addActionListener(this);
       重置.addActionListener(this);
       选择照片.addActionListener(this); 
       Box box1=Box.createHorizontalBox();              
       box1.add(new JLabel("输入要修改信息的学号:",JLabel.CENTER));
       box1.add(学号);
       box1.add(开始修改);
       Box box2=Box.createHorizontalBox();              
       box2.add(new JLabel("(新)姓名:",JLabel.CENTER));
       box2.add(姓名);
       Box box3=Box.createHorizontalBox();              
       box3.add(new JLabel("(新)性别:",JLabel.CENTER));
       box3.add(男);
       box3.add(女);
       Box box4=Box.createHorizontalBox();              
       box4.add(new JLabel("(新)专业:",JLabel.CENTER));
       box4.add(专业);
       Box box5=Box.createHorizontalBox();              
       box5.add(new JLabel("(新)年级:",JLabel.CENTER));
       box5.add(年级);
       Box box6=Box.createHorizontalBox();              
       box6.add(new JLabel("(新)出生:",JLabel.CENTER));
       box6.add(出生);
       Box box7=Box.createHorizontalBox();              
       box7.add(new JLabel("学生已有的数据:",JLabel.CENTER));
       box7.add(oldMess); 
       Box boxH=Box.createVerticalBox();              
       boxH.add(box1);
       boxH.add(box2);
       boxH.add(box3);
       boxH.add(box4);
       boxH.add(box5);
       boxH.add(box6);
       boxH.add(box7);
       boxH.add(Box.createVerticalGlue());
       JPanel picPanel=new JPanel(); 
       picPanel.setBackground(Color.green);             
       picPanel.add(new JLabel("选择照片:",JLabel.CENTER));
       picPanel.add(选择照片);
       JPanel putButton=new JPanel();
       putButton.setBackground(Color.yellow); 
       putButton.add(录入修改);
       putButton.add(重置);          
       JPanel messPanel=new JPanel();
       messPanel.setBackground(Color.pink);
       messPanel.add(boxH);
       setLayout(new BorderLayout());
       JSplitPane splitV=
       new JSplitPane(JSplitPane.VERTICAL_SPLIT,picPanel,pic);
       JSplitPane splitH=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,messPanel,splitV);
       add(splitH,BorderLayout.CENTER);
       add(putButton,BorderLayout.SOUTH); 
       validate();
    }
    public void actionPerformed(ActionEvent e){
     if(e.getSource()==开始修改||e.getSource()==学号){
        oldMess.removeAllItems();
        String number="";
        imagePic=null;
        stu=null; 
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
               录入修改.setEnabled(true);
               选择照片.setEnabled(true);
               stu=基本信息表.get(number);
               oldMess.addItem("姓名:"+stu.getName());
               oldMess.addItem("专业:"+stu.getDisciping());
               oldMess.addItem("年级:"+stu.getGrade());
               oldMess.addItem("出生日期:"+stu.getBorth()); 
               if(stu.getSex().equals("男"))
                   男.setSelected(true);
               else
                   女.setSelected(true);
               imagePic=stu.getImagePic();
               pic.setImage(imagePic);
               pic.repaint();
               姓名.setText(stu.getName());
               年级.setText(stu.getGrade());
               出生.setText(stu.getBorth());
          }
          else{
               录入修改.setEnabled(false);
               选择照片.setEnabled(false);
               String warning="该学号不存在!";
               JOptionPane.showMessageDialog(this,warning,"警告",
                                             JOptionPane.WARNING_MESSAGE);
               clearMess();
          }
        }
        else{
          录入修改.setEnabled(false); 
          选择照片.setEnabled(false);
          String warning="必须要输入学号!";
          JOptionPane.showMessageDialog(this,warning,"警告",JOptionPane.WARNING_MESSAGE);
          clearMess();
        }
     } 
     else if(e.getSource()==录入修改){
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
                  String question="该生基本信息已存在,您想修改他(她)的基本信息吗?";
                  JOptionPane.showMessageDialog(this,question,"警告",
                                                JOptionPane.QUESTION_MESSAGE);
                  String m="基本信息将被修改!";
                  int ok=JOptionPane.showConfirmDialog(this,m,"确认",
                        JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE);
                  if(ok==JOptionPane.YES_OPTION){
                      String name=姓名.getText();
                      if(name.length()==0)
                         name=stu.getName();
                      String discipling=(String)专业.getSelectedItem();
                      if(discipling==null)
                         discipling=stu.getDisciping();  
                      String grade=年级.getText();
                      if(grade.length()==0)
                         grade=stu.getGrade(); 
                      String borth=出生.getText();
                      if(borth.length()==0)
                         borth=stu.getBorth();
                      String sex=null;
                      if(男.isSelected())
                          sex=男.getText();
                      else
                          sex=女.getText();
                       if(imagePic==null) 
                         imagePic=stu.getImagePic();  
                      Student  学生=new Student();
                      学生.setNumber(number);
                      学生.setName(name);
                      学生.setDiscipling(discipling);
                      学生.setGrade(grade);
                      学生.setBorth(borth);
                      学生.setSex(sex);
                      学生.setImagePic(imagePic);
                      try{
                         outOne=new FileOutputStream(systemFile);
                         outTwo=new ObjectOutputStream(outOne);
                         基本信息表.put(number,学生);
                         outTwo.writeObject(基本信息表);
  
                         outTwo.close();
                         outOne.close();
                         clearMess();
                      }
                      catch(Exception ee){}
                      录入修改.setEnabled(false);
                      选择照片.setEnabled(false); 
                  }
                  else if(ok==JOptionPane.NO_OPTION){
                      录入修改.setEnabled(true);
                      选择照片.setEnabled(true);
                  }
            }
            else{
              String warning="该学号没有基本信息,不能修改!";
              JOptionPane.showMessageDialog(this,warning,"警告",
                                            JOptionPane.WARNING_MESSAGE);
              录入修改.setEnabled(false); 
              选择照片.setEnabled(false);
              clearMess();
            }
        }
        else{
            String warning="必须要输入学号!";
            JOptionPane.showMessageDialog(this,warning,"警告",JOptionPane.WARNING_MESSAGE);
            录入修改.setEnabled(false);
            clearMess();
            录入修改.setEnabled(false); 
            选择照片.setEnabled(false);
        }
     }
     else if(e.getSource()==选择照片){
         JFileChooser chooser=new JFileChooser();
         FileNameExtensionFilter filter = new FileNameExtensionFilter(
                                           "JPG & GIF Images", "jpg", "gif");
         chooser.setFileFilter(filter);
         int state=chooser.showOpenDialog(null);
         File choiceFile=chooser.getSelectedFile();
         if(choiceFile!=null&&state==JFileChooser.APPROVE_OPTION){
            选择照片.setText(choiceFile.getName());
            imagePic=choiceFile;
            pic.setImage(imagePic);
            pic.repaint();
         }
     } 
     else if(e.getSource()==重置){
        clearMess();
        录入修改.setEnabled(false);
        选择照片.setEnabled(false);
     }
   }
   public void clearMess(){
       学号.setText(null);
       姓名.setText(null);
       年级.setText(null);
       出生.setText(null);
       oldMess.removeAllItems();  
       选择照片.setText("选择");
       imagePic=null;
       pic.setImage(imagePic);
       pic.repaint();
   }
}
