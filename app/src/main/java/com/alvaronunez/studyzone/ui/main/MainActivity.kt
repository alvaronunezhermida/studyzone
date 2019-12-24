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
import com.alvaronunez.studyzone.ui.main.MainViewModel.FabsModel
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity(){

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter : ItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setListeners()
        viewModelSetUp()
    }

    override fun onResume() {
        super.onResume()
        viewModel.resume()
    }

    private fun setListeners() {
        fab.setOnClickListener{ viewModel.onFabClicked() }
        fab_item.setOnClickListener { viewModel.onFabItemClicked() }
    }

    private fun viewModelSetUp() {
        viewModel = ViewModelProviders.of(
            this,
            MainViewModelFactory())[MainViewModel::class.java]

        adapter = ItemsAdapter(viewModel::onItemClicked)
        recycler.adapter = adapter
        viewModel.model.observe(this, Observer(::updateUi))
        viewModel.fabsModel.observe(this, Observer(::updateFabs))
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
            is UiModel.Finish -> finish()
            is UiModel.NavigateToCreateItem -> startActivity<CreateItemActivity>()
        }
    }

    private fun updateFabs(fabsModel: FabsModel) {
        when(fabsModel){
            is FabsModel.Closed -> animateFabs(R.anim.rotate_clockwise, R.anim.fab_close)
            is FabsModel.Opened -> animateFabs(R.anim.rotate_anticlockwise, R.anim.fab_open)
        }
    }

    private fun animateFabs(mainFabAnimRes: Int, fabsAnimRes: Int) {
        val mainFabAnim = AnimationUtils.loadAnimation(this, mainFabAnimRes)
        val fabsAnim = AnimationUtils.loadAnimation(this, fabsAnimRes)
        fab.startAnimation(mainFabAnim)
        fab_item.startAnimation(fabsAnim)
        fab_image.startAnimation(fabsAnim)
        fab_audio.startAnimation(fabsAnim)
        fab_file.startAnimation(fabsAnim)
    }
}
