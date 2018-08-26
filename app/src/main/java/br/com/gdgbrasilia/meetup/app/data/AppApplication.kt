package br.com.gdgbrasilia.meetup.app.data

import android.app.Application
import br.com.gdgbrasilia.meetup.app.data.di.components.DaggerNetComponent
import br.com.gdgbrasilia.meetup.app.data.di.components.DaggerRepositoryComponent
import br.com.gdgbrasilia.meetup.app.data.di.components.NetComponent
import br.com.gdgbrasilia.meetup.app.data.di.components.RepositoryComponent
import br.com.gdgbrasilia.meetup.app.data.di.modules.AppModule
import br.com.gdgbrasilia.meetup.app.data.di.modules.NetModule
import br.com.gdgbrasilia.meetup.app.data.di.modules.RepositoryModule

class AppApplication : Application() {

    companion object {

        @JvmStatic
        lateinit var instance: AppApplication

        @JvmStatic
        lateinit var NetComponent: NetComponent

        @JvmStatic
        lateinit var RepositoryComponent: RepositoryComponent

    }

    override fun onCreate() {
        super.onCreate()

        instance = this

//        JodaTimeAndroid.init(this)

        NetComponent = DaggerNetComponent.builder()
                .appModule(AppModule(this))
                .netModule(NetModule())
                .build()

        RepositoryComponent = DaggerRepositoryComponent.builder()
                .netComponent(NetComponent)
                .repositoryModule(RepositoryModule())
                .build()


    }

}