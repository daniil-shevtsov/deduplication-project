package com.daniilshevtsov.deduplication.feature.indextable.domain

import com.daniilshevtsov.deduplication.feature.core.Reference

interface IndexTableRepository {

    fun checkContains(key: Int): Boolean

    fun save(key: Int, reference: Reference)

    fun get(key: Int): Reference?

}