package com.lightricks.paginationbugdemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.lightricks.paginationbugdemo.room.Item
import com.lightricks.paginationbugdemo.room.ItemsDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SampleViewModel(db: ItemsDatabase) : ViewModel() {

    private val itemsDao = db.itemsDao()

    @ExperimentalPagingApi
    fun bindUi(): Flow<PagingData<Item>> {
        viewModelScope.launch {
            itemsDao.insertItems((0..20).map { Item(it.toString(), false) })
        }
        return Pager(
            config = PagingConfig(pageSize = 3, enablePlaceholders = false),
            pagingSourceFactory = { itemsDao.homeFeedPagingSource() }
        ).flow.cachedIn(viewModelScope)
    }

    fun onItemClicked(item: Item) {
        viewModelScope.launch {
            val newItem = item.copy(isTagged = !item.isTagged)
            itemsDao.updateItem(newItem)
        }
    }
}

class Factory(private val db: ItemsDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SampleViewModel(db) as T
    }

}