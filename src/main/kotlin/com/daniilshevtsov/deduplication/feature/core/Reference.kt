package com.daniilshevtsov.deduplication.feature.core

data class Reference(
    val id: Int,
    val pageId: String,
    val segmentPosition: Long
)