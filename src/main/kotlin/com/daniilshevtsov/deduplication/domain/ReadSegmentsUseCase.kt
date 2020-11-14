package com.daniilshevtsov.deduplication.domain

import com.daniilshevtsov.deduplication.AppConfig
import java.io.InputStream
import javax.inject.Inject

class ReadSegmentsUseCase @Inject constructor(
    private val appConfig: AppConfig
) {

    operator fun invoke(inputStream: InputStream) = inputStream.chunkedSequence(chunk = appConfig.chunkSize)

    private fun InputStream.chunkedSequence(chunk: Int): Sequence<List<Byte>> {
        val buffer = ByteArray(chunk)

        return generateSequence {
            val currentChunk = read(buffer)
            if (currentChunk >= 0) {
                buffer.copyOf(currentChunk)
            } else {
                close()
                null
            }?.toList()
        }
    }

}