package com.daniilshevtsov.deduplication.feature.indextable.data

data class ReferenceEntity(
    val segmentHash: String,
    val fileName: String,
    val segmentPosition: Long,
    val segmentCount: Long
)