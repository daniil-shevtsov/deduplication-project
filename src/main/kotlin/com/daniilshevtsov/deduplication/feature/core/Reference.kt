package com.daniilshevtsov.deduplication.feature.core

data class Reference(
    val id: String,
    val pageId: String,
    val segmentPosition: Long
)