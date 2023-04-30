package com.example.proyectofinal.model.adapters.listaPasos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.R

class ListaPasosAdapter(var lista: MutableList<String>,
                        private val onItemDelete:(Int)->Unit,
                        private val admin: Boolean): RecyclerView.Adapter<ListaPasosViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaPasosViewHolder {
        val v=LayoutInflater.from(parent.context).inflate(R.layout.paso_card,parent,false)
        return ListaPasosViewHolder(v)
    }

    override fun onBindViewHolder(holder: ListaPasosViewHolder, position: Int) {
        holder.render(lista[position], onItemDelete, admin)
    }

    override fun getItemCount(): Int {
        return lista.size
    }
}