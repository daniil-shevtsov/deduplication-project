package com.daniilshevtsov.deduplication.feature.storage.data

import com.daniilshevtsov.deduplication.feature.core.Chunk
import com.daniilshevtsov.deduplication.feature.core.Reference

interface StorageApi {

    fun saveChunkByValue(chunk: Chunk): Reference

    fun getByReference(reference: Reference): Chunk

    fun saveChunkByReference(reference: Reference)

    fun readAsSequence(): Sequence<String>

}