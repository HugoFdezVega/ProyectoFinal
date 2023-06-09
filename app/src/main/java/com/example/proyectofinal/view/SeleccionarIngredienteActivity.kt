package com.example.proyectofinal.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.ActivitySeleccionarIngredienteBinding
import com.example.proyectofinal.model.Ingrediente
import com.example.proyectofinal.model.adapters.seleccionarIngr.SeleccionarIngrAdapter
import com.example.proyectofinal.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Seleccionar ingrediente activity
 *
 * @constructor Create empty Seleccionar ingrediente activity
 */
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

    /**
     * Set listeners
     *
     */
    private fun setListeners() {
        binding.btAddIngrediente.setOnClickListener {
            startActivity(Intent(this,AddIngredienteActivity::class.java))
        }
        binding.svBuscarIngredientes.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                buscarEnLista(newText)
                return true
            }
        })
        binding.btOcultarSelect.setOnClickListener {
            binding.svBuscarIngredientes.setQuery("",false)
            it.ocultarTeclado()
            binding.svBuscarIngredientes.clearFocus()
        }
    }

    /**
     * Ocultar teclado
     *
     * Oculta el teclado si se pulsa en la equis de la barra de busqyeda
     *
     */
    private fun View.ocultarTeclado(){
        val inputManager=context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken,0)
    }

    /**
     * Buscar en lista
     *
     * Busca en la lista en funcion de las letras o restaura la lista si se borra todo
     *
     * @param query
     */
    private fun buscarEnLista(query: String?) {
        if(!query!!.isBlank()){
            val listaFiltrada= mutableListOf<Ingrediente>()
            val listaAFiltrar=lista
            for (i in listaAFiltrar) {
                if (i.nombre!!.lowercase().contains(query.lowercase())){
                    listaFiltrada.add(i)
                }
            }
            if(listaFiltrada.isEmpty()){
                adapter.lista= mutableListOf()
            } else {
                adapter.lista=listaFiltrada
            }
            adapter.notifyDataSetChanged()
        } else {
            adapter.lista=lista
            adapter.notifyDataSetChanged()
        }
    }

    /**
     * Set recycler
     *
     */
    private fun setRecycler() {
        binding.rvSeleccionarIngredientes.layoutManager=GridLayoutManager(this,3)
        adapter= SeleccionarIngrAdapter(lista,{onItemSelected(it)})
        binding.rvSeleccionarIngredientes.adapter=adapter
    }

    /**
     * Observar ingredientes
     *
     * Observa y actualiza la lista de ingredientes en funcion de la BBDD
     *
     */
    private fun observarIngredientes() {
        vm.readIngredientes().observe(this, Observer{
            binding.pbSeleccionarIngr.isVisible=true
            adapter.lista=it
            lista=it
            adapter.notifyDataSetChanged()
            binding.pbSeleccionarIngr.isVisible=false
        })
    }

    /**
     * On item selected
     *
     * Cuando se selecciona un ingrediente, no muestra un alertDialog para elegir su cantidad, crea
     * el ingrediente y lo devuelve para añadirlo a la lista
     *
     * @param ingrediente
     */
    private fun onItemSelected(ingrediente: Ingrediente) {
        var ingrSeleccionado=Ingrediente(ingrediente)
        val builder= AlertDialog.Builder(this)
        val inflater=layoutInflater
        val dialogLayout=inflater.inflate(R.layout.dialog_cantidad,null)
        val etCantidad=dialogLayout.findViewById<EditText>(R.id.etDialogCantidad)
        val tvMedida=dialogLayout.findViewById<TextView>(R.id.tvDialogMedida)
        tvMedida.text=ingrSeleccionado.medida
        with(builder){
            setTitle("Introduzca la cantidad de ${ingrSeleccionado.nombre} que desea agregar:")
            setPositiveButton("Aceptar") { dialog, which ->
                val textoCantidad = etCantidad.text.toString().trim()
                if(textoCantidad.isBlank()){
                    Toast.makeText(this@SeleccionarIngredienteActivity,"Error: La cantidad no puede estar vacía", Toast.LENGTH_LONG).show()
                } else {
                    val cantidad=textoCantidad.toDouble()
                    if(cantidad<=0.0){
                        Toast.makeText(this@SeleccionarIngredienteActivity,"Error: La cantidad debe ser mayor que 0", Toast.LENGTH_LONG).show()
                    } else {
                        ingrSeleccionado.cantidad=cantidad
                        val i=Intent()
                        i.apply {
                            putExtra("seleccionado",ingrSeleccionado)
                        }
                        setResult(RESULT_OK,i)
                        finish()
                    }
                    dialog.dismiss()
                }
            }
            setNegativeButton("Cancelar") { dialog, which ->
                dialog.dismiss()
            }
            setCancelable(false)
            setView(dialogLayout)
            show()
        }
    }

}