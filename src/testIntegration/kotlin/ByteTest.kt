import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.File
import java.io.RandomAccessFile

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ByteTest {
    @AfterEach
    fun onTeardown() {
        File(STORAGE_FILE_NAME).delete()
    }

    @Test
    fun `bytearray to string`() {
        val text = "lol kek\r\ncheburek"
        val textByteArray = text.toByteArray(Charsets.UTF_8)
        val utfText = textByteArray.toString(Charsets.UTF_8)
        val finalText = text.replace('˪', 'l')
    }

    @Test
    fun `kek`() {
        val file = RandomAccessFile(STORAGE_FILE_NAME, "rw")
        with(file) {
            val text = "lll"
            val finalText = text.replace('l', '˪')
            write(finalText.toByteArray())
        }
        file.close()

        val strings = File(STORAGE_FILE_NAME).readLines()

        val inputFile = RandomAccessFile(STORAGE_FILE_NAME, "r")
        val text = with(inputFile) {
            val text = readLine().toByteArray(Charsets.ISO_8859_1).toString(Charsets.UTF_8)
            val finalText = text.replace('˪', 'l')
            return@with finalText
        }
        inputFile.close()

        val outputFile = RandomAccessFile(STORAGE_FILE_NAME + "o", "rw")
        with(outputFile) {
            write(text.toByteArray())
        }
        file.close()
    }

    private companion object {
        const val STORAGE_FILE_NAME = "kekk.txt"
    }
}