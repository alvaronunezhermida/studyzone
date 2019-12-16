package com.alvaronunez.studyzone.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.alvaronunez.studyzone.R
import com.alvaronunez.studyzone.ui.createitem.CreateItemActivity
import com.alvaronunez.studyzone.ui.main.MainViewModel.UiModel
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity(){

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter : ItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setListeners()

        viewModel = ViewModelProviders.of(
            this,
            MainViewModelFactory())[MainViewModel::class.java]

            adapter = ItemsAdapter(viewModel::onItemClicked)
            recycler.adapter = adapter
        viewModel.model.observe(this, Observer(::updateUi))
    }

    override fun onResume() {
        super.onResume()
        viewModel.resume()
    }

    private fun setListeners() {
        fab.setOnClickListener{ viewModel.onFabClicked() }
        fab_item.setOnClickListener { viewModel.onFabItemClicked() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_sign_up -> viewModel.onSignOutClicked()
        }
        return false
    }

    private fun updateUi(model: UiModel) {
        loading.visibility = if (model is UiModel.Loading) View.VISIBLE else View.GONE
        when (model) {
            is UiModel.Content -> adapter.items = model.items
            is UiModel.Message -> Toast.makeText(this, model.message, Toast.LENGTH_LONG).show()
            is UiModel.OpenFabs -> openFabs()
            is UiModel.CloseFabs -> closeFabs()
            is UiModel.Finish -> finish()
            is UiModel.NavigateToCreateItem -> startActivity<CreateItemActivity>()
        }
    }

    private fun closeFabs() {
        val fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close)
        val fabRClocwise = AnimationUtils.loadAnimation(this, R.anim.rotate_clockwise)
        fab_item.startAnimation(fabClose)
        fab_image.startAnimation(fabClose)
        fab_audio.startAnimation(fabClose)
        fab_file.startAnimation(fabClose)
        fab.startAnimation(fabRClocwise)
    }

    private fun openFabs() {
        val fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open)
        val fabRAntiClockwise = AnimationUtils.loadAnimation(this, R.anim.rotate_anticlockwise)
        fab_item.startAnimation(fabOpen)
        fab_image.startAnimation(fabOpen)
        fab_audio.startAnimation(fabOpen)
        fab_file.startAnimation(fabOpen)
        fab.startAnimation(fabRAntiClockwise)
    }
}
