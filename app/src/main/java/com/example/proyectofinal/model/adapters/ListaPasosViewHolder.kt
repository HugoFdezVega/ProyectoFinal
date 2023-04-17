package com.example.proyectofinal.model.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.databinding.PasoCardBinding

class ListaPasosViewHolder(v: View): RecyclerView.ViewHolder(v){
    val binding=PasoCardBinding.bind(v)

    fun render(paso: String){
        binding.tvNPaso.text=" - Paso ${adapterPosition+1}"
        binding.etPaso.setText(paso)
    }

}
