import com.daniilshevtsov.deduplication.DeduplicationApp
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.File

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DeduplicationAppTest {
    private lateinit var deduplicationApp: DeduplicationApp

    @BeforeEach
    fun onSetup() {
        deduplicationApp = DeduplicationApp()
    }

    @Test
    fun `when program launched - then writes correct output file`() {
        deduplicationApp.start(args = arrayOf(INPUT_PATH, ACTUAL_OUTPUT_PATH))

        assertOutputFile(expectedOutputPath = EXPECTED_OUTPUT_PATH)
    }

    @Test
    fun `when program launched with unique input - then writes correct output file`() {
        deduplicationApp.start(args = arrayOf(UNIQUE_INPUT_PATH, ACTUAL_OUTPUT_PATH))

        assertOutputFile(expectedOutputPath = UNIQUE_EXPECTED_OUTPUT_PATH)
    }

    private fun assertOutputFile(expectedOutputPath: String) {
        val expected = File(expectedOutputPath).readLines()
        val actual = File(ACTUAL_OUTPUT_PATH).readLines()
        actual shouldBe expected
    }

    private companion object {
        val INPUT_PATH: String = DeduplicationAppTest::class.java.getResource("app_input.txt").path
        val EXPECTED_OUTPUT_PATH: String = DeduplicationAppTest::class.java.getResource("app_expected_output.txt").path

        val UNIQUE_INPUT_PATH: String = DeduplicationAppTest::class.java.getResource("app_unique_input.txt").path
        val UNIQUE_EXPECTED_OUTPUT_PATH: String =
            DeduplicationAppTest::class.java.getResource("app_unique_expected_output.txt").path

        const val ACTUAL_OUTPUT_PATH = "output.txt"
    }
}