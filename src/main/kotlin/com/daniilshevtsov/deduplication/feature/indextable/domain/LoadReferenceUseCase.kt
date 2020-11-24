package com.daniilshevtsov.deduplication.feature.indextable.domain

import javax.inject.Inject

class LoadReferenceUseCase @Inject constructor(
    private val indexTableRepository: IndexTableRepository
) {

    operator fun invoke(key: String) = indexTableRepository.get(key = key)

}