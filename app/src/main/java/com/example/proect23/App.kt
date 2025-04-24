package com.example.proect23

import android.app.Application
import com.example.proect23.data.api.AuthApi
import com.example.proect23.data.api.CurrencyApi
import com.example.proect23.data.api.EnterpriseApi
import com.example.proect23.data.api.ExchangeRateApi
import com.example.proect23.data.api.IndicatorApi
import com.example.proect23.data.api.IndicatorValueApi
import com.example.proect23.data.api.WeightedIndicatorApi
import com.example.proect23.util.AuthInterceptor
import com.example.proect23.util.PrefsManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {

    companion object {
        lateinit var retrofit: Retrofit

        /** Экземпляр AuthApi для регистрации и логина **/
        val authApi: AuthApi by lazy {
            retrofit.create(AuthApi::class.java)
        }

        /** Экземпляр EnterpriseApi для работы с предприятиями **/
        val enterpriseApi: EnterpriseApi by lazy {
            retrofit.create(EnterpriseApi::class.java)
        }

        /** Экземпляр IndicatorApi для работы с показателями **/
        val indicatorApi: IndicatorApi by lazy {
            retrofit.create(IndicatorApi::class.java)
        }

        /** Экземпляр CurrencyApi для работы с валютами **/
        val currencyApi: CurrencyApi by lazy {
            retrofit.create(CurrencyApi::class.java)
        }

        /** Экземпляр ExchangeRateApi для работы с курсами валют **/
        val exchangeRateApi: ExchangeRateApi by lazy {
            retrofit.create(ExchangeRateApi::class.java)
        }

        /** Экземпляр IndicatorValueApi для работы со значениями показателей **/
        val indicatorValueApi: IndicatorValueApi by lazy {
            retrofit.create(IndicatorValueApi::class.java)
        }

        val weightedIndicatorApi: WeightedIndicatorApi by lazy {
            retrofit.create(WeightedIndicatorApi::class.java)
        }
    }

    override fun onCreate() {
        super.onCreate()

        // Инициализируем SharedPreferences
        PrefsManager.init(this)

        // Логирование сетевых запросов
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // OkHttpClient с интерцептором авторизации
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .addInterceptor(logging)
            .build()

        // Собираем Retrofit
        retrofit = Retrofit.Builder()
            .baseUrl("http://<YOUR_API_BASE_URL>/") // TODO: заменить на адрес FastAPI
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
