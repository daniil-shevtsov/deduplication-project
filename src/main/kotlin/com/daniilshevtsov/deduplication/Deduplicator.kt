package com.daniilshevtsov.deduplication

import com.daniilshevtsov.deduplication.feature.HandleChunkUseCase
import com.daniilshevtsov.deduplication.feature.consoleparsing.ConsoleArguments
import com.daniilshevtsov.deduplication.feature.consoleparsing.ParseConsoleArgumentsUseCase
import com.daniilshevtsov.deduplication.feature.input.domain.PrepareInputUseCase
import com.daniilshevtsov.deduplication.feature.input.domain.SplitToChunksUseCase
import com.daniilshevtsov.deduplication.feature.output.PrepareOutputUseCase
import com.daniilshevtsov.deduplication.feature.storage.domain.GetResultingChunkUseCase
import com.daniilshevtsov.deduplication.feature.storage.domain.GetStorageAsSequenceUseCase
import com.daniilshevtsov.deduplication.feature.storage.domain.SetCurrentPageIdUseCase
import mu.KLogger
import javax.inject.Inject

class Deduplicator @Inject constructor(
    private val parseConsoleArguments: ParseConsoleArgumentsUseCase,
    private val prepareInputStream: PrepareInputUseCase,
    private val setCurrentPageIdUseCase: SetCurrentPageIdUseCase,
    private val prepareOutputStream: PrepareOutputUseCase,
    private val handleChunk: HandleChunkUseCase,
    private val getStorageAsSequence: GetStorageAsSequenceUseCase,
    private val getResultingChunk: GetResultingChunkUseCase,
    private val splitToChunks: SplitToChunksUseCase,
    private val logger: KLogger
) {
    fun run(args: Array<String>) {
        val parsedArguments = try {
            parseConsoleArguments(args)
        } catch (exception: Exception) {
            logger.error { "FATAL ERROR: ${exception.message}" }
            return
        }

        when (parsedArguments) {
            is ConsoleArguments.Store -> readAndStore(sourceFileName = parsedArguments.sourceFileName)
            is ConsoleArguments.Read -> loadFromStorageAndWrite(outputFileName = parsedArguments.outputFileName)
        }
    }

    private fun readAndStore(sourceFileName: String) {
        logger.debug { "read from $sourceFileName" }
        prepareInputStream(fileName = sourceFileName).run {
            setCurrentPageIdUseCase(pageId = sourceFileName)
            splitToChunks(inputStream = this)
                .forEach(handleChunk::invoke)
        }
    }

    private fun loadFromStorageAndWrite(outputFileName: String) {
        logger.debug { "write to $outputFileName" }
        prepareOutputStream(fileName = outputFileName).run {
            getStorageAsSequence().toList()
                .forEach { savedData ->
                    val chunk = getResultingChunk(savedData = savedData)
                    write(chunk.value.toByteArray())
                }
            flush()
        }
    }


}