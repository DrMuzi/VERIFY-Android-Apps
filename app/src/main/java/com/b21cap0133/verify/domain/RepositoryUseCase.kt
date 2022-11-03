package com.b21cap0133.verify.domain

import android.annotation.SuppressLint
import com.b21cap0133.verify.remotesource.*
import com.b21cap0133.verify.utility.ClassMapper
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class RepositoryUseCase(private val remote: RemoteDataSource): RepositoryInterface {
    private val result = PublishSubject.create<NewsResponse>()
    private val mCompositeDisposable = CompositeDisposable()

    @SuppressLint("CheckResult")
    override fun getResult(content: Request): Flowable<News> {
        val requestMessage = RequestEntity(
            content.message
        )
        remote.getResult(requestMessage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .take(1)
            .doOnComplete {
                mCompositeDisposable.dispose()
            }
            .subscribe{
                when(it){
                    is ApiResponse.Success -> result.onNext(it.data)
                    else -> result.onNext(
                        NewsResponse(
                            "notfound404"
                        )
                    )
                }
            }
        val value = result.toFlowable(BackpressureStrategy.BUFFER)
        return value.map { ClassMapper.mapResponseToDomain(it) }
    }
}