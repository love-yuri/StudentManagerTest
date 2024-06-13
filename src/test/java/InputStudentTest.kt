import org.assertj.swing.core.BasicRobot
import org.assertj.swing.core.GenericTypeMatcher
import org.assertj.swing.core.Robot
import org.assertj.swing.core.matcher.JButtonMatcher
import org.assertj.swing.fixture.FrameFixture
import org.assertj.swing.fixture.JFileChooserFixture
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.awt.FlowLayout
import java.awt.Point
import java.io.*
import java.util.concurrent.CountDownLatch
import javax.swing.JFrame
import javax.swing.JRadioButton
import javax.swing.SwingUtilities


class InputStudentTest {
    private lateinit var window: FrameFixture
    private lateinit var robot: Robot
    private lateinit var tempFile: File

    @BeforeEach
    @Throws(InterruptedException::class)
    fun setUp() {
        robot = BasicRobot.robotWithNewAwtHierarchy()
        val latch = CountDownLatch(1)

        // 创建临时文件
        try {
            tempFile = File.createTempFile("students", ".dat")
            val initialData = HashMap<String, Student>()
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
                add(InputStudent(tempFile))
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
        val testStudent = Student().apply {
            name = "张三"
            sex = "女"
            discipling = "计算机科学与技术"
            grade = "大三"
            borth = "1997-01-01"
            imagePic = File("${File(".").absolutePath.dropLast(2)}/photo_2024-01-30_23-46-12.jpg")
            number = "09221208"
        }

        window.button(JButtonMatcher.withText("录入")).requireVisible()
        window.textBox(Matcher.TextMatcher(0)).setText(testStudent.number)
        window.textBox(Matcher.TextMatcher(1)).enterText(testStudent.name)
        window.comboBox().selectItem(testStudent.discipling)
        window.textBox(Matcher.TextMatcher(2)).enterText(testStudent.grade)
        window.textBox(Matcher.TextMatcher(3)).enterText(testStudent.borth)
        window.radioButton(object: GenericTypeMatcher<JRadioButton>(JRadioButton::class.java) {
            override fun isMatching(component: JRadioButton): Boolean {
                return component.text == testStudent.sex && component.isShowing
            }
        } ).check()

        val chooseBtn = window.button(JButtonMatcher.withText("选择")).apply {
            click()
        }

        // 选择文件
        JFileChooserFixture(robot).apply {
            selectFile(testStudent.imagePic)
            approve()
        }

        // 点击录入按钮
        window.button(JButtonMatcher.withText("录入")).click()

        // 验证对话框内容
        window.optionPane().requireMessage("基本信息将被录入!")
        window.optionPane().yesButton().click()

        // 验证信息是否被清空
        window.textBox(Matcher.TextMatcher(0)).requireText("")
        window.textBox(Matcher.TextMatcher(0)).requireText("")
        window.textBox(Matcher.TextMatcher(0)).requireText("")
        window.textBox(Matcher.TextMatcher(0)).requireText("")
        chooseBtn.requireText("选择")

        // 验证信息是否被录入
        ObjectInputStream(FileInputStream(tempFile)).use { ois ->
            val data = ois.readObject() as HashMap<*, *>
            assertTrue(data.containsKey(testStudent.number))
            val student = data[testStudent.number]
            assertTrue { student is Student }
            student as Student
            
            // 比较写入的内容
            assertNotNull(student)
            assertEquals(student.name, testStudent.name)
            assertEquals(student.sex, testStudent.sex)
            assertEquals(student.grade, testStudent.grade)
            assertEquals(student.borth, testStudent.borth)
            assertEquals(student.imagePic.absoluteFile, testStudent.imagePic.absoluteFile)

            println("""
                ***** 添加的信息 *****
                姓名: ${testStudent.name}
                学号: ${testStudent.number}
                性别: ${testStudent.sex}
                学院: ${testStudent.discipling}
                年级: ${testStudent.grade}
                出生日期: ${testStudent.borth}
                图片: ${testStudent.imagePic.name}
                ***** 添加的信息 *****
            """.trimIndent())
        }
    }
}