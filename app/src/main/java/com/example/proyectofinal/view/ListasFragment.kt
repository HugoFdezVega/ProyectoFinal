package com.example.proyectofinal.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.R
import com.example.proyectofinal.model.Comida
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListasFragment : Fragment() {
    lateinit var btAdd: FloatingActionButton
    lateinit var sbBusqueda: SearchView
    lateinit var cbVegano: CheckBox
    lateinit var cbGlutenFree: CheckBox
    lateinit var rbComidas: RadioButton
    lateinit var rbIngredientes: RadioButton
    lateinit var rvListas: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_listas, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inicializar(view)
        setListeners()
    }

    private fun inicializar(view: View) {
        btAdd=view.findViewById(R.id.btAddListas)
        sbBusqueda=view.findViewById(R.id.sbListas)
        cbVegano=view.findViewById(R.id.cbVegano)
        cbGlutenFree=view.findViewById(R.id.cbGlutenFree)
        rbComidas=view.findViewById(R.id.rbComidas)
        rbIngredientes=view.findViewById(R.id.rbIngredientes)
        rvListas=view.findViewById(R.id.rvListas)
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