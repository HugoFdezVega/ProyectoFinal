package com.example.proyectofinal.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.viewModels
import com.example.proyectofinal.R
import com.example.proyectofinal.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuBarFragment : Fragment() {
    var listener: InterfazMenuBar?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu_bar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btHome=view.findViewById<ImageButton>(R.id.btHome)
        val btListas=view.findViewById<ImageButton>(R.id.btListas)
        val btLogOut=view.findViewById<ImageButton>(R.id.btLogOut)
        btHome.setOnClickListener {
            btHome.setImageResource(R.drawable.home_in)
            btListas.setImageResource(R.drawable.lista)
            listener?.botonPulsado(0)
        }
        btListas.setOnClickListener {
            btHome.setImageResource(R.drawable.home)
            btListas.setImageResource(R.drawable.lista_in)
            listener?.botonPulsado(1)
        }
        btLogOut.setOnClickListener {
            listener?.botonPulsado(2)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is InterfazMenuBar){
            listener=context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener=null
    }

}