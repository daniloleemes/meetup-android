package br.com.gdgbrasilia.meetup.app.view.activities

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import br.com.gdgbrasilia.meetup.app.R
import br.com.gdgbrasilia.meetup.app.business.model.IntentFieldNames
import br.com.gdgbrasilia.meetup.app.business.model.ViewTypes
import br.com.gdgbrasilia.meetup.app.business.vo.AppFoto
import br.com.gdgbrasilia.meetup.app.business.vo.Evento
import br.com.gdgbrasilia.meetup.app.data.AppConstants.BASE_URL
import br.com.gdgbrasilia.meetup.app.util.extensions.displayAsDate
import br.com.gdgbrasilia.meetup.app.util.extensions.loadImg
import br.com.gdgbrasilia.meetup.app.util.extensions.startActivity
import br.com.gdgbrasilia.meetup.app.view.adapters.ViewPagerAdapter
import br.com.gdgbrasilia.meetup.app.view.fragments.evento.EventoAvaliacoesFragment
import br.com.gdgbrasilia.meetup.app.view.fragments.evento.EventoComoChegarFragment
import br.com.gdgbrasilia.meetup.app.view.fragments.evento.EventoSobreFragment
import kotlinx.android.synthetic.main.activity_evento.*
import kotlinx.android.synthetic.main.toolbar_main.*


class EventoActivity : AppCompatActivity() {

    lateinit var evento: Evento
    private val viewPagerAdapter by lazy { ViewPagerAdapter(supportFragmentManager) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evento)
        setupToolbar()

        evento = intent.extras.getSerializable(IntentFieldNames.EVENTO) as Evento

        setupViews()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_estabelecimento_evento, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_compartilhar -> compartilhar()
            R.id.menu_denunciar -> {
                val bundle = Bundle()
                bundle.putInt(IntentFieldNames.VIEW_TYPE, ViewTypes.EVENTO)
                bundle.putString(IntentFieldNames.ITEM_ID, evento.eventoID)
                startActivity(DenunciaActivity::class.java, bundle)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun compartilhar() {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Dê uma olhada neste estabelecimento\n" +
                "${evento.nome}\n" +
                "${evento.descricao}\n" +
                "${evento.endereco.logradouro}, ${evento.endereco.bairro}, ${evento.endereco.cidadeNome}")
        sendIntent.type = "text/plain"
        startActivity(Intent.createChooser(sendIntent, resources.getText(R.string.send_to)))
    }

    private fun setupViews() {
        estabelecimentoPicture.loadImg(BASE_URL + evento.fotos.firstOrNull()?.url)
        estabelecimentoPicture.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(IntentFieldNames.FOTOS, evento.fotos as ArrayList<AppFoto>)
            startActivity(GalleryAcitivity::class.java, bundle)
        }
        estabelecimentoName.text = evento.nome
        estabelecimentoTipo.text = evento.tipoEvento.nome
        estabelecimentoUltimaAtualizacao.text = "Atualizado por ${evento.usuario.nome} em ${evento.dataAtualizacao.displayAsDate()}"
        setupViewPager()
    }

    private fun setupViewPager() {
        addSobreFragment()
        addAvaliacoesFragment()
        addComoChegarFragment()

        estabelecimentoViewPager.adapter = viewPagerAdapter
        estabelecimentoViewPager.offscreenPageLimit = 3
        estabelecimentoTabLayout.setupWithViewPager(estabelecimentoViewPager)
    }

    private fun addSobreFragment() {
        val sobreFragment = EventoSobreFragment()
        setArguments(sobreFragment)
        viewPagerAdapter.addFragment(sobreFragment, "Sobre")
    }

    private fun addAvaliacoesFragment() {
        val avaliacoesFragment = EventoAvaliacoesFragment()
        setArguments(avaliacoesFragment)
        viewPagerAdapter.addFragment(avaliacoesFragment, "Comentários")
    }

    private fun addComoChegarFragment() {
        val comoChegarFragment = EventoComoChegarFragment()
        setArguments(comoChegarFragment)
        viewPagerAdapter.addFragment(comoChegarFragment, "Como chegar")
    }

    private fun setArguments(fragment: Fragment) {
        val args = Bundle()
        args.putSerializable(IntentFieldNames.EVENTO, evento)
        fragment.arguments = args
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Evento"
    }
}
