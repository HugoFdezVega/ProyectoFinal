package com.example.proyectofinal.model.adapters.seleccionarIngr

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.R
import com.example.proyectofinal.model.Ingrediente

/**
 * Seleccionar ingr adapter
 *
 * @property lista
 * @property onItemSelected
 * @constructor Create empty Seleccionar ingr adapter
 */
class SeleccionarIngrAdapter(var lista: MutableList<Ingrediente>,
                             var onItemSelected:(Ingrediente)->Unit): RecyclerView.Adapter<SeleccionarIngrViewHolder>() {
    /**
     * On create view holder
     *
     * @param parent
     * @param viewType
     * @return
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeleccionarIngrViewHolder {
        val v=LayoutInflater.from(parent.context).inflate((R.layout.card_seleccionar_ingrediente),parent,false)
        return SeleccionarIngrViewHolder(v)
    }

    /**
     * On bind view holder
     *
     * @param holder
     * @param position
     */
    override fun onBindViewHolder(holder: SeleccionarIngrViewHolder, position: Int) {
        holder.render(lista[position],onItemSelected)
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