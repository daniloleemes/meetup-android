package br.com.gdgbrasilia.meetup.app.view.activities.cadastro

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import br.com.happyin.app.util.MaskUtil
import br.com.gdgbrasilia.meetup.app.R
import br.com.gdgbrasilia.meetup.app.business.model.IntentFieldNames
import br.com.gdgbrasilia.meetup.app.business.vo.*
import br.com.gdgbrasilia.meetup.app.data.AppStatics
import br.com.gdgbrasilia.meetup.app.util.DialogUtil
import br.com.gdgbrasilia.meetup.app.util.extensions.*
import br.com.gdgbrasilia.meetup.app.view.adapters.HorarioEventoAdapter
import br.com.gdgbrasilia.meetup.app.view.adapters.LocalPictureAdapter
import br.com.gdgbrasilia.meetup.app.view.bottomsheet.BottomsheetFotoPicker
import br.com.gdgbrasilia.meetup.app.view.bottomsheet.FotoPickerListener
import br.com.gdgbrasilia.meetup.app.view.viewholder.LocalPictureSelectListener
import br.com.gdgbrasilia.meetup.app.view.viewholder.LocalPictureViewHolder
import br.com.gdgbrasilia.meetup.app.view.viewmodel.EstabelecimentoViewModel
import br.com.gdgbrasilia.meetup.app.view.viewmodel.EventoViewModel
import br.com.gdgbrasilia.meetup.app.view.viewmodel.PictureViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_novo_evento.*
import kotlinx.android.synthetic.main.toolbar_main.*
import java.text.SimpleDateFormat
import java.util.*

class NovoEventoActivity : AppCompatActivity(), LocalPictureSelectListener, FotoPickerListener {

    private lateinit var tipoEvento: TipoEvento
    private lateinit var coordenadas: LatLng
    private lateinit var localImageView: ImageView
    private lateinit var localPictureDTO: LocalPictureDTO
    private lateinit var localPictureViewHolder: LocalPictureViewHolder

    private val estabelecimentoViewModel by lazy { getViewModel(EstabelecimentoViewModel::class.java) }
    private val eventoViewModel by lazy { getViewModel(EventoViewModel::class.java) }
    private val pictureViewModel by lazy { getViewModel(PictureViewModel::class.java) }

    private var doubleBackToExitPressedOnce = false
    private var endereco: Endereco? = null
    private var imageUri: Uri? = null
    private var evento: Evento? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novo_evento)

        tipoEvento = intent.extras.getSerializable(IntentFieldNames.TIPO_EVENTO) as TipoEvento
        evento = intent.extras.getSerializable(IntentFieldNames.EVENTO) as? Evento
        val lat = this.intent.extras.getSerializable(IntentFieldNames.LAT) as? String ?: "0"
        val lon = this.intent.extras.getSerializable(IntentFieldNames.LON) as? String ?: "0"

        coordenadas = LatLng(lat.toDouble(), lon.toDouble())

        setupToolbar()
        setupWatchers()
        setupListeners()
        setupHorarios()
        setupPictureRecycler()
        setupEditData(evento)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        pictureViewModel.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        pictureViewModel.onActivityResult(this, requestCode, resultCode, data) { imageUri ->
            this.imageUri = imageUri
            this.localPictureDTO.uri = imageUri
            this.localPictureViewHolder.bind(this.localPictureDTO)
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Pressione novamente para voltar", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    override fun onPictureSelected(localPicture: ImageView, localPictureDTO: LocalPictureDTO, localPictureViewHolder: LocalPictureViewHolder) {
        this.localImageView = localPicture
        this.localPictureDTO = localPictureDTO
        this.localPictureViewHolder = localPictureViewHolder

        val bottomsheet = BottomsheetFotoPicker()
        bottomsheet.show(supportFragmentManager, "PICKER_SHEET")
    }


    override fun onGalleryPicked() {
        pictureViewModel.checkGalleryPermission(this)
    }

    override fun onCameraPicked() {
        pictureViewModel.checkCameraPermission(this)
    }

    private fun setupHorarios() {
        horariosRecycler.adapter = HorarioEventoAdapter(this)
        horariosRecycler.layoutManager = LinearLayoutManager(this)
    }

    private fun setupEditData(evento: Evento?) {
        evento?.let {
            title = "Evento"
            this.endereco = it.endereco
            coordenadas = LatLng(it.endereco.latitude, it.endereco.longitude)
            eventoNome.setText(it.nome)
            eventoEmail.setText(it.email)
            eventoFone.setText(it.telefone)
            eventoCEP.setText(it.endereco.CEP)
            eventoLogradouro.setText(it.endereco.logradouro)
            eventoDescricao.setText(it.descricao)
            eventoSite.setText(it.site)
            eventoFacebook.setText(it.facebook)
            eventoTwitter.setText(it.twitter)
            eventoInstagram.setText(it.instagram)
            eventoYoutube.setText(it.youtube)

            petRadioGroup.check(if (it.aceitaPet) R.id.petSim else R.id.petNao)
            entradaRadioGroup.check(if (it.valorEntrada > 0) R.id.entradaPaga else R.id.entradaGratuita)

            (horariosRecycler.adapter as HorarioEventoAdapter).list.clear()
            (horariosRecycler.adapter as HorarioEventoAdapter).addAll(evento.horarios.filter { it.horaAbertura > 0 && it.horaFechamento > 0 })
            val params = horarioWrapper.layoutParams
            params.height = (evento.horarios.count()) * (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50f, resources.displayMetrics)).toInt()
        }
    }

    private fun validateFields(): EventoDTO? {
        val visualizacao = findViewById<RadioButton>(visualizacaoRadioGroup.checkedRadioButtonId).text
        val entrada = findViewById<RadioButton>(entradaRadioGroup.checkedRadioButtonId).text
        val pet = findViewById<RadioButton>(petRadioGroup.checkedRadioButtonId).text

        val nome = eventoNome.text.toString()
        val email = eventoEmail.text.toString()
        val telefone = eventoFone.text.toString()
        val cep = eventoCEP.text.toString()
        val descricao = eventoDescricao.text.toString()
        val site = eventoSite.text.toString()
        val facebook = eventoFacebook.text.toString()
        val twitter = eventoTwitter.text.toString()
        val instagram = eventoInstagram.text.toString()
        val youtube = eventoYoutube.text.toString()
        val endereco: Endereco

        val fields = listOf(eventoNome,
                eventoCEP,
                eventoLogradouro,
                eventoDescricao)
        val horarios = (horariosRecycler.adapter as HorarioEventoAdapter).list

        if (fields.any { it.text.isEmpty() }) {
            Toast.makeText(this, "${fields.first { it.text.isEmpty() }.tag} não preenchido", Toast.LENGTH_LONG).show()
        } else if (horarios.none { it.horaAbertura > 0 && it.horaFechamento > 0 }) {
            Toast.makeText(this, "Pelo menos um horário é necessário", Toast.LENGTH_LONG).show()
        } else {
            if (!email.isValidEmail()) {
                Toast.makeText(this, "E-mail inválido", Toast.LENGTH_LONG).show()
            } else {
                endereco = this.endereco!!

                val enderecoDTO = EnderecoDTO(nome = "endereco",
                        CEP = endereco.CEP,
                        logradouro = endereco.logradouro,
                        complemento = "",
                        numero = "0",
                        estadoID = endereco.estadoID,
                        cidadeID = endereco.cidadeID,
                        bairro = endereco.bairro,
                        principal = true,
                        latitude = coordenadas.latitude,
                        longitude = coordenadas.longitude)

                val eventoDTO = EventoDTO(nome = nome,
                        email = email,
                        descricao = descricao,
                        tipoEventoID = tipoEvento.tipoEventoID,
                        telefone = telefone,
                        valorEntrada = if (entrada == "Sim") 1.0 else 0.0,
                        ativo = true,
                        aceitaPet = pet == "Sim",
                        privado = visualizacao == "Privada",
                        site = site,
                        twitter = twitter,
                        facebook = facebook,
                        instagram = instagram,
                        youtube = youtube,
                        endereco = enderecoDTO,
                        horarios = horarios.filter { it.horaAbertura > 0 && it.horaFechamento > 0 }.map {
                            HorarioDTO(data = it.data,
                                    diaDaSemana = it.diaDaSemana,
                                    horaAbertura = it.horaAbertura.displayAsHour(),
                                    horaFechamento = it.horaFechamento.displayAsHour())
                        })

                return eventoDTO
            }
        }

        return null
    }

    private fun setupListeners() {
        adicionaHorarioBtn.setOnClickListener {
            val adapter = horariosRecycler.adapter as HorarioEventoAdapter
            val item = adapter.list.last()
            if (item.horaAbertura > 0 && item.horaFechamento > 0 && item.data.isNotEmpty() && SimpleDateFormat("dd/MM/yyyy").parse(item.data).after(Date())) {
                adapter.add()
            } else {
                Toast.makeText(this, "Data inválida", Toast.LENGTH_SHORT).show()
            }


            if (adapter.list.size > 1) {
                removeHorarioBtn.visible()
            }

            val params = horarioWrapper.layoutParams
            params.height = (horariosRecycler.childCount + 1) * (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50f, resources.displayMetrics)).toInt()
        }

        removeHorarioBtn.setOnClickListener {
            val adapter = horariosRecycler.adapter as HorarioEventoAdapter
            adapter.remove()

            if (adapter.list.size < 2) {
                removeHorarioBtn.gone()
            }

            val params = horarioWrapper.layoutParams
            params.height = (horariosRecycler.childCount - 1) * (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50f, resources.displayMetrics)).toInt()
        }

        buscarEnderecoBtn.setOnClickListener(buscarEnderecoClick())
        publicarBtn.setOnClickListener {
            val evento = validateFields()
            val fotosUri = (eventoFotosRecycler.adapter as LocalPictureAdapter).items.filter { (it as? LocalPictureDTO)?.uri != null }.map { it as LocalPictureDTO }

            if (fotosUri.isEmpty()) {
                Toast.makeText(this, "Ao menos uma foto é necessária", Toast.LENGTH_LONG).show()
            } else {
                if (evento != null) {
                    val dialog = DialogUtil.getDialog(this, "Aguarde...")
                    dialog.show()
                    eventoViewModel.saveEvento(this, evento, fotosUri) { sucesso ->
                        dialog.dismiss()
                        if (sucesso) {
                            Toast.makeText(this, "Evento salvo com sucesso", Toast.LENGTH_LONG).show()
                            finish()
                            AppStatics.refreshMap = true
                        } else {
                            Toast.makeText(this, "Erro ao salvar Evento", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    private fun buscarEnderecoClick(): (View) -> Unit {
        return {
            if (eventoCEP.text.isNotEmpty()) {
                estabelecimentoViewModel.consultarCEP(eventoCEP.text.toString()) { endereco ->
                    if (endereco != null) {
                        this.endereco = endereco
                        this.eventoLogradouro.setText(endereco.logradouro)
                    }
                }
            }
        }
    }

    private fun setupWatchers() {
        eventoFone.addTextChangedListener(MaskUtil.insert("(##) #####-####", eventoFone))
        eventoCEP.addTextChangedListener(MaskUtil.insertCEP(eventoCEP))
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Novo Evento"
    }

    private fun setupPictureRecycler() {
        eventoFotosRecycler.adapter = LocalPictureAdapter(this)
        eventoFotosRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        eventoFotosRecycler.setHasFixedSize(true)
    }
}
