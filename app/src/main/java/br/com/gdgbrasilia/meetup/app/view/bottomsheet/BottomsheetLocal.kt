package br.com.gdgbrasilia.meetup.app.view.bottomsheet

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import br.com.gdgbrasilia.meetup.app.R
import br.com.gdgbrasilia.meetup.app.business.model.IntentFieldNames
import br.com.gdgbrasilia.meetup.app.business.model.ViewType
import br.com.gdgbrasilia.meetup.app.business.vo.Estabelecimento
import br.com.gdgbrasilia.meetup.app.business.vo.Evento
import br.com.gdgbrasilia.meetup.app.data.AppConstants.BASE_URL
import br.com.gdgbrasilia.meetup.app.util.extensions.displayAsDate
import br.com.gdgbrasilia.meetup.app.util.extensions.gone
import br.com.gdgbrasilia.meetup.app.util.extensions.loadImg
import br.com.gdgbrasilia.meetup.app.util.extensions.visible
import br.com.gdgbrasilia.meetup.app.view.activities.EstabelecimentoActivity
import br.com.gdgbrasilia.meetup.app.view.activities.EventoActivity
import kotlinx.android.synthetic.main.bottomsheet_estabelecimento.view.*
import kotlinx.android.synthetic.main.fragment_bottomsheet_estabelecimento.view.*
import org.joda.time.DateTime
import org.joda.time.Instant
import org.joda.time.Interval
import org.joda.time.LocalDate
import java.util.*

interface BottomsheetLocaListener {
    fun selected(item: ViewType)
}

class BottomsheetLocal : BottomSheetDialogFragment() {

    private var items: List<ViewType> = listOf()
    private var itemIndex = -1

    override fun onStart() {
        super.onStart()

        val window = dialog.window
        val windowParams = window!!.attributes
        windowParams.dimAmount = 0f
        windowParams.flags = windowParams.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
        window.attributes = windowParams
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val contentView = View.inflate(context, R.layout.bottomsheet_estabelecimento, null)
        dialog.setContentView(contentView)

        items = arguments?.get(IntentFieldNames.ITEMS) as? List<ViewType> ?: listOf()
        itemIndex = arguments?.get(IntentFieldNames.ITEM_INDEX) as Int

        setupBehavior(contentView)
        setupViews(contentView)
    }

    private fun setupViews(contentView: View?) {
        contentView?.let {
            val container = it.bottomsheetPagerContainer
            val pager = container.viewPager
            val adapter = EstabelecimentoPagerAdapter()
            pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {

                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }

                override fun onPageSelected(position: Int) {
                    (activity as BottomsheetLocaListener).selected(items[position])
                }
            })
            pager.adapter = adapter
            pager.currentItem = if (itemIndex >= 0) itemIndex else 0
            pager.clipChildren = false
            pager.offscreenPageLimit = 10
            pager.pageMargin = 32
        }
    }

    private fun setupBehavior(contentView: View) {
        val layoutParams = (contentView.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        val behavior = layoutParams.behavior
        if (behavior != null && behavior is BottomSheetBehavior<*>) {
            behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        dismiss()
                    }
                }
            })
        }
    }

    private inner class EstabelecimentoPagerAdapter : PagerAdapter() {

        override fun instantiateItem(container: ViewGroup, position: Int): Any {

            val view = LayoutInflater.from(context).inflate(R.layout.fragment_bottomsheet_estabelecimento, null)
            val item = items[position]

            if (item is Estabelecimento) {
                view.estabelecimentoPicture.loadImg(BASE_URL + item.fotos.firstOrNull()?.url)
                view.estabelecimentoName.text = item.nome
                view.estabelecimentoTipo.text = item.tipoEstabelecimento.nome
                view.estabelecimentoUltimaAtualizacao.text = "Atualizado por ${item.usuario.nome} em ${item.dataAtualizacao.displayAsDate()}"
                view.estabelecimentoNota.visible()
                view.eventoData.gone()
                view.estabelecimentoNota.text = String.format("%.2f", item.avaliacao)

                view.setOnClickListener {
                    val intent = Intent(context, EstabelecimentoActivity::class.java)
                    intent.putExtra(IntentFieldNames.ESTABELECIMENTO, item)
                    startActivity(intent)
                }
            } else if (item is Evento) {
                view.estabelecimentoPicture.loadImg(BASE_URL + item.fotos.firstOrNull()?.url)
                view.estabelecimentoName.text = item.nome
                view.estabelecimentoTipo.text = item.tipoEvento.nome
                view.estabelecimentoUltimaAtualizacao.text = "Atualizado por ${item.usuario.nome} em ${item.dataAtualizacao.displayAsDate()}"
                view.estabelecimentoNota.gone()
                view.eventoData.visible()
                view.eventoData.text = item.horarios.firstOrNull {
                    try {
                        val millis = getMillis(it.data)
                        val interval = Interval(Instant(millis), Instant(Date().time))
                        isToday(DateTime(millis)) || interval.isAfterNow
                    } catch (e: Exception) {
                        true
                    }
                }?.data?.displayAsDate()

                view.setOnClickListener {
                    val intent = Intent(context, EventoActivity::class.java)
                    intent.putExtra(IntentFieldNames.EVENTO, item)
                    startActivity(intent)
                }
            }

            container.addView(view)
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

        override fun getCount(): Int {
            return items.count()
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }
    }

    private fun getMillis(time: String): Long {
        return time.replace("/Date(", "").replace(")/", "").toLong()
    }

    fun isToday(time: DateTime): Boolean {
        return LocalDate.now().compareTo(LocalDate(time)) == 0
    }

}