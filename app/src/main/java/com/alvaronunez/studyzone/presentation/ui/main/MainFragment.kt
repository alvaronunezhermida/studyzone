package com.alvaronunez.studyzone.presentation.ui.main

import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.alvaronunez.studyzone.R
import com.alvaronunez.studyzone.databinding.FragmentMainBinding
import com.alvaronunez.studyzone.presentation.ui.common.EventObserver
import com.alvaronunez.studyzone.presentation.ui.common.bindingInflate
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.ext.android.viewModel


class MainFragment : Fragment() {

    private val viewModel: MainViewModel by currentScope.viewModel(this)
    private lateinit var adapter : ItemsAdapter
    private lateinit var navController: NavController
    private var binding: FragmentMainBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = container?.bindingInflate(R.layout.fragment_main, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = view.findNavController()

        adapter = ItemsAdapter(viewModel::onItemClicked)
        binding?.apply {
            viewmodel = viewModel
            recycler.adapter = adapter
            lifecycleOwner = this@MainFragment
        }

        setUpObservers()
    }

    private fun setUpObservers() {
        viewModel.finish.observe(this, EventObserver {
            //TODO: finish()
        })

        viewModel.navigateToCreateItem.observe(this, EventObserver{
            navController.navigate(R.id.action_mainFragment_to_createItemFragment)
        })

        viewModel.navigateToCamera.observe(this, EventObserver{
            navController.navigate(R.id.action_mainFragment_to_createPhotoItemFragment)
        })

        viewModel.openFabs.observe(this, Observer { openFabs ->
            if(openFabs) animateFabs(R.anim.rotate_anticlockwise, R.anim.fab_open)
            else animateFabs(R.anim.rotate_clockwise, R.anim.fab_close)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_sign_up -> viewModel.onSignOutClicked()
        }
        return false
    }

    private fun animateFabs(mainFabAnimRes: Int, fabsAnimRes: Int) {
        context?.let {
            val mainFabAnim = AnimationUtils.loadAnimation(it, mainFabAnimRes)
            val fabsAnim = AnimationUtils.loadAnimation(it, fabsAnimRes)
            fab.startAnimation(mainFabAnim)
            fab_item.startAnimation(fabsAnim)
            fab_image.startAnimation(fabsAnim)
            fab_audio.startAnimation(fabsAnim)
            fab_file.startAnimation(fabsAnim)
        }
    }
}