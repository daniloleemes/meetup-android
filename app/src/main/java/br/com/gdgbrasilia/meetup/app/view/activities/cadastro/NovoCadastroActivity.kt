package br.com.gdgbrasilia.meetup.app.view.activities.cadastro

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import br.com.gdgbrasilia.meetup.app.R
import br.com.gdgbrasilia.meetup.app.business.model.IntentFieldNames
import br.com.gdgbrasilia.meetup.app.business.model.NovoEventoListener
import br.com.gdgbrasilia.meetup.app.business.vo.MarkerType
import br.com.gdgbrasilia.meetup.app.business.vo.TipoEstabelecimento
import br.com.gdgbrasilia.meetup.app.business.vo.TipoEvento
import br.com.gdgbrasilia.meetup.app.util.extensions.getViewModel
import br.com.gdgbrasilia.meetup.app.view.adapters.CategoriaNovoCadastroAdapter
import br.com.gdgbrasilia.meetup.app.view.viewmodel.EstabelecimentoViewModel
import br.com.gdgbrasilia.meetup.app.view.viewmodel.EventoViewModel
import kotlinx.android.synthetic.main.activity_novo_cadastro.*
import kotlinx.android.synthetic.main.toolbar_main.*

class NovoCadastroActivity : AppCompatActivity(), NovoEventoListener {

    private val estabelecimentoViewModel by lazy { getViewModel(EstabelecimentoViewModel::class.java) }
    private val eventoViewModel by lazy { getViewModel(EventoViewModel::class.java) }
    private var isEstabelecimento: MutableLiveData<Boolean> = MutableLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novo_cadastro)
        isEstabelecimento.value = true
        isEstabelecimento.observe(this, Observer { value ->
            if (value == false) {
                setupList(eventoViewModel.tiposEvento.value ?: listOf())
            } else {
                setupList(estabelecimentoViewModel.tiposEstabelecimento.value ?: listOf())
            }
        })

        setupToolbar()
        setupToggles()

        fetchList()

        swipeLayout.setOnRefreshListener {
            fetchList()
        }

    }

    fun fetchList() {
        swipeLayout.isRefreshing = true
        estabelecimentoViewModel.fetchTiposEstabelecimento().observe(this, Observer { tipos ->
            swipeLayout.isRefreshing = false
            tipos?.let {
                if (isEstabelecimento.value == true) {
                    setupList(it)
                }
            }
        })
        eventoViewModel.fetchTiposEvento().observe(this, Observer { tipos ->
            swipeLayout.isRefreshing = false
            tipos?.let {
                if (isEstabelecimento.value == false) {
                    setupList(it)
                }
            }
        })
    }

    private fun setupList(list: List<MarkerType>) {
        categoriasRecycler.adapter = CategoriaNovoCadastroAdapter(list, this, this)
        categoriasRecycler.layoutManager = GridLayoutManager(this, 3)
    }

    private fun setupToggles() {
        estabelecimentoToggle.setOnClickListener {
            estabelecimentoToggle.isChecked = true
            eventoToggle.isChecked = false
            isEstabelecimento.value = true
        }

        eventoToggle.setOnClickListener {
            eventoToggle.isChecked = true
            estabelecimentoToggle.isChecked = false
            isEstabelecimento.value = false
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Novo Cadastro"
    }

    override fun handle(tipo: MarkerType) {
        lateinit var intent: Intent
        if (tipo is TipoEstabelecimento) {
            if (isEstabelecimento.value == true) {
                intent = Intent(this, NovoEstabelecimentoActivity::class.java)
                intent.putExtra(IntentFieldNames.LAT, this.intent.extras.getSerializable(IntentFieldNames.LAT))
                intent.putExtra(IntentFieldNames.LON, this.intent.extras.getSerializable(IntentFieldNames.LON))
                intent.putExtra(IntentFieldNames.TIPO_ESTABELECIMENTO, tipo)
            }
        } else if (tipo is TipoEvento) {
            intent = Intent(this, NovoEventoActivity::class.java)
            intent.putExtra(IntentFieldNames.LAT, this.intent.extras.getSerializable(IntentFieldNames.LAT))
            intent.putExtra(IntentFieldNames.LON, this.intent.extras.getSerializable(IntentFieldNames.LON))
            intent.putExtra(IntentFieldNames.TIPO_EVENTO, tipo)
        }
        startActivity(intent)
        finish()
    }

}
