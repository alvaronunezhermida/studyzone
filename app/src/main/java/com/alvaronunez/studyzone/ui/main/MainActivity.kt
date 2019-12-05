package com.alvaronunez.studyzone.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.alvaronunez.studyzone.R
import com.alvaronunez.studyzone.data.model.ItemDTO
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainPresenter.View {

    private val presenter by lazy { MainPresenter() }
    private val adapter = ItemsAdapter(presenter::onItemClicked)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.onCreate(this)
        recycler.adapter = adapter
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_sign_up -> presenter.onSignOutClicked()
        }
        return false
    }

    override fun finishActivity() {
        finish()
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun showProgress() {
        loading.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        loading.visibility = View.GONE
    }

    override fun updateData(items: List<ItemDTO>) {
        adapter.items = items
    }
}
