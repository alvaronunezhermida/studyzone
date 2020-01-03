package com.alvaronunez.studyzone.presentation.main

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alvaronunez.studyzone.R
import com.alvaronunez.studyzone.data.model.ItemDTO
import com.alvaronunez.studyzone.databinding.ViewItemBinding
import com.alvaronunez.studyzone.presentation.common.basicDiffUtil
import com.alvaronunez.studyzone.presentation.common.bindingInflate

class ItemsAdapter (private val listener: (ItemDTO) -> Unit) :
    RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {

    var items: List<ItemDTO> by basicDiffUtil(
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