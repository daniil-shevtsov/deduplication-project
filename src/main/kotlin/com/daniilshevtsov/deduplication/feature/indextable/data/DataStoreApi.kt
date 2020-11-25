package com.daniilshevtsov.deduplication.feature.indextable.data

interface DataStoreApi {
    fun deleteEverything()

    fun saveReferences(references: List<ReferenceEntity>)

    fun saveReference(reference: ReferenceEntity)

    fun findReferenceByHash(hash: String): ReferenceEntity?

    fun getReference(id: String): ReferenceEntity?

}