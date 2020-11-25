package com.daniilshevtsov.deduplication.feature.output

import java.io.File
import java.io.OutputStream
import javax.inject.Inject

class PrepareOutputUseCase @Inject constructor() {
    operator fun invoke(fileName: String): OutputStream {
        File("output").mkdirs()
        return File(fileName).outputStream().buffered()
    }
}