package com.daniilshevtsov.deduplication

import com.daniilshevtsov.deduplication.feature.HandleChunkUseCase
import com.daniilshevtsov.deduplication.feature.consoleparsing.ConsoleArguments
import com.daniilshevtsov.deduplication.feature.consoleparsing.ParseConsoleArgumentsUseCase
import com.daniilshevtsov.deduplication.feature.indextable.domain.SaveReferencesUseCase
import com.daniilshevtsov.deduplication.feature.input.domain.PrepareInputUseCase
import com.daniilshevtsov.deduplication.feature.input.domain.SplitToChunksUseCase
import com.daniilshevtsov.deduplication.feature.output.PrepareOutputUseCase
import com.daniilshevtsov.deduplication.feature.storage.domain.Counter
import com.daniilshevtsov.deduplication.feature.storage.domain.GetResultingChunkUseCase
import com.daniilshevtsov.deduplication.feature.storage.domain.GetStorageAsSequenceUseCase
import com.daniilshevtsov.deduplication.feature.storage.domain.SetCurrentPageIdUseCase
import mu.KLogger
import java.io.File
import javax.inject.Inject

class Deduplicator @Inject constructor(
    private val parseConsoleArguments: ParseConsoleArgumentsUseCase,
    private val prepareInputStream: PrepareInputUseCase,
    private val setCurrentPageIdUseCase: SetCurrentPageIdUseCase,
    private val prepareOutputStream: PrepareOutputUseCase,
    private val handleChunk: HandleChunkUseCase,
    private val saveReferences: SaveReferencesUseCase,
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
            is ConsoleArguments.Read -> loadFromStorageAndWrite(
                sourceFileName = parsedArguments.sourceFileName,
                outputFileName = parsedArguments.outputFileName
            )
            is ConsoleArguments.Clean -> clean()
        }
    }

    private fun readAndStore(sourceFileName: String) {
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
        saveReferences()
    }

    private fun loadFromStorageAndWrite(
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
        println("number of errors: ${Counter.count}")
    }

    private fun clean() {
        File("data.db").delete()

        //TODO: Use dynamic storage name
        File("storage").deleteRecursively()

        //TODO: store source and output in special directories
        File(".").listFiles()
            ?.filter { it.name.endsWith(".txt") }
            ?.forEach { it.delete() }
    }


}