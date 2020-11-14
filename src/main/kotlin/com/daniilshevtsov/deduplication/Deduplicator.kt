package com.daniilshevtsov.deduplication

import com.daniilshevtsov.deduplication.feature.consoleparsing.ParseConsoleArgumentsUseCase
import com.daniilshevtsov.deduplication.feature.indextable.domain.CalculateHashForChunkUseCase
import com.daniilshevtsov.deduplication.feature.indextable.domain.CheckChunkDuplicatedUseCase
import com.daniilshevtsov.deduplication.feature.indextable.domain.LoadReferenceUseCase
import com.daniilshevtsov.deduplication.feature.indextable.domain.SaveReferenceUseCase
import com.daniilshevtsov.deduplication.feature.input.PrepareInputUseCase
import com.daniilshevtsov.deduplication.feature.input.ReadSegmentsUseCase
import com.daniilshevtsov.deduplication.feature.output.PrepareOutputUseCase
import com.daniilshevtsov.deduplication.feature.storage.domain.GetResultingChunkUseCase
import com.daniilshevtsov.deduplication.feature.storage.domain.GetStorageAsSequenceUseCase
import com.daniilshevtsov.deduplication.feature.storage.domain.SaveChunkToStorageUseCase
import com.daniilshevtsov.deduplication.feature.storage.domain.SaveReferenceToStorageUseCase
import mu.KLogger
import javax.inject.Inject

class Deduplicator @Inject constructor(
    private val parseConsoleArguments: ParseConsoleArgumentsUseCase,
    private val prepareInputStream: PrepareInputUseCase,
    private val prepareOutputStream: PrepareOutputUseCase,
    private val calculateHashForChunk: CalculateHashForChunkUseCase,
    private val checkChunkDuplicated: CheckChunkDuplicatedUseCase,
    private val loadReference: LoadReferenceUseCase,
    private val saveReference: SaveReferenceUseCase,
    private val saveReferenceToStorage: SaveReferenceToStorageUseCase,
    private val saveChunkToStorage: SaveChunkToStorageUseCase,
    private val getStorageAsSequence: GetStorageAsSequenceUseCase,
    private val getResultingChunk: GetResultingChunkUseCase,
    private val readSegments: ReadSegmentsUseCase,
    private val logger: KLogger
) {
    fun run(args: Array<String>) {
        val parsedArguments = try {
            parseConsoleArguments(args)
        } catch (exception: Exception) {
            println(exception.message)
            return
        }

        readAndStore(sourceFileName = parsedArguments.sourceFileName)

        loadFromStorageAndWrite(outputFileName = parsedArguments.outputFileName)
    }

    private fun readAndStore(sourceFileName: String) {
        logger.debug { "read from $sourceFileName" }
        prepareInputStream(fileName = sourceFileName).run {
            val chunks = readSegments(inputStream = this)
            chunks.forEach { chunk ->
                val key = calculateHashForChunk(chunk = chunk)

                if (checkChunkDuplicated(key = key)) {
                    val reference = loadReference(key = key)
                    if (reference != null) {
                        saveReferenceToStorage(reference = reference)
                    }

                } else {
                    val reference = saveChunkToStorage(chunk = chunk)
                    saveReference(key = key, reference = reference)
                }
            }
        }
    }

    private fun loadFromStorageAndWrite(outputFileName: String) {
        logger.debug { "write to $outputFileName" }
        val outputStream = prepareOutputStream(fileName = outputFileName)
        outputStream.run {
            getStorageAsSequence().toList()
                .forEach { savedData ->
                    logger.debug { "write $savedData" }
                    val chunk = getResultingChunk(savedData = savedData)
                    outputStream.write(chunk.value.toByteArray())
                }
            flush()
        }
    }


}