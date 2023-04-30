package com.example.proyectofinal.model.adapters.listaIngredientes

import android.view.View
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.databinding.CardIngredienteBinding
import com.example.proyectofinal.model.Ingrediente
import com.squareup.picasso.Picasso

class ListaIngredientesViewHolder(v: View): RecyclerView.ViewHolder(v) {
    private val binding=CardIngredienteBinding.bind(v)

    fun render(
        ingrediente: Ingrediente,
        actividad: String,
        onItemDelete: (Int) -> Unit,
        onItemUpdate: (Ingrediente) -> Unit,
        admin: Boolean
    ){
        if(actividad=="add" && admin){
            binding.etCantidadIngredienteAdd.isGone=false
            binding.tvUnidadIngredienteAdd.isGone=false
            binding.btSuprimirIngredienteAdd.isGone=false
            binding.tvUnidadIngredienteAdd.text=ingrediente.medida
            binding.btSuprimirIngredienteAdd.setOnClickListener {
                onItemDelete(adapterPosition)
            }
        }
        if(ingrediente.imagen!="null"){
            Picasso.get().load(ingrediente.imagen).into(binding.ivIngredienteAdd)
        }
        binding.tvNombreIngredienteAdd.text=ingrediente.nombre
        binding.cbVeganAdd.isChecked= ingrediente.vegano!!
        binding.cbGlutenFreeAdd.isChecked=ingrediente.glutenFree!!
        itemView.setOnClickListener {
            onItemUpdate(ingrediente)
        }

    }

}
