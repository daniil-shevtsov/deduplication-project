package com.daniilshevtsov.deduplication.feature.main

import java.io.File
import javax.inject.Inject

class CleanFilesUseCase @Inject constructor() {
    operator fun invoke() {
        File("data.db").delete()

        //TODO: Use dynamic storage name
        File("storage").deleteRecursively()

        //TODO: store source and output in special directories
        File(".").listFiles()
            ?.filter { it.name.endsWith(".txt") }
            ?.forEach { it.delete() }
    }
}