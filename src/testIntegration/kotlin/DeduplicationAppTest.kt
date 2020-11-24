import com.daniilshevtsov.deduplication.DeduplicationApp
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.AfterEach
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
        File("storage").listFiles()
            ?.filter { it.name.startsWith("test") }
            ?.forEach { file -> file.delete() }
        File(ACTUAL_OUTPUT_PATH).delete()
        File("data.db").delete()

        deduplicationApp = DeduplicationApp()
    }

    @AfterEach
    fun onTeardown() {

    }

    @Test
    fun `when program launched - then writes correct output file`() {
        testApp(inputFile = INPUT_PATH, expectedOutputFile = EXPECTED_OUTPUT_PATH)
    }

    @Test
    fun `when program launched with unique input - then writes correct output file`() {
        testApp(inputFile = UNIQUE_INPUT_PATH, expectedOutputFile = UNIQUE_EXPECTED_OUTPUT_PATH)
    }

    @Test
    fun `when program launched with war of worlds - then writes correct output file`() {
        testApp(inputFile = WAR_INPUT_PATH, expectedOutputFile = WAR_EXPECTED_OUTPUT_PATH)
    }

    private fun testApp(inputFile: String, expectedOutputFile: String) {
        deduplicationApp.start(args = arrayOf(STORE_KEY, inputFile))
        deduplicationApp.start(args = arrayOf(READ_KEY, inputFile, ACTUAL_OUTPUT_PATH))

        assertOutputFile(
            actualOutputPath = ACTUAL_OUTPUT_PATH,
            expectedOutputPath = expectedOutputFile
        )
    }

    private fun assertOutputFile(
        actualOutputPath: String,
        expectedOutputPath: String
    ) {
        val expected = File(expectedOutputPath).readLines()
        val actual = File(actualOutputPath).readLines()
        actual shouldBe expected
    }

    private companion object {
        const val STORE_KEY = "-s"
        const val READ_KEY = "-r"

        val INPUT_PATH: String = getResourcePath("test_app_input.txt")
        val EXPECTED_OUTPUT_PATH: String = getResourcePath("test_app_expected_output.txt")

        val UNIQUE_INPUT_PATH: String = getResourcePath("test_app_unique_input.txt")
        val UNIQUE_EXPECTED_OUTPUT_PATH: String = getResourcePath("test_app_unique_expected_output.txt")

        val WAR_INPUT_PATH: String = getResourcePath("test_app_source_war_input.txt")
        val WAR_EXPECTED_OUTPUT_PATH: String = getResourcePath("test_app_source_war_expected_output.txt")

        const val ACTUAL_OUTPUT_PATH = "output/test_output.txt"

        private fun getResourcePath(fileName: String) =
            Paths.get(DeduplicationAppTest::class.java.getResource(fileName).toURI()).toString()
    }
}