package com.daniilshevtsov.deduplication

import com.daniilshevtsov.deduplication.feature.HandleChunkUseCase
import com.daniilshevtsov.deduplication.feature.consoleparsing.ParseConsoleArgumentsUseCase
import com.daniilshevtsov.deduplication.feature.input.domain.PrepareInputUseCase
import com.daniilshevtsov.deduplication.feature.input.domain.SplitToChunksUseCase
import com.daniilshevtsov.deduplication.feature.output.PrepareOutputUseCase
import com.daniilshevtsov.deduplication.feature.storage.domain.GetResultingChunkUseCase
import com.daniilshevtsov.deduplication.feature.storage.domain.GetStorageAsSequenceUseCase
import mu.KLogger
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection
import javax.inject.Inject

class Deduplicator @Inject constructor(
    private val parseConsoleArguments: ParseConsoleArgumentsUseCase,
    private val prepareInputStream: PrepareInputUseCase,
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
            println(exception.message)
            return
        }

        val db = Database.connect(
            url = "jdbc:sqlite:./data.db",
            driver = "org.sqlite.JDBC"
        )
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE

        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(ReferenceTable)

            val id = ReferenceTable.insert {
                it[fullName] = "kek"
                it[lastName] = "kekovich"
            } get ReferenceTable.id
            ReferenceTable.insert {
                it[fullName] = "lol"
                it[lastName] = "lolovich"
            }
        }

        readAndStore(sourceFileName = parsedArguments.sourceFileName)

        loadFromStorageAndWrite(outputFileName = parsedArguments.outputFileName)
    }

    private fun readAndStore(sourceFileName: String) {
        logger.debug { "read from $sourceFileName" }
        prepareInputStream(fileName = sourceFileName).run {
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