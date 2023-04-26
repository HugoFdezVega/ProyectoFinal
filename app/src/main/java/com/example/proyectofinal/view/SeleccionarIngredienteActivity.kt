package com.example.proyectofinal.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.proyectofinal.databinding.ActivitySeleccionarIngredienteBinding
import com.example.proyectofinal.model.Ingrediente
import com.example.proyectofinal.model.adapters.seleccionarIngr.SeleccionarIngrAdapter
import com.example.proyectofinal.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SeleccionarIngredienteActivity : AppCompatActivity() {
    private val vm: MainViewModel by viewModels()
    private var lista= mutableListOf<Ingrediente>()

    lateinit var adapter: SeleccionarIngrAdapter
    lateinit var binding: ActivitySeleccionarIngredienteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySeleccionarIngredienteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setRecycler()
        observarIngredientes()
        setListeners()
    }

    private fun setListeners() {
        binding.btAddIngrediente.setOnClickListener {
            startActivity(Intent(this,AddIngredienteActivity::class.java))
        }
    }

    private fun setRecycler() {
        binding.rvSeleccionarIngredientes.layoutManager=GridLayoutManager(this,3)
        adapter= SeleccionarIngrAdapter(lista,{onItemSelected(it)})
        binding.rvSeleccionarIngredientes.adapter=adapter
    }

    private fun observarIngredientes() {
        vm.readIngredientes().observe(this, Observer{
            binding.pbSeleccionarIngr.isVisible=true
            adapter.lista=it
            adapter.notifyDataSetChanged()
            binding.pbSeleccionarIngr.isVisible=false
        })
    }

    private fun onItemSelected(ingrediente: Ingrediente) {
        val i=Intent()
        i.apply {
            putExtra("seleccionado",ingrediente)
        }
        setResult(RESULT_OK,i)
        finish()
    }
}