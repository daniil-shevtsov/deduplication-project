package com.daniilshevtsov.deduplication.feature.indextable.data

interface DataStoreApi {
    fun findReferenceByHash(hash: Int): ReferenceEntity?

    fun getReference(id: Int): ReferenceEntity?

    suspend fun saveReference(referenceEntity: ReferenceEntity)
}