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
import br.com.gdgbrasilia.meetup.app.business.vo.Estabelecimento
import br.com.gdgbrasilia.meetup.app.data.AppConstants.BASE_URL
import br.com.gdgbrasilia.meetup.app.util.extensions.*
import br.com.gdgbrasilia.meetup.app.view.adapters.ViewPagerAdapter
import br.com.gdgbrasilia.meetup.app.view.fragments.estabelecimento.EstabelecimentoAvaliacoesFragment
import br.com.gdgbrasilia.meetup.app.view.fragments.estabelecimento.EstabelecimentoComoChegarFragment
import br.com.gdgbrasilia.meetup.app.view.fragments.estabelecimento.EstabelecimentoSobreFragment
import br.com.gdgbrasilia.meetup.app.view.viewmodel.EstabelecimentoViewModel
import kotlinx.android.synthetic.main.activity_estabelecimento.*
import kotlinx.android.synthetic.main.toolbar_main.*

class EstabelecimentoActivity : AppCompatActivity() {

    private lateinit var estabelecimento: Estabelecimento
    private val viewPagerAdapter by lazy { ViewPagerAdapter(supportFragmentManager) }
    private val estabelecimentoViewModel by lazy { getViewModel(EstabelecimentoViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estabelecimento)
        setupToolbar()

        estabelecimento = intent.extras.getSerializable(IntentFieldNames.ESTABELECIMENTO) as Estabelecimento

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
                bundle.putInt(IntentFieldNames.VIEW_TYPE, ViewTypes.ESTABELECIMENTO)
                bundle.putString(IntentFieldNames.ITEM_ID, estabelecimento.estabelecimentoID)
                startActivity(DenunciaActivity::class.java, bundle)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun compartilhar() {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Dê uma olhada neste estabelecimento\n" +
                "${estabelecimento.nome}\n" +
                "${estabelecimento.descricao}\n" +
                "${estabelecimento.endereco.logradouro}, ${estabelecimento.endereco.bairro}, ${estabelecimento.endereco.cidadeNome}")
        sendIntent.type = "text/plain"
        startActivity(Intent.createChooser(sendIntent, resources.getText(R.string.send_to)))
    }

    private fun setupViews() {
        estabelecimentoPicture.loadImg(BASE_URL + estabelecimento.fotos.firstOrNull()?.url)
        estabelecimentoPicture.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(IntentFieldNames.FOTOS, estabelecimento.fotos as ArrayList<AppFoto>)
            startActivity(GalleryAcitivity::class.java, bundle)
        }
        estabelecimentoName.text = estabelecimento.nome
        estabelecimentoTipo.text = estabelecimento.tipoEstabelecimento.nome
        estabelecimentoUltimaAtualizacao.text = "Atualizado por ${estabelecimento.usuario.nome} em ${estabelecimento.dataAtualizacao.displayAsDate()}"
        estabelecimentoNota.text = String.format("%.2f", estabelecimento.avaliacao)

        favoritoBtn.setOnClickListener {
            estabelecimentoViewModel.marcarFavorito(estabelecimento.estabelecimentoID, !estabelecimento.favorito) { sucesso ->
                if (sucesso) {
                    estabelecimento.favorito = !estabelecimento.favorito
                    favoritoBtn.setMarked(estabelecimento.favorito, R.color.colorPink)
                }
            }
        }

        jaFuiSwitch.setOnClickListener {
            estabelecimentoViewModel.marcarVisita(estabelecimento.estabelecimentoID, jaFuiSwitch.isChecked) { sucesso ->
                jaFuiSwitch.isChecked = sucesso
            }
        }

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
        val sobreFragment = EstabelecimentoSobreFragment()
        setArguments(sobreFragment)
        viewPagerAdapter.addFragment(sobreFragment, "Sobre")
    }

    private fun addAvaliacoesFragment() {
        val avaliacoesFragment = EstabelecimentoAvaliacoesFragment()
        setArguments(avaliacoesFragment)
        viewPagerAdapter.addFragment(avaliacoesFragment, "Avaliações")
    }

    private fun addComoChegarFragment() {
        val comoChegarFragment = EstabelecimentoComoChegarFragment()
        setArguments(comoChegarFragment)
        viewPagerAdapter.addFragment(comoChegarFragment, "Como chegar")
    }

    private fun setArguments(fragment: Fragment) {
        val args = Bundle()
        args.putSerializable(IntentFieldNames.ESTABELECIMENTO, estabelecimento)
        fragment.arguments = args
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Estabelecimento"
    }
}
