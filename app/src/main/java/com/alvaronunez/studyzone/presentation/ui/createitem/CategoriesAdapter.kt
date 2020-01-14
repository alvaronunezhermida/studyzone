package com.alvaronunez.studyzone.presentation.ui.createitem

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alvaronunez.studyzone.R
import com.alvaronunez.studyzone.domain.Category
import com.alvaronunez.studyzone.presentation.ui.common.basicDiffUtil
import com.alvaronunez.studyzone.presentation.ui.common.inflate
import kotlinx.android.synthetic.main.view_category.view.*

class CategoriesAdapter (private val listener: (Category) -> Unit) :
    RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    var categories: List<Category> by basicDiffUtil(
        emptyList(),
        areItemsTheSame = { old, new -> old.uid == new.uid }
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.view_category, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = categories.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category)
        holder.itemView.setOnClickListener { listener(category) }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(category: Category) {
            itemView.apply{
                category_title.text = category.title
                category_title.setTextColor(Color.parseColor(category.color))
                category_divider.setBackgroundColor(Color.parseColor(category.color))
            }
        }
    }

}