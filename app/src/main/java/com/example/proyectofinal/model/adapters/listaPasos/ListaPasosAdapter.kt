package com.example.proyectofinal.model.adapters.listaPasos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.R

/**
 * Lista pasos adapter
 *
 * @property lista
 * @property onItemDelete
 * @property admin
 * @property onPasoUpdate
 * @constructor Create empty Lista pasos adapter
 */
class ListaPasosAdapter(var lista: MutableList<String>,
                        private val onItemDelete:(Int)->Unit,
                        private val admin: Boolean,
                        private val onPasoUpdate:(String)->Unit,): RecyclerView.Adapter<ListaPasosViewHolder>() {
    /**
     * On create view holder
     *
     * @param parent
     * @param viewType
     * @return
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaPasosViewHolder {
        val v=LayoutInflater.from(parent.context).inflate(R.layout.paso_card,parent,false)
        return ListaPasosViewHolder(v)
    }

    /**
     * On bind view holder
     *
     * @param holder
     * @param position
     */
    override fun onBindViewHolder(holder: ListaPasosViewHolder, position: Int) {
        holder.render(lista[position], onItemDelete, onPasoUpdate,admin)
    }

    /**
     * Get item count
     *
     * @return
     */
    override fun getItemCount(): Int {
        return lista.size
    }
}