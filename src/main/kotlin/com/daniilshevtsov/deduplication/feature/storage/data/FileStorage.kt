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
        RandomAccessFile(storageFileName, "rw").apply {
            //seek(length())
            write("value:".toByteArray())
            write(chunk.value.toByteArray())
            return Reference(
                id = chunk.hashCode(),
                pageId = storageFileName,
                segmentPosition = length()
            )
        }
    }

    override fun getByReference(reference: Reference): Chunk {
        RandomAccessFile(reference.pageId, "r").apply {
            seek(reference.segmentPosition)
            val line = readLine()
            val payload = line.substringAfter("reference:")
            val lineBytes = payload.toByteArray().toList()
            return Chunk(value = lineBytes)
        }
    }

    override fun saveChunkByReference(reference: Reference) {
        RandomAccessFile(storageFileName, "rw").apply {
            seek(length())
            writeUTF("reference:")
            write(reference.id)
        }
    }

    override fun readAsSequence(): Sequence<String> {
        return File(storageFileName).bufferedReader().lineSequence()
    }
}