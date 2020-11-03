package com.example.rssrt.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.rssrt.model.database.Item
import com.example.rssrt.model.RssRepository
import com.example.rssrt.model.database.getDatabase
import com.example.rssrt.util.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItemListViewModel(application: Application, private val channelId: Long) :
    AndroidViewModel(application) {

    private val rssRepository = RssRepository(getDatabase(application))
    val items = rssRepository.getChannelItems(channelId)

    val navigateToItemDetails = SingleLiveEvent<Item>()

    fun onItemClicked(item: Item) {
        navigateToItemDetails.value = item
    }

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            rssRepository.refreshChannel(channelId)
        }
    }

}