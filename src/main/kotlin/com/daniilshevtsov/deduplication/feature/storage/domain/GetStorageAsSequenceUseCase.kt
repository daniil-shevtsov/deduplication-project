package com.daniilshevtsov.deduplication.feature.storage.domain

import javax.inject.Inject

class GetStorageAsSequenceUseCase @Inject constructor(
    private val storageRepository: StorageRepository
) {
    operator fun invoke(pageId:String) = storageRepository.readAsSequence(pageId)
}