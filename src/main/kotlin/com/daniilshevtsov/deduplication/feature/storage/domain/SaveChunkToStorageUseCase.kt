package com.daniilshevtsov.deduplication.feature.storage.domain

import com.daniilshevtsov.deduplication.feature.core.Chunk
import com.daniilshevtsov.deduplication.feature.core.Reference
import javax.inject.Inject

class SaveChunkToStorageUseCase @Inject constructor(
    private val storageRepository: StorageRepository
) {
    operator fun invoke(chunk: Chunk): Reference = storageRepository.saveChunk(chunk = chunk)
}