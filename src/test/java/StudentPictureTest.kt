import org.assertj.swing.core.BasicRobot
import org.assertj.swing.core.Robot
import org.assertj.swing.fixture.FrameFixture
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.awt.FlowLayout
import java.awt.Point
import java.io.*
import java.util.concurrent.CountDownLatch
import javax.swing.*


class StudentPictureTest {
    private lateinit var window: FrameFixture
    private lateinit var robot: Robot
    private lateinit var tempFile: File
    private lateinit var studentPicture: StudentPicture

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
                studentPicture = StudentPicture()
                studentPicture.name = "student_image"
                add(studentPicture)
                setLocationRelativeTo(null)
                setSize(400, 400)
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
        /* 测试空白图片显示 */
        studentPicture.setImage(null)

        /* 测试从文件加载图片 */
        studentPicture.setImage(File("${File(".").absolutePath.dropLast(2)}/photo_2024-01-30_23-46-12.jpg"))
    }
}