package br.com.gdgbrasilia.meetup.app.view.activities

import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.PopupWindow
import android.widget.Toast
import br.com.gdgbrasilia.meetup.app.R
import br.com.gdgbrasilia.meetup.app.data.AppConstants.BASE_URL
import br.com.gdgbrasilia.meetup.app.data.AppStatics
import br.com.gdgbrasilia.meetup.app.util.extensions.displayAsDate
import br.com.gdgbrasilia.meetup.app.util.extensions.getViewModel
import br.com.gdgbrasilia.meetup.app.util.extensions.loadImg
import br.com.gdgbrasilia.meetup.app.util.extensions.startActivity
import br.com.gdgbrasilia.meetup.app.view.viewmodel.AccountViewModel
import com.facebook.login.LoginManager
import kotlinx.android.synthetic.main.activity_perfil.*
import kotlinx.android.synthetic.main.toolbar_main.*


class PerfilActivity : AppCompatActivity() {

    private val accountViewModel by lazy { getViewModel(AccountViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)
        setupToolbar()
        setupViews()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_excluir_conta -> excluirConta()
            R.id.menu_informacoes -> startActivity(SobreActivity::class.java)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun excluirConta() {
        try {
            val display = windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            val width = size.x
            val margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120f, resources.displayMetrics)
            val layout = layoutInflater.inflate(R.layout.layout_popup_excluir_conta, null)
            val popupWindow = PopupWindow(layout, (width - margin).toInt(), CoordinatorLayout.LayoutParams.WRAP_CONTENT, true)

            val excluirBtn = layout.findViewById<Button>(R.id.excluirBtn)
            excluirBtn.setOnClickListener {
                popupWindow.dismiss()
                AppStatics.currentUser?.let {
                    accountViewModel.excluirUsuario(it.usuarioID) { success ->
                        if (success) {
                            logout()
                        } else {
                            Toast.makeText(this, "Não foi possível excluir a conta", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }

            popupWindow.showAtLocation(parentView, Gravity.CENTER, 0, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun confirmLogout() {
        AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_logotipo_inverse)
                .setTitle("Sair")
                .setMessage("Você deseja realmente sair?")
                .setPositiveButton("Sim") { dialog, which ->
                    logout()
                }
                .setNegativeButton("Não", null)
                .show()
    }

    fun logout() {
        getSharedPreferences(getString(R.string.app_name), 0).edit().clear().commit()
        AppStatics.currentUser = null
        LoginManager.getInstance().logOut()
        val i = Intent(applicationContext, LauncherActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(i)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_perfil, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupViews() {
        AppStatics.currentUser?.let {
            userPicture.loadImg("$BASE_URL${it.fotos?.firstOrNull()?.url}")
            userNameText.text = it.nome
            userEmailText.text = it.email
            userFoneText.text = it.telefone ?: "Não informado"
            userSexo.text = "Não informado"
            userBirthday.text = it.dataNascimento?.displayAsDate() ?: "Não informado"
        }
        logoutBtn.setOnClickListener { confirmLogout() }
        editarBtn.setOnClickListener { startActivity(EditarPerfilActivity::class.java) }
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Perfil"
    }
}
