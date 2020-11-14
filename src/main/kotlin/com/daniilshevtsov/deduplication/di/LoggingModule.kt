package com.daniilshevtsov.deduplication.di

import com.daniilshevtsov.deduplication.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides
import mu.KLogger
import mu.KotlinLogging

@Module
class LoggingModule {
    @ApplicationScope
    @Provides
    fun provideLogger(): KLogger = KotlinLogging.logger("Deduplication")
}