package com.daniilshevtsov.deduplication.feature.indextable.domain

import com.daniilshevtsov.deduplication.feature.core.Reference

interface IndexTableRepository {

    fun checkContains(key: String): Boolean

    fun saveAll(references: List<Reference>)

    fun saveReference(reference: Reference)

    fun get(key: String): Reference?

}