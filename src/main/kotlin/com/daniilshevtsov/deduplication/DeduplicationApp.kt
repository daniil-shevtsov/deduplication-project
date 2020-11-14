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

    fun start(args: Array<String>) {
        appComponent.inject(this)

        val parsedArguments = try {
            parseConsoleArguments(args)
        } catch (exception: Exception) {
            println(exception.message)
            return
        }

        val storage = mutableListOf<ByteArray>()
        logger.debug { "read from ${parsedArguments.sourceFileName}" }
        File(parsedArguments.sourceFileName).inputStream().buffered().run {
            readSegments(inputStream = this).forEach {
                logger.debug { "save $it" }
                storage.add(it)
            }
        }

        logger.debug { "write to ${parsedArguments.outputFileName}" }
        File(parsedArguments.outputFileName).outputStream().buffered().run {
            storage.forEach { byteArray ->
                logger.debug { "write $byteArray" }
                write(byteArray)
            }
            flush()
        }
    }
}