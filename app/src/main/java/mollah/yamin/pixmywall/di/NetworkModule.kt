package mollah.yamin.pixmywall.di

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mollah.yamin.pixmywall.api.Api
import mollah.yamin.pixmywall.api.ApiService
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Module to tell Hilt how to provide instances of types that cannot be constructor-injected.
 *
 * As these types are scoped to the application lifecycle using @Singleton, they're installed
 * in Hilt's ApplicationComponent.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logger = HttpLoggingInterceptor { Log.d("API", it) }
        logger.level = HttpLoggingInterceptor.Level.BASIC
        return logger
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
            .followRedirects(true)
            .followSslRedirects(true)
            .retryOnConnectionFailure(true)
            .connectTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)

        return clientBuilder.build()
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun provideScalarsConverterFactory(): ScalarsConverterFactory = ScalarsConverterFactory.create()

    @Provides
    @Singleton
    fun provideNullOrEmptyConverterFactory(): Converter.Factory =
        object : Converter.Factory() {
            override fun responseBodyConverter(
                type: Type,
                annotations: Array<out Annotation>,
                retrofit: Retrofit
            ): Converter<ResponseBody, Any?> {
                val nextResponseBodyConverter = retrofit.nextResponseBodyConverter<Any?>(
                    this,
                    type,
                    annotations
                )

                return Converter { body: ResponseBody ->
                    if (body.contentLength() == 0L) null
                    else nextResponseBodyConverter.convert(body)
                }
            }
        }

    @Provides
    @Singleton
    fun provideRetrofitBuilder(
        nullOrEmptyConverterFactory: Converter.Factory,
        scalarsConverterFactory: ScalarsConverterFactory,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit.Builder =
        Retrofit.Builder()
            .baseUrl(Api.API_ROOT_URL.toHttpUrlOrNull()!!)
            .addConverterFactory(scalarsConverterFactory)
            .addConverterFactory(gsonConverterFactory)
            .addConverterFactory(nullOrEmptyConverterFactory)

    @Provides
    @Singleton
    fun provideApiService(
        okHttpClient: OkHttpClient,
        retrofitBuilder: Retrofit.Builder
    ): ApiService {
        return retrofitBuilder
            .client(okHttpClient).build()
            .create(ApiService::class.java)
    }

}