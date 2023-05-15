package com.example.proyectofinal.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.ActivityListaCompraBinding
import com.example.proyectofinal.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListaCompraActivity : AppCompatActivity() {
    private val vm: MainViewModel by viewModels()

    lateinit var binding: ActivityListaCompraBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityListaCompraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observarListaCompra()
        setListeners()
    }

    private fun setListeners() {
        binding.btVolverListaCompra.setOnClickListener {
            finish()
        }
        binding.btCopiarListaCompra.setOnClickListener {
            copiarPortapapeles()
        }
    }

    private fun copiarPortapapeles() {
        val textoCopiar=binding.etListaCompra.text.toString()
        val clipboardManager=getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData=ClipData.newPlainText("texto",textoCopiar)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(this, "Texto copiado en portapapeles", Toast.LENGTH_LONG).show()
    }

    private fun observarListaCompra() {
        vm.readListaCompra().observe(this, Observer {
            if(!it.isNullOrEmpty()){
                binding.etListaCompra.setText(it)
            }
        })
    }



}