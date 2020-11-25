package com.daniilshevtsov.deduplication.feature.main

import com.daniilshevtsov.deduplication.feature.indextable.data.DataStoreApi
import java.io.File
import javax.inject.Inject

class CleanFilesUseCase @Inject constructor(
    private val dataStoreApi: DataStoreApi
) {
    operator fun invoke() {
        dataStoreApi.deleteEverything()

        //TODO: Use dynamic storage name
        File("storage").deleteRecursively()

        File("output").deleteRecursively()

        File("data.db").delete()
    }
}