package com.daniilshevtsov.deduplication.feature.input.domain

import com.daniilshevtsov.deduplication.feature.core.AppConfig
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class SplitToChunksUseCaseTest {

    private lateinit var useCase: SplitToChunksUseCase

    @BeforeEach
    fun setUp() {
        useCase = SplitToChunksUseCase(
            appConfig = AppConfig(
                chunkSize = chunkSize,
                storageDirectoryName = "",
                storageFileName = ""
            )
        )
    }

    @Test
    fun `test simple chars`() {
        val originalString = "lol"
        val chunks = useCase.invoke(inputStream = originalString.byteInputStream())
        val restoredString = chunks
            .map { chunk ->
                chunk.value.toByteArray()
                    .toString(Charsets.UTF_8)
            }
            .joinToString(separator = "")

        restoredString shouldBe originalString
    }

//    @Test
//    fun `test complex chars`() {
//        val originalString = "‘‘‘"
//        val chunks = useCase.invoke(inputStream = originalString.byteInputStream())
//        val restoredString = chunks
//            .map { chunk ->
//                chunk.value.toByteArray()
//                    .toString(Charsets.UTF_8)
//            }
//            .joinToString(separator = "")
//
//        restoredString shouldBe originalString
//    }

    private companion object {
        const val chunkSize = 4
    }
}