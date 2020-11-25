package com.daniilshevtsov.deduplication.feature.main

import java.io.File
import javax.inject.Inject

class CountErrorsUseCase @Inject constructor() {
    operator fun invoke(
        sourceFileName: String,
        outputFileName: String
    ) {
        val sourceBytes = File(sourceFileName).readBytes()
        val outputBytes = File(outputFileName).readBytes()

        val differentCount = sourceBytes.zip(outputBytes)
            .filter { it.first != it.second }
            .count()

        val percent = differentCount / (sourceBytes.size * 0.01)
        println("error: ${String.format("%.2f", percent)} %")
    }
}