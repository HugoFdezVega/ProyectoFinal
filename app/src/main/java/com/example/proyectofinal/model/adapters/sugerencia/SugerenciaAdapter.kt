package com.example.proyectofinal.model.adapters.sugerencia

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.R
import com.example.proyectofinal.model.Sugerencia

class SugerenciaAdapter(var lista: MutableList<Sugerencia>,
                        val onItemDelete:(Sugerencia)->Unit): RecyclerView.Adapter<SugerenciaViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SugerenciaViewHolder {
        val v=LayoutInflater.from(parent.context).inflate(R.layout.card_sugerencias,parent,false)
        return SugerenciaViewHolder(v)
    }

    override fun onBindViewHolder(holder: SugerenciaViewHolder, position: Int) {
        holder.render(lista[position],onItemDelete)
    }

    override fun getItemCount(): Int {
        return lista.size
    }
}