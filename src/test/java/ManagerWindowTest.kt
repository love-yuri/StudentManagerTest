import org.assertj.swing.core.BasicRobot
import org.assertj.swing.core.Robot
import org.assertj.swing.core.matcher.DialogMatcher
import org.assertj.swing.core.matcher.JButtonMatcher
import org.assertj.swing.fixture.DialogFixture
import org.assertj.swing.fixture.FrameFixture
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.concurrent.CountDownLatch
import javax.swing.JFrame
import javax.swing.SwingUtilities

class ManagerWindowTest {
    private lateinit var window: FrameFixture
    private lateinit var robot: Robot

    @BeforeEach
    @Throws(InterruptedException::class)
    fun setUp() {
        robot = BasicRobot.robotWithNewAwtHierarchy()
        val latch = CountDownLatch(1)

        // 等待窗口初始化完成
        SwingUtilities.invokeLater {
            val frame = ManagerWindow()
            frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            frame.isVisible = true
            window = FrameFixture(robot, frame)
            latch.countDown()
        }

        latch.await()
    }

    @AfterEach
    fun tearDown() {
        window.cleanUp()
    }

    @Test
    fun testClickMenuItems() {
        // 验证点击菜单后是否正常条找

        /* 验证录入面板 */
        window.menuItemWithPath("菜单选项", "录入学生基本信息").click()
        // 有无录入按钮
        window.button(JButtonMatcher.withText("录入")).requireVisible()

        /* 验证修改面板 */
        window.menuItemWithPath("菜单选项", "修改学生基本信息").click()
        // 有无修改按钮
        window.button(JButtonMatcher.withText("录入修改")).requireVisible()

        /* 验证查询与打印面板 */
        window.menuItemWithPath("菜单选项", "查询与打印学生基本信息").click()

        // 验证查询对话框
        val queryDialog: DialogFixture = window.dialog(DialogMatcher.withTitle("查询对话框"))
        queryDialog.requireVisible()

        // 有无查询按钮
        queryDialog.button(JButtonMatcher.withText("查询")).requireVisible()

        /* 验证删除面板 */
        window.menuItemWithPath("菜单选项", "删除学生基本信息").click()
        // 有无删除按钮
        window.button(JButtonMatcher.withText("删除")).requireVisible()

        /* 验证欢迎界面 */
        window.menuItemWithPath("菜单选项", "欢迎界面").click()
    }
}