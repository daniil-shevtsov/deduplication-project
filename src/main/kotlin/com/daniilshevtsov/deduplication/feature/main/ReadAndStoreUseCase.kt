package com.daniilshevtsov.deduplication.feature.main

import com.daniilshevtsov.deduplication.feature.HandleChunkUseCase
import com.daniilshevtsov.deduplication.feature.indextable.domain.SaveReferencesUseCase
import com.daniilshevtsov.deduplication.feature.input.domain.PrepareInputUseCase
import com.daniilshevtsov.deduplication.feature.input.domain.SplitToChunksUseCase
import com.daniilshevtsov.deduplication.feature.storage.domain.SetCurrentPageIdUseCase
import mu.KLogger
import javax.inject.Inject

class ReadAndStoreUseCase @Inject constructor(
    private val prepareInputStream: PrepareInputUseCase,
    private val setCurrentPageIdUseCase: SetCurrentPageIdUseCase,
    private val handleChunk: HandleChunkUseCase,
    private val saveReferences: SaveReferencesUseCase,
    private val splitToChunks: SplitToChunksUseCase,
    private val logger: KLogger
) {

    operator fun invoke(sourceFileName: String) {
        logger.debug { "read from $sourceFileName" }

        prepareInputStream(fileName = sourceFileName).run {
            setCurrentPageIdUseCase(pageId = sourceFileName)
            val list = splitToChunks(inputStream = this)
            val listSize = list.size
            list.forEachIndexed { index, chunk ->
                println("${String.format("%.2f", (index + 1) / (listSize.toDouble() * 0.01))} / 100")
                handleChunk(chunk)
            }
        }
    }
}