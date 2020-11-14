package com.daniilshevtsov.deduplication.di

import com.daniilshevtsov.deduplication.feature.indextable.data.IndexTableRepositoryImpl
import com.daniilshevtsov.deduplication.feature.indextable.domain.IndexTableRepository
import com.daniilshevtsov.deduplication.feature.storage.data.StorageRepositoryImpl
import com.daniilshevtsov.deduplication.feature.storage.domain.StorageRepository
import dagger.Binds
import dagger.Module

@Module
interface AppModule {

    @Binds
    fun bindIndexTableRepository(repository: IndexTableRepositoryImpl): IndexTableRepository

    @Binds
    fun bindStorageRepository(repository: StorageRepositoryImpl): StorageRepository

}