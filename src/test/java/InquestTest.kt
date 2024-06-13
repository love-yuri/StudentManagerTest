import org.assertj.swing.core.BasicRobot
import org.assertj.swing.core.GenericTypeMatcher
import org.assertj.swing.core.Robot
import org.assertj.swing.core.matcher.JButtonMatcher
import org.assertj.swing.fixture.DialogFixture
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.awt.Point
import java.io.*
import java.util.concurrent.CountDownLatch
import javax.swing.JRadioButton
import javax.swing.SwingUtilities



class InquestTest {
    private lateinit var window: DialogFixture
    private lateinit var robot: Robot
    private lateinit var tempFile: File

    private val testStudent = Student().apply {
        name = "张三"
        sex = "女"
        discipling = "计算机科学与技术"
        grade = "大三"
        borth = "1997-01-01"
        imagePic = File("${File(".").absolutePath.dropLast(2)}/photo_2024-01-30_23-46-12.jpg")
        number = "09221208"
    }

    @BeforeEach
    @Throws(InterruptedException::class)
    fun setUp() {
        robot = BasicRobot.robotWithNewAwtHierarchy()
        val latch = CountDownLatch(1)

        // 创建临时文件
        try {
            tempFile = File.createTempFile("students", ".dat")
            val initialData = HashMap<String, Student>()
            initialData[testStudent.number] = testStudent
            ObjectOutputStream(FileOutputStream(tempFile)).use { oos ->
                oos.writeObject(initialData)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // 等待窗口初始化完成
        SwingUtilities.invokeLater {
            window = DialogFixture(robot, Inquest(tempFile))
            latch.countDown()
        }
        latch.await()
        window.show()
        window.moveTo(Point(500, 500))
    }

    @AfterEach
    fun tearDown() {
        window.cleanUp()
    }

    @Test
    fun testClickMenuItems() {

        /* 不存用户验证 */
        window.textBox(Matcher.TextMatcher(0)).setText(testStudent.number + "test")
        window.button(JButtonMatcher.withText("查询")).click()

        // 验证对话框内容
        window.optionPane().requireMessage("该学号不存在!")
        window.optionPane().button(JButtonMatcher.withText("确定")).click()

        /* 存在用户验证 */
        window.textBox(Matcher.TextMatcher(0)).setText(testStudent.number)
        window.button(JButtonMatcher.withText("查询")).click()

        // 验证信息是否正确
        val number = window.textBox(Matcher.TextMatcher(0)).text()
        val name = window.textBox(Matcher.TextMatcher(1)).text()
        val college = window.textBox(Matcher.TextMatcher(2)).text()

        val sex = window.radioButton(object : GenericTypeMatcher<JRadioButton>(JRadioButton::class.java) {
            override fun isMatching(component: JRadioButton): Boolean {
                return component.isSelected
            }
        }).text()
        val grade = window.textBox(Matcher.TextMatcher(3)).text()
        val birth = window.textBox(Matcher.TextMatcher(4)).text()

        println("$name $college $grade $birth $sex")

        /* 验证数据真确性 */
        assertEquals(testStudent.number, number)
        assertEquals(testStudent.name, name)
        assertEquals(testStudent.discipling, college)
        assertEquals(testStudent.grade, grade)
        assertEquals(testStudent.borth, birth)
        assertEquals(testStudent.sex, sex)

        println("""
            ***** 查询到的信息 *****
            姓名: ${testStudent.name}
            学号: ${testStudent.number}
            性别: ${testStudent.sex}
            学院: ${testStudent.discipling}
            年级: ${testStudent.grade}
            出生日期: ${testStudent.borth}
            图片: ${testStudent.imagePic.name}
            ***** 查询到的信息 *****
        """.trimIndent())
    }
}