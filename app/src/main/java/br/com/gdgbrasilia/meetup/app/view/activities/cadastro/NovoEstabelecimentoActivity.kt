package br.com.gdgbrasilia.meetup.app.view.activities.cadastro

import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TimePicker
import android.widget.Toast
import br.com.happyin.app.util.MaskUtil
import br.com.gdgbrasilia.meetup.app.R
import br.com.gdgbrasilia.meetup.app.business.model.IntentFieldNames
import br.com.gdgbrasilia.meetup.app.business.vo.*
import br.com.gdgbrasilia.meetup.app.data.AppStatics
import br.com.gdgbrasilia.meetup.app.util.DialogUtil
import br.com.gdgbrasilia.meetup.app.util.extensions.displayAsHour
import br.com.gdgbrasilia.meetup.app.util.extensions.getViewModel
import br.com.gdgbrasilia.meetup.app.util.extensions.isValidEmail
import br.com.gdgbrasilia.meetup.app.view.adapters.LocalPictureAdapter
import br.com.gdgbrasilia.meetup.app.view.bottomsheet.BottomsheetFotoPicker
import br.com.gdgbrasilia.meetup.app.view.bottomsheet.FotoPickerListener
import br.com.gdgbrasilia.meetup.app.view.viewholder.LocalPictureSelectListener
import br.com.gdgbrasilia.meetup.app.view.viewholder.LocalPictureViewHolder
import br.com.gdgbrasilia.meetup.app.view.viewmodel.EstabelecimentoViewModel
import br.com.gdgbrasilia.meetup.app.view.viewmodel.PictureViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_novo_estabelecimento.*
import kotlinx.android.synthetic.main.toolbar_main.*
import org.jetbrains.anko.collections.forEachWithIndex
import java.util.*
import java.util.concurrent.TimeUnit

class NovoEstabelecimentoActivity : AppCompatActivity(), LocalPictureSelectListener, FotoPickerListener {


    private lateinit var tipoEstabelecimento: TipoEstabelecimento
    private lateinit var coordenadas: LatLng
    private lateinit var localImageView: ImageView
    private lateinit var localPictureDTO: LocalPictureDTO
    private lateinit var localPictureViewHolder: LocalPictureViewHolder

    private val estabelecimentoViewModel by lazy { getViewModel(EstabelecimentoViewModel::class.java) }
    private val pictureViewModel by lazy { getViewModel(PictureViewModel::class.java) }
    private val horarios: MutableList<Horario> = mutableListOf()

    private var endereco: Endereco? = null
    private var estabelecimento: Estabelecimento? = null
    private var imageUri: Uri? = null
    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novo_estabelecimento)

        tipoEstabelecimento = intent.extras.getSerializable(IntentFieldNames.TIPO_ESTABELECIMENTO) as TipoEstabelecimento
        estabelecimento = intent.extras.getSerializable(IntentFieldNames.ESTABELECIMENTO) as? Estabelecimento

        val lat = this.intent.extras.getSerializable(IntentFieldNames.LAT) as? String ?: "0"
        val lon = this.intent.extras.getSerializable(IntentFieldNames.LON) as? String ?: "0"

        coordenadas = LatLng(lat.toDouble(), lon.toDouble())

        setupToolbar()
        setupListeners()
        setupWatchers()
        setupPictureRecycler()
        setupEditData(estabelecimento)
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

    private fun setupEditData(estabelecimento: Estabelecimento?) {
        estabelecimento?.let {
            title = "Estabelecimento"
            this.endereco = it.endereco
            coordenadas = LatLng(it.endereco.latitude, it.endereco.longitude)
            estabelecimentoName.setText(it.nome)
            estabelecimentoCNPJ.setText(it.cnpj)
            estabelecimentoEmail.setText(it.email)
            estabelecimentoFone.setText(it.telefone)
            estabelecimentoCEP.setText(it.endereco.CEP)
            estabelecimentoLogradouro.setText(it.endereco.logradouro)
            estabelecimentoDescricao.setText(it.descricao)
            estabelecimentoSite.setText(it.site)
            estabelecimentoFacebook.setText(it.facebook)
            estabelecimentoTwitter.setText(it.twitter)
            estabelecimentoInstagram.setText(it.instagram)
            estabelecimentoYoutube.setText(it.youtube)

            petRadioGroup.check(if (it.aceitaPet) R.id.petSim else R.id.petNao)
            entradaRadioGroup.check(if (it.valorEntrada > 0) R.id.entradaPaga else R.id.entradaGratuita)

            val abertura = listOf(aberturaDom, aberturaSeg, aberturaTer, aberturaQua, aberturaQui, aberturaSex, aberturaSab)
            val fechamento = listOf(fechamentoDom, fechamentoSeg, fechamentoTer, fechamentoQua, fechamentoQui, fechamentoSex, fechamentoSab)

            it.horarios
                    .filter { it.horaAbertura > 0 && it.horaFechamento > 0 }
                    .forEach { horario ->
                        abertura[horario.diaDaSemana - 1].text = horario.horaAbertura.displayAsHour()
                        fechamento[horario.diaDaSemana - 1].text = horario.horaFechamento.displayAsHour()
                    }
        }
    }

    private fun validateFields(): EstabelecimentoDTO? {
        val duracao = findViewById<RadioButton>(duracaoRadioGroup.checkedRadioButtonId).text
        val feriado = findViewById<RadioButton>(feriadoRadioGroup.checkedRadioButtonId).text
        val entrada = findViewById<RadioButton>(entradaRadioGroup.checkedRadioButtonId).text
        val pet = findViewById<RadioButton>(petRadioGroup.checkedRadioButtonId).text

        val nome = estabelecimentoName.text.toString()
        val cnpj = estabelecimentoCNPJ.text.toString()
        val email = estabelecimentoEmail.text.toString()
        val telefone = estabelecimentoFone.text.toString()
        val cep = estabelecimentoCEP.text.toString()
        val descricao = estabelecimentoDescricao.text.toString()
        val site = estabelecimentoSite.text.toString()
        val facebook = estabelecimentoFacebook.text.toString()
        val twitter = estabelecimentoTwitter.text.toString()
        val instagram = estabelecimentoInstagram.text.toString()
        val youtube = estabelecimentoYoutube.text.toString()

        val fields = listOf(estabelecimentoName,
                estabelecimentoCNPJ,
                estabelecimentoFone,
                estabelecimentoEmail,
                estabelecimentoCEP,
                estabelecimentoLogradouro,
                estabelecimentoDescricao)

        if (fields.any { it.text.isEmpty() }) {
            Toast.makeText(this, "${fields.first { it.text.isEmpty() }.tag} não preenchido", Toast.LENGTH_LONG).show()
        } else if (horarios.none { it.horaAbertura > 0 && it.horaFechamento > 0 }) {
            Toast.makeText(this, "Pelo menos um horário é necessário", Toast.LENGTH_LONG).show()
        } else {
            if (!email.isValidEmail()) {
                Toast.makeText(this, "E-mail inválido", Toast.LENGTH_LONG).show()
            } else {
                val endereco = this.endereco!!

                val enderecoDTO = EnderecoDTO(
                        nome = "endereco",
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

                val estabelecimentoDTO = EstabelecimentoDTO(nome = nome,
                        email = email,
                        descricao = descricao,
                        tipoEstabelecimentoID = tipoEstabelecimento.tipoEstabelecimentoID,
                        telefone = telefone,
                        valorEntrada = if (entrada == "Sim") 1.0 else 0.0,
                        ativo = true,
                        aceitaPet = pet == "Sim",
                        site = site,
                        twitter = twitter,
                        facebook = facebook,
                        instagram = instagram,
                        youtube = youtube,
                        endereco = enderecoDTO,
                        horarios = horarios
                                .filter { it.horaAbertura > 0 && it.horaFechamento > 0 }
                                .map {
                                    HorarioDTO(data = it.data,
                                            diaDaSemana = it.diaDaSemana,
                                            horaAbertura = it.horaAbertura.displayAsHour(),
                                            horaFechamento = it.horaFechamento.displayAsHour())
                                })

                return estabelecimentoDTO
            }
        }

        return null
    }

    private fun setupListeners() {
        buscarEnderecoBtn.setOnClickListener(buscarEnderecoClick())
        publicarBtn.setOnClickListener {
            val estabelecimento = validateFields()
            val fotosUri = (estabelecimentoFotosRecycler.adapter as LocalPictureAdapter).items.filter { (it as? LocalPictureDTO)?.uri != null }.map { it as LocalPictureDTO }

            if (fotosUri.isEmpty()) {
                Toast.makeText(this, "Ao menos uma foto é necessária", Toast.LENGTH_LONG).show()
            } else {
                if (estabelecimento != null) {
                    val dialog = DialogUtil.getDialog(this, "Aguarde...")
                    dialog.show()
                    estabelecimentoViewModel.saveEstabelecimento(this, estabelecimento, fotosUri) { sucesso ->
                        dialog.dismiss()
                        if (sucesso) {
                            Toast.makeText(this, "Estabelecimento salvo com sucesso", Toast.LENGTH_LONG).show()
                            finish()
                            AppStatics.refreshMap = true
                        } else {
                            Toast.makeText(this, "Erro ao salvar Estabelecimento", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }

        }
        setupHorario()
    }

    private fun setupHorario() {
        val abertura = listOf(aberturaDom, aberturaSeg, aberturaTer, aberturaQua, aberturaQui, aberturaSex, aberturaSab)
        val fechamento = listOf(fechamentoDom, fechamentoSeg, fechamentoTer, fechamentoQua, fechamentoQui, fechamentoSex, fechamentoSab)

        for (i in 1..7) {
            horarios.add(Horario("", "", i, 0L, 0L))
        }

        abertura.forEachWithIndex { index, element ->
            element.setOnClickListener { view ->
                pickHour { timePicker, selectedHour, selectedMinutes ->
                    (view as AppCompatTextView).text = "${String.format("%02d", selectedHour)}:${String.format("%02d", selectedMinutes)}"
                    val minutes = ((if (selectedHour == 0) 24 else selectedHour) * 60) + selectedMinutes
                    horarios[index] = horarios[index].copy(horaAbertura = TimeUnit.MINUTES.toMillis(minutes.toLong()))
                }
            }
        }

        fechamento.forEachWithIndex { index, element ->
            element.setOnClickListener { view ->
                pickHour { timePicker, selectedHour, selectedMinutes ->
                    (view as AppCompatTextView).text = "${String.format("%02d", selectedHour)}:${String.format("%02d", selectedMinutes)}"
                    val minutes = ((if (selectedHour == 0) 24 else selectedHour) * 60) + selectedMinutes
                    horarios[index] = horarios[index].copy(horaFechamento = TimeUnit.MINUTES.toMillis(minutes.toLong()))
                }
            }
        }
    }

    private fun buscarEnderecoClick(): (View) -> Unit {
        return {
            if (estabelecimentoCEP.text.isNotEmpty()) {
                estabelecimentoViewModel.consultarCEP(estabelecimentoCEP.text.toString()) { endereco ->
                    if (endereco != null) {
                        this.endereco = endereco
                        this.estabelecimentoLogradouro.setText(endereco.logradouro)
                    }
                }
            }
        }
    }

    private fun setupWatchers() {
        estabelecimentoCNPJ.addTextChangedListener(MaskUtil.insertCPFNJ(estabelecimentoCNPJ))
        estabelecimentoFone.addTextChangedListener(MaskUtil.insert("(##) #####-####", estabelecimentoFone))
        estabelecimentoCEP.addTextChangedListener(MaskUtil.insertCEP(estabelecimentoCEP))
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Novo Estabelecimento"
    }

    private fun pickHour(listener: (timePicker: TimePicker, selectedHour: Int, selectedMinutes: Int) -> Unit) {
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrentTime.get(Calendar.MINUTE)
        val mTimePicker: TimePickerDialog
        mTimePicker = TimePickerDialog(this@NovoEstabelecimentoActivity, TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
            listener(timePicker, selectedHour, selectedMinute)
        }, hour, minute, true)//Yes 24 hour time
        mTimePicker.setTitle("Selecione o horário")
        mTimePicker.show()
    }

    private fun setupPictureRecycler() {
        estabelecimentoFotosRecycler.adapter = LocalPictureAdapter(this)
        estabelecimentoFotosRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        estabelecimentoFotosRecycler.setHasFixedSize(true)
    }
}
