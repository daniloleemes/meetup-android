package br.com.gdgbrasilia.meetup.app.view.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.widget.Toast
import br.com.gdgbrasilia.meetup.app.R
import br.com.gdgbrasilia.meetup.app.business.model.IntentFieldNames
import br.com.gdgbrasilia.meetup.app.util.extensions.getViewModel
import br.com.gdgbrasilia.meetup.app.util.extensions.startActivity
import br.com.gdgbrasilia.meetup.app.util.extensions.visible
import br.com.gdgbrasilia.meetup.app.view.viewmodel.PoliticaViewModel
import kotlinx.android.synthetic.main.activity_termos.*
import kotlinx.android.synthetic.main.toolbar_main.*

class TermosActivity : AppCompatActivity() {

    private val politicaViewModel by lazy { getViewModel(PoliticaViewModel::class.java) }
    private var isInitial = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_termos)
        setupToolbar()
        politicaViewModel.fetchTermos { termosText.text = Html.fromHtml(it.first().politica) }

        isInitial = intent.extras?.getBoolean(IntentFieldNames.IS_INITIAL_FLOW) ?: false

        if (isInitial) {
            confirmLayout.visible()
            entrarBtn.setOnClickListener {
                if (agreeCheckbox.isChecked) {
                    startActivity(MainActivity::class.java)
                    finish()
                } else {
                    Toast.makeText(this, "É necessário estar de acordo com os termos", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Termos"
    }
}
