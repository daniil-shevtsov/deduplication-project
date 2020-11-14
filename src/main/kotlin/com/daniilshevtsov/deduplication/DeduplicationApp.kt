package com.daniilshevtsov.deduplication

import com.daniilshevtsov.deduplication.di.AppComponent
import com.daniilshevtsov.deduplication.di.DaggerAppComponent
import com.daniilshevtsov.deduplication.domain.ParseConsoleArgumentsUseCase
import com.daniilshevtsov.deduplication.domain.ReadSegmentsUseCase
import mu.KotlinLogging
import java.io.File
import javax.inject.Inject

class DeduplicationApp {
    private val logger = KotlinLogging.logger {}

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory()
            .create(
                appConfig = AppConfig(chunkSize = 4)
            )
    }

    @Inject
    lateinit var parseConsoleArguments: ParseConsoleArgumentsUseCase

    @Inject
    lateinit var readSegments: ReadSegmentsUseCase

    sealed class SavedData {
        data class Chunk(val value: List<Byte>) : SavedData()
        data class Reference(val value: Int) : SavedData()
    }

    fun start(args: Array<String>) {
        appComponent.inject(this)

        val parsedArguments = try {
            parseConsoleArguments(args)
        } catch (exception: Exception) {
            println(exception.message)
            return
        }

        val storage = mutableListOf<SavedData>()
        val indexTable = mutableMapOf<Int, Int>()
        logger.debug { "read from ${parsedArguments.sourceFileName}" }


        File(parsedArguments.sourceFileName).inputStream().buffered().run {
            readSegments(inputStream = this)
                .forEach { chunk ->
                    val key = chunk.hashCode()

                    if (indexTable.containsKey(key)) {
                        val index = indexTable[key]!!
                        storage.add(SavedData.Reference(value = index))
                        logger.debug { "reuse $chunk with $key at $index" }
                    } else {
                        val savedValue = SavedData.Chunk(value = chunk)
                        storage.add(savedValue)
                        val index = storage.indexOf(savedValue)
                        logger.debug { "save $chunk with $key to $index" }
                        indexTable[key] = index
                    }
                }
        }

        logger.debug { "write to ${parsedArguments.outputFileName}" }
        File(parsedArguments.outputFileName).outputStream().buffered().run {
            storage.forEach { savedData ->
                when (savedData) {
                    is SavedData.Chunk -> {
                        val value = savedData.value.toByteArray()
                        logger.debug { "write chunk $value" }
                        write(value)
                    }
                    is SavedData.Reference -> {
                        val index = savedData.value
                        val value = (storage[index] as SavedData.Chunk).value.toByteArray()
                        logger.debug { "write chunk $value from index $index" }
                        write(value)
                    }
                }
            }
            flush()
        }
    }
}