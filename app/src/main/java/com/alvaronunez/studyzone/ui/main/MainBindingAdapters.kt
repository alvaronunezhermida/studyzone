package com.alvaronunez.studyzone.ui.main

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alvaronunez.studyzone.data.model.ItemDTO

@BindingAdapter("items")
fun RecyclerView.setItems(items: List<ItemDTO>?) {
    (adapter as? ItemsAdapter)?.let {
        it.items = items ?: emptyList()
    }
}