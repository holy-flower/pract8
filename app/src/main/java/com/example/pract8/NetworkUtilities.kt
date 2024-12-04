package com.example.pract8

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.InputStream

class NetworkUtilities {

    fun downloadImage(url: String): Deferred<Bitmap?> {
        return kotlinx.coroutines.GlobalScope.async(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val inputStream: InputStream = response.body?.byteStream() ?: return@async null
                    BitmapFactory.decodeStream(inputStream)
                } else {
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}
