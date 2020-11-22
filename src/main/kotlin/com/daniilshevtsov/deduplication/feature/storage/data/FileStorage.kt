package com.daniilshevtsov.deduplication.feature.storage.data

import com.daniilshevtsov.deduplication.feature.core.AppConfig
import com.daniilshevtsov.deduplication.feature.core.Chunk
import com.daniilshevtsov.deduplication.feature.core.Reference
import java.io.File
import java.io.RandomAccessFile
import java.nio.file.Paths
import javax.inject.Inject
import kotlin.properties.Delegates

class FileStorage @Inject constructor(
    private val appConfig: AppConfig
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
            write("value:".toByteArray())
            val payload = chunk.value.toByteArray().toString(Charsets.UTF_8)
                .replace(LINE_BREAK, LINE_BREAK_STAND_IN)
                .replace(CARRIAGE_RETURN, CARRIAGE_RETURN_STAND_IN)

            write(payload.toByteArray())
            write("\n".toByteArray())

            Reference(
                id = chunk.hashCode(),
//                pageId = storagePath,
                pageId = Paths.get(storagePath).fileName.toString(),
                segmentPosition = length()
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
            val payload = line.substringAfter("reference:")
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
            write("reference:".toByteArray())
            write(reference.id.toString().toByteArray())
            write("\n".toByteArray())

            close()
        }
    }

    override fun readAsSequence(pageId: String): Sequence<String> {
        return File(getFullPath(fileName = pageId)).bufferedReader().lineSequence().map {
            it.replace(LINE_BREAK_STAND_IN, LINE_BREAK)
                .replace(CARRIAGE_RETURN_STAND_IN, CARRIAGE_RETURN)
        }
    }

    private fun getFullPath(fileName: String): String {
        val filePath = Paths.get(fileName)
        val storageDirectoryPath = Paths.get(appConfig.storageDirectoryName)
        val mergedPath = storageDirectoryPath.resolve(filePath)
        return mergedPath.normalize().toString()
    }

    private companion object {
        const val LINE_BREAK = '\n'
        const val CARRIAGE_RETURN = '\r'
        const val LINE_BREAK_STAND_IN = '˫'
        const val CARRIAGE_RETURN_STAND_IN = '˪'
    }
}