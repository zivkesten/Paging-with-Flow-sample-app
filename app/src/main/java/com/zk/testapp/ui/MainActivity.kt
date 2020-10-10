package com.zk.testapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.zk.testapp.R
import com.zk.testapp.model.ViewEffect
import com.zk.testapp.viewModel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val model by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, PhotoListFragment.newInstance(), PhotoListFragment::class.qualifiedName)
            .commit()

        observeViewEffect()
    }

    private fun observeViewEffect() {
        model.obtainViewEffects.observe(this, Observer {
            trigger(it)
        })
    }

    private fun trigger(effect: ViewEffect) {
        when(effect) {
            is ViewEffect.TransitionToScreen -> {
                TODO( "implement second screen")
//                val intent = Intent(this, SinglePhotoActivity::class.java)
//                intent.putExtra(getString(R.string.extra_item), effect.article)
//                startActivity(intent)
            }
        }
    }
}
