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
    fun `when no key - then throws`() {
        val rawArguments = arrayOf<String>()
        shouldThrow<IllegalArgumentException> { parseConsoleArguments(rawArguments = rawArguments) }
    }

    @Test
    fun `when invalid number of arguments for storing - then throws`() {
        val rawArguments = arrayOf("--store", "a", "b")
        shouldThrow<IllegalArgumentException> { parseConsoleArguments(rawArguments = rawArguments) }
    }

    @Test
    fun `when invalid number of arguments for reading - then throws`() {
        val rawArguments = arrayOf("--read", "a", "b", "c")
        shouldThrow<IllegalArgumentException> { parseConsoleArguments(rawArguments = rawArguments) }
    }

    @Test
    fun `when invalid key - then throws`() {
        val rawArguments = arrayOf("x", "a")
        shouldThrow<IllegalArgumentException> { parseConsoleArguments(rawArguments = rawArguments) }
    }

    @Test
    fun `when storing with full key - then parses correctly`() {
        val expected = ConsoleArguments.Store(
            sourceFileName = "lol.txt"
        )
        val rawArguments = arrayOf("--store", "lol.txt")

        val actual = parseConsoleArguments(rawArguments = rawArguments)

        expected shouldBe actual
    }

    @Test
    fun `when storing with short key - then parses correctly`() {
        val expected = ConsoleArguments.Store(
            sourceFileName = "lol.txt"
        )
        val rawArguments = arrayOf("-s", "lol.txt")

        val actual = parseConsoleArguments(rawArguments = rawArguments)

        expected shouldBe actual
    }

    @Test
    fun `when reading with full key - then parses correctly`() {
        val expected = ConsoleArguments.Read(
            sourceFileName = "kek.txt",
            outputFileName = "lol.txt"
        )
        val rawArguments = arrayOf("--read", "kek.txt", "lol.txt")

        val actual = parseConsoleArguments(rawArguments = rawArguments)

        expected shouldBe actual
    }

    @Test
    fun `when reading with short key - then parses correctly`() {
        val expected = ConsoleArguments.Read(
            sourceFileName = "kek.txt",
            outputFileName = "lol.txt"
        )
        val rawArguments = arrayOf("-r", "kek.txt", "lol.txt")

        val actual = parseConsoleArguments(rawArguments = rawArguments)

        expected shouldBe actual
    }

    @Test
    fun `when clean - then parses correctly`() {
        val expected = ConsoleArguments.Clean

        val rawArguments = arrayOf("-c")

        val actual = parseConsoleArguments(rawArguments = rawArguments)

        expected shouldBe actual
    }

}