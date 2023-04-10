package com.example.proyectofinal.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.ActivityAddComidaBinding
import com.example.proyectofinal.databinding.ActivityAddIngredienteBinding

class AddIngredienteActivity : AppCompatActivity() {



    lateinit var binding: ActivityAddIngredienteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddIngredienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }


}