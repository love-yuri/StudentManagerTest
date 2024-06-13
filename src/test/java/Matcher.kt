import org.assertj.swing.core.GenericTypeMatcher
import javax.swing.JComboBox
import javax.swing.JRadioButton
import javax.swing.JTextField

object Matcher {
    class TextMatcher(
        private val index: Int
    ) : GenericTypeMatcher<JTextField>(JTextField::class.java) {
        private var count = 0
        override fun isMatching(component: JTextField): Boolean {
            if (count == index) {
                count++
                return true
            }
            count++
            return false
        }
    }

    class ComboBoxMatcher(
        private val index: Int
    ) : GenericTypeMatcher<JComboBox<*>>(JComboBox::class.java) {
        private var count = 0
        override fun isMatching(component: JComboBox<*>): Boolean {
            if (count == index) {
                count++
                return true
            }
            count++
            return false
        }
    }

}

