package br.com.gdgbrasilia.meetup.app.view.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import br.com.gdgbrasilia.meetup.app.business.vo.Avaliacao
import br.com.gdgbrasilia.meetup.app.data.AppConstants.BASE_URL
import br.com.gdgbrasilia.meetup.app.util.extensions.displayAsDate
import br.com.gdgbrasilia.meetup.app.util.extensions.loadImg
import kotlinx.android.synthetic.main.holder_avaliacao.view.*

class AvaliacaoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val userPicture = view.userPicture
    val userName = view.userNameText
    val avaliacaoDate = view.commentDateText
    val avaliacaoValue = view.commentRatingBar
    val commentText = view.commentText

    var avaliacao: Avaliacao? = null

    fun bind(avaliacao: Avaliacao) {
        this.avaliacao = avaliacao
        userPicture.loadImg("${BASE_URL}${avaliacao.usuario.fotos?.firstOrNull()?.url}")
        userName.text = avaliacao.usuario.nome
        avaliacaoDate.text = avaliacao.dataAvaliacao?.displayAsDate()
        avaliacaoValue.progress = avaliacao.avaliacao
        commentText.text = avaliacao.comentario
    }

}