package com.daniilshevtsov.deduplication.feature.indextable.domain

import com.daniilshevtsov.deduplication.feature.core.Reference
import com.daniilshevtsov.deduplication.feature.indextable.data.ReferenceCache
import javax.inject.Inject

class SaveReferenceToCacheUseCase @Inject constructor(
    private val referenceCache: ReferenceCache
) {
    operator fun invoke(reference: Reference) {
        referenceCache.saveReference(reference = reference)
    }
}