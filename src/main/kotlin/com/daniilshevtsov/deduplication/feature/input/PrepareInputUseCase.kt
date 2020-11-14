package com.daniilshevtsov.deduplication.feature.input

import java.io.File
import java.io.InputStream
import javax.inject.Inject

class PrepareInputUseCase @Inject constructor() {
    operator fun invoke(fileName: String): InputStream {
        return File(fileName).inputStream().buffered()
    }
}