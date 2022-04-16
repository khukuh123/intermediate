package com.miko.story.di

import com.miko.story.data.StoryDataStore
import com.miko.story.data.StoryRepository
import com.miko.story.data.remote.StoryApiClient
import com.miko.story.domain.StoryInteractor
import com.miko.story.domain.StoryUseCase
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {

    single {
//        HttpLoggingInterceptor().setLevel(
//            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
//        )
    }

    single {
        OkHttpClient.Builder()
//            .addInterceptor { chain ->
//                val original: Request = chain.request()
//
//                val request = original.newBuilder()
//                    .addHeader("Authorization", "token ${AppConst.GITHUB_API_KEY}")
//                    .method(original.method, original.body)
//                    .build()
//
//                chain.proceed(request)
//            }
//            .addInterceptor(loggingInterceptor)
            .build()
    }

    single<StoryApiClient> {
        val retrofit = Retrofit.Builder()
            .baseUrl("")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(StoryApiClient::class.java)
    }
}

val repositoryModule = module {
    single<StoryRepository> { StoryDataStore( get() ) }
}

val useCaseModule = module {
    single<StoryUseCase> { StoryInteractor( get() ) }
}

val viewModelModule = module {
}