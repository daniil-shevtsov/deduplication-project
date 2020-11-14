package com.daniilshevtsov.deduplication.feature.storage.domain

import com.daniilshevtsov.deduplication.feature.core.Reference
import javax.inject.Inject

class SaveReferenceToStorageUseCase @Inject constructor(
    private val storageRepository: StorageRepository
) {
    operator fun invoke(reference: Reference) {
        storageRepository.saveReference(reference = reference)
    }
}