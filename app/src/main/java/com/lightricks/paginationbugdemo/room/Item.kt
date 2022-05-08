package com.lightricks.paginationbugdemo.room

import android.content.Context
import androidx.paging.PagingSource
import androidx.room.*

@Entity(tableName = "items")
data class Item(
    @PrimaryKey
    val label: String,
    val isTagged: Boolean,
)

@Dao
interface ItemsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<Item>)

    @Update
    suspend fun updateItem(item: Item)

    @Query("SELECT * FROM items")
    fun homeFeedPagingSource(): PagingSource<Int, Item>
}

@Database(
    entities = [
        Item::class,
    ],
    version = 1,
)
abstract class ItemsDatabase : RoomDatabase() {

    abstract fun itemsDao(): ItemsDao

    internal object Factory {
        fun getInstance(
            applicationContext: Context,
        ): ItemsDatabase = Room.databaseBuilder(
            applicationContext,
            ItemsDatabase::class.java, "items-db"
        ).build()
    }
}