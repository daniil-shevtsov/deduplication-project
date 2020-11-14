package com.daniilshevtsov.deduplication.feature

import com.daniilshevtsov.deduplication.feature.core.Chunk
import com.daniilshevtsov.deduplication.feature.indextable.domain.CalculateHashForChunkUseCase
import com.daniilshevtsov.deduplication.feature.indextable.domain.CheckChunkDuplicatedUseCase
import com.daniilshevtsov.deduplication.feature.indextable.domain.LoadReferenceUseCase
import com.daniilshevtsov.deduplication.feature.indextable.domain.SaveReferenceUseCase
import com.daniilshevtsov.deduplication.feature.storage.domain.SaveChunkToStorageUseCase
import com.daniilshevtsov.deduplication.feature.storage.domain.SaveReferenceToStorageUseCase
import javax.inject.Inject

class HandleChunkUseCase @Inject constructor(
    private val calculateHashForChunk: CalculateHashForChunkUseCase,
    private val checkChunkDuplicated: CheckChunkDuplicatedUseCase,
    private val loadReference: LoadReferenceUseCase,
    private val saveReference: SaveReferenceUseCase,
    private val saveReferenceToStorage: SaveReferenceToStorageUseCase,
    private val saveChunkToStorage: SaveChunkToStorageUseCase
) {
    operator fun invoke(chunk: Chunk) {
        val key = calculateHashForChunk(chunk = chunk)

        if (checkChunkDuplicated(key = key)) {
            val reference = loadReference(key = key)
            if (reference != null) {
                saveReferenceToStorage(reference = reference)
            }
        } else {
            val reference = saveChunkToStorage(chunk = chunk)
            saveReference(key = key, reference = reference)
        }
    }
}