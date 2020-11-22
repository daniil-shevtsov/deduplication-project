package com.daniilshevtsov.deduplication.feature.consoleparsing

sealed class ConsoleArguments {
    data class Store(val sourceFileName: String) : ConsoleArguments()

    data class Read(val outputFileName: String) : ConsoleArguments()
}