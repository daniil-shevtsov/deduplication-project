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

    override fun checkContains(key: Int): Boolean = referenceCache.checkContains(key)//dataStoreApi.findReferenceByHash(hash = key) != null

    //TODO: Fill segment count correctly
    override fun save(references: List<Reference>) {
        dataStoreApi.saveReferences(references = references.map { reference ->
            ReferenceEntity(
                segmentHash = reference.id,
                fileName = reference.pageId,
                segmentPosition = reference.segmentPosition,
                segmentCount = 0
            )
        })
    }

    override fun get(key: Int): Reference? {
        return dataStoreApi.findReferenceByHash(hash = key)?.let { referenceEntity ->
            Reference(
                id = referenceEntity.segmentHash,
                pageId = referenceEntity.fileName,
                segmentPosition = referenceEntity.segmentPosition
            )
        }
    }
}