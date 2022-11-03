package com.b21cap0133.verify.utility

import com.b21cap0133.verify.domain.News
import com.b21cap0133.verify.remotesource.NewsResponse

object ClassMapper{

    fun mapResponseToDomain(response: NewsResponse): News{
        return News(
            response.judul,
            response.prediksi,
            response.linkBerita,
            response.tanggalBerita,
            response.preview
        )
    }

}
