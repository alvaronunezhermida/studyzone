package com.alvaronunez.studyzone.presentation.createitem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alvaronunez.studyzone.data.AuthRepository
import com.alvaronunez.studyzone.data.model.CategoryDTO
import com.alvaronunez.studyzone.data.DatabaseRepository
import com.alvaronunez.studyzone.presentation.common.Scope
import kotlinx.coroutines.launch

class CreateItemViewModel : ViewModel(), Scope by Scope.Impl() {

    private val _model = MutableLiveData<UiModel>()
    val model: LiveData<UiModel>
        get() {
            if (_model.value == null) refresh()
            return _model
        }

    private val authRepository : AuthRepository by lazy { AuthRepository() }
    private val databaseRepository : DatabaseRepository by lazy { DatabaseRepository() }

    sealed class UiModel {
        class Categories(val categories: List<CategoryDTO>) : UiModel()
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
            databaseRepository.getCategoriesByUser(authRepository.getCurrentUser()?.uid)?.let { categories ->
                _model.value = UiModel.Categories(categories) //TODO: crear opción añadir categoría cuando la lista viene vacía
            }?: run {
                _model.value = UiModel.Message("Error al leer datos!")
            }
        }
    }

    fun onCategoryClicked(category: CategoryDTO ) {
        _model.value = UiModel.Message("${category.title} clicked!")
    }

    override fun onCleared() {
        destroyScope()
        super.onCleared()
    }

    fun addCategory(itemTitle: String, itemDescription: String) {
        launch {
            _model.value = databaseRepository.addItem(
                hashMapOf(
                    "title" to itemTitle,
                    "description" to itemDescription,
                    "userId" to databaseRepository.getUserById(authRepository.getCurrentUser()?.uid)
                    )
            )?.let {
                if(it) UiModel.Finish
                else UiModel.Message("Can't create")
            }
        }
    }

}

@Suppress("UNCHECKED_CAST")
class CreateItemViewModelFactory :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        CreateItemViewModel() as T
}