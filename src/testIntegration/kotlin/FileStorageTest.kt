import com.daniilshevtsov.deduplication.feature.core.AppConfig
import com.daniilshevtsov.deduplication.feature.core.Chunk
import com.daniilshevtsov.deduplication.feature.core.Reference
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
    fun `when writing chunk - it is written correctly`() {
        fileStorage.saveChunkByValue(chunk = Chunk(value = "lol".toByteArray().toList()))

        val expected = File(EXPECTED_CHUNK_FILE_PATH).readLines()
        val actual = File(STORAGE_FILE_NAME).readLines()
        actual shouldBe expected
    }

    @Test
    fun `when writing reference - it is written correctly`() {
        fileStorage.saveChunkByReference(reference = Reference(id = 5, pageId = "kek.txt", segmentPosition = 0))

        val expected = File(EXPECTED_REFERENCE_FILE_PATH).readLines()
        val actual = File(STORAGE_FILE_NAME).readLines()
        actual shouldBe expected
    }

    @Test
    fun `when writing value and reference - they are written correctly`() {
        fileStorage.saveChunkByValue(chunk = Chunk(value = "lol".toByteArray().toList()))
        fileStorage.saveChunkByReference(reference = Reference(id = 5, pageId = "kek.txt", segmentPosition = 0))
        fileStorage.saveChunkByValue(chunk = Chunk(value = "kek".toByteArray().toList()))

        val expected = File(EXPECTED_VALUE_AND_REFERENCE_FILE_PATH).readLines()
        val actual = File(STORAGE_FILE_NAME).readLines()
        actual shouldBe expected
    }

    private companion object {
        private val EXPECTED_CHUNK_FILE_PATH =
            FileStorageTest::class.java.getResource("file_storage_expected_chunk.txt").path
        private val EXPECTED_REFERENCE_FILE_PATH =
            FileStorageTest::class.java.getResource("file_storage_expected_reference.txt").path
        private val EXPECTED_VALUE_AND_REFERENCE_FILE_PATH =
            FileStorageTest::class.java.getResource("file_storage_expected_value_and_reference.txt").path
        private const val STORAGE_FILE_NAME = "test_storage.txt"
    }

}