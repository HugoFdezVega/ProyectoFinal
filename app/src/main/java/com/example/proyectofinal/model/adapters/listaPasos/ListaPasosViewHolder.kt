package com.example.proyectofinal.model.adapters.listaPasos

import android.view.View
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.databinding.PasoCardBinding

class ListaPasosViewHolder(v: View): RecyclerView.ViewHolder(v){
    val binding=PasoCardBinding.bind(v)

    fun render(paso: String, onItemDelete: (Int) -> Unit, admin: Boolean){
        binding.tvNPaso.text=" - Paso ${adapterPosition+1}"
        binding.etPaso.setText(paso)
        if(admin){
            binding.btBorrarPaso.isGone=false
            binding.etPaso.isEnabled=true
            binding.btBorrarPaso.setOnClickListener {
                onItemDelete(adapterPosition)
            }
        }
    }

}
