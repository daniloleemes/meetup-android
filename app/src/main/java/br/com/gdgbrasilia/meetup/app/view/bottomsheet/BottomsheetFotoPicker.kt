package br.com.gdgbrasilia.meetup.app.view.bottomsheet

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.CoordinatorLayout
import android.view.View
import android.view.WindowManager
import br.com.gdgbrasilia.meetup.app.R
import kotlinx.android.synthetic.main.bottomsheet_foto_picker.view.*

interface FotoPickerListener {
    fun onGalleryPicked()
    fun onCameraPicked()
}

class BottomsheetFotoPicker : BottomSheetDialogFragment() {

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
        val contentView = View.inflate(context, R.layout.bottomsheet_foto_picker, null)
        dialog.setContentView(contentView)

        setupBehavior(contentView)
        setupViews(contentView)
    }

    private fun setupViews(contentView: View?) {
        contentView?.let {
            it.cameraBtn.setOnClickListener {
                (activity as? FotoPickerListener)?.onCameraPicked()
                dismiss()
            }

            it.galleryBtn.setOnClickListener {
                (activity as? FotoPickerListener)?.onGalleryPicked()
                dismiss()
            }
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
}