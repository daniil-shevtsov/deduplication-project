package com.daniilshevtsov.deduplication.feature.core

data class AppConfig(
    val chunkSize: Int,
    val storageDirectoryName: String,
    val storageFileName: String
)