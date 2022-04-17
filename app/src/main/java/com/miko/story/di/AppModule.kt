package com.miko.story.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.miko.story.BuildConfig
import com.miko.story.data.StoryDataStore
import com.miko.story.data.StoryRepository
import com.miko.story.data.remote.StoryApiClient
import com.miko.story.domain.StoryInteractor
import com.miko.story.domain.StoryUseCase
import com.miko.story.presentation.membership.MembershipViewModel
import com.miko.story.presentation.story.StoryViewModel
import com.miko.story.utils.AppConst
import com.miko.story.utils.PreferenceUtil
import com.miko.story.utils.SettingPreferences
import com.miko.story.utils.dataStore
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val networkModule = module {

    val httpLogging = "http_logging"

    single<Interceptor>(named(httpLogging)) {
        HttpLoggingInterceptor().setLevel(
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        )
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
            .addInterceptor(interceptor = get(named(httpLogging)))
            .build()
    }

    single<StoryApiClient> {
        val retrofit = Retrofit.Builder()
            .baseUrl(AppConst.API_URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(StoryApiClient::class.java)
    }
}

val repositoryModule = module {
    single<StoryRepository> { StoryDataStore(get()) }
}

val useCaseModule = module {
    single<StoryUseCase> { StoryInteractor(get()) }
}

val dataStoreModule = module {
    single { get<Context>().dataStore }

    single { SettingPreferences(get()) }
}

val viewModelModule = module {
    viewModel { MembershipViewModel(get()) }
    viewModel { StoryViewModel(get()) }
}