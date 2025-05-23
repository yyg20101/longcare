package com.ytone.longcare.di

import com.ytone.longcare.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoggingModule {

    @Provides
    @Singleton
    fun provideTimberTree(): Timber.Tree {
        return if (BuildConfig.DEBUG) {
            Timber.DebugTree()
        } else {
            // In a real app, you'd have a CrashReportingTree here.
            // For this example, we'll just use a DebugTree for release too,
            // or a tree that does nothing.
            object : Timber.Tree() {
                override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                    // Log to crash reporting tool in production
                }
            }
        }
    }
}
