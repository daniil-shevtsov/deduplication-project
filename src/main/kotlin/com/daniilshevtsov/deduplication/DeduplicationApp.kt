package com.daniilshevtsov.deduplication

import com.daniilshevtsov.deduplication.di.AppComponent
import com.daniilshevtsov.deduplication.di.DaggerAppComponent
import com.daniilshevtsov.deduplication.feature.core.AppConfig
import java.io.File
import javax.inject.Inject

class DeduplicationApp {
    private val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory()
            .create(
                appConfig = AppConfig(chunkSize = 4, storageDirectoryName = "storage", storageFileName = "storage.txt")
            )
    }

    @Inject
    lateinit var deduplicator: Deduplicator

    fun start(args: Array<String>) {
        //TODO: Remove
        clean()

        appComponent.inject(this)
        deduplicator.run(args = args)
    }

    private fun clean() {
        File("output.txt").delete()
        File("data.db").delete()
    }

}