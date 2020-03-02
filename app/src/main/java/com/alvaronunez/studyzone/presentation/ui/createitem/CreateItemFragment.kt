package com.alvaronunez.studyzone.presentation.ui.createitem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.alvaronunez.studyzone.R
import com.alvaronunez.studyzone.presentation.ui.createitem.CreateItemViewModel.UiModel
import kotlinx.android.synthetic.main.fragment_create_item.*
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.ext.android.viewModel

class CreateItemFragment : Fragment() {

    private val viewModel: CreateItemViewModel by currentScope.viewModel(this)
    private lateinit var adapter : CategoriesAdapter
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = view.findNavController()
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
            is UiModel.Message -> Toast.makeText(this.context, model.message, Toast.LENGTH_LONG).show()
            is UiModel.Finish -> return//TODO: finish()
        }
    }
}