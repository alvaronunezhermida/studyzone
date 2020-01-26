package com.alvaronunez.studyzone.presentation.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.alvaronunez.studyzone.R
import com.alvaronunez.studyzone.databinding.ActivityMainBinding
import com.alvaronunez.studyzone.presentation.ui.common.EventObserver
import com.alvaronunez.studyzone.presentation.ui.createitem.CreateItemActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(){

    private val viewModel: MainViewModel by currentScope.viewModel(this)
    private lateinit var adapter : ItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
