package com.example.proyectofinal.model.adapters.listaComidas

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.databinding.CardComidaBinding
import com.example.proyectofinal.model.Comida
import com.example.proyectofinal.model.Ingrediente
import com.squareup.picasso.Picasso

class ListaComidasViewHolder(v: View):RecyclerView.ViewHolder(v) {
    private val binding=CardComidaBinding.bind(v)

    fun render(comida: Comida, actividad: String, onItemDelete: (Int) -> Unit, onItemUpdate: (Ingrediente) -> Unit){
        if(actividad=="menu"){
            //TODO pintar los controles del menu
        }
        binding.tvNombreComidaLista.text = comida.nombre
        binding.tvTag1ComidaLista.text = comida.tags!![0]
        binding.tvTag2ComidaLista.text = comida.tags!![1]
        binding.tvDescrComidaLista.text = comida.descripcion
        if(comida.imagen!="null"){
            Picasso.get().load(comida.imagen).into(binding.ivComidaLista)
        }
    }
}
