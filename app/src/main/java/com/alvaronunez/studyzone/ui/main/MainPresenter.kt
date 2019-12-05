package com.alvaronunez.studyzone.ui.main

import com.alvaronunez.studyzone.data.model.AuthRepository
import com.alvaronunez.studyzone.data.model.DatabaseRepository
import com.alvaronunez.studyzone.data.model.ItemDTO

class MainPresenter {

    interface View {
        fun showMessage(message: String)
        fun finishActivity()
        fun showProgress()
        fun hideProgress()
        fun updateData(items: List<ItemDTO>)
    }

    private val databaseRepository : DatabaseRepository by lazy { DatabaseRepository() }
    private val authRepository : AuthRepository by lazy { AuthRepository() }
    private var view: View? = null

    fun onCreate(view: View) {
        this.view = view

        view.showProgress()
        databaseRepository.getItemsByUser(authRepository.getCurrentUser()?.uid){ result ->
            view.hideProgress()
            result.onSuccess { itemList ->
                view.updateData(itemList)
            }
            result.onFailure {
                view.showMessage("Error al leer datos!")
            }
        }

    }

    fun onItemClicked(item: ItemDTO) {
        view?.showMessage("${item.title} clicked!")
    }

    fun onSignOutClicked() {
        authRepository.signOut()
        view?.showMessage("Sesión cerrada")
        view?.finishActivity()
    }

    fun onDestroy() {
        this.view = null
    }



}