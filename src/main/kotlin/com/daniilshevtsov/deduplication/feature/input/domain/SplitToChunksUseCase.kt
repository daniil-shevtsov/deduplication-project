package com.daniilshevtsov.deduplication.feature.input.domain

import com.daniilshevtsov.deduplication.feature.core.AppConfig
import com.daniilshevtsov.deduplication.feature.core.Chunk
import java.io.InputStream
import javax.inject.Inject

class SplitToChunksUseCase @Inject constructor(
    private val appConfig: AppConfig
) {

    //TODO: Find out why sequence swallows errors and just hangs tests
    operator fun invoke(inputStream: InputStream) =
        inputStream.chunkedSequence(chunkSize = appConfig.chunkSize).toList()

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