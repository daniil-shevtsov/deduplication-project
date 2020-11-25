package com.daniilshevtsov.deduplication.feature.indextable.domain

import com.daniilshevtsov.deduplication.feature.indextable.data.ReferenceCache
import javax.inject.Inject

class SaveReferencesUseCase @Inject constructor(
    private val referenceCache: ReferenceCache,
    private val indexTableRepository: IndexTableRepository
) {
    operator fun invoke() {
        indexTableRepository.saveAll(references = referenceCache.getReferences())
    }
}