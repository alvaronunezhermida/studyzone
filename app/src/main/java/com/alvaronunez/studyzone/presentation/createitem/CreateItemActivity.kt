package com.alvaronunez.studyzone.presentation.createitem

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.alvaronunez.studyzone.R
import com.alvaronunez.studyzone.presentation.createitem.CreateItemViewModel.UiModel
import kotlinx.android.synthetic.main.activity_create_item.*

class CreateItemActivity : AppCompatActivity() {

    private lateinit var viewModel: CreateItemViewModel
    private lateinit var adapter : CategoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_item)
        setListeners()

        viewModel = ViewModelProviders.of(
            this,
            CreateItemViewModelFactory())[CreateItemViewModel::class.java]

        adapter = CategoriesAdapter(viewModel::onCategoryClicked)
        itemCategoryList.adapter = adapter
        viewModel.model.observe(this, Observer(::updateUi))
    }

    private fun setListeners() {
        add_category_button.setOnClickListener { viewModel.addCategory(itemTitle.text.toString(), itemDescription.text.toString()) }
    }


    private fun updateUi(model: UiModel) {
        loading.visibility = if (model is UiModel.Loading) View.VISIBLE else View.GONE
        when(model){
            is UiModel.Categories -> adapter.categories = model.categories
            is UiModel.Message -> Toast.makeText(this, model.message, Toast.LENGTH_LONG).show()
            is UiModel.Finish -> finish()
        }
    }
}
