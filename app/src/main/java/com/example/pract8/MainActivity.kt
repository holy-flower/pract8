package com.example.pract8

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class MainActivity : AppCompatActivity() {
    lateinit var urlEditText: EditText
    lateinit var downloadButton: Button
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        urlEditText = findViewById(R.id.urlEditText)
        downloadButton = findViewById(R.id.downloadButton)
        imageView = findViewById(R.id.imageView)

        downloadButton.setOnClickListener {
            val url = urlEditText.text.toString()
            if (url.isNotEmpty()) {
                downloadImage(url)
            } else {
                Toast.makeText(this, "Please enter a valid URL", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun downloadImage(url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("ImageDownload", "Start downloading image from URL: $url")
                val bitmap = downloadImageFromNetwork(url)

                if (bitmap != null) {
                    saveImageToDisk(bitmap)
                    withContext(Dispatchers.Main) {
                        Log.d("ImageDownload", "Image downloaded successfully")
                        imageView.setImageBitmap(bitmap)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, "Failed to download image", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun downloadImageFromNetwork(url: String): Bitmap? {
        return try {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                val inputStream: InputStream = response.body?.byteStream() ?: return null

                val options = BitmapFactory.Options().apply {
                    inJustDecodeBounds = true
                    BitmapFactory.decodeStream(inputStream, null, this)

                    inSampleSize = calculateInSampleSize(this, 500, 500)
                    inJustDecodeBounds = false
                }
                val newInputStream: InputStream = client.newCall(request).execute().body?.byteStream() ?: return null
                BitmapFactory.decodeStream(newInputStream, null, options)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun saveImageToDisk(bitmap: Bitmap) {
        val directory = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "downloadedImages")
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val file = File(directory, "downloaded_image.png")
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
    }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val (height: Int, width: Int) = options.outHeight to options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqHeight) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}