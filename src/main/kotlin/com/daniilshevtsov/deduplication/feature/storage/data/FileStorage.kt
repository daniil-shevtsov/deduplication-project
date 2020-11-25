package com.daniilshevtsov.deduplication.feature.storage.data

import com.daniilshevtsov.deduplication.feature.core.AppConfig
import com.daniilshevtsov.deduplication.feature.core.Chunk
import com.daniilshevtsov.deduplication.feature.core.Reference
import com.daniilshevtsov.deduplication.feature.indextable.domain.CalculateHashForChunkUseCase
import java.io.File
import java.io.RandomAccessFile
import java.nio.file.Paths
import javax.inject.Inject
import kotlin.properties.Delegates

class FileStorage @Inject constructor(
    private val appConfig: AppConfig,
    private val calculateHashForChunkUseCase: CalculateHashForChunkUseCase
) : StorageApi {

    private var currentFileName: String by Delegates.observable("") { _, _, newFileName ->
        storagePath = getFullPath(fileName = newFileName)
    }

    private var storagePath = ""

    init {
        File(appConfig.storageDirectoryName).apply {
            if (!exists()) {
                mkdirs()
            }
        }
    }

    override fun setCurrentPageId(pageId: String) {
        currentFileName = pageId
    }

    override fun saveChunkByValue(chunk: Chunk): Reference {
        val file = RandomAccessFile(storagePath, "rw")
        val reference = with(file) {
            seek(length())
            write(VALUE_PREFIX.toByteArray())
            val position = length()
            val payload = chunk.value.toByteArray().toString(Charsets.UTF_8)
                .replace(LINE_BREAK, LINE_BREAK_STAND_IN)
                .replace(CARRIAGE_RETURN, CARRIAGE_RETURN_STAND_IN)

            write(payload.toByteArray())
            write("\n".toByteArray())

            Reference(
                id = calculateHashForChunkUseCase(chunk),
                pageId = Paths.get(storagePath).fileName.toString(),
                segmentPosition = position
            )
        }
        file.close()
        return reference
    }

    override fun getByReference(reference: Reference): Chunk {
        val file = RandomAccessFile(getFullPath(fileName = reference.pageId), "r")
        val chunk = with(file) {
            seek(reference.segmentPosition)
            val line = readLine().toByteArray(Charsets.ISO_8859_1).toString(Charsets.UTF_8)
            val payload = line
                .replace(LINE_BREAK_STAND_IN, LINE_BREAK)
                .replace(CARRIAGE_RETURN_STAND_IN, CARRIAGE_RETURN)
            val lineBytes = payload.toByteArray().toList()

            Chunk(value = lineBytes)
        }
        file.close()
        return chunk
    }

    override fun saveChunkByReference(reference: Reference) {
        RandomAccessFile(storagePath, "rw").apply {
            seek(length())
            write(REFERENCE_PREFIX.toByteArray())
            write(reference.id.toString().toByteArray())
            write("\n".toByteArray())

            close()
        }
    }

    override fun readAsSequence(pageId: String): Sequence<SavedData> {
        return File(getFullPath(fileName = pageId)).bufferedReader().lineSequence().map { line ->
            when {
                line.startsWith(REFERENCE_PREFIX) -> {
                    SavedData.TableReference(referenceId = line.substringAfter(REFERENCE_PREFIX))
                }
                line.startsWith(VALUE_PREFIX) -> {
                    val entry = line.substringAfter(VALUE_PREFIX)
                        .replace(LINE_BREAK_STAND_IN, LINE_BREAK)
                        .replace(CARRIAGE_RETURN_STAND_IN, CARRIAGE_RETURN)
                    SavedData.Value(chunk = Chunk(value = entry.toByteArray().toList()))
                }
                else -> throw IllegalStateException("line >$line< is not a value nor a reference")
            }
        }
    }

    private fun getFullPath(fileName: String): String {
        val filePath = Paths.get(fileName).fileName
        val storageDirectoryPath = Paths.get(appConfig.storageDirectoryName).toAbsolutePath()
        val mergedPath = storageDirectoryPath.resolve(filePath)
        return mergedPath.normalize().toString()
    }

    private companion object {
        const val VALUE_PREFIX = "v"
        const val REFERENCE_PREFIX = "r"

        const val LINE_BREAK = '\n'
        const val CARRIAGE_RETURN = '\r'
        const val LINE_BREAK_STAND_IN = '˫'
        const val CARRIAGE_RETURN_STAND_IN = '˪'
    }
}