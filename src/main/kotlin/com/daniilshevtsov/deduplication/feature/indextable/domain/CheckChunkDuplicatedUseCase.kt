package com.daniilshevtsov.deduplication.feature.indextable.domain

import javax.inject.Inject

class CheckChunkDuplicatedUseCase @Inject constructor(
    private val indexTableRepository: IndexTableRepository
) {
    operator fun invoke(key: String): Boolean = indexTableRepository.checkContains(key = key)
}