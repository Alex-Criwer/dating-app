package com.example.sonder_dating_app.presentation.utils.hepler

import android.graphics.Bitmap
import com.example.sonder_domain.models.Media

interface MediaLoader {
    suspend fun downloadMedia(media: Media): Bitmap?
}
