package com.example.proyectofinal.model.adapters.listaPasos

import android.view.View
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.databinding.PasoCardBinding

/**
 * Lista pasos view holder
 *
 * @constructor
 *
 * @param v
 */
class ListaPasosViewHolder(v: View): RecyclerView.ViewHolder(v){
    val binding=PasoCardBinding.bind(v)

    /**
     * Render
     *
     * @param paso
     * @param onItemDelete
     * @param onPasoUpdate
     * @param admin
     * @receiver
     * @receiver
     */
    fun render(paso: String, onItemDelete: (Int) -> Unit, onPasoUpdate: (String) -> Unit, admin: Boolean){
        binding.tvNPaso.text=" - Paso ${adapterPosition+1}"
        binding.etPaso.setText(paso)
        if(admin){
            binding.btBorrarPaso.isGone=false
            binding.btBorrarPaso.setOnClickListener {
                onItemDelete(adapterPosition)
            }
            binding.btPaso.setOnClickListener {
                onPasoUpdate(paso)
            }
        }
    }

}
