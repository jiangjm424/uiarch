package com.grank.uiarch.di

import com.grank.uiarch.testdi.HiltTest
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object AppComponent {

    @Singleton
    @Provides
    fun provideHiltTest():HiltTest {
        return HiltTest()
    }
}