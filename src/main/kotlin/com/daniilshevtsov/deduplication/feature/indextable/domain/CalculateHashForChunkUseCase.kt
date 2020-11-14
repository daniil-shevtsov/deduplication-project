package com.daniilshevtsov.deduplication.feature.indextable.domain

import com.daniilshevtsov.deduplication.feature.core.Chunk
import javax.inject.Inject

class CalculateHashForChunkUseCase @Inject constructor() {
    operator fun invoke(chunk: Chunk): Int = chunk.hashCode()
}