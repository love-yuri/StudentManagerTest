import java.io.*;
public class Student implements Serializable{
    String number,name,discipling,grade,borth,sex;
    File imagePic;
    public void setNumber(String number){
       this.number=number;
    }
    public String getNumber(){
       return number;
    }
    public void setName(String name){
       this.name=name;
    }
    public String getName(){
       return name;
    }
    public void setDiscipling(String discipling){
       this.discipling=discipling;
    }
    public String getDisciping(){
       return discipling;
    }
    public void setGrade(String grade){
       this.grade=grade;
    }
    public String getGrade(){
       return grade;
    }
    public void setBorth(String borth){
       this.borth=borth;
    }
    public String getBorth(){
       return borth;
    }
    public void setSex(String sex){
       this.sex=sex;
    }
    public String getSex(){
       return sex;
    }
    public void setImagePic(File image){
        imagePic=image;
    }
    public File getImagePic(){
        return imagePic;
    }
}
