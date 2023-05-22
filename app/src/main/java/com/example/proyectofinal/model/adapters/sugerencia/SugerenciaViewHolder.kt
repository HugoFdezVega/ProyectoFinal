package com.example.proyectofinal.model.adapters.sugerencia

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.databinding.CardSugerenciasBinding
import com.example.proyectofinal.model.Sugerencia
import java.text.SimpleDateFormat
import java.util.Date

class SugerenciaViewHolder(v: View): RecyclerView.ViewHolder(v) {
    val binding=CardSugerenciasBinding.bind(v)

    fun render(sugerencia: Sugerencia, onItemDelete: (Sugerencia)->Unit){
        binding.tvPeticionarioSug.text=sugerencia.peticionario
        binding.tvAsuntoSug.text=sugerencia.asunto
        binding.tvContenidoSug.text=sugerencia.contenido
        binding.tvFechaSug.text=formatearFecha(sugerencia.fecha)
        binding.btImplementadaSug.setOnClickListener {
            onItemDelete(sugerencia)
        }
    }

    private fun formatearFecha(time: Long): String{
        val date=Date(time)
        val formato=SimpleDateFormat("dd/MM/yyyy HH:mm")
        return formato.format(date)
    }

}
