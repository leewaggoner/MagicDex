package com.wreckingball.magicdex.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.wreckingball.magicdex.callbacks.MagicBoundaryCallback
import com.wreckingball.magicdex.database.CardDatabase
import com.wreckingball.magicdex.database.NewsDatabase
import com.wreckingball.magicdex.network.*
import com.wreckingball.magicdex.repositories.*
import com.wreckingball.magicdex.ui.dashboard.DashboardViewModel
import com.wreckingball.magicdex.ui.formats.FormatsViewModel
import com.wreckingball.magicdex.ui.home.HomeViewModel
import com.wreckingball.magicdex.ui.magicdex.MagicDexViewModel
import com.wreckingball.magicdex.ui.search.SearchViewModel
import com.wreckingball.magicdex.ui.sets.SetsViewModel
import com.wreckingball.magicdex.ui.types.TypesViewModel
import com.wreckingball.magicdex.utils.PreferencesWrapper
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module(override = true) {
    viewModel { HomeViewModel(get()) }
    viewModel { MagicDexViewModel(get()) }
    viewModel { DashboardViewModel() }
    viewModel { SetsViewModel(get()) }
    viewModel { TypesViewModel(get()) }
    viewModel { FormatsViewModel(get()) }
    viewModel { (searchString: String) -> SearchViewModel(searchString) }
    factory { (searchString: String) -> SearchRepository(searchString) }
    single { FormatsRepository(get()) }
    single { TypesRepository(get(), get(), get()) }
    single { SetsRepository(get()) }
    single { MagicCardsRepository(get()) }
    single { MagicRssRepository(get(), get()) }
    factory { (searchString: String) ->  SearchDataSourceFactory(searchString) }
    factory { (searchString: String) -> SearchDataSource(searchString, get()) }
    single {
        MagicBoundaryCallback(
            get(),
            get()
        )
    }
    single { get<CardDatabase>().cardDao() }
    single { Room.databaseBuilder(androidApplication(), CardDatabase::class.java, "card_db").build() }
    single { get<NewsDatabase>().newsDao() }
    single { Room.databaseBuilder(androidApplication(), NewsDatabase::class.java, "news_db").build() }
    single { provideRssService(get()) }
    single { MagicRSSApiService(get()) }
    single { RSSRetrofit() }
    single { provideCardService(get()) }
    single { provideFormatsService(get()) }
    single { provideSupertypesService(get()) }
    single { provideTypesService(get()) }
    single { provideSubtypesService(get()) }
    single { provideSetsService(get())}
    single { MagicCardApiService(get()) }
    single { CardRetrofit() }
    single { PreferencesWrapper(getSharedPrefs(androidContext())) }
}

fun provideRssService(magicRSSApiService: MagicRSSApiService) : RssService {
    return magicRSSApiService.createService(RssService::class.java)
}

fun provideCardService(magicCardApiService: MagicCardApiService) : CardService {
    return magicCardApiService.createService(CardService::class.java)
}

fun provideFormatsService(magicCardApiService: MagicCardApiService) : FormatsService {
    return magicCardApiService.createService(FormatsService::class.java)
}

fun provideSupertypesService(magicCardApiService: MagicCardApiService) : SupertypesService {
    return magicCardApiService.createService(SupertypesService::class.java)
}

fun provideTypesService(magicCardApiService: MagicCardApiService) : TypesService {
    return magicCardApiService.createService(TypesService::class.java)
}

fun provideSubtypesService(magicCardApiService: MagicCardApiService) : SubtypesService {
    return magicCardApiService.createService(SubtypesService::class.java)
}

fun provideSetsService(magicCardApiService: MagicCardApiService) : SetsService {
    return magicCardApiService.createService(SetsService::class.java)
}

private fun getSharedPrefs(context: Context) : SharedPreferences {
    return PreferenceManager.getDefaultSharedPreferences(context)
}
