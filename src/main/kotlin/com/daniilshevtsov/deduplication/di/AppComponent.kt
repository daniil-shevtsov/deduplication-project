package com.daniilshevtsov.deduplication.di

import com.daniilshevtsov.deduplication.AppConfig
import com.daniilshevtsov.deduplication.DeduplicationApp
import com.daniilshevtsov.deduplication.di.scope.ApplicationScope
import dagger.BindsInstance
import dagger.Component


@ApplicationScope
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(app: DeduplicationApp)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance appConfig: AppConfig): AppComponent
    }
}