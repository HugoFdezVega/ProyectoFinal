package com.example.proyectofinal.model.adapters.listaIngredientes.seleccionarIngr

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.R
import com.example.proyectofinal.model.Ingrediente

class SeleccionarIngrAdapter(var lista: MutableList<Ingrediente>,
                             var onItemSelected:(Ingrediente)->Unit): RecyclerView.Adapter<SeleccionarIngrViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeleccionarIngrViewHolder {
        val v=LayoutInflater.from(parent.context).inflate((R.layout.card_seleccionar_ingrediente),parent,false)
        return SeleccionarIngrViewHolder(v)
    }

    override fun onBindViewHolder(holder: SeleccionarIngrViewHolder, position: Int) {
        holder.render(lista[position],onItemSelected)
    }

    override fun getItemCount(): Int {
        return lista.size
    }
}