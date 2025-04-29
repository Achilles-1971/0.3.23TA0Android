package com.example.proect23

import android.app.Application
import com.example.proect23.data.api.*
import com.example.proect23.util.AuthInterceptor
import com.example.proect23.util.PrefsManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {

    companion object {
        lateinit var retrofit: Retrofit

        val authApi: AuthApi by lazy { retrofit.create(AuthApi::class.java) }
        val enterpriseApi: EnterpriseApi by lazy { retrofit.create(EnterpriseApi::class.java) }
        val indicatorApi: IndicatorApi by lazy { retrofit.create(IndicatorApi::class.java) }
        val currencyApi: CurrencyApi by lazy { retrofit.create(CurrencyApi::class.java) }
        val exchangeRateApi: ExchangeRateApi by lazy { retrofit.create(ExchangeRateApi::class.java) }
        val indicatorValueApi: IndicatorValueApi by lazy { retrofit.create(IndicatorValueApi::class.java) }
        val weightedIndicatorApi: WeightedIndicatorApi by lazy { retrofit.create(WeightedIndicatorApi::class.java) }
    }

    override fun onCreate() {
        super.onCreate()

        // Инициализация SharedPreferences
        PrefsManager.init(this)

        // Логирование HTTP-запросов
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // OkHttpClient с AuthInterceptor
        val client = OkHttpClient.Builder()
            
            .addInterceptor(AuthInterceptor(PrefsManager))
            .addInterceptor(logging)
            .build()


        // Инициализация Retrofit
        retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/") // адрес твоего локального Django-сервера для эмулятора
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
