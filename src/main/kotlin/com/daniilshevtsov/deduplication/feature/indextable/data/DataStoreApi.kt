package com.daniilshevtsov.deduplication.feature.indextable.data

interface DataStoreApi {
    fun saveReferences(references: List<ReferenceEntity>)

    fun saveReference(reference: ReferenceEntity)

    fun findReferenceByHash(hash: Int): ReferenceEntity?

    fun getReference(id: Int): ReferenceEntity?

}