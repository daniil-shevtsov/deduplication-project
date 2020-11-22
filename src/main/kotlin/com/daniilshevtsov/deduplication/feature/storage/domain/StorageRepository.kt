package com.daniilshevtsov.deduplication.feature.storage.domain

import com.daniilshevtsov.deduplication.feature.core.Chunk
import com.daniilshevtsov.deduplication.feature.core.Reference
import com.daniilshevtsov.deduplication.feature.storage.data.SavedData

interface StorageRepository {

    fun saveChunk(chunk: Chunk): Reference

    fun saveReference(reference: Reference)

    fun getByReference(reference: Reference): Chunk

    fun readAsSequence(): Sequence<SavedData>

    fun setCurrentPageId(pageId: String)

}