package org.blueclub.di

import android.content.Context
import com.kakao.sdk.user.UserApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import org.blueclub.data.service.KakaoAuthService

@Module
@InstallIn(ActivityComponent::class)
object SignModule {
    @Provides
    @ActivityScoped
    fun provideUserApiClient(): UserApiClient = UserApiClient.instance

    @Provides
    fun provideKakaoSignService(
        @ActivityContext context: Context,
        client: UserApiClient,
    ) = KakaoAuthService(context, client)
}
