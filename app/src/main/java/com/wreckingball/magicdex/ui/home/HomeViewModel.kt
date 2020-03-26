package com.wreckingball.magicdex.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wreckingball.magicdex.R
import com.wreckingball.magicdex.models.Menu
import com.wreckingball.magicdex.models.NewsList
import com.wreckingball.magicdex.repositories.MagicRssRepository

class HomeViewModel(private val newsRepository: MagicRssRepository) : ViewModel() {
    private var menuList = MutableLiveData<List<Menu>>()
    var newsList = MutableLiveData<NewsList>()

    init {
        newsRepository.newsList = newsList
    }

    companion object {
        enum class MenuId {
            MAGIC_DEX,
            SETS,
            TYPES,
            FORMATS
        }
    }

    fun getMenuList(context: Context): LiveData<List<Menu>> {
        menuList.value = listOf(
            Menu(MenuId.MAGIC_DEX, context.resources.getString(R.string.menu_item_1), R.color.lightGreen),
            Menu(MenuId.SETS, context.resources.getString(R.string.menu_item_2) , R.color.lightBlack),
            Menu(MenuId.TYPES, context.resources.getString(R.string.menu_item_3), R.color.lightRed),
            Menu(MenuId.FORMATS, context.resources.getString(R.string.menu_item_4), R.color.lightBlue)
        )
        return menuList
    }

    fun getNewsList() {
        newsList.value = NewsList()
        newsRepository.getRssFeed()
    }
}