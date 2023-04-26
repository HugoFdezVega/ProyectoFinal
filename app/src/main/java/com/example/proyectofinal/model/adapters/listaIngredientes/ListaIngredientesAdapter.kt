package com.example.proyectofinal.model.adapters.listaIngredientes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.R
import com.example.proyectofinal.model.Ingrediente

class ListaIngredientesAdapter(
    var lista: MutableList<Ingrediente>,
    private val actividad: String,
    private val onItemDelete:(Int)->Unit,
    private val onItemUpdate:(Ingrediente)->Unit): RecyclerView.Adapter<ListaIngredientesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaIngredientesViewHolder {
        val v=LayoutInflater.from(parent.context).inflate(R.layout.card_ingrediente,parent,false)
        return ListaIngredientesViewHolder(v)
    }

    override fun onBindViewHolder(holder: ListaIngredientesViewHolder, position: Int) {
        holder.render(lista[position], actividad, onItemDelete, onItemUpdate)
    }

    override fun getItemCount(): Int {
        return lista.size
    }
}