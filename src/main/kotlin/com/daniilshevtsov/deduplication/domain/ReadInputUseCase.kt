package com.daniilshevtsov.deduplication.domain

import java.io.InputStream
import javax.inject.Inject

class ReadInputUseCase @Inject constructor() {
    operator fun invoke(): InputStream {
        return InputStream.nullInputStream()
    }
}