package com.b21cap0133.verify.di

import com.b21cap0133.verify.domain.RepositoryInterface
import com.b21cap0133.verify.domain.RepositoryUseCase
import com.b21cap0133.verify.remotesource.RemoteDataSource
import com.b21cap0133.verify.remotesource.RemoteInterface
import com.b21cap0133.verify.ui.checkhoax.CheckHoaxViewModel
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@JvmField
val netModule = module{
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }
    single{
        val retrofit = Retrofit.Builder()
            .baseUrl("https://capstone-deploy-3owb6alr3q-et.a.run.app")
            .client(get())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
        retrofit.create(RemoteInterface::class.java)
    }
}

val remoteSource = module{
    single{
        RemoteDataSource(get())
    }
}

val repoModule = module{
    factory<RepositoryInterface> { RepositoryUseCase(get()) }
}

val viewModule = module{
    viewModel { CheckHoaxViewModel(get()) }
}