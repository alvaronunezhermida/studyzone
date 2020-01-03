package com.alvaronunez.studyzone.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.alvaronunez.studyzone.R
import com.alvaronunez.studyzone.presentation.databinding.ActivityMainBinding
import com.alvaronunez.studyzone.presentation.common.EventObserver
import com.alvaronunez.studyzone.presentation.createitem.CreateItemActivity
import com.alvaronunez.studyzone.presentations.createitem.CreateItemActivity
import com.alvaronunez.studyzone.presentation.main.MainViewModel.UiModel
import com.alvaronunez.studyzone.presentation.main.MainViewModel.FabsModel
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity(){

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter : ItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(
            this,
            MainViewModelFactory())[MainViewModel::class.java]

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        adapter = ItemsAdapter(viewModel::onItemClicked)
        binding.recycler.adapter = adapter

        setUpObservers()
    }

    private fun setUpObservers() {
        viewModel.finish.observe(this, EventObserver {
            finish()
        })

        viewModel.navigateToCreateItem.observe(this, EventObserver{
            startActivity<CreateItemActivity>()
        })

        viewModel.openFabs.observe(this, Observer { openFabs ->
            if(openFabs) animateFabs(R.anim.rotate_anticlockwise, R.anim.fab_open)
            else animateFabs(R.anim.rotate_clockwise, R.anim.fab_close)
        })
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
