package com.daniilshevtsov.deduplication.feature.main

import com.daniilshevtsov.deduplication.feature.indextable.data.DataStoreApi
import java.io.File
import javax.inject.Inject

class CleanFilesUseCase @Inject constructor(
    private val dataStoreApi: DataStoreApi
) {
    operator fun invoke() {
        dataStoreApi.deleteEverything()
        File("data.db").delete()

        //TODO: Use dynamic storage name
        File("storage").deleteRecursively()

        //TODO: store source and output in special directories
        File(".").listFiles()
            ?.filter { it.name.endsWith(".txt") }
            ?.forEach { it.delete() }
    }
}