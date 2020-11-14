package com.daniilshevtsov.deduplication.feature.consoleparsing

import javax.inject.Inject

class ParseConsoleArgumentsUseCase @Inject constructor() {

    operator fun invoke(rawArguments: Array<String>): ConsoleArguments {
        require(rawArguments.size == ARGUMENTS_COUNT) { "Two console arguments required: input and output file names" }

        return ConsoleArguments(
            sourceFileName = rawArguments.first(),
            outputFileName = rawArguments[1]
        )
    }

    private companion object {
        const val ARGUMENTS_COUNT = 2
    }
}