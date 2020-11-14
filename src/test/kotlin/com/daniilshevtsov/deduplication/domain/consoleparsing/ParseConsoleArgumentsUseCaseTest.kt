package com.daniilshevtsov.deduplication.domain.consoleparsing

import com.daniilshevtsov.deduplication.feature.consoleparsing.ConsoleArguments
import com.daniilshevtsov.deduplication.feature.consoleparsing.ParseConsoleArgumentsUseCase
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ParseConsoleArgumentsUseCaseTest {
    private lateinit var parseConsoleArguments: ParseConsoleArgumentsUseCase

    @BeforeEach
    fun onSetup() {
        parseConsoleArguments = ParseConsoleArgumentsUseCase()
    }

    @Test
    fun `when invalid number of arguments - then throws`() {
        val rawArguments = Array(REQUIRED_NUMBER_OF_ARGUMENTS + 1) { "lol" }
        shouldThrow<IllegalArgumentException> { parseConsoleArguments(rawArguments = rawArguments) }
    }

    @Test
    fun `when valid number of arguments - then parses correctly`() {
        val expected = ConsoleArguments(
            sourceFileName = "lol.txt",
            outputFileName = "kek.txt"
        )
        val rawArguments = arrayOf("lol.txt", "kek.txt")

        val actual = parseConsoleArguments(rawArguments = rawArguments)

        expected shouldBe actual
    }

    private companion object {
        const val REQUIRED_NUMBER_OF_ARGUMENTS = 2

    }

}