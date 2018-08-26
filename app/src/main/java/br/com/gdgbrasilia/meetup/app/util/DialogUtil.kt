package br.com.gdgbrasilia.meetup.app.util

import android.app.ProgressDialog
import android.content.Context
import br.com.gdgbrasilia.meetup.app.R

/**
 * Created by danilolemes on 23/12/2017.
 */
class DialogUtil {
    companion object {
        private lateinit var mProgressDialog: ProgressDialog

        fun getDialog(context: Context, message: String): ProgressDialog {

            mProgressDialog = ProgressDialog(context, R.style.AppProgressDialog)
            mProgressDialog.setMessage(message)
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            mProgressDialog.setIndeterminate(true)
            mProgressDialog.setCancelable(true)

            return mProgressDialog
        }
    }
}