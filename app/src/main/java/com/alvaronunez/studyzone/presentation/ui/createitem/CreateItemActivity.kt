package com.alvaronunez.studyzone.presentation.ui.createitem

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.alvaronunez.studyzone.R
import com.alvaronunez.studyzone.presentation.ui.createitem.CreateItemViewModel.UiModel
import kotlinx.android.synthetic.main.activity_create_item.*
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.ext.android.viewModel

class CreateItemActivity : AppCompatActivity() {

    private val viewModel: CreateItemViewModel by currentScope.viewModel(this)
    private lateinit var adapter : CategoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_item)
        setListeners()

        adapter = CategoriesAdapter(viewModel::onCategoryClicked)
        itemCategoryList.adapter = adapter
        viewModel.model.observe(this, Observer(::updateUi))
    }

    private fun setListeners() {
        add_item_button.setOnClickListener { viewModel.addItem(itemTitle.text.toString(), itemDescription.text.toString()) }
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
