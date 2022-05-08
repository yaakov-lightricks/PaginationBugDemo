package com.lightricks.paginationbugdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.lightricks.paginationbugdemo.room.Item
import com.lightricks.paginationbugdemo.room.ItemsDatabase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    @ExperimentalPagingApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewModel = ViewModelProvider(this, Factory(ItemsDatabase.Factory.getInstance(applicationContext)))[SampleViewModel::class.java]
        val itemsAdapter = ItemsAdapter(viewModel::onItemClicked)
        recyclerView = findViewById<RecyclerView?>(R.id.recycler_view).apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = itemsAdapter
            PagerSnapHelper().attachToRecyclerView(this)
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.bindUi().collectLatest { pagingData ->
                    itemsAdapter.submitData(pagingData)
                }
            }
        }
    }
}

class ItemsAdapter(private val onItemClicked: (Item) -> Unit): PagingDataAdapter<Item, ItemViewHolder>(COMPARATOR) {

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)!!
        holder.label.text = item.label
        holder.switchTag.isChecked = item.isTagged
        holder.itemView.setOnClickListener {
            onItemClicked(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false))
    }

}

class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val label: TextView = itemView.findViewById(R.id.label)
    val switchTag: SwitchCompat = itemView.findViewById(R.id.tag)
}

object COMPARATOR : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.label == newItem.label
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem == newItem
    }
}