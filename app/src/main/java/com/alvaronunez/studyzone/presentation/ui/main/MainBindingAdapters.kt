package com.alvaronunez.studyzone.presentation.ui.main

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alvaronunez.studyzone.domain.Item

@BindingAdapter("items")
fun RecyclerView.setItems(items: List<Item>?) {
    (adapter as? ItemsAdapter)?.let {
        it.items = items ?: emptyList()
    }
}