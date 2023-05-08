package com.example.proyectofinal.model.adapters.listaIngredientes.seleccionarIngr

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
        } else {
            Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/randomeater-e0c93.appspot.com/o/ingredientes%2Fingrediente.png?alt=media&token=698498aa-9d2a-49c4-940e-03f1578cec5f").into(binding.ivSeleccionarIngrediente)
        }
        binding.tvNombreIngrediente.text=ingrediente.nombre
        itemView.setOnClickListener {
            onItemSelected(ingrediente)
        }
    }

}
