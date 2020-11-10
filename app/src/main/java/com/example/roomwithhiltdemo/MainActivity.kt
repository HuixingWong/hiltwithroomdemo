package com.example.roomwithhiltdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var mainRepository: MainRepository

//    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        lifecycleScope.launch {
            val databaseSize = mainRepository.numberOfItemsInDB()
            tv.text = databaseSize.toString()

        }

        tv.setOnClickListener {
            lifecycleScope.launch {
                mainRepository.insertdata(User())
            }
        }



    }
}