package com.alvaronunez.studyzone.ui.main

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alvaronunez.studyzone.R
import com.alvaronunez.studyzone.data.model.ItemDTO
import com.alvaronunez.studyzone.ui.common.basicDiffUtil
import com.alvaronunez.studyzone.ui.common.inflate
import kotlinx.android.synthetic.main.view_item.view.*

class ItemsAdapter (private val listener: (ItemDTO) -> Unit) :
    RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {

    var items: List<ItemDTO> by basicDiffUtil(
        emptyList(),
        areItemsTheSame = { old, new -> old.uid == new.uid }
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.view_item, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { listener(item) }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: ItemDTO) {
            itemView.item_title.text = item.title
            itemView.item_description.text = item.description
            //itemView.item_color.background = Color.parseColor(item.)
        }
    }

}