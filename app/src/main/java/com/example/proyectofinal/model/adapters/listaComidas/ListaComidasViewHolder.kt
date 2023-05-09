package com.example.proyectofinal.model.adapters.listaComidas

import android.view.View
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.databinding.CardComidaBinding
import com.example.proyectofinal.model.Comida
import com.squareup.picasso.Picasso

class ListaComidasViewHolder(v: View):RecyclerView.ViewHolder(v) {
    private val binding=CardComidaBinding.bind(v)
    private val diasSemana= arrayListOf<String>("Lunes","Martes","Miércoles","Jueves","Viernes")

    fun render(comida: Comida, actividad: String, onItemDelete: (Int) -> Unit, onItemUpdate: (Comida) -> Unit, onComidaOtra: (Int) -> Unit, onComidaParecida: (Int) -> Unit){
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
        if(comida.imagen!="null"){
            Picasso.get().load(comida.imagen).into(binding.ivComidaLista)
        } else {
            Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/randomeater-e0c93.appspot.com/o/comidas%2Fcomida.png?alt=media&token=f677154c-3aa8-4f30-a320-4606ff385bcf").into(binding.ivComidaLista)
        }
        binding.btOtra.setOnClickListener {
            onComidaOtra(adapterPosition)
        }
        binding.btParecida.setOnClickListener {
            onComidaParecida(adapterPosition)
        }
    }
}
