package com.daniilshevtsov.deduplication.feature.main

import com.daniilshevtsov.deduplication.feature.output.PrepareOutputUseCase
import com.daniilshevtsov.deduplication.feature.storage.domain.GetResultingChunkUseCase
import com.daniilshevtsov.deduplication.feature.storage.domain.GetStorageAsSequenceUseCase
import mu.KLogger
import javax.inject.Inject

class LoadFromStorageAndWriteUseCase @Inject constructor(
    private val prepareOutputStream: PrepareOutputUseCase,
    private val getStorageAsSequence: GetStorageAsSequenceUseCase,
    private val getResultingChunk: GetResultingChunkUseCase,
    private val logger: KLogger
) {
    operator fun invoke(
        sourceFileName: String,
        outputFileName: String
    ) {
        logger.debug { "write to $outputFileName" }

        prepareOutputStream(fileName = outputFileName).run {
            val list = getStorageAsSequence(pageId = sourceFileName).toList()
            val listSize = list.size
            list.forEachIndexed { index, savedData ->
                println("${String.format("%.2f", (index + 1) / (listSize.toDouble() * 0.01))} / 100")
                val chunk = getResultingChunk(savedData = savedData)
                write(chunk.value.toByteArray())
            }
            flush()
        }
    }
}