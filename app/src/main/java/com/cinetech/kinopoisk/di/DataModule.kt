package com.cinetech.kinopoisk.di

import android.content.Context
import androidx.room.Room
import com.cinetech.data.local.AppDatabase
import com.cinetech.data.network.MovieService
import com.cinetech.data.repository.NetworkMovieRepositoryImp
import com.cinetech.domain.repository.NetworkMovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataModule {

    @Provides
    @Singleton
    fun database(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "Kinopoisk"
        ).build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder().addInterceptor { chain ->
            val originalRequest: Request = chain.request()
            val builder: Request.Builder = originalRequest.newBuilder().header(
                "X-API-KEY",
                "FX7WGBF-KKM4XZR-J2PCP7Q-FM1PX06"
            )
            val newRequest: Request = builder.build()
            chain.proceed(newRequest)
        }.build()
    }

    @Provides
    @Singleton
    fun provideRetrofitMovieService(client: OkHttpClient): MovieService {
        return Retrofit.Builder()
            .client(client)
            .baseUrl("https://api.kinopoisk.dev")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieService::class.java)
    }

    @Provides
    @Singleton
    fun  provideNetworkMovieRepository(movieService: MovieService):NetworkMovieRepository = NetworkMovieRepositoryImp(movieService = movieService)
}