package com.example.proyectofinal.view

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import androidx.activity.viewModels
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.ActivitySugerenciasUserBinding
import com.example.proyectofinal.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SugerenciasUserActivity : AppCompatActivity() {
    private val vm: MainViewModel by viewModels()
    private val maxCaracteres=500
    private val filtroCaracteres=InputFilter.LengthFilter(maxCaracteres)
    lateinit var binding: ActivitySugerenciasUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySugerenciasUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
    }

    private fun setListeners() {
        binding.etTextoSugsUser.filters= arrayOf(filtroCaracteres)
        binding.etTextoSugsUser.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val caracteresActuales= s?.length ?:0
                binding.tvCharsSugsUser.text="${caracteresActuales}/500"
                if(caracteresActuales==500){
                    binding.tvCharsSugsUser.setTextColor(Color.RED)
                }
                else if(caracteresActuales>399){
                    binding.tvCharsSugsUser.setTextColor(Color.rgb(255,160,64))
                }
                else{
                    binding.tvCharsSugsUser.setTextColor(Color.BLACK)
                }
            }
        })
    }

}