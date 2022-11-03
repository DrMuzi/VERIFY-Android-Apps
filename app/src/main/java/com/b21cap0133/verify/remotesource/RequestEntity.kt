package com.b21cap0133.verify.remotesource

import com.google.gson.annotations.SerializedName

data class RequestEntity(
    @SerializedName("message")
    val message: String = ""
)
