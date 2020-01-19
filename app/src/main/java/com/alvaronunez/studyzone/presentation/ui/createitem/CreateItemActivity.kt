package com.alvaronunez.studyzone.presentation.ui.createitem

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.alvaronunez.studyzone.R
import com.alvaronunez.studyzone.data.repository.AuthenticationRepository
import com.alvaronunez.studyzone.data.repository.Repository
import com.alvaronunez.studyzone.presentation.data.FirebaseAuthDataSource
import com.alvaronunez.studyzone.presentation.data.FirebaseDataSource
import com.alvaronunez.studyzone.presentation.ui.common.getViewModel
import com.alvaronunez.studyzone.presentation.ui.createitem.CreateItemViewModel.UiModel
import com.alvaronunez.studyzone.usecases.AddItem
import com.alvaronunez.studyzone.usecases.GetCategoriesByUser
import com.alvaronunez.studyzone.usecases.GetSignedUser
import kotlinx.android.synthetic.main.activity_create_item.*

class CreateItemActivity : AppCompatActivity() {

    private lateinit var viewModel: CreateItemViewModel
    private lateinit var adapter : CategoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_item)
        setListeners()

        viewModel = getViewModel { CreateItemViewModel(GetCategoriesByUser(Repository(FirebaseDataSource())),
                                    GetSignedUser(AuthenticationRepository(FirebaseAuthDataSource())),
                                    AddItem(Repository(FirebaseDataSource()))) }

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
