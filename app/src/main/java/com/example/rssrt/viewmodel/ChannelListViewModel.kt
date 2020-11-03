package com.example.rssrt.viewmodel

import android.app.Application
import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.example.rssrt.R
import com.example.rssrt.model.database.Channel
import com.example.rssrt.model.ChannelCreationResult
import com.example.rssrt.model.RssRepository
import com.example.rssrt.model.database.getDatabase
import com.example.rssrt.util.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChannelListViewModel(application: Application) : AndroidViewModel(application) {

    val newSourceInputValue = MutableLiveData<String>()
    val isSourceInputClearRequired = SingleLiveEvent<Boolean>()
    val navigateToChannelDetails = SingleLiveEvent<Long>()
    val toastMessage = SingleLiveEvent<Int>()

    val updateProgress = MutableLiveData(0)
    val isUpdateProressVisible: LiveData<Boolean> = Transformations.map(updateProgress) {
        it != 0 && it != 100
    }

    private val rssRepository = RssRepository(getDatabase(application))
    val channels = rssRepository.channels

    init {
        viewModelScope.launch(Dispatchers.IO) {
            rssRepository.refreshAllChannels(updateProgress)
        }
    }

    fun onAddClicked() {
        newSourceInputValue.value?.let {
            viewModelScope.launch(Dispatchers.IO) {
                if (it.isNotBlank()) {
                    val result = rssRepository.createChannel(it)
                    toastMessage.postValue(getCreationResultMessage(result))
                    if (result == ChannelCreationResult.CREATED)
                        isSourceInputClearRequired.postValue(true)
                }
            }
        }
    }

    @StringRes
    private fun getCreationResultMessage(result: ChannelCreationResult): Int {
        return when (result) {
            ChannelCreationResult.CREATED -> R.string.channel_added
            ChannelCreationResult.ALREADY_PRESENT -> R.string.channel_present
            ChannelCreationResult.NOT_RSS -> R.string.url_not_rss
        }
    }

    fun onChannelClicked(channel: Channel) {
        navigateToChannelDetails.value = channel.id
    }

    fun onChannelLongClicked(channel: Channel): Boolean {
        viewModelScope.launch(Dispatchers.IO) {
            rssRepository.removeChannel(channel)
        }
        return true
    }

}

class ViewModelFactory(val application: Application, val channelId: Long) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemListViewModel::class.java)) {
            return ItemListViewModel(application, channelId) as T
        }
        throw IllegalArgumentException("Unsupported ViewModel")
    }

}