import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.HashMap;
public class ManagerWindow extends JFrame implements ActionListener{
   InputStudent 基本信息录入;          
   ModifySituation 基本信息修改;          
   Inquest         基本信息查询与打印; 
   Delete          基本信息删除; 
   JMenuBar bar;
   JMenu fileMenu;
   JMenuItem 录入,修改,查询与打印,删除,欢迎界面;      
   HashMap<String,Student> 基本信息=null;                    
   File file=null;                             
   CardLayout card=null;                       
   JLabel label=null;                          
   JPanel pCenter;
   public ManagerWindow(){
     录入=new JMenuItem("录入学生基本信息");
     修改=new JMenuItem("修改学生基本信息");
     查询与打印=new JMenuItem("查询与打印学生基本信息");
     删除=new JMenuItem("删除学生基本信息");
     欢迎界面=new JMenuItem("欢迎界面");
     bar=new JMenuBar();
     fileMenu=new JMenu("菜单选项");
     fileMenu.add(录入);
     fileMenu.add(修改);
     fileMenu.add(查询与打印);
     fileMenu.add(删除);
     fileMenu.add(欢迎界面);
     bar.add(fileMenu);
     setJMenuBar(bar);
     label=new JLabel("学籍管理系统",JLabel.CENTER);
     label.setIcon(new ImageIcon("welcome.jpg"));
     label.setFont(new Font("隶书",Font.BOLD,36));
     label.setHorizontalTextPosition(SwingConstants.CENTER);
     label.setForeground(Color.red);
     基本信息=new HashMap<String,Student>();
     录入.addActionListener(this);
     修改.addActionListener(this);
     查询与打印.addActionListener(this);
     删除.addActionListener(this);
     欢迎界面.addActionListener(this);
     card=new CardLayout();
     pCenter=new JPanel();
     pCenter.setLayout(card); 
     file=new File("基本信息.txt");
     if(!file.exists()){
        try{
             FileOutputStream out=new FileOutputStream(file);
             ObjectOutputStream objectOut=new ObjectOutputStream(out);
             objectOut.writeObject(基本信息);
             objectOut.close();
             out.close();
        }
        catch(IOException e){}
     } 
     基本信息录入=new InputStudent(file);
     基本信息修改=new ModifySituation(file);
     基本信息查询与打印=new Inquest(file);
     基本信息删除=new Delete(file);
     pCenter.add("欢迎界面",label);
     pCenter.add("录入界面",基本信息录入);
     pCenter.add("修改界面",基本信息修改);
     pCenter.add("删除界面",基本信息删除);
     add(pCenter,BorderLayout.CENTER);
     validate();
     setVisible(true);
     setBounds(100,50,460,280);
     setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
     addWindowListener(new WindowAdapter(){
                    public void windowClosing(WindowEvent e){
                      int n=JOptionPane.showConfirmDialog(null,"确认退出吗?","确认对话框",
                                               JOptionPane.YES_NO_OPTION );
                      if(n==JOptionPane.YES_OPTION)  
                         System.exit(0);
                    }});
     validate();   
   }
   public void actionPerformed(ActionEvent e){
     if(e.getSource()==录入){
         基本信息录入.clearMess();
         card.show(pCenter,"录入界面");
     }
     else if(e.getSource()==修改){
         基本信息修改.clearMess();
         card.show(pCenter,"修改界面");
     }
     else if(e.getSource()==查询与打印){
         基本信息查询与打印.clearMess();
         基本信息查询与打印.setLocation(getBounds().x+getBounds().width,getBounds().y);
         基本信息查询与打印.setVisible(true);
     }
     else if(e.getSource()==删除)
         card.show(pCenter,"删除界面");
     else if(e.getSource()==欢迎界面)
         card.show(pCenter,"欢迎界面");
   }
   public static void main(String args[]){
     new ManagerWindow();
   }
}
