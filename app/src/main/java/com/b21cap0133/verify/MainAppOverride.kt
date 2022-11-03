package com.b21cap0133.verify

import android.app.Application
import com.b21cap0133.verify.di.netModule
import com.b21cap0133.verify.di.remoteSource
import com.b21cap0133.verify.di.repoModule
import com.b21cap0133.verify.di.viewModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainAppOverride: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidLogger()
            androidContext(this@MainAppOverride)
            //add modules here
            modules(listOf(
                netModule,
                remoteSource,
                repoModule,
                viewModule
            ))
        }
    }
}