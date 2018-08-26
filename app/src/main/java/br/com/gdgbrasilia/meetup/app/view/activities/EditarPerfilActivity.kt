package br.com.gdgbrasilia.meetup.app.view.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import br.com.gdgbrasilia.meetup.app.R
import br.com.gdgbrasilia.meetup.app.data.AppConstants
import br.com.gdgbrasilia.meetup.app.data.AppStatics
import br.com.gdgbrasilia.meetup.app.util.extensions.displayAsDate
import br.com.gdgbrasilia.meetup.app.util.extensions.getViewModel
import br.com.gdgbrasilia.meetup.app.util.extensions.loadImg
import br.com.gdgbrasilia.meetup.app.view.viewmodel.AccountViewModel
import kotlinx.android.synthetic.main.activity_editar_perfil.*
import kotlinx.android.synthetic.main.toolbar_main.*

class EditarPerfilActivity : AppCompatActivity() {

    private val accountViewModel by lazy { getViewModel(AccountViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil)
        setupToolbar()
        setupViews()
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun setupViews() {
        AppStatics.currentUser?.let {
            userPicture.loadImg("${AppConstants.BASE_URL}${it.fotos?.firstOrNull()?.url}")
            userNameText.setText(it.nome)
            userEmailText.setText(it.email)
            userFoneText.setText(it.telefone ?: "Não informado")
            userBirthday.setText(it.dataNascimento?.displayAsDate() ?: "Não informado")
        }

        salvarBtn.setOnClickListener {
            val name = userNameText.text.toString()
            val email = userEmailText.text.toString()
            val fone = userFoneText.text.toString()
            val nascimento = userBirthday.text.toString()
            if (name.isNotEmpty() && email.isNotEmpty() && fone.isNotEmpty() && nascimento.isNotEmpty()) {
                AppStatics.currentUser?.let {
                    val newUser = it.copy(nome = name, email = email, telefone = fone, dataNascimento = nascimento)
                    accountViewModel.salvarUsuario(newUser) { success ->
                        if (success) {
                            AppStatics.currentUser = newUser
                            finish()
                        } else {
                            Toast.makeText(this, "Não foi possível atualizar o usuário", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {

            }
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Editar Perfil"
    }
}
