package com.b21cap0133.verify.remotesource

import android.annotation.SuppressLint
import android.util.Log
import com.b21cap0133.verify.domain.RemoteDataInterface
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class RemoteDataSource(private val retrofit: RemoteInterface): RemoteDataInterface {
    @SuppressLint("CheckResult")
    override fun getResult(content: RequestEntity): Flowable<ApiResponse<NewsResponse>> {
        val returnValue = PublishSubject.create<ApiResponse<NewsResponse>>()
        val call = retrofit.fetchNews(content)
        call
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .take(1)
            .subscribe ({ response ->
                returnValue.onNext(if (response.judul.isNotEmpty()) ApiResponse.Success(response) else ApiResponse.Empty)
            }, { error ->
                returnValue.onNext(ApiResponse.Error(error.message.toString()))
                Log.e("RemoteDataSource", error.toString())
            })
        return returnValue.toFlowable(BackpressureStrategy.BUFFER)
    }

    /*@SuppressLint("CheckResult")
    override fun getResult(content: RequestEntity): Flowable<ApiResponse<NewsResponse>> {
        val returnValue = PublishSubject.create<ApiResponse<NewsResponse>>()
        val call = retrofit.testFetchNews(content.message)
        call
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .take(1)
            .subscribe ({ response ->
                Log.d("OwO", "getResult: $response")
                returnValue.onNext(if (response.judul.isNotEmpty()) ApiResponse.Success(response) else ApiResponse.Empty)
            }, { error ->
                Log.d("OwO", "getResult: ${error.message}")
                returnValue.onNext(ApiResponse.Error(error.message.toString()))
                Log.e("RemoteDataSource", error.toString())
            })
        return returnValue.toFlowable(BackpressureStrategy.BUFFER)
    }*/
}