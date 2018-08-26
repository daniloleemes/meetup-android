package br.com.gdgbrasilia.meetup.app.view.fragments.estabelecimento

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
import br.com.gdgbrasilia.meetup.app.business.vo.Estabelecimento
import br.com.gdgbrasilia.meetup.app.util.DialogUtil
import br.com.gdgbrasilia.meetup.app.util.extensions.getActivityViewModel
import br.com.gdgbrasilia.meetup.app.util.extensions.inflate
import br.com.gdgbrasilia.meetup.app.view.adapters.AvaliacaoAdapter
import br.com.gdgbrasilia.meetup.app.view.viewmodel.EstabelecimentoViewModel
import kotlinx.android.synthetic.main.fragment_avaliacoes_estabelecimento.*

class EstabelecimentoAvaliacoesFragment : Fragment() {

    private val estabelecimentoViewModel by lazy { getActivityViewModel(EstabelecimentoViewModel::class.java) }
    private lateinit var estabelecimento: Estabelecimento

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            container?.inflate(R.layout.fragment_avaliacoes_estabelecimento)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        estabelecimento = arguments!!.getSerializable(IntentFieldNames.ESTABELECIMENTO) as Estabelecimento

        fetchAvalicaoes()

        enviarBtn.setOnClickListener {
            val avaliacao = avaliacaoRatingBar.progress
            val comentario = avaliacaoComentario.text.toString()
            if (avaliacao > 0 && comentario.isNotEmpty()) {
                val dialog = DialogUtil.getDialog(activity!!, "Aguarde...")
                dialog.show()
                estabelecimentoViewModel.avaliar(estabelecimento.estabelecimentoID, avaliacaoRatingBar.progress, avaliacaoComentario.text.toString()) { sucesso ->
                    dialog.dismiss()
                    if (sucesso) {
                        resetForm()
                        fetchAvalicaoes()
                    } else {
                        Toast.makeText(activity, "Erro", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(activity, "Preencha os campos", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun fetchAvalicaoes() {
        estabelecimentoViewModel.fetchAvaliacoes(estabelecimento.estabelecimentoID) { avaliacoes ->
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