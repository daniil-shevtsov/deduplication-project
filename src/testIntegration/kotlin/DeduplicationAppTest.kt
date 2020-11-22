import com.daniilshevtsov.deduplication.DeduplicationApp
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.File

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
        deduplicationApp.start(args = arrayOf(STORE_KEY, INPUT_PATH))
        deduplicationApp.start(args = arrayOf(READ_KEY, INPUT_PATH, ACTUAL_OUTPUT_PATH))

        assertOutputFile(
            actualOutputPath = ACTUAL_OUTPUT_PATH,
            expectedOutputPath = EXPECTED_OUTPUT_PATH
        )
    }

//    @Test
//    fun `when program launched with unique input - then writes correct output file`() {
//        deduplicationApp.start(args = arrayOf(STORE_KEY, UNIQUE_INPUT_PATH))
//        deduplicationApp.start(args = arrayOf(READ_KEY, UNIQUE_INPUT_PATH, ACTUAL_OUTPUT_PATH))
//
//        assertOutputFile(expectedOutputPath = UNIQUE_EXPECTED_OUTPUT_PATH)
//    }

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
        val EXPECTED_OUTPUT_PATH: String =
            DeduplicationAppTest::class.java.getResource("test_app_expected_output.txt").path

        val UNIQUE_INPUT_PATH: String = getResourcePath("test_app_unique_input.txt")
        val UNIQUE_EXPECTED_OUTPUT_PATH: String =
            DeduplicationAppTest::class.java.getResource("test_app_unique_expected_output.txt").path

        const val ACTUAL_OUTPUT_PATH = "test_output.txt"

        private fun getResourcePath(fileName: String) =
            DeduplicationAppTest::class.java.getResource(fileName).path.drop(1)
//            File(DeduplicationAppTest::class.java.getResource(fileName).path).absolutePath
//        Paths.get(DeduplicationAppTest::
//        class.java.getResource(fileName).toURI()).toString()
    }
}