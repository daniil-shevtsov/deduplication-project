package com.daniilshevtsov.deduplication.feature.indextable.data

import com.daniilshevtsov.deduplication.di.scope.ApplicationScope
import com.daniilshevtsov.deduplication.feature.core.Reference
import com.daniilshevtsov.deduplication.feature.indextable.domain.IndexTableRepository
import javax.inject.Inject

@ApplicationScope
class IndexTableRepositoryImpl @Inject constructor(
    private val dataStoreApi: DataStoreApi
) : IndexTableRepository {

    override fun checkContains(key: Int): Boolean = dataStoreApi.findReferenceByHash(hash = key) != null

    //TODO: Fill fields correctly
    override fun save(key: Int, reference: Reference) {
        dataStoreApi.saveReference(
            ReferenceEntity(
                segmentHash = key,
                fileName = reference.pageId,
                segmentPosition = reference.segmentPosition,
                segmentCount = 0
            )
        )
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