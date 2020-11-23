package com.daniilshevtsov.deduplication

import com.daniilshevtsov.deduplication.feature.consoleparsing.ConsoleArguments
import com.daniilshevtsov.deduplication.feature.consoleparsing.ParseConsoleArgumentsUseCase
import com.daniilshevtsov.deduplication.feature.main.CleanFilesUseCase
import com.daniilshevtsov.deduplication.feature.main.LoadFromStorageAndWriteUseCase
import com.daniilshevtsov.deduplication.feature.main.ReadAndStoreUseCase
import mu.KLogger
import javax.inject.Inject

class Deduplicator @Inject constructor(
    private val parseConsoleArguments: ParseConsoleArgumentsUseCase,
    private val readAndStore: ReadAndStoreUseCase,
    private val loadFromStorageAndWrite: LoadFromStorageAndWriteUseCase,
    private val cleanFiles: CleanFilesUseCase,
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
            is ConsoleArguments.Clean -> cleanFiles()
        }
    }


}