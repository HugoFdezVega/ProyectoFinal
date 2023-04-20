package com.example.proyectofinal.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectofinal.databinding.ActivityAddComidaBinding
import com.example.proyectofinal.model.Comida
import com.example.proyectofinal.model.Ingrediente
import com.example.proyectofinal.model.adapters.ListaIngredientesAdapter
import com.example.proyectofinal.model.adapters.ListaPasosAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddComidaActivity : AppCompatActivity() {
    private val responseLauncherSelect=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode==RESULT_OK){
            val ingredienteSeleccionado=it.data?.getSerializableExtra("seleccionado") as Ingrediente
            if(listaIngredientes.contains(ingredienteSeleccionado)){
                Toast.makeText(this,"Error: El ingrediente seleccionado ya se encuentra en la lista", Toast.LENGTH_LONG).show()
            } else {
                listaIngredientes.add(ingredienteSeleccionado)
                ingredientesAdapter.notifyItemInserted(listaIngredientes.size-1)
            }
        }
    }
    private val responseLauncherUpdate=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode==RESULT_OK){
            val ingredieneRetorno=it.data?.getSerializableExtra("retorno") as Ingrediente
            var indice=listaIngredientes.indexOf(ingredieneRetorno)
            listaIngredientes.removeAt(indice)
            listaIngredientes.add(indice,ingredieneRetorno)
            pasosAdapter.notifyItemChanged(indice)
        }
    }

    private var listaPasos= mutableListOf("")
    private var listaIngredientes= mutableListOf<Ingrediente>()
    lateinit var pasosAdapter:ListaPasosAdapter
    lateinit var ingredientesAdapter:ListaIngredientesAdapter
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
        //Recycler de los pasos
        binding.rvPasos.layoutManager=LinearLayoutManager(this)
        pasosAdapter=ListaPasosAdapter(listaPasos)
        binding.rvPasos.adapter=pasosAdapter
        //Recycler de los ingredientes
        binding.rvIngredientesComida.layoutManager=LinearLayoutManager(this)
        ingredientesAdapter= ListaIngredientesAdapter(listaIngredientes,"add",{onItemDelete(it)},{onItemUpdate(it)})
        binding.rvIngredientesComida.adapter=ingredientesAdapter
    }

    private fun setListeners() {
        binding.btAddIngredientesComida.setOnClickListener {
            responseLauncherSelect.launch(Intent(this,SeleccionarIngredienteActivity::class.java))
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
        pasosAdapter.notifyItemInserted(listaPasos.size-1)
        binding.rvPasos.scrollToPosition(listaPasos.size-1) //Bajamos el recycler
        binding.scrollView.smoothScrollTo(0, binding.btAdPasos.top) //Scrolleamos hacia el botón
    }

    private fun onItemDelete(posicion: Int){
        listaIngredientes.removeAt(posicion)
        ingredientesAdapter.notifyItemRemoved(posicion)
    }

    private fun onItemUpdate(ingrediente: Ingrediente){
        val i=Intent(this,AddIngredienteActivity::class.java).apply {
            ingrediente
        }
        responseLauncherUpdate.launch(i)
    }
}