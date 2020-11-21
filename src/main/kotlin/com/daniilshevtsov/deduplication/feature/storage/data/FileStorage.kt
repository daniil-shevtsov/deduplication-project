package com.daniilshevtsov.deduplication.feature.storage.data

import com.daniilshevtsov.deduplication.feature.core.AppConfig
import com.daniilshevtsov.deduplication.feature.core.Chunk
import com.daniilshevtsov.deduplication.feature.core.Reference
import java.io.File
import java.io.RandomAccessFile
import javax.inject.Inject

class FileStorage @Inject constructor(
    private val appConfig: AppConfig
) : StorageApi {

    //TODO: Use different file names for different files
    private val storageFileName = appConfig.storageFileName

    override fun saveChunkByValue(chunk: Chunk): Reference {
        val file = RandomAccessFile(storageFileName, "rw")
        val reference = with(file) {
            seek(length())
            write("value:".toByteArray())
            write(chunk.value.toByteArray())
            write("\n".toByteArray())

            Reference(
                id = chunk.hashCode(),
                pageId = storageFileName,
                segmentPosition = length()
            )
        }
        file.close()
        return reference
    }

    override fun getByReference(reference: Reference): Chunk {
        val file = RandomAccessFile(reference.pageId, "r")
        val chunk = with(file) {
            seek(reference.segmentPosition)
            val line = readLine()
            val payload = line.substringAfter("reference:")
            val lineBytes = payload.toByteArray().toList()

            Chunk(value = lineBytes)
        }
        file.close()
        return chunk
    }

    override fun saveChunkByReference(reference: Reference) {
        RandomAccessFile(storageFileName, "rw").apply {
            seek(length())
            write("reference:".toByteArray())
            write(reference.id.toString().toByteArray())
            write("\n".toByteArray())

            close()
        }
    }

    override fun readAsSequence(): Sequence<String> {
        return File(storageFileName).bufferedReader().lineSequence()
    }
}