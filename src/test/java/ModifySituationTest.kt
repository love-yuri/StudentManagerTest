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


class ModifySituationTest {
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

    val newTestStudent = Student().apply {
        name = "李四"
        sex = "男"
        discipling = "数学"
        grade = "大四"
        borth = "1997-02-01"
        imagePic = File("${File(".").absolutePath.dropLast(2)}/photo_test.jpg")
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
                add(ModifySituation(tempFile))
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
        window.textBox(Matcher.TextMatcher(0)).setText(testStudent.number)
        window.button(JButtonMatcher.withText("开始修改")).also {
            it.requireVisible()
            it.click()
        }

        window.textBox(Matcher.TextMatcher(1)).setText(newTestStudent.name)
        window.comboBox(Matcher.ComboBoxMatcher(0)).selectItem(newTestStudent.discipling)
        window.textBox(Matcher.TextMatcher(2)).setText(newTestStudent.grade)
        window.textBox(Matcher.TextMatcher(3)).setText(newTestStudent.borth)
        window.radioButton(object: GenericTypeMatcher<JRadioButton>(JRadioButton::class.java) {
            override fun isMatching(component: JRadioButton): Boolean {
                return component.text == newTestStudent.sex && component.isShowing
            }
        } ).check()

        val chooseBtn = window.button(JButtonMatcher.withText("选择")).apply {
            click()
        }

        // 选择文件
        JFileChooserFixture(robot).apply {
            selectFile(newTestStudent.imagePic)
            approve()
        }

        // 点击录入按钮
        window.button(JButtonMatcher.withText("录入修改")).click()

        // 验证对话框内容
        window.optionPane().requireMessage("该生基本信息已存在,您想修改他(她)的基本信息吗?")
        window.optionPane().button(JButtonMatcher.withText("确定")).click()
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
            assertEquals(student.name, newTestStudent.name)
            assertEquals(student.sex, newTestStudent.sex)
            assertEquals(student.grade, newTestStudent.grade)
            assertEquals(student.borth, newTestStudent.borth)
            assertEquals(student.imagePic.absoluteFile, newTestStudent.imagePic.absoluteFile)

            println("""
                ***** 原始信息 *****
                姓名: ${testStudent.name}
                学号: ${testStudent.number}
                性别: ${testStudent.sex}
                学院: ${testStudent.discipling}
                年级: ${testStudent.grade}
                出生日期: ${testStudent.borth}
                图片: ${testStudent.imagePic.name}
                ***** 原始信息 *****
            """.trimIndent())

            println("""
                ***** 修改后的信息 *****
                姓名: ${newTestStudent.name}
                学号: ${newTestStudent.number}
                性别: ${newTestStudent.sex}
                学院: ${newTestStudent.discipling}
                年级: ${newTestStudent.grade}
                出生日期: ${newTestStudent.borth}
                图片: ${newTestStudent.imagePic.name}
                ***** 修改的信息 *****
            """.trimIndent())
        }
    }
}