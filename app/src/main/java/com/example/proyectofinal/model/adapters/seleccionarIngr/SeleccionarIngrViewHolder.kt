package com.example.proyectofinal.model.adapters.seleccionarIngr

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.databinding.CardSeleccionarIngredienteBinding
import com.example.proyectofinal.model.Ingrediente
import com.squareup.picasso.Picasso

/**
 * Seleccionar ingr view holder
 *
 * @constructor
 *
 * @param v
 */
class SeleccionarIngrViewHolder(v: View): RecyclerView.ViewHolder(v) {
    val binding=CardSeleccionarIngredienteBinding.bind(v)

    /**
     * Render
     *
     * @param ingrediente
     * @param onItemSelected
     * @receiver
     */
    fun render(ingrediente: Ingrediente, onItemSelected: (Ingrediente)->Unit){
        Picasso.get().load(ingrediente.imagen).into(binding.ivSeleccionarIngrediente)
        binding.tvNombreIngrediente.text=ingrediente.nombre
        itemView.setOnClickListener {
            onItemSelected(ingrediente)
        }
    }

}
