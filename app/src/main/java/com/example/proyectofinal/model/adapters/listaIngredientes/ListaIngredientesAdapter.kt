package com.example.proyectofinal.model.adapters.listaIngredientes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.R
import com.example.proyectofinal.model.Ingrediente

/**
 * Lista ingredientes adapter
 *
 * @property lista
 * @property actividad
 * @property onItemDelete
 * @property onItemUpdate
 * @property admin
 * @property onCantidadUpdate
 * @constructor Create empty Lista ingredientes adapter
 */
class ListaIngredientesAdapter(
    var lista: MutableList<Ingrediente>,
    private val actividad: String,
    private val onItemDelete:(Int)->Unit,
    private val onItemUpdate:(Ingrediente)->Unit,
    private val admin: Boolean,
    private val onCantidadUpdate:(Ingrediente)->Unit,): RecyclerView.Adapter<ListaIngredientesViewHolder>() {
    /**
     * On create view holder
     *
     * @param parent
     * @param viewType
     * @return
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaIngredientesViewHolder {
        val v=LayoutInflater.from(parent.context).inflate(R.layout.card_ingrediente,parent,false)
        return ListaIngredientesViewHolder(v)
    }

    /**
     * On bind view holder
     *
     * @param holder
     * @param position
     */
    override fun onBindViewHolder(holder: ListaIngredientesViewHolder, position: Int) {
        holder.render(lista[position], actividad, onItemDelete, onItemUpdate, admin, onCantidadUpdate)
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