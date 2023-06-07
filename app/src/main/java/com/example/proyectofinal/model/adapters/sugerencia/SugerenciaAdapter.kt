package com.example.proyectofinal.model.adapters.sugerencia

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.R
import com.example.proyectofinal.model.Sugerencia

/**
 * Sugerencia adapter
 *
 * @property lista
 * @property onItemDelete
 * @constructor Create empty Sugerencia adapter
 */
class SugerenciaAdapter(var lista: MutableList<Sugerencia>,
                        val onItemDelete:(Sugerencia)->Unit): RecyclerView.Adapter<SugerenciaViewHolder>() {
    /**
     * On create view holder
     *
     * @param parent
     * @param viewType
     * @return
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SugerenciaViewHolder {
        val v=LayoutInflater.from(parent.context).inflate(R.layout.card_sugerencias,parent,false)
        return SugerenciaViewHolder(v)
    }

    /**
     * On bind view holder
     *
     * @param holder
     * @param position
     */
    override fun onBindViewHolder(holder: SugerenciaViewHolder, position: Int) {
        holder.render(lista[position],onItemDelete)
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