package com.daniilshevtsov.deduplication.feature.storage.domain

import com.daniilshevtsov.deduplication.feature.core.Chunk
import com.daniilshevtsov.deduplication.feature.indextable.domain.IndexTableRepository
import com.daniilshevtsov.deduplication.feature.storage.data.SavedData
import javax.inject.Inject

class GetResultingChunkUseCase @Inject constructor(
    private val storageRepository: StorageRepository,
    private val tableRepository: IndexTableRepository
) {
    operator fun invoke(savedData: SavedData): Chunk = when (savedData) {
        is SavedData.Value -> savedData.chunk
        is SavedData.TableReference -> storageRepository.getByReference(reference = tableRepository.get(key = savedData.referenceId)!!)
    }
}