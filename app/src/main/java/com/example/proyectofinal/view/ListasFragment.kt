package com.example.proyectofinal.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
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

/**
 * Listas fragment
 *
 * @constructor Create empty Listas fragment
 */
@AndroidEntryPoint
class ListasFragment : Fragment() {
    private val vm: MainViewModel by viewModels()
    private val listaIngredientes = mutableListOf<Ingrediente>()
    private val listaComidas = mutableListOf<Comida>()
    private var admin = false

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
    lateinit var tvTotal: TextView
    lateinit var btOcultar: Button


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
        observarComidas()
        observarIngredientes()
        inicializar(view)
        setListeners()
        setRecyclers(view)
    }

    /**
     * Comprobar admin
     *
     */
    private fun comprobarAdmin() {
        var admins = resources.getStringArray(R.array.admins)
        if (admins.contains(vm.obtenerUsuario())) {
            admin = true
            btAdd.isVisible = true
        }
    }

    /**
     * Observar comidas
     *
     */
    private fun observarComidas() {
        vm.readComidas().observe(viewLifecycleOwner, Observer {
            pbListas.isVisible = false
            comprobarFiltros()
        })
    }

    /**
     * Observar ingredientes
     *
     */
    private fun observarIngredientes() {
        vm.readIngredientes().observe(viewLifecycleOwner, Observer {
            pbListas.isVisible = false
            comprobarFiltros()
        })
    }

    /**
     * Comprobar filtros
     *
     * Comprobara los filtros que estan seleccionados y asignara una lista al recycler en funcion de ello
     *
     */
    private fun comprobarFiltros() {
        if (rbComidas.isChecked) {
            rvListas.adapter = adapterComidas
            if (cbVegano.isChecked && cbGlutenFree.isChecked) {
                adapterComidas.lista = vm.getComidasVeganasGlutenFree()
            } else if (cbVegano.isChecked) {
                adapterComidas.lista = vm.getComidasVeganas()
            } else if (cbGlutenFree.isChecked) {
                adapterComidas.lista = vm.getComidasGlutenFree()
            } else {
                adapterComidas.lista = vm.getListaComidas()
            }
            tvTotal.text = "Total comidas: ${adapterComidas.lista.size}"
        } else {
            rvListas.adapter = adapterIngredientes
            if (cbVegano.isChecked && cbGlutenFree.isChecked) {
                adapterIngredientes.lista = vm.getIngrVeganosGlutenFree()
            } else if (cbVegano.isChecked) {
                adapterIngredientes.lista = vm.getIngrVeganos()
            } else if (cbGlutenFree.isChecked) {
                adapterIngredientes.lista = vm.getIngrGlutenFree()
            } else {
                adapterIngredientes.lista = vm.getListaIngredientes()
            }
            tvTotal.text = "Total ingredientes: ${adapterIngredientes.lista.size}"
        }
    }

    /**
     * Set recyclers
     *
     * @param view
     */
    private fun setRecyclers(view: View) {
        rvListas.layoutManager = LinearLayoutManager(view.context)
        adapterIngredientes = ListaIngredientesAdapter(
            listaIngredientes,
            "null",
            { onIngrDelete(it) },
            { onIngrUpdate(it) },
            admin,
            { onCantidadUpdate(it) })
        adapterComidas = ListaComidasAdapter(
            listaComidas,
            "null",
            { onComidaDelete(it) },
            { onComidaUpdate(it) },
            { onComidaOtra(it) },
            { onComidaParecida(it) },
            { posicion, raciones -> onComidaRaciones(posicion, raciones) })
    }

    private fun onCantidadUpdate(it: Ingrediente) {
    }

    private fun onComidaRaciones(posicion: Int, raciones: Int) {
    }

    private fun onComidaParecida(it: Int) {
    }

    private fun onComidaOtra(it: Int) {
    }

    private fun onComidaDelete(it: Int) {
    }

    /**
     * On comida update
     *
     * Abre la vista detalle de las comidas
     *
     * @param comida
     */
    private fun onComidaUpdate(comida: Comida) {
        val intent = Intent(btAdd.context, AddComidaActivity::class.java).apply {
            putExtra("comida", comida)
        }
        startActivity(intent)
    }

    /**
     * On ingr update
     *
     * Abre la vista detalle de los ingredientes
     *
     * @param ingrediente
     */
    private fun onIngrUpdate(ingrediente: Ingrediente) {
        val i = Intent(btAdd.context, AddIngredienteActivity::class.java).apply {
            putExtra("ingrediente", ingrediente)
        }
        startActivity(i)
    }

    private fun onIngrDelete(it: Int) {
    }

    /**
     * Inicializar
     *
     * Inicializa todos los controles
     *
     * @param view
     */
    private fun inicializar(view: View) {
        btAdd = view.findViewById(R.id.btAddListas)
        sbBusqueda = view.findViewById(R.id.sbListas)
        cbVegano = view.findViewById(R.id.cbVeganoListas)
        cbGlutenFree = view.findViewById(R.id.cbGlutenFreeListas)
        rbComidas = view.findViewById(R.id.rbComidas)
        rbIngredientes = view.findViewById(R.id.rbIngredientes)
        rvListas = view.findViewById(R.id.rvListas)
        pbListas = view.findViewById(R.id.pbListas)
        tvTotal = view.findViewById(R.id.tvTotal)
        btOcultar = view.findViewById(R.id.btOcultar)
        comprobarAdmin()
    }

    /**
     * Set listeners
     *
     */
    private fun setListeners() {
        btAdd.setOnClickListener {
            if (rbComidas.isChecked) {
                startActivity(Intent(btAdd.context, AddComidaActivity::class.java))
            } else {
                startActivity(Intent(btAdd.context, AddIngredienteActivity::class.java))
            }
        }
        sbBusqueda.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                buscarEnLista(newText)
                return true
            }
        })
        btOcultar.setOnClickListener {
            sbBusqueda.setQuery("",false)
            it.ocultarTeclado()
            sbBusqueda.clearFocus()
        }
        cbVegano.setOnClickListener { comprobarFiltros() }
        cbGlutenFree.setOnClickListener { comprobarFiltros() }
        rbComidas.setOnClickListener { comprobarFiltros() }
        rbIngredientes.setOnClickListener { comprobarFiltros() }
    }

    /**
     * Ocultar teclado
     *
     */
    private fun View.ocultarTeclado(){
        val inputManager=context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken,0)
    }

    /**
     * Buscar en lista
     *
     * Metodo para buscar en las listas, filtandolas segun letras o reasignando la lista inicial de
     * borrarse todo
     *
     * @param query
     */
    private fun buscarEnLista(query: String?) {
        if (!query!!.isBlank()) {
            if (rbComidas.isChecked) {
                comprobarFiltros()
                var listaFiltrada = mutableListOf<Comida>()
                val listaAFiltrar = adapterComidas.lista
                for (i in listaAFiltrar) {
                    if (i.nombre!!.lowercase().contains(query.lowercase())){
                        listaFiltrada.add(i)
                    }
                }
                if(listaFiltrada.isEmpty()){
                    adapterComidas.lista= mutableListOf()
                } else {
                    adapterComidas.lista=listaFiltrada
                }
                adapterComidas.notifyDataSetChanged()
                tvTotal.text = "Total comidas: ${adapterComidas.lista.size}"
            } else {
                comprobarFiltros()
                var listaFiltrada = mutableListOf<Ingrediente>()
                val listaAFiltrar = adapterIngredientes.lista
                for (i in listaAFiltrar) {
                    if (i.nombre!!.lowercase().contains(query.lowercase())){
                        listaFiltrada.add(i)
                    }
                }
                if(listaFiltrada.isEmpty()){
                    adapterIngredientes.lista= mutableListOf()
                } else {
                    adapterIngredientes.lista=listaFiltrada
                }
                adapterIngredientes.notifyDataSetChanged()
                tvTotal.text = "Total ingredientes: ${adapterIngredientes.lista.size}"
            }
        } else {
            comprobarFiltros()
        }
    }

    override fun onResume() {
        super.onResume()
        sbBusqueda.clearFocus()
    }


}