package com.daniilshevtsov.deduplication.feature.indextable.data

import com.daniilshevtsov.deduplication.di.scope.ApplicationScope
import com.daniilshevtsov.deduplication.feature.core.Reference
import com.daniilshevtsov.deduplication.feature.indextable.domain.IndexTableRepository
import javax.inject.Inject

@ApplicationScope
class IndexTableRepositoryImpl @Inject constructor(
    private val referenceCache: ReferenceCache,
    private val dataStoreApi: DataStoreApi
) : IndexTableRepository {

    override fun checkContains(key: String): Boolean =
        dataStoreApi.findReferenceByHash(hash = key) != null

    //TODO: Fill segment count correctly
    override fun saveAll(references: List<Reference>) {
        dataStoreApi.saveReferences(references = references.map { reference -> reference.toEntity() })
    }

    override fun saveReference(reference: Reference) {
        dataStoreApi.saveReference(reference = reference.toEntity())
    }

    override fun get(key: String): Reference? {
        return dataStoreApi.findReferenceByHash(hash = key)?.let { referenceEntity ->
            Reference(
                id = referenceEntity.segmentHash,
                pageId = referenceEntity.fileName,
                segmentPosition = referenceEntity.segmentPosition
            )
        }
    }

    private fun Reference.toEntity() = ReferenceEntity(
        segmentHash = id,
        fileName = pageId,
        segmentPosition = segmentPosition,
        segmentCount = 0
    )
}