import com.daniilshevtsov.deduplication.feature.core.AppConfig
import com.daniilshevtsov.deduplication.feature.core.Chunk
import com.daniilshevtsov.deduplication.feature.storage.data.FileStorage
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.File

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FileStorageTest {

    private lateinit var fileStorage: FileStorage

    @BeforeEach
    fun onSetup() {
        fileStorage = FileStorage(appConfig = AppConfig(chunkSize = 4, storageFileName = STORAGE_FILE_NAME))
    }

    @AfterEach
    fun onTeardown() {
        File(STORAGE_FILE_NAME).delete()
    }

    @Test
    fun `when writing - everything okay`() {
        fileStorage.saveChunkByValue(chunk = Chunk(value = "lol".toByteArray().toList()))

        val expected = File(EXPECTED_CHUNK_FILE_PATH).readLines()
        val actual = File(STORAGE_FILE_NAME).readLines()
        actual shouldBe expected
    }

    private companion object {
        private val EXPECTED_CHUNK_FILE_PATH =
            DeduplicationAppTest::class.java.getResource("file_storage_expected_chunk.txt").path
        private const val STORAGE_FILE_NAME = "test_storage.txt"
    }

}