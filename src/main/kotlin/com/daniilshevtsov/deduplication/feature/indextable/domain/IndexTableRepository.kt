package com.daniilshevtsov.deduplication.feature.indextable.domain

import com.daniilshevtsov.deduplication.feature.core.Reference

interface IndexTableRepository {

    fun checkContains(key: Int): Boolean

    fun saveAll(references: List<Reference>)

    fun saveReference(reference: Reference)

    fun get(key: Int): Reference?

}