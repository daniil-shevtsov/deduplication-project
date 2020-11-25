package com.daniilshevtsov.deduplication.feature.storage.data

import com.daniilshevtsov.deduplication.feature.core.Chunk

sealed class SavedData {
    data class Value(val chunk: Chunk) : SavedData()
    data class TableReference(val referenceId: String) : SavedData()
}