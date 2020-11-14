package com.daniilshevtsov.deduplication.domain

import javax.inject.Inject

class CheckDuplicatedUseCase @Inject constructor() {
    operator fun invoke(): Boolean {
        return false
    }
}