package com.example.proyectofinal.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.R
import com.example.proyectofinal.model.Comida
import com.example.proyectofinal.model.Ingrediente
import com.example.proyectofinal.model.adapters.listaComidas.ListaComidasAdapter
import com.example.proyectofinal.model.adapters.listaIngredientes.ListaIngredientesAdapter
import com.example.proyectofinal.viewmodel.MainViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListasFragment : Fragment() {
    private val vm: MainViewModel by viewModels()
    private val listaIngredientes= mutableListOf<Ingrediente>()
    private val listaComidas= mutableListOf<Comida>()
    private var admin=false

    lateinit var btAdd: FloatingActionButton
    lateinit var sbBusqueda: SearchView
    lateinit var cbVegano: CheckBox
    lateinit var cbGlutenFree: CheckBox
    lateinit var rbComidas: RadioButton
    lateinit var rbIngredientes: RadioButton
    lateinit var rvListas: RecyclerView
    lateinit var adapterIngredientes: ListaIngredientesAdapter
    lateinit var adapterComidas: ListaComidasAdapter
    lateinit var pbListas: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        vm.inicializar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_listas, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observarComidas()
        inicializar(view)
        setListeners()
        setRecyclers(view)
        //observarIngredientes()

    }

    private fun observarComidas() {
        vm.isLoading.observe(viewLifecycleOwner, Observer {
            pbListas.isVisible=it
            if(!pbListas.isVisible){
                rvListas.adapter=adapterComidas
                adapterComidas.lista=vm.getListaComidas()
                adapterComidas.notifyDataSetChanged()
            }
        })
    }

    private fun setRecyclers(view: View) {
        rvListas.layoutManager=LinearLayoutManager(view.context)
        adapterIngredientes= ListaIngredientesAdapter(listaIngredientes,"null",{onIngrDelete(it)},{onIngrUpdate(it)},admin)
        adapterComidas= ListaComidasAdapter(listaComidas,"null",{onComidaDelete(it)},{onComidaUpdate(it)})

    }

    private fun onComidaUpdate(it: Ingrediente) {
        TODO("Not yet implemented")
    }

    private fun onComidaDelete(it: Int) {
        TODO("Not yet implemented")
    }

    private fun onIngrUpdate(it: Ingrediente) {
        TODO("Not yet implemented")
    }

    private fun onIngrDelete(it: Int) {
        TODO("Not yet implemented")
    }

/*
    private fun observarIngredientes() {
        vm.readIngredientes().observe(viewLifecycleOwner, Observer{
                rvListas.adapter=adapterIngredientes
                adapterIngredientes.lista=it
                adapterIngredientes.notifyDataSetChanged()
        })
    }
 */



    private fun inicializar(view: View) {
        btAdd=view.findViewById(R.id.btAddListas)
        sbBusqueda=view.findViewById(R.id.sbListas)
        cbVegano=view.findViewById(R.id.cbVeganoListas)
        cbGlutenFree=view.findViewById(R.id.cbGlutenFreeListas)
        rbComidas=view.findViewById(R.id.rbComidas)
        rbIngredientes=view.findViewById(R.id.rbIngredientes)
        rvListas=view.findViewById(R.id.rvListas)
        pbListas=view.findViewById(R.id.pbListas)
    }

    private fun setListeners() {
        btAdd.setOnClickListener {
            if(rbComidas.isChecked){
                startActivity(Intent(btAdd.context,AddComidaActivity::class.java))
            } else {
                startActivity(Intent(btAdd.context,AddIngredienteActivity::class.java))
            }
        }


        sbBusqueda.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                listaFiltrada(newText)
                return true
            }
        })
    }

    private fun listaFiltrada(busqueda: String?) {
        if(busqueda!=null){
            val listaFiltrada=ArrayList<Comida>()
            //TODO https://www.youtube.com/watch?v=SD097oVVrPE&ab_channel=CodingSTUFF min 13:00
        }
    }

}