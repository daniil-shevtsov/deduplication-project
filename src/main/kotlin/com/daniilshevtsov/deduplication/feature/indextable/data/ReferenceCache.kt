package com.daniilshevtsov.deduplication.feature.indextable.data

import com.daniilshevtsov.deduplication.di.scope.ApplicationScope
import com.daniilshevtsov.deduplication.feature.core.Reference
import javax.inject.Inject

@ApplicationScope
class ReferenceCache @Inject constructor() {

    private var references: MutableMap<Int, Reference> = mutableMapOf()

    fun checkContains(hash: Int) = references.containsKey(key = hash)

    fun saveReference(reference: Reference) {
        references[reference.id] = reference
    }

    fun getReferences(): List<Reference> = references.values.toList()

}