package com.grank.uiarch.di

import android.app.Application
import android.content.Context
import android.view.WindowManager
import androidx.lifecycle.*
import com.grank.netcore.SERVER
import com.grank.netcore.ServerApi
import com.grank.netcore.core.ApiFactory
import com.grank.netcore.core.VendorPlatform
import com.grank.uiarch.testdi.HiltTest
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import me.jessyan.autosize.AutoAdaptStrategy
import me.jessyan.autosize.DefaultAutoAdaptStrategy
import me.jessyan.autosize.WrapperAutoAdaptStrategy
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object AppComponent {

    @Singleton
    @Provides
    fun provideServerApi(apiFactory: ApiFactory):ServerApi {
        return apiFactory.create(ServerApi::class.java)
    }

    @Singleton
    @Provides
    fun provideApiFactory(
            context: Application,
    ):ApiFactory {
        return ApiFactory(context, VendorPlatform(context), SERVER, true)
    }

    @Singleton
    @Provides
    fun provideHiltTest():HiltTest {
        return HiltTest()
    }
    @Singleton
    @Provides
    fun provideWindowManager(context: Application): WindowManager {
        return context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }
    @Singleton
    @Provides
    fun provideAutoSizeStrategy(): AutoAdaptStrategy {
        return WrapperAutoAdaptStrategy(DefaultAutoAdaptStrategy())
    }

    @Provides
    @Singleton
    @Named("app")
    fun provideAppLifecycleScope(): LifecycleCoroutineScope {
        return ProcessLifecycleOwner.get().lifecycleScope
    }

    @Provides
    @Singleton
    @Named("app")
    fun provideAppLifecycleOwner(): LifecycleOwner {
        return ProcessLifecycleOwner.get()
    }

    @Provides
    @Singleton
    @Named("app")
    fun provideAppLifecycle(): Lifecycle {
        return ProcessLifecycleOwner.get().lifecycle
    }
}