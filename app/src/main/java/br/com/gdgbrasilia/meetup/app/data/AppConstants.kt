package br.com.gdgbrasilia.meetup.app.data

object AppConstants {
    val BASE_URL = "https://api.themoviedb.org/3/"
    val IMAGE_PATH = "https://image.tmdb.org/t/p/w780"
    val THUMB_PATH = "https://image.tmdb.org/t/p/w300"
//    val BASE_URL = "http://192.168.0.23:8081/"

    val CAMERA_PERMISSION = android.Manifest.permission.CAMERA
    val READ_EXTERNAL_STORAGE_PERMISSION = android.Manifest.permission.READ_EXTERNAL_STORAGE
    val WRITE_EXTERNAL_STORAGE_PERMISSION = android.Manifest.permission.WRITE_EXTERNAL_STORAGE

    val CAMERA_REQUEST_CODE = 9001
    val GALLERY_PICK_REQUEST_CODE = 9004
    val CROP_REQUEST_CODE = 9002
    val GALLERY_REQUEST_CODE = 9003
    val SETTINGS_REQUEST_CODE = 31337

    val CAMERA_PERMISSION_REQUEST_CODE = 8001
    val GALLERY_PERMISSION_REQUEST_CODE = 8002
    val FACEBOOK_GALLERY_PERMISSION_REQUEST_CODE = 8003
    val LOCATION_PERMISSION_REQUEST_CODE = 8004
}