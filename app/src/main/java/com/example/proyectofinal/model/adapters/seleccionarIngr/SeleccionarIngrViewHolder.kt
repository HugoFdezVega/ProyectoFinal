package com.example.proyectofinal.model.adapters.seleccionarIngr

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.databinding.CardSeleccionarIngredienteBinding
import com.example.proyectofinal.model.Ingrediente
import com.squareup.picasso.Picasso

class SeleccionarIngrViewHolder(v: View): RecyclerView.ViewHolder(v) {
    val binding=CardSeleccionarIngredienteBinding.bind(v)

    fun render(ingrediente: Ingrediente, onItemSelected: (Ingrediente)->Unit){
        if(ingrediente.imagen!="null"){
            Picasso.get().load(ingrediente.imagen).into(binding.ivSeleccionarIngrediente)
        }
        binding.tvNombreIngrediente.text=ingrediente.nombre
        itemView.setOnClickListener {
            onItemSelected(ingrediente)
        }
    }

}
