package br.com.gdgbrasilia.meetup.app.view.viewholder

import android.app.AlertDialog
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import br.com.gdgbrasilia.meetup.app.business.model.IntentFieldNames
import br.com.gdgbrasilia.meetup.app.business.model.ViewType
import br.com.gdgbrasilia.meetup.app.business.vo.Estabelecimento
import br.com.gdgbrasilia.meetup.app.business.vo.Evento
import br.com.gdgbrasilia.meetup.app.data.AppConstants.BASE_URL
import br.com.gdgbrasilia.meetup.app.util.extensions.loadImg
import br.com.gdgbrasilia.meetup.app.util.extensions.startActivity
import br.com.gdgbrasilia.meetup.app.view.activities.EstabelecimentoActivity
import br.com.gdgbrasilia.meetup.app.view.activities.EventoActivity
import br.com.gdgbrasilia.meetup.app.view.activities.cadastro.NovoEstabelecimentoActivity
import br.com.gdgbrasilia.meetup.app.view.activities.cadastro.NovoEventoActivity
import br.com.gdgbrasilia.meetup.app.view.viewmodel.EstabelecimentoViewModel
import br.com.gdgbrasilia.meetup.app.view.viewmodel.EventoViewModel
import kotlinx.android.synthetic.main.holder_minhas_publicacoes.view.*

class MinhasPublicacoesViewHolder(view: View, val context: Context, val listener: (Boolean) -> Unit) : RecyclerView.ViewHolder(view), View.OnClickListener {

    private val eventoViewModel by lazy { ViewModelProviders.of(context as AppCompatActivity).get(EventoViewModel::class.java) }
    private val estabelecimentoViewModel by lazy { ViewModelProviders.of(context as AppCompatActivity).get(EstabelecimentoViewModel::class.java) }

    val localPicture = view.localPicture
    val localName = view.localName
    val localTipo = view.localTipo
    val localNota = view.localNota
    val ativoSwitch = view.ativarSwitch
    val ativarLabel = view.ativarLabel
    val editarBtn = view.editarBtn
    val excluirBtn = view.excluirBtn

    var local: ViewType? = null

    init {
        view.setOnClickListener(this)
    }

    fun bind(local: ViewType) {
        this.local = local
        if (local is Estabelecimento) {
            localPicture.loadImg("$BASE_URL${local.fotos.firstOrNull()?.url}")
            localName.text = local.nome
            localTipo.text = local.tipoEstabelecimento.nome
            localNota.text = String.format("%.2f", local.avaliacao)
            ativoSwitch.isChecked = local.ativo
            ativarLabel.text = if (local.ativo) "Ativada" else "Desativada"

            ativoSwitch.setOnClickListener {
                local.ativo = ativoSwitch.isChecked
                estabelecimentoViewModel.saveEstabelecimento(local) { success ->
                    if (success) {
                        ativarLabel.text = if (local.ativo) "Ativada" else "Desativada"
                    } else {
                        local.ativo = !local.ativo
                    }
                }
            }

            editarBtn.setOnClickListener {
                val bundle = Bundle()
                bundle.putSerializable(IntentFieldNames.ESTABELECIMENTO, local)
                bundle.putSerializable(IntentFieldNames.TIPO_ESTABELECIMENTO, local.tipoEstabelecimento)
                context.startActivity(NovoEstabelecimentoActivity::class.java, bundle)
            }

            excluirBtn.setOnClickListener {
                AlertDialog.Builder(context)
                        .setTitle("Excluir")
                        .setMessage("Você deseja realmente excluir este estabelecimento? Isso não pode ser desfeito.")
                        .setPositiveButton("Sim") { dialog, which ->
                            estabelecimentoViewModel.excluir(local.estabelecimentoID, listener)
                        }
                        .setNegativeButton("Não", null)
                        .show()
            }
        } else if (local is Evento) {
            localPicture.loadImg("$BASE_URL${local.fotos.firstOrNull()?.url}")
            localName.text = local.nome
            localTipo.text = local.tipoEvento.nome
            localNota.text = String.format("%.2f", local.avaliacao)
            ativoSwitch.isChecked = local.ativo
            ativarLabel.text = if (local.ativo) "Ativada" else "Desativada"

            ativoSwitch.setOnClickListener {
                local.ativo = ativoSwitch.isChecked
                eventoViewModel.saveEvento(local) { success ->
                    if (success) {
                        ativarLabel.text = if (local.ativo) "Ativada" else "Desativada"
                    } else {
                        local.ativo = !local.ativo
                    }
                }
            }

            editarBtn.setOnClickListener {
                val bundle = Bundle()
                bundle.putSerializable(IntentFieldNames.EVENTO, local)
                bundle.putSerializable(IntentFieldNames.TIPO_EVENTO, local.tipoEvento)
                context.startActivity(NovoEventoActivity::class.java, bundle)
            }

            excluirBtn.setOnClickListener {
                AlertDialog.Builder(context)
                        .setTitle("Excluir")
                        .setMessage("Você deseja realmente excluir este evento? Isso não pode ser desfeito.")
                        .setPositiveButton("Sim") { dialog, which ->
                            eventoViewModel.excluir(local.eventoID, listener)
                        }
                        .setNegativeButton("Não", null)
                        .show()
            }
        }
    }

    override fun onClick(view: View?) {
        if (local is Estabelecimento) {
            val bundle = Bundle()
            bundle.putSerializable(IntentFieldNames.ESTABELECIMENTO, local as Estabelecimento)
            context.startActivity(EstabelecimentoActivity::class.java, bundle)
        } else if (local is Evento) {
            val bundle = Bundle()
            bundle.putSerializable(IntentFieldNames.EVENTO, local as Evento)
            context.startActivity(EventoActivity::class.java, bundle)
        }
    }
}