package br.com.gdgbrasilia.meetup.app.view.activities

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import br.com.gdgbrasilia.meetup.app.R
import br.com.gdgbrasilia.meetup.app.business.model.IntentFieldNames
import br.com.gdgbrasilia.meetup.app.business.model.ViewTypes
import br.com.gdgbrasilia.meetup.app.business.vo.TipoDenuncia
import br.com.gdgbrasilia.meetup.app.util.extensions.getViewModel
import br.com.gdgbrasilia.meetup.app.view.viewmodel.DenunciaViewModel
import kotlinx.android.synthetic.main.activity_denuncia.*
import kotlinx.android.synthetic.main.toolbar_main.*

class DenunciaActivity : AppCompatActivity() {

    private val denunciaViewModel by lazy { getViewModel(DenunciaViewModel::class.java) }
    private var viewType: Int = -1
    private var itemID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_denuncia)
        setupToolbar()

        viewType = intent.extras.getInt(IntentFieldNames.VIEW_TYPE)
        itemID = intent.extras.getString(IntentFieldNames.ITEM_ID)

        denunciaViewModel.fetchTiposDenuncia().observe(this, Observer { tiposDenuncia ->
            tiposDenuncia?.let {
                it.forEach {
                    val radioButton = View.inflate(this, R.layout.layout_template_radio_button, null) as RadioButton
                    radioButton.id = View.generateViewId()
                    radioButton.tag = it
                    radioButton.text = it.nome
                    motivoDenunciaRadioGroup.addView(radioButton)
                }
            }
        })

        enviarBtn.setOnClickListener {
            if (motivoDenunciaRadioGroup.checkedRadioButtonId == -1 && denunciaDescricao.text.isEmpty()) {
                Toast.makeText(this, "Preencha os campos", Toast.LENGTH_SHORT).show()
            } else {
                val tipoDenuncia = motivoDenunciaRadioGroup.findViewById<RadioButton>(motivoDenunciaRadioGroup.checkedRadioButtonId).tag as TipoDenuncia
                if (viewType == ViewTypes.ESTABELECIMENTO) {
                    denunciaViewModel.denunciarEstabelecimento(itemID, tipoDenuncia.tipoDenunciaID, denunciaDescricao.text.toString(), listener())
                } else {
                    denunciaViewModel.denunciarEvento(itemID, tipoDenuncia.tipoDenunciaID, denunciaDescricao.text.toString(), listener())
                }
            }
        }
    }

    private fun listener(): (Boolean) -> Unit {
        return { sucesso ->
            if (sucesso) {
                Toast.makeText(this, "Denúncia recebida com sucesso", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Erro ao salvar denúncia", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Denúncia"
    }
}
