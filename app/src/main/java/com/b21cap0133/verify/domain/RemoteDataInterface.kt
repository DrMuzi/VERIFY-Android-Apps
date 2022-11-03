package com.b21cap0133.verify.domain

import com.b21cap0133.verify.remotesource.ApiResponse
import com.b21cap0133.verify.remotesource.NewsResponse
import com.b21cap0133.verify.remotesource.RequestEntity
import io.reactivex.Flowable

interface RemoteDataInterface {
    fun getResult(content: RequestEntity): Flowable<ApiResponse<NewsResponse>>
}