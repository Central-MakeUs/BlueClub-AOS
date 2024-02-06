package org.blueclub.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.blueclub.data.repository.AuthRepositoryImpl
import org.blueclub.data.repository.MyPageRepositoryImpl
import org.blueclub.data.repository.UserRepositoryImpl
import org.blueclub.data.repository.WorkbookRepositoryImpl
import org.blueclub.domain.repository.AuthRepository
import org.blueclub.domain.repository.MyPageRepository
import org.blueclub.domain.repository.UserRepository
import org.blueclub.domain.repository.WorkbookRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl,
    ): AuthRepository

    @Binds
    @Singleton
    fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl,
    ): UserRepository

    @Binds
    @Singleton
    fun bindMyPageRepository(
        myPageRepositoryImpl: MyPageRepositoryImpl,
    ): MyPageRepository

    @Binds
    @Singleton
    fun bindWorkbookRepository(
        workbookRepositoryImpl: WorkbookRepositoryImpl,
    ): WorkbookRepository
}