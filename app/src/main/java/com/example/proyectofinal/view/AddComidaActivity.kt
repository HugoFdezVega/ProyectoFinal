package com.example.proyectofinal.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.ActivityAddComidaBinding

class AddComidaActivity : AppCompatActivity() {
    private val responseLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode==RESULT_OK){
            //TODO recoger la comida seleccionada
        }
    }

    lateinit var binding: ActivityAddComidaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddComidaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
    }

    private fun setListeners() {
        binding.btAddIngredientesComida.setOnClickListener {
            responseLauncher.launch(Intent(this,SeleccionarIngredienteActivity::class.java))
        }
    }
}