import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
public class Delete extends JPanel implements ActionListener{
    HashMap<String,Student> 基本信息表=null;                           
    JTextField 学号,姓名,专业,年级,出生;                 
    JRadioButton 男,女;
    JButton 删除;
    ButtonGroup group=null;
    FileInputStream inOne=null;
    ObjectInputStream inTwo=null;
    FileOutputStream outOne=null;
    ObjectOutputStream outTwo=null;
    File systemFile=null;                                           
    public Delete(File file){
       systemFile=file;
       学号=new JTextField(10);
       删除=new JButton("删除");
       学号.addActionListener(this);
       删除.addActionListener(this);
       姓名=new JTextField(10);
  
       姓名.setEditable(false);
       专业=new JTextField(10);
       专业.setEditable(false);
       年级=new JTextField(10);
       年级.setEditable(false);
       出生=new JTextField(10);
       出生.setEditable(false);
       男=new JRadioButton("男",false);
       女=new JRadioButton("女",false);
       group=new ButtonGroup();
       group.add(男);
       group.add(女);
       Box box1=Box.createHorizontalBox();              
       box1.add(new JLabel("输入要删除的学号:",JLabel.CENTER));
       box1.add(学号);
       box1.add(删除);
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
       JPanel pCenter=new JPanel();
       pCenter.add(boxH);
       setLayout(new BorderLayout());
       add(pCenter,BorderLayout.CENTER);
  
       validate();
    }
    public void actionPerformed(ActionEvent e){
       if(e.getSource()==删除||e.getSource()==学号){
         String number="";
         number=学号.getText();
         if(number.length()>0){
             try {
                    inOne=new FileInputStream(systemFile);
                    inTwo=new ObjectInputStream(inOne);
                    基本信息表=(HashMap)inTwo.readObject();
                    inOne.close();
                    inTwo.close();
             }
             catch(Exception ee){}
             if(基本信息表.containsKey(number)){          
                 Student stu=(Student)基本信息表.get(number);
                 姓名.setText(stu.getName());
                 专业.setText(stu.getDisciping());
                 年级.setText(stu.getGrade());
                 出生.setText(stu.getBorth()); 
                 if(stu.getSex().equals("男"))
                     男.setSelected(true);
                 else
                     女.setSelected(true);
                 String m="确定要删除该学号及全部信息吗?";
                 int ok=JOptionPane.showConfirmDialog(this,m,"确认",
                 JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                 if(ok==JOptionPane.YES_OPTION){
                     基本信息表.remove(number);
                     try{
                          outOne=new FileOutputStream(systemFile);
                          outTwo=new ObjectOutputStream(outOne);
                          outTwo.writeObject(基本信息表);
                          outTwo.close();
                          outOne.close();
                          学号.setText(null);
                          姓名.setText(null);                                
                          专业.setText(null);
                          年级.setText(null);
                          出生.setText(null);
                      }
                      catch(Exception ee){}
                 }
                 else if(ok==JOptionPane.NO_OPTION){
                      学号.setText(null);
                      姓名.setText(null);
                      专业.setText(null);
                      年级.setText(null);
                      出生.setText(null);
                 }
             }
             else{
                  String warning="该学号不存在!";
                  JOptionPane.showMessageDialog(this,warning,"警告",
                                                 JOptionPane.WARNING_MESSAGE);
                  学号.setText(null);
             }
         }
         else{
             String warning="必须要输入学号!";
             JOptionPane.showMessageDialog(this,warning,"警告",
                                           JOptionPane.WARNING_MESSAGE);
         }
       } 
    }
}
