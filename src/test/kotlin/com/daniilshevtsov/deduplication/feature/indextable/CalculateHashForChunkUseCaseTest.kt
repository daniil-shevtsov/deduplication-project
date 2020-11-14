package com.daniilshevtsov.deduplication.feature.indextable

import com.daniilshevtsov.deduplication.feature.core.Chunk
import com.daniilshevtsov.deduplication.feature.indextable.domain.CalculateHashForChunkUseCase
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class CalculateHashForChunkUseCaseTest {
    private lateinit var calculateHashForChunkUseCase: CalculateHashForChunkUseCase

    @BeforeEach
    fun onSetup() {
        calculateHashForChunkUseCase =
            CalculateHashForChunkUseCase()
    }

    @Test
    fun `when data is the same - then hash is the same`() {
        val first = Chunk(value = "kek".toByteArray().toList())
        val second = Chunk(value = "kek".toByteArray().toList())

        val firstHash = calculateHashForChunkUseCase(chunk = first)
        val secondHash = calculateHashForChunkUseCase(chunk = second)

        firstHash shouldBe secondHash
    }

    @Test
    fun `when data is different - then hash is different`() {
        val first = Chunk(value = "lol".toByteArray().toList())
        val second = Chunk(value = "kek".toByteArray().toList())

        val firstHash = calculateHashForChunkUseCase(chunk = first)
        val secondHash = calculateHashForChunkUseCase(chunk = second)

        firstHash shouldNotBe secondHash
    }

}