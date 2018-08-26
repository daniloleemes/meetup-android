package br.com.gdgbrasilia.meetup.app.view.fragments.evento

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import br.com.gdgbrasilia.meetup.app.R
import br.com.gdgbrasilia.meetup.app.business.model.IntentFieldNames
import br.com.gdgbrasilia.meetup.app.business.vo.Avaliacao
import br.com.gdgbrasilia.meetup.app.business.vo.Evento
import br.com.gdgbrasilia.meetup.app.util.DialogUtil
import br.com.gdgbrasilia.meetup.app.util.extensions.getActivityViewModel
import br.com.gdgbrasilia.meetup.app.util.extensions.inflate
import br.com.gdgbrasilia.meetup.app.view.adapters.AvaliacaoAdapter
import br.com.gdgbrasilia.meetup.app.view.viewmodel.EventoViewModel
import kotlinx.android.synthetic.main.fragment_avaliacoes_evento.*

class EventoAvaliacoesFragment : Fragment() {

    private val eventoViewModel by lazy { getActivityViewModel(EventoViewModel::class.java) }
    private lateinit var evento: Evento

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            container?.inflate(R.layout.fragment_avaliacoes_evento)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        evento = arguments!!.getSerializable(IntentFieldNames.EVENTO) as Evento

        fetchAvaliacoes()

        enviarBtn.setOnClickListener {
            val avaliacao = avaliacaoRatingBar.progress
            val comentario = avaliacaoComentario.text.toString()
            if (comentario.isNotEmpty()) {
                val dialog = DialogUtil.getDialog(activity!!, "Aguarde...")
                dialog.show()
                eventoViewModel.avaliar(evento.eventoID, avaliacao, comentario) { sucesso ->
                    dialog.dismiss()
                    if (sucesso) {
                        resetForm()
                        fetchAvaliacoes()
                    } else {
                        Toast.makeText(activity, "Erro", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(activity, "Preencha os campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun fetchAvaliacoes() {
        eventoViewModel.fetchAvaliacoes(evento.eventoID) { avaliacoes ->
            if (avaliacoes != null && avaliacoes.isNotEmpty()) {
                avaliacoesRecycler.adapter = AvaliacaoAdapter(avaliacoes as MutableList<Avaliacao>)
                avaliacoesRecycler.layoutManager = LinearLayoutManager(activity)
            } else {

            }
        }
    }

    private fun resetForm() {
        avaliacaoComentario.text = null
        avaliacaoRatingBar.progress = 0
    }

}