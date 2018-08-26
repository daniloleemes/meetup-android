package br.com.gdgbrasilia.meetup.app.view.activities

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import br.com.gdgbrasilia.meetup.app.R
import br.com.gdgbrasilia.meetup.app.view.activities.abs.AccountActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AccountActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_login)

        loginBtn.setOnClickListener {
            super.facebookLogin()
        }
    }
}
