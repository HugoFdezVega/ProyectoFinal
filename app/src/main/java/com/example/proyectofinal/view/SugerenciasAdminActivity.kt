package com.example.proyectofinal.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.ActivitySugerenciasAdminBinding
import com.example.proyectofinal.model.Sugerencia
import com.example.proyectofinal.model.adapters.sugerencia.SugerenciaAdapter
import com.example.proyectofinal.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Sugerencias admin activity
 *
 * @constructor Create empty Sugerencias admin activity
 */
@AndroidEntryPoint
class SugerenciasAdminActivity : AppCompatActivity() {
    private val vm: MainViewModel by viewModels()
    var lista= mutableListOf<Sugerencia>()

    lateinit var adapter: SugerenciaAdapter
    lateinit var binding: ActivitySugerenciasAdminBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySugerenciasAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        setRecycler()
        observarSugerencias()
    }

    /**
     * Observar sugerencias
     *
     */
    private fun observarSugerencias() {
        vm.readSugerencias().observe(this, Observer {
            lista=it
            adapter.lista=it
            adapter.notifyDataSetChanged()
        })
    }

    /**
     * Set recycler
     *
     */
    private fun setRecycler() {
        binding.rvSugsAdmin.layoutManager=LinearLayoutManager(this)
        adapter=SugerenciaAdapter(lista, {onItemDelete(it)})
        binding.rvSugsAdmin.adapter=adapter
    }

    /**
     * On item delete
     *
     * Borra la sugerencia de la lista y de la BD
     *
     * @param sugerencia
     */
    private fun onItemDelete(sugerencia: Sugerencia) {
        val index=lista.indexOf(sugerencia)
        lista.remove(sugerencia)
        adapter.notifyItemRemoved(index)
        vm.borrarSugerencia(sugerencia)
    }

    /**
     * Set listeners
     *
     */
    private fun setListeners() {
        binding.btVolverSugsAdmin.setOnClickListener {
            finish()
        }
    }
}