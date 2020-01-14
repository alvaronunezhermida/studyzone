package com.alvaronunez.studyzone.presentation.ui.main

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alvaronunez.studyzone.R
import com.alvaronunez.studyzone.databinding.ViewItemBinding
import com.alvaronunez.studyzone.domain.Item
import com.alvaronunez.studyzone.presentation.ui.common.basicDiffUtil
import com.alvaronunez.studyzone.presentation.ui.common.bindingInflate

class ItemsAdapter (private val listener: (Item) -> Unit) :
    RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {

    var items: List<Item> by basicDiffUtil(
        emptyList(),
        areItemsTheSame = { old, new -> old.uid == new.uid }
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(parent.bindingInflate(R.layout.view_item, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.dataBinding.item = item
        holder.itemView.setOnClickListener { listener(item) }
    }

    class ViewHolder(val dataBinding: ViewItemBinding) : RecyclerView.ViewHolder(dataBinding.root)


}