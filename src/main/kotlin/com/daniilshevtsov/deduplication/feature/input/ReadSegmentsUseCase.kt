package com.daniilshevtsov.deduplication.feature.input

import com.daniilshevtsov.deduplication.feature.core.AppConfig
import com.daniilshevtsov.deduplication.feature.core.Chunk
import java.io.InputStream
import javax.inject.Inject

class ReadSegmentsUseCase @Inject constructor(
    private val appConfig: AppConfig
) {

    operator fun invoke(inputStream: InputStream): Sequence<Chunk> =
        inputStream.chunkedSequence(chunkSize = appConfig.chunkSize)

    private fun InputStream.chunkedSequence(chunkSize: Int): Sequence<Chunk> {
        val buffer = ByteArray(chunkSize)

        return generateSequence {
            val currentChunk = read(buffer)
            if (currentChunk >= 0) {
                Chunk(value = buffer.copyOf(currentChunk).toList())
            } else {
                close()
                null
            }
        }
    }

}