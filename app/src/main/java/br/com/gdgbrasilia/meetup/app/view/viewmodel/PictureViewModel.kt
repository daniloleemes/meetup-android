package br.com.gdgbrasilia.meetup.app.view.viewmodel

import android.app.Activity.RESULT_OK
import android.arch.lifecycle.ViewModel
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import br.com.gdgbrasilia.meetup.app.data.AppConstants.CAMERA_PERMISSION
import br.com.gdgbrasilia.meetup.app.data.AppConstants.CAMERA_PERMISSION_REQUEST_CODE
import br.com.gdgbrasilia.meetup.app.data.AppConstants.CAMERA_REQUEST_CODE
import br.com.gdgbrasilia.meetup.app.data.AppConstants.GALLERY_PERMISSION_REQUEST_CODE
import br.com.gdgbrasilia.meetup.app.data.AppConstants.GALLERY_PICK_REQUEST_CODE
import br.com.gdgbrasilia.meetup.app.data.AppConstants.READ_EXTERNAL_STORAGE_PERMISSION
import br.com.gdgbrasilia.meetup.app.data.AppConstants.WRITE_EXTERNAL_STORAGE_PERMISSION
import br.com.gdgbrasilia.meetup.app.providers.ImageProvider
import java.io.File

class PictureViewModel : ViewModel() {

    var photoPath = ""

    fun checkCameraPermission(activity: AppCompatActivity) =
            if (ContextCompat.checkSelfPermission(activity, CAMERA_PERMISSION) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(activity, WRITE_EXTERNAL_STORAGE_PERMISSION) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(activity, READ_EXTERNAL_STORAGE_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, arrayOf(CAMERA_PERMISSION, WRITE_EXTERNAL_STORAGE_PERMISSION, READ_EXTERNAL_STORAGE_PERMISSION), CAMERA_PERMISSION_REQUEST_CODE)
            } else {
                openCamera(activity)
            }

    fun checkGalleryPermission(activity: AppCompatActivity) {
        if (ContextCompat.checkSelfPermission(activity, CAMERA_PERMISSION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(activity, WRITE_EXTERNAL_STORAGE_PERMISSION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(activity, READ_EXTERNAL_STORAGE_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(CAMERA_PERMISSION, WRITE_EXTERNAL_STORAGE_PERMISSION, READ_EXTERNAL_STORAGE_PERMISSION), GALLERY_PERMISSION_REQUEST_CODE)
        } else {
            openGallery(activity)
        }
    }

    fun onRequestPermissionsResult(activity: AppCompatActivity, requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> openCamera(activity)
            GALLERY_PERMISSION_REQUEST_CODE -> openGallery(activity)
        }
    }

    fun onActivityResult(activity: AppCompatActivity, requestCode: Int, resultCode: Int, data: Intent?, listener: (Uri) -> Unit) {
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> cameraResult(activity, ImageProvider.instance.getImageUri(activity, photoPath), listener)
                GALLERY_PICK_REQUEST_CODE -> galleryResult(activity, data?.data, listener)
            }
        }
    }

    private fun galleryResult(activity: AppCompatActivity, galleryImageUri: Uri?, listener: (Uri) -> Unit) {
        galleryImageUri?.let { listener(it) }
    }

    private fun cameraResult(activity: AppCompatActivity, fullImageUri: Uri?, listener: (Uri) -> Unit) {
        if (fullImageUri != null) {
            listener(fullImageUri)
        } else {
            System.gc()
            Toast.makeText(activity, "Ocorreu um erro. Tente novamente.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGallery(activity: AppCompatActivity) {
        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        getIntent.type = "image/*"

        val pickIntent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*"

        val chooserIntent = Intent.createChooser(getIntent, "Selecione")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

        activity.startActivityForResult(chooserIntent, GALLERY_PICK_REQUEST_CODE)
    }

    private fun openCamera(activity: AppCompatActivity) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(activity.packageManager) != null) {
            var photoFile: File? = null

            try {
                photoFile = ImageProvider.instance.createImageFile(activity)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            photoFile?.let {
                val photoURI = FileProvider.getUriForFile(activity, activity.packageName + ".fileprovider", it)
                photoPath = it.absolutePath
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                activity.startActivityForResult(intent, CAMERA_REQUEST_CODE)
            }
        }
    }

}