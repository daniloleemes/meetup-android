package br.com.gdgbrasilia.meetup.app.data.providers

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Debug
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ImageProvider {

    private val TAG = this.javaClass.canonicalName
    val SAFETY_MEMORY_BUFFER: Long = 10 //MB

    companion object {
        val instance = ImageProvider()
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    fun createImageFile(context: Context): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        // Save a file: path for use with ACTION_VIEW intents
        return File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )
    }

    fun getUriFromImage(inContext: Context, inImage: Bitmap, quality: Int): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, quality, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return try {
            bytes.close()
            Uri.parse(path)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } finally {
            inImage.recycle()
        }
    }

    fun resizeRotatePicture(path: String): Bitmap? {
        // Get the dimensions of the View
        val targetW = 400
        val targetH = 400
        val rotation = detectRotation(path)

        // Get the dimensions of the bitmap
        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, bmOptions)
        val photoW = bmOptions.outWidth
        val photoH = bmOptions.outHeight

        if (photoH >= targetH || photoW >= targetW) {
            // Determine how much to scale down the image
            val scaleFactor = Math.min(photoW / targetW, photoH / targetH)
            bmOptions.inSampleSize = scaleFactor
        }

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false
        bmOptions.inPurgeable = true

        val bitmap = BitmapFactory.decodeFile(path, bmOptions)
        val matrix = Matrix()
        matrix.postRotate(rotation.toFloat())
        if (bitmap != null) {
            val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            val out = ByteArrayOutputStream()
            try {
                rotatedBitmap.compress(Bitmap.CompressFormat.PNG, 45, out)

                return if (canBitmapFitInMemory(bmOptions)) {
                    rotatedBitmap
                } else {
                    null
                }
            } catch (e: Exception) {
                Log.e(TAG, "[EXCEPTION]", e)
                return null
            } finally {
                try {
                    out?.close()
                } catch (e: IOException) {
                    Log.e(TAG, "[EXCEPTION]", e)
                }

            }
        }

        return null
    }

    fun detectRotation(filePath: String): Int {
        return try {
            val ei = ExifInterface(filePath)
            val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)

            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                ExifInterface.ORIENTATION_NORMAL -> 0
                else -> 0
            }
        } catch (e: IOException) {
            Log.e(TAG, "[EXCEPTION]", e)
            0
        }

    }

    fun getImageUri(inContext: Context, absolutePath: String): Uri? {
        val inImage = resizeRotatePicture(absolutePath)

        if (inImage != null) {
            val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
            inImage.recycle()
            return Uri.parse(path)
        }

        return null
    }

    //ref:http://stackoverflow.com/questions/6073744/android-how-to-check-how-much-memory-is-remaining
    fun availableMemoryMB(): Double {
        val max = (Runtime.getRuntime().maxMemory() / 1024).toDouble()
        val memoryInfo = Debug.MemoryInfo()
        Debug.getMemoryInfo(memoryInfo)
        return (max - memoryInfo.getTotalPss()) / 1024
    }

    fun canBitmapFitInMemory(options: BitmapFactory.Options): Boolean {
        val size = (options.outHeight * options.outWidth * 32 / (1024 * 1024 * 8)).toLong()
        Log.d(TAG, "image MB:" + size)
        return size <= availableMemoryMB() - SAFETY_MEMORY_BUFFER
    }

    fun calculatePixels(fromDP: Int?, context: Context): Int? {
        val multiplyFactor = context.resources.displayMetrics.density
        return java.lang.Double.valueOf((fromDP!! * multiplyFactor).toDouble())!!.toInt()
    }

}