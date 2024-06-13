import org.assertj.swing.core.BasicRobot
import org.assertj.swing.core.Robot
import org.assertj.swing.core.matcher.JButtonMatcher
import org.assertj.swing.fixture.FrameFixture
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.awt.FlowLayout
import java.awt.Point
import java.io.*
import java.util.concurrent.CountDownLatch
import javax.swing.JFrame
import javax.swing.SwingUtilities


class DeleteTest {
    private lateinit var window: FrameFixture
    private lateinit var robot: Robot
    private lateinit var tempFile: File

    val testStudent = Student().apply {
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
            val frame = JFrame().apply {
                layout = FlowLayout()
                isResizable = false
                defaultCloseOperation = JFrame.EXIT_ON_CLOSE
                add(Delete(tempFile))
                pack()  // 调整窗口大小以适应其内容
                setLocationRelativeTo(null)
            }
            window = FrameFixture(robot, frame)
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
        window.button(JButtonMatcher.withText("删除")).click()

        // 验证对话框内容
        window.optionPane().requireMessage("该学号不存在!")
        window.optionPane().button(JButtonMatcher.withText("确定")).click()

        /* 存在用户验证 */

        window.textBox(Matcher.TextMatcher(0)).setText(testStudent.number)
        window.button(JButtonMatcher.withText("删除")).click()

        // 验证对话框内容
        window.optionPane().requireMessage("确定要删除该学号及全部信息吗?")
        window.optionPane().yesButton().click()

        // 验证信息是否被删除
        ObjectInputStream(FileInputStream(tempFile)).use { ois ->
            val data = ois.readObject() as HashMap<*, *>
            assertTrue(!data.containsKey(testStudent.number))

            println("""
                ***** 被删除的信息 *****
                姓名: ${testStudent.name}
                学号: ${testStudent.number}
                性别: ${testStudent.sex}
                学院: ${testStudent.discipling}
                年级: ${testStudent.grade}
                出生日期: ${testStudent.borth}
                图片: ${testStudent.imagePic.name}
                ***** 被删除的信息 *****
            """.trimIndent())
        }
    }
}