package com.example.proyectofinal.model.adapters.listaComidas

import android.view.View
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.CardComidaBinding
import com.example.proyectofinal.model.Comida
import com.squareup.picasso.Picasso

class ListaComidasViewHolder(v: View):RecyclerView.ViewHolder(v) {
    private val binding=CardComidaBinding.bind(v)
    private val diasSemana= arrayListOf<String>("Lunes","Martes","Miércoles","Jueves","Viernes")
    private var index=0

    fun render(
        comida: Comida,
        actividad: String,
        onItemDelete: (Int) -> Unit,
        onItemUpdate: (Comida) -> Unit,
        onComidaOtra: (Int) -> Unit,
        onComidaParecida: (Int) -> Unit,
        onComidaRaciones: (Int, Int) -> Unit
    ){
        if(actividad=="menu"){
            binding.tvRacionesLb.isGone=false
            binding.tvNumRaciones.isGone=false
            binding.btMasRaciones.isGone=false
            binding.btMenosRaciones.isGone=false
            binding.btOtra.isGone=false
            binding.btParecida.isGone=false
            binding.tvDia.isGone=false
            binding.tvDia.text=diasSemana[adapterPosition]
        }
        binding.tvNombreComidaLista.text = comida.nombre
        binding.tvTag1ComidaLista.text = comida.tags!![0]
        binding.tvTag2ComidaLista.text = comida.tags!![1]
        binding.tvDescrComidaLista.text = comida.descripcion
        binding.tvNumRaciones.text=comida.raciones.toString()
        index=comida.raciones
        Picasso.get().load(comida.imagen).into(binding.ivComidaLista)
        binding.btOtra.setOnClickListener {
            onComidaOtra(adapterPosition)
        }
        binding.btParecida.setOnClickListener {
            onComidaParecida(adapterPosition)
        }
        binding.btMasRaciones.setOnClickListener {
            index++
            binding.tvNumRaciones.text=index.toString()
            onComidaRaciones(adapterPosition, index)
        }
        binding.btMenosRaciones.setOnClickListener {
            if(index!=0){
                index--
                binding.tvNumRaciones.text=index.toString()
                onComidaRaciones(adapterPosition, index)
            }
        }
        itemView.setOnClickListener {
            onItemUpdate(comida)
        }
    }
}
