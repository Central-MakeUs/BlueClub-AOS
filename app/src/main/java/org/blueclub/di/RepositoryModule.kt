package org.blueclub.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.blueclub.data.repository.AuthRepositoryImpl
import org.blueclub.domain.repository.AuthRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl,
    ): AuthRepository
}