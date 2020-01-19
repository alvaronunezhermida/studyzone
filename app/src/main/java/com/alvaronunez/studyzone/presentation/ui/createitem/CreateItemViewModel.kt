package com.alvaronunez.studyzone.presentation.ui.createitem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alvaronunez.studyzone.domain.Category
import com.alvaronunez.studyzone.domain.Item
import com.alvaronunez.studyzone.presentation.ui.common.ScopedViewModel
import com.alvaronunez.studyzone.usecases.AddItem
import com.alvaronunez.studyzone.usecases.GetCategoriesByUser
import com.alvaronunez.studyzone.usecases.GetSignedUser
import kotlinx.coroutines.launch

class CreateItemViewModel(private val getCategoriesByUser: GetCategoriesByUser,
                          private val getSignedUser: GetSignedUser,
                          private val addItem: AddItem) : ScopedViewModel() {

    private val _model = MutableLiveData<UiModel>()
    val model: LiveData<UiModel>
        get() {
            if (_model.value == null) refresh()
            return _model
        }

    sealed class UiModel {
        class Categories(val categories: List<Category>) : UiModel()
        object Loading : UiModel()
        class Message(val message: String) : UiModel()
        object Finish : UiModel()
    }

    init {
        initScope()
    }

    private fun refresh(){
        _model.value = UiModel.Loading
        launch{
            getSignedUser.invoke()?.let {
                _model.value = UiModel.Categories(getCategoriesByUser.invoke(it.id)) //TODO: crear opción añadir categoría cuando la lista viene vacía
            }
        }
    }

    fun onCategoryClicked(category: Category) {
        _model.value = UiModel.Message("${category.title} clicked!")
    }

    override fun onCleared() {
        destroyScope()
        super.onCleared()
    }

    fun addItem(itemTitle: String, itemDescription: String) {
        launch {
            getSignedUser.invoke()?.let {user ->
                _model.value = addItem.invoke(Item(title = itemTitle, description = itemDescription, userId = user.id)).let {
                    if(it) UiModel.Finish
                    else UiModel.Message("Can't create")
                }
            }?: run {
                _model.value = UiModel.Message("Can't create")
            }
        }
    }

}