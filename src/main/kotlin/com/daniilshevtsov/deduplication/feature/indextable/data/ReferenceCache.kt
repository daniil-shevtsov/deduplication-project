package com.daniilshevtsov.deduplication.feature.indextable.data

import com.daniilshevtsov.deduplication.di.scope.ApplicationScope
import com.daniilshevtsov.deduplication.feature.core.Reference
import javax.inject.Inject

@ApplicationScope
class ReferenceCache @Inject constructor() {

    private var references: MutableList<Reference> = mutableListOf()

    fun saveReference(reference: Reference) {
        references.add(reference)
    }

    fun getReferences(): List<Reference> = references

}