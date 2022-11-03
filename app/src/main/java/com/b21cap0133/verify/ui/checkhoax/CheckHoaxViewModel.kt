package com.b21cap0133.verify.ui.checkhoax

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.b21cap0133.verify.domain.News
import com.b21cap0133.verify.domain.RepositoryInterface
import com.b21cap0133.verify.domain.Request

class CheckHoaxViewModel(private val repository: RepositoryInterface): ViewModel() {
    fun getResult(content: Request): LiveData<News> {
        return LiveDataReactiveStreams.fromPublisher(repository.getResult(content))
    }
}