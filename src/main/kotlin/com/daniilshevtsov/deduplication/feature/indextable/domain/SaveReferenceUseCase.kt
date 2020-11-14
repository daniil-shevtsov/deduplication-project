package com.daniilshevtsov.deduplication.feature.indextable.domain

import com.daniilshevtsov.deduplication.feature.core.Reference
import javax.inject.Inject

class SaveReferenceUseCase @Inject constructor(
    private val indexTableRepository: IndexTableRepository
) {
    operator fun invoke(key: Int, reference: Reference) {
        indexTableRepository.save(key = key, reference = reference)
    }
}