package com.daniilshevtsov.deduplication.feature.indextable.data

import com.daniilshevtsov.deduplication.di.scope.ApplicationScope
import com.daniilshevtsov.deduplication.feature.core.Reference
import com.daniilshevtsov.deduplication.feature.indextable.domain.IndexTableRepository
import javax.inject.Inject

@ApplicationScope
class IndexTableRepositoryImpl @Inject constructor(

) : IndexTableRepository {

    private val indexTable = mutableMapOf<Int, Reference>()

    override fun checkContains(key: Int): Boolean {
        return indexTable.containsKey(key)
    }

    override fun save(key: Int, reference: Reference) {
        indexTable[key] = reference
    }

    override fun get(key: Int): Reference? {
        return indexTable[key]
    }
}