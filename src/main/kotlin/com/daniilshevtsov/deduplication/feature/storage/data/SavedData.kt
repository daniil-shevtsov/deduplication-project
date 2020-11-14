package com.daniilshevtsov.deduplication.feature.storage.data

import com.daniilshevtsov.deduplication.feature.core.Chunk
import com.daniilshevtsov.deduplication.feature.core.Reference

sealed class SavedData {
    data class Value(val chunk: Chunk) : SavedData()
    data class TableReference(val reference: Reference) : SavedData()
}