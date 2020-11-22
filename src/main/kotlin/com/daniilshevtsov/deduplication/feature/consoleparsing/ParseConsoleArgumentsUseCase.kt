package com.daniilshevtsov.deduplication.feature.consoleparsing

import javax.inject.Inject

class ParseConsoleArgumentsUseCase @Inject constructor() {

    operator fun invoke(rawArguments: Array<String>): ConsoleArguments {
        require(rawArguments.isNotEmpty()) { "Requires one key: $STORE_KEY_WORD for storing, $READ_KEY_WORD for reading or $CLEAN_KEY_WORD to clean" }

        return when (val key = rawArguments.first()) {
            in STORE_KEY_WORD -> parseStoreArguments(rawArguments.drop(1))
            in READ_KEY_WORD -> parseReadArguments(rawArguments.drop(1))
            in CLEAN_KEY_WORD -> parseCleanArguments()
            else -> throw IllegalArgumentException("unknown key argument: $key")
        }
    }

    private fun parseStoreArguments(rawArguments: List<String>): ConsoleArguments.Store {
        require(rawArguments.size == STORE_ARGUMENTS_COUNT) { "Storing requires one argument: input file name" }

        return ConsoleArguments.Store(
            sourceFileName = rawArguments.first()
        )
    }

    private fun parseReadArguments(rawArguments: List<String>): ConsoleArguments.Read {
        require(rawArguments.size == READ_ARGUMENTS_COUNT) { "Reading requires two arguments: source file name and output file name" }

        return ConsoleArguments.Read(
            sourceFileName = rawArguments.first(),
            outputFileName = rawArguments[1]
        )
    }

    private fun parseCleanArguments(): ConsoleArguments = ConsoleArguments.Clean

    private companion object {
        val CLEAN_KEY_WORD = listOf("-c", "--clean")
        val STORE_KEY_WORD = listOf("-s", "--store")
        val READ_KEY_WORD = listOf("-r", "--read")

        const val STORE_ARGUMENTS_COUNT = 1
        const val READ_ARGUMENTS_COUNT = 2
    }
}