import com.daniilshevtsov.deduplication.DeduplicationApp
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.File
import java.nio.file.Paths

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DeduplicationAppTest {
    private lateinit var deduplicationApp: DeduplicationApp

    @BeforeEach
    fun onSetup() {
        deduplicationApp = DeduplicationApp()
    }

    @Test
    fun `when program launched - then writes correct output file`() {
        deduplicationApp.start(args = arrayOf(STORE_KEY, INPUT_PATH))
        deduplicationApp.start(args = arrayOf(READ_KEY, EXPECTED_OUTPUT_PATH))

        assertOutputFile(expectedOutputPath = EXPECTED_OUTPUT_PATH)
    }

    @Test
    fun `when program launched with unique input - then writes correct output file`() {
        deduplicationApp.start(args = arrayOf(STORE_KEY, UNIQUE_INPUT_PATH))
        deduplicationApp.start(args = arrayOf(READ_KEY, ACTUAL_OUTPUT_PATH))

        assertOutputFile(expectedOutputPath = UNIQUE_EXPECTED_OUTPUT_PATH)
    }

    private fun assertOutputFile(expectedOutputPath: String) {
        val expected = File(expectedOutputPath).readLines()
        val actual = File(ACTUAL_OUTPUT_PATH).readLines()
        actual shouldBe expected
    }

    private companion object {
        const val STORE_KEY = "-s"
        const val READ_KEY = "-r"

        val INPUT_PATH: String = getResourcePath("app_input.txt")
        val EXPECTED_OUTPUT_PATH: String = DeduplicationAppTest::class.java.getResource("app_expected_output.txt").path

        val UNIQUE_INPUT_PATH: String = DeduplicationAppTest::class.java.getResource("app_unique_input.txt").path
        val UNIQUE_EXPECTED_OUTPUT_PATH: String =
            DeduplicationAppTest::class.java.getResource("app_unique_expected_output.txt").path

        const val ACTUAL_OUTPUT_PATH = "output.txt"

        private fun getResourcePath(fileName: String) =
//            File(DeduplicationAppTest::class.java.getResource(fileName).path).absolutePath
            Paths.get(DeduplicationAppTest::class.java.getResource(fileName).toURI()).toString()
    }
}