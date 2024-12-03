package com.example.outdoorsport.presentation

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.outdoorsport.R
import com.example.outdoorsport.databinding.ActivityMainBinding
import com.example.outdoorsport.presentation.fragments.StartGameFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(
            layoutInflater
        )

        setContentView(binding.root)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        supportFragmentManager.beginTransaction()
            .add(R.id.container_main, StartGameFragment.newInstance())
            .addToBackStack(null)
            .commitAllowingStateLoss()

       /* lifecycleScope.launch {
            delay(2000L)
            supportFragmentManager.beginTransaction()
                .add(R.id.container_main, StartGameFragment.newInstance())
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }*/
    }
}