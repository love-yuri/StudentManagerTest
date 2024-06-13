import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import javax.swing.filechooser.*;
public class InputStudent extends JPanel implements ActionListener{
   Student  学生=null;
   StudentPicture pic;
   HashMap<String,Student> 基本信息表=null;                           
   JTextField 学号,姓名,年级,出生;
   JButton 选择照片;
   JComboBox 专业;                 
   JRadioButton 男,女;
   ButtonGroup group=null;
   JButton 录入,重置;
   FileInputStream inOne=null;
   ObjectInputStream inTwo=null;
   FileOutputStream outOne=null;
   ObjectOutputStream outTwo=null;
   File systemFile,imagePic;
   public InputStudent(File file){
      systemFile=file;
      pic=new StudentPicture();
      学号=new JTextField(5);
      姓名=new JTextField(5);
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
      年级=new JTextField(5);
      出生=new JTextField(5);
      选择照片=new JButton("选择"); 
      group=new ButtonGroup();
      男=new JRadioButton("男",true);
      女=new JRadioButton("女",false);
      group.add(男);
      group.add(女);
      录入=new JButton("录入");
      重置=new JButton("重置");
      录入.addActionListener(this);
      选择照片.addActionListener(this);
      重置.addActionListener(this);
      Box box1=Box.createHorizontalBox();              
      box1.add(new JLabel("学号:",JLabel.CENTER));
      box1.add(学号);
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
      JPanel picPanel=new JPanel();
      picPanel.setBackground(Color.green);              
      picPanel.add(new JLabel("选择照片:",JLabel.CENTER));
      picPanel.add(选择照片);
      JPanel putButton=new JPanel(); 
      putButton.add(录入);
      putButton.add(重置);            
      JPanel messPanel=new JPanel();
      messPanel.add(boxH);
      messPanel.setBackground(Color.cyan);
      putButton.setBackground(Color.red);
      setLayout(new BorderLayout());
      JSplitPane splitV=
      new JSplitPane(JSplitPane.VERTICAL_SPLIT,picPanel,pic);
      JSplitPane splitH=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,messPanel,splitV);
      add(splitH,BorderLayout.CENTER);
      add(putButton,BorderLayout.SOUTH);
      validate();
   }
   public void actionPerformed(ActionEvent e){
     if(e.getSource()==录入){
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
                String warning="该生基本信息已存在,请到修改页面修改!";
                JOptionPane.showMessageDialog(this,warning,"警告",
                                              JOptionPane.WARNING_MESSAGE);
            }
            else{  
               String m="基本信息将被录入!";
               int ok=JOptionPane.showConfirmDialog(this,m,"确认",
                           JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE);
               if(ok==JOptionPane.YES_OPTION){
                     String name=姓名.getText();
                     String discipling=(String)专业.getSelectedItem();
                     String grade=年级.getText();
                     String borth=出生.getText();
                     String sex=null;
                        if(男.isSelected())
                            sex=男.getText();
                        else
                            sex=女.getText();
                     学生=new Student();
                     学生.setNumber(number);
                     学生.setName(name);
                     学生.setDiscipling(discipling);
                     学生.setGrade(grade);
                     学生.setBorth(borth);
                     学生.setSex(sex);
                     学生.setImagePic(imagePic);
                     try { outOne=new FileOutputStream(systemFile);
                           outTwo=new ObjectOutputStream(outOne);
                           基本信息表.put(number,学生);
                           outTwo.writeObject(基本信息表);
                           outTwo.close();
                           outOne.close();
                           clearMess();
                      }
                      catch(Exception ee){}
               }
            } 
        }
        else{
            String warning="必须要输入学号!";
            JOptionPane.showMessageDialog(this,warning,"警告",JOptionPane.WARNING_MESSAGE);
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
     }
   }
   public void clearMess(){
       学号.setText(null);
       姓名.setText(null);
       年级.setText(null);
       出生.setText(null);
       选择照片.setText("选择");
       imagePic=null;
       pic.setImage(imagePic);
       pic.repaint();
   }
}
