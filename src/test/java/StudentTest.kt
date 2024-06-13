import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File

class StudentTest {

    @Test
    fun setAndGetTest() {
        val student = Student()

        // 测试设置和获取学号
        student.setNumber("12345")
        Assertions.assertEquals("12345", student.getNumber())

        // 测试设置和获取姓名
        student.setName("Alice")
        Assertions.assertEquals("Alice", student.getName())

        // 测试设置和获取专业
        student.setDiscipling("Computer Science")
        Assertions.assertEquals("Computer Science", student.disciping)

        // 测试设置和获取年级
        student.setGrade("Junior")
        Assertions.assertEquals("Junior", student.getGrade())

        // 测试设置和获取生日
        student.setBorth("2000-01-01")
        Assertions.assertEquals("2000-01-01", student.getBorth())

        // 测试设置和获取性别
        student.setSex("Female")
        Assertions.assertEquals("Female", student.getSex())

        // 测试设置和获取图片文件
        val imageFile = File("test.jpg")
        student.setImagePic(imageFile)
        Assertions.assertEquals(imageFile, student.getImagePic())

    }
}