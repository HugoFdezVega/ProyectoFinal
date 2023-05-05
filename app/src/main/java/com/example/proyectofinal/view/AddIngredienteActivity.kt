package com.example.proyectofinal.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isGone
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.ActivityAddComidaBinding
import com.example.proyectofinal.databinding.ActivityAddIngredienteBinding
import com.example.proyectofinal.model.Ingrediente
import com.example.proyectofinal.viewmodel.MainViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddIngredienteActivity : AppCompatActivity() {
    private val pickMedia=registerForActivityResult(ActivityResultContracts.PickVisualMedia()){
        if(it!=null){
            binding.ivIngrediente.setImageURI(it)
            img=it
        }
    }

    private val vm: MainViewModel by viewModels()
    private var nombre=""
    private var medida="Gramos"
    private var admin=false
    private var datos: Bundle? =null
    private var img: Uri?=null
    private var nuevoIngr: Ingrediente?=null

    lateinit var ingrediente: Ingrediente
    lateinit var binding: ActivityAddIngredienteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddIngredienteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inicializar()
        setListeners()
    }

    private fun setListeners() {
        binding.btVolverAddIngrediente.setOnClickListener {
            finish()
        }
        binding.btGuardarIngrediente.setOnClickListener {
            guardarIngrediente()
        }
        binding.ivIngrediente.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.spMedidaIngrediente.onItemSelectedListener=object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                medida= parent!!.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

    }

    private fun guardarIngrediente() {
        if(!existeError()){
            // Si no hemos recogido datos, es un ingrediente nuevo, así que lo creamos pasándole null
            //a la imagen y lo mandamos al vm junto con la img, que será null o no. De lo contrario,
            //estamos editando un ingrediente así que lo creamos con todos sus datos y lo pasamos con img.
            if(datos==null){
                nuevoIngr=Ingrediente(nombre,medida,"null",binding.cbVeganoAddIngrediente.isChecked,binding.cbGlutenFreeAddIngrediente.isChecked)
            } else {
                nuevoIngr=Ingrediente(nombre,medida,ingrediente.imagen,binding.cbVeganoAddIngrediente.isChecked,binding.cbGlutenFreeAddIngrediente.isChecked)
            }
            vm.crearIngrediente(nuevoIngr!!, img)
        }
        Toast.makeText(this, "Ingrediente creado correctamente", Toast.LENGTH_LONG).show()
        finish()
    }

    private fun existeError(): Boolean {
        nombre=binding.etNombreIngrediente.text.toString().trim()
        if(nombre.isBlank()){
            Toast.makeText(this,"El nombre del ingrediente no puede estar vacío",Toast.LENGTH_LONG).show()
            binding.etNombreIngrediente.requestFocus()
            return true
        } else {
            // Creamos un ingrediente ficticio para comprobar si ya existe en la listaIngr. Si existe y el etNombreIngrediente
            //está editable, significa que intengamos crear un ingrediente repetido y da error. Si no, significa que no está
            //repetido o que estamos editando un ingrediente, por lo que procedemos a guardar/editar.
            var ingrRepetido=Ingrediente(nombre)
            if(vm.getListaIngredientes().contains(ingrRepetido)&&binding.etNombreIngrediente.isEnabled){
                Toast.makeText(this,"Ya existe un ingrediente con este nombre",Toast.LENGTH_LONG).show()
                binding.etNombreIngrediente.requestFocus()
                return true
            } else {
                return false
            }
        }
    }

    private fun inicializar() {
        recogerDatos()
        comprobarAdmin()
    }

    private fun comprobarAdmin() {
        // Cogemos el array de admins y comprobamos si el correod el usuario actual está en dicho
        //array. De ser así, es un admin y activamos el modo admin
        var admins=resources.getStringArray(R.array.admins)
        if(admins.contains(vm.obtenerUsuario())){
            modoAdmin()
        }
    }

    private fun modoAdmin() {
        // Podemos la bandera de admin a true y activamos los controles para el administrador. Si no
        //hemos recogido datos, significa que estamos creando así que permitidos editar el editText
        //correspondiente. Si no, significa que estamos editando y cambiamos el text del botón guardar.
        admin=true
        binding.cbVeganoAddIngrediente.isEnabled=true
        binding.cbGlutenFreeAddIngrediente.isEnabled=true
        binding.btBorrarIngrediente.isGone=false
        binding.btGuardarIngrediente.isGone=false
        binding.spMedidaIngrediente.isEnabled=true
        if(datos==null){
            binding.etNombreIngrediente.isEnabled=true
        } else {
            binding.btGuardarIngrediente.text = "Editar"
        }
    }

    private fun recogerDatos() {
        datos=intent.extras
        if(datos!=null){
            ingrediente=datos?.get("ingrediente") as Ingrediente
            //Pintamos los datos obtenidos
            binding.etNombreIngrediente.setText(ingrediente.nombre)
            binding.cbVeganoAddIngrediente.isChecked= ingrediente.vegano!!
            binding.cbGlutenFreeAddIngrediente.isChecked=ingrediente.glutenFree!!
            var listaMedida=resources.getStringArray(R.array.Unidades)
            var index=listaMedida.indexOf(ingrediente.medida)
            binding.spMedidaIngrediente.setSelection(index)
            Picasso.get().load(ingrediente.imagen).into(binding.ivIngrediente)
        }
    }


}