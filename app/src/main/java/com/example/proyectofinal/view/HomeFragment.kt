package com.example.proyectofinal.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.R
import com.example.proyectofinal.model.Comida
import com.example.proyectofinal.model.adapters.listaComidas.ListaComidasAdapter
import com.example.proyectofinal.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val vm: MainViewModel by viewModels()
    private var listaMenu= mutableListOf<Comida>()

    lateinit var cbVeganoMenu: CheckBox
    lateinit var cbGlutenFreeMenu: CheckBox
    lateinit var btGenerarMenu: Button
    lateinit var btCarrito: ImageButton
    lateinit var recMenu: RecyclerView
    lateinit var btGenerarListaCompra: Button
    lateinit var pbHome: ProgressBar
    lateinit var ivNotificacion: ImageView
    lateinit var tvVacio: TextView
    lateinit var adapter: ListaComidasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inicializar(view)
        observarMenu()
        setRecycler(view)
        setListeners()
    }

    private fun setListeners() {
        btGenerarMenu.setOnClickListener {
            vm.generarMenu(cbVeganoMenu.isChecked,cbGlutenFreeMenu.isChecked)
        }
    }

    private fun setRecycler(view: View) {
        recMenu.layoutManager=LinearLayoutManager(view.context)
        adapter= ListaComidasAdapter(listaMenu,"menu",{onComidaDelete(it)},{onComidaUpdate(it)},{onComidaOtra(it)},{onComidaParecida(it)})
        recMenu.adapter=adapter
    }

    private fun onComidaParecida(posicion: Int) {

    }

    private fun onComidaOtra(posicion: Int) {
        val otraComida=vm.otraComida(posicion, cbVeganoMenu.isChecked,cbGlutenFreeMenu.isChecked)
        listaMenu[posicion]=otraComida
        adapter.lista=listaMenu
        adapter.notifyItemChanged(posicion)
    }

    private fun onComidaUpdate(comida: Comida) {

    }

    private fun onComidaDelete(posicion: Int) {

    }

    private fun observarMenu() {
        vm.ldListaMenu.observe(viewLifecycleOwner, Observer{
            pbHome.isVisible=false
            if(it.size<5){
                tvVacio.isGone=false
                recMenu.isVisible=false
                btGenerarListaCompra.isVisible=false
            } else {
                tvVacio.isGone=true
                recMenu.isVisible=true
                btGenerarListaCompra.isVisible=true
                listaMenu=it
                adapter.lista=listaMenu
                adapter.notifyDataSetChanged()
            }
        })
        vm.readMenu()
    }

    private fun inicializar(view: View) {
        cbVeganoMenu=view.findViewById(R.id.cbVeganoMenu)
        cbGlutenFreeMenu=view.findViewById(R.id.cbGlutenFreeMenu)
        btGenerarMenu=view.findViewById(R.id.btGenerarMenu)
        btCarrito=view.findViewById(R.id.btCarrito)
        recMenu=view.findViewById(R.id.recMenu)
        btGenerarListaCompra=view.findViewById(R.id.btGenerarLista)
        pbHome=view.findViewById(R.id.pbHome)
        ivNotificacion=view.findViewById(R.id.ivNotificacion)
        tvVacio=view.findViewById(R.id.tvVacio)
    }

}