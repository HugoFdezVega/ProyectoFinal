package com.example.proyectofinal.model.adapters.listaComidas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.R
import com.example.proyectofinal.model.Comida

class ListaComidasAdapter(var lista: MutableList<Comida>,
                          private val actividad: String,
                          private val onItemDelete:(Int)->Unit,
                          private val onItemUpdate:(Comida)->Unit,
                          private val onComidaOtra:(Int)->Unit,
                          private val onComidaParecida:(Int)->Unit,
                          private val onComidaRaciones:(Int, Int)->Unit,): RecyclerView.Adapter<ListaComidasViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaComidasViewHolder {
        val v=LayoutInflater.from(parent.context).inflate(R.layout.card_comida,parent,false)
        return ListaComidasViewHolder(v)
    }

    override fun onBindViewHolder(holder: ListaComidasViewHolder, position: Int) {
        holder.render(lista[position], actividad, onItemDelete, onItemUpdate, onComidaOtra, onComidaParecida, onComidaRaciones)

    }

    override fun getItemCount(): Int {
        return lista.size
    }



}