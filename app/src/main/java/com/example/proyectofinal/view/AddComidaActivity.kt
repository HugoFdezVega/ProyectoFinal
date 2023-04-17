package com.example.proyectofinal.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.ActivityAddComidaBinding
import com.example.proyectofinal.model.adapters.ListaPasosAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddComidaActivity : AppCompatActivity() {
    private val responseLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode==RESULT_OK){
            //TODO recoger la comida seleccionada
        }
    }
    private var listaPasos= mutableListOf("")
    lateinit var pasosAdapter:ListaPasosAdapter
    lateinit var binding: ActivityAddComidaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddComidaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        inicializar()
    }

    private fun inicializar() {
        setRecyclers()

    }

    private fun setRecyclers() {
        binding.rvPasos.layoutManager=LinearLayoutManager(this)
        pasosAdapter=ListaPasosAdapter(listaPasos)
        binding.rvPasos.adapter=pasosAdapter
    }

    private fun setListeners() {
        binding.btAddIngredientesComida.setOnClickListener {
            responseLauncher.launch(Intent(this,SeleccionarIngredienteActivity::class.java))
        }
        binding.btVolverAddComida.setOnClickListener {
            finish()
        }
        binding.btAdPasos.setOnClickListener {
            agregarPaso()
        }
    }

    private fun agregarPaso() {
        listaPasos.add("")
        pasosAdapter.notifyDataSetChanged()
        binding.rvPasos.scrollToPosition(listaPasos.size-1) //Bajamos el recycler
        binding.scrollView.smoothScrollTo(0, binding.btAdPasos.top) //Scrolleamos hacia el botón
    }
}