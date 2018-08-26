package br.com.gdgbrasilia.meetup.app.view.activities

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import br.com.gdgbrasilia.meetup.app.R
import br.com.gdgbrasilia.meetup.app.business.vo.MarkerType
import br.com.gdgbrasilia.meetup.app.business.vo.TipoEstabelecimento
import br.com.gdgbrasilia.meetup.app.business.vo.TipoEvento
import br.com.gdgbrasilia.meetup.app.data.AppConstants.BASE_URL
import br.com.gdgbrasilia.meetup.app.data.AppStatics
import br.com.gdgbrasilia.meetup.app.util.extensions.getGlideUrl
import br.com.gdgbrasilia.meetup.app.util.extensions.getViewModel
import br.com.gdgbrasilia.meetup.app.view.viewmodel.AccountViewModel
import com.bumptech.glide.Glide
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import java.io.File
import java.io.FileOutputStream


class LauncherActivity : AppCompatActivity() {

    private val accountViewModel by lazy { getViewModel(AccountViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_launcher)

        val sharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
        val usuarioID = sharedPreferences.getString(getString(R.string.usuario_id), null)


        if (usuarioID != null) {
            accountViewModel.fetchCategorias { categorias ->
                fetchImages(categorias) {
                    fetchUser(usuarioID)
                }
            }
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }

    fun fetchUser(usuarioID: String) {
        accountViewModel.fetchProfile(usuarioID) { usuario ->
            if (usuario != null) {
                AppStatics.currentUser = usuario
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun fetchImages(categorias: List<MarkerType>, listener: () -> Unit) = async(UI) {
        categorias
                .forEach {
                    val uri = if (it is TipoEvento) "TipoEvento" else "TipoEstabelecimento"
                    val id = (it as? TipoEvento)?.tipoEventoID
                            ?: (it as TipoEstabelecimento).tipoEstabelecimentoID

                    val url = "$BASE_URL/$uri/Download?id=$id"

                    val drawable = try {
                        async(CommonPool) {
                            Glide.with(this@LauncherActivity)
                                    .asDrawable()
                                    .load(getGlideUrl(url))
                                    .submit()
                                    .get()
                        }.await()
                    } catch (e: Exception) {
                        null
                    }

                    if (drawable != null) {
                        saveImage(drawableToBitmap(drawable), id)
                    }
                }
        listener()
    }

    fun drawableToBitmap(drawable: Drawable): Bitmap {
        var bitmap: Bitmap? = null

        if (drawable is BitmapDrawable) {
            if (drawable.bitmap != null) {
                return drawable.bitmap
            }
        }

        if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888) // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        }

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        val newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig())
        val canvas2 = Canvas(newBitmap)
        canvas.drawColor(Color.TRANSPARENT)
        canvas2.drawBitmap(bitmap, 0f, 0f, null)

        return bitmap
    }


    private fun saveImage(image: Bitmap, categoriaID: String): String? {
        var savedImagePath: String? = null

        val imageFileName = "$categoriaID"
        val storageDir = File("$filesDir")
        var success = true
        if (!storageDir.exists()) {
            success = storageDir.mkdirs()
        }
        if (success) {
            val imageFile = File(storageDir, imageFileName)
            savedImagePath = imageFile.absolutePath
            try {
                val fOut = FileOutputStream(imageFile)
                image.compress(Bitmap.CompressFormat.PNG, 100, fOut)
                fOut.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return savedImagePath
    }
}
