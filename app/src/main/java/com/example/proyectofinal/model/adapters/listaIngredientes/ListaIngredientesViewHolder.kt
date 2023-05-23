package com.example.proyectofinal.model.adapters.listaIngredientes

import android.view.View
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.databinding.CardIngredienteBinding
import com.example.proyectofinal.model.Ingrediente
import com.squareup.picasso.Picasso

class ListaIngredientesViewHolder(v: View): RecyclerView.ViewHolder(v) {
    private val binding=CardIngredienteBinding.bind(v)

    fun render(ingrediente: Ingrediente, actividad: String, onItemDelete: (Int) -> Unit, onItemUpdate: (Ingrediente) -> Unit, admin: Boolean, onCantidadUpdate: (Ingrediente) -> Unit){
        if(actividad=="add" && admin){
            binding.etCantidadIngredienteAdd.isGone=false
            binding.tvUnidadIngredienteAdd.isGone=false
            binding.btSuprimirIngredienteAdd.isGone=false
            binding.btCantidad.isGone=false
            binding.tvUnidadIngredienteAdd.text=ingrediente.medida
            binding.btSuprimirIngredienteAdd.setOnClickListener {
                onItemDelete(adapterPosition)
            }
        }
        if(ingrediente.imagen!="null"){
            Picasso.get().load(ingrediente.imagen).into(binding.ivIngredienteAdd)
        } else {
            Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/randomeater-e0c93.appspot.com/o/ingredientes%2Fingrediente.png?alt=media&token=698498aa-9d2a-49c4-940e-03f1578cec5f").into(binding.ivIngredienteAdd)
        }
        binding.tvNombreIngredienteAdd.text=ingrediente.nombre
        binding.cbVeganAdd.isChecked= ingrediente.vegano!!
        binding.cbGlutenFreeAdd.isChecked=ingrediente.glutenFree!!
        binding.etCantidadIngredienteAdd.setText(ingrediente.cantidad.toString())
        itemView.setOnClickListener {
            onItemUpdate(ingrediente)
        }
        binding.btCantidad.setOnClickListener {
            onCantidadUpdate(ingrediente)
        }
        binding.tvUnidadIngredienteAdd.setOnClickListener {
            onCantidadUpdate(ingrediente)
        }

    }

}
