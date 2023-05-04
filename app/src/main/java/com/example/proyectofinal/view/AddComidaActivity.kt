package com.example.proyectofinal.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.ActivityAddComidaBinding
import com.example.proyectofinal.model.Comida
import com.example.proyectofinal.model.Ingrediente
import com.example.proyectofinal.model.adapters.listaIngredientes.ListaIngredientesAdapter
import com.example.proyectofinal.model.adapters.listaPasos.ListaPasosAdapter
import com.example.proyectofinal.viewmodel.MainViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddComidaActivity : AppCompatActivity() {
    //Recoge el ingrediente que seleccionemos en la activity para seleccionar ingredientes
    private val responseLauncherSelect=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode==RESULT_OK){
            val ingredienteSeleccionado=it.data?.getSerializableExtra("seleccionado") as Ingrediente
            if(listaIngredientes.contains(ingredienteSeleccionado)){
                Toast.makeText(this,"Error: El ingrediente seleccionado ya se encuentra en la lista", Toast.LENGTH_LONG).show()
            } else {
                agregarIngrediente(ingredienteSeleccionado)
            }
        }
    }

    //Recoge el ingrediente (y la posicion) del ingrediente que actualicemos/borremos al hacerle click
    private val responseLauncherUpdate=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode==RESULT_OK){
            val ingredieneRetorno=it.data?.getSerializableExtra("retorno") as Ingrediente
            var indice=listaIngredientes.indexOf(ingredieneRetorno)
            listaIngredientes.removeAt(indice)
            listaIngredientes.add(indice,ingredieneRetorno)
            pasosAdapter.notifyItemChanged(indice)
        }
    }

    private val pickMedia=registerForActivityResult(ActivityResultContracts.PickVisualMedia()){
        if(it!=null){
            binding.ivComida.setImageURI(it)
            img=it
        }
    }

    private val vm: MainViewModel by viewModels()
    private var listaPasos= mutableListOf("")
    private var listaIngredientes= mutableListOf<Ingrediente>()
    private var datos: Bundle? =null
    private var admin=false
    private var nombre=""
    private var tag1=""
    private var tag2=""
    private var descr=""
    private var img: Uri?=null
    private var nuevaComida: Comida?=null

    lateinit var comida: Comida
    lateinit var pasosAdapter: ListaPasosAdapter
    lateinit var ingredientesAdapter: ListaIngredientesAdapter
    lateinit var binding: ActivityAddComidaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddComidaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        inicializar()
    }

    private fun inicializar() {
        recogerDatos()
        comprobarAdmin()
        setRecyclers()

    }

    private fun recogerDatos() {
        datos=intent.extras
        if(datos!=null){
            comida=datos?.get("comida") as Comida
            // TODO Pintamos los datos obtenidos
        }
    }

    private fun comprobarAdmin() {
        var admins=resources.getStringArray(R.array.admins)
        if(admins.contains(vm.obtenerUsuario())){
            modoAdmin()
        }
    }

    private fun modoAdmin() {
        admin=true
        binding.etTag1.isEnabled=true
        binding.etTag2.isEnabled=true
        binding.etDescripcionComida.isEnabled=true
        if(datos==null){
            binding.etNombreComida.isEnabled=true
        }
    }

    private fun setRecyclers() {
        //Recycler de los pasos
        binding.rvPasos.layoutManager=LinearLayoutManager(this)
        pasosAdapter= ListaPasosAdapter(listaPasos,{onPasoDelete(it)},admin)
        binding.rvPasos.adapter=pasosAdapter
        //Recycler de los ingredientes
        binding.rvIngredientesComida.layoutManager=LinearLayoutManager(this)
        ingredientesAdapter= ListaIngredientesAdapter(listaIngredientes,"add",{onIngrDelete(it)},{onIngrUpdate(it)}, admin)
        binding.rvIngredientesComida.adapter=ingredientesAdapter
    }

    private fun onPasoDelete(posicion: Int) {
        if(listaPasos.size>1){
            pasosAdapter.notifyItemRemoved(posicion)
            listaPasos.removeAt(posicion)
        }
    }

    private fun setListeners() {
        binding.btAddIngredientesComida.setOnClickListener {
            responseLauncherSelect.launch(Intent(this,SeleccionarIngredienteActivity::class.java))
        }
        binding.btVolverAddComida.setOnClickListener {
            finish()
        }
        binding.btAdPasos.setOnClickListener {
            agregarPaso()
        }
        binding.btBorrarComida.setOnClickListener {
            borrarComida()
        }
        binding.btGuardarComida.setOnClickListener {
            if(!existeError()){
                crearComida()
            }
        }
        binding.ivComida.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun crearComida() {
        //Mediante estos métodos comprobamos que no haya errores en los elementos de los recyclers
        if(obtenerCantidades() && obtenerPasos()){
            //Comprobamos que la lista de ingredientes no esté vacía
            if(listaIngredientes.isEmpty()){
                Toast.makeText(this, "ERROR: La lista de ingredientes no puede estar vacía",Toast.LENGTH_LONG).show()
            }
            //Comprobamos que la lista de pasos no esté vacía
            else if(listaPasos.isEmpty()){
                Toast.makeText(this, "ERROR: La lista de pasos no puede estar vacía",Toast.LENGTH_LONG).show()
            } else {
                val tags= arrayListOf<String>()
                tags.add(tag1)
                tags.add(tag2)
                // Si no tenemos datos de otra activity, es que estamos creando un nuevo ingrediente,
                //por tanto, le asignamos la imagen a null provisionalmente. Si tenemos datos, es que
                //estamos editando una comida y, por tanto, le asignamos la imagen que tenía ya.
                if(datos==null){
                    nuevaComida=Comida(nombre,descr,tags,"null",listaIngredientes,listaPasos)
                } else {
                    nuevaComida=Comida(nombre,descr,tags,comida.imagen,listaIngredientes,listaPasos)
                }
                //Creamos la nueva comida con la comida y la img que se haya seleccionado
                vm.crearComida(nuevaComida!!,img)
                Toast.makeText(this, "Comida creada correctamente", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    // Este método va a recorrer todos los elementos del recycler de pasos, obteniendo el texto de su EditText.
    //Después comprobará que no esté en blanco (de lo contrario, informa del error, retorna true y detiene la ejecución)
    // y lo asignará a su posición correspondiente en la lista de pasos (donde todos estaban en blanco).
    private fun obtenerPasos(): Boolean {
        var obtenido=false
        for(i in 0 until binding.rvPasos.childCount){
            val view=binding.rvPasos.getChildAt(i)
            val editText=view.findViewById<EditText>(R.id.etPaso)
            val paso=editText.text.toString().trim()
            if(paso.isBlank()){
                editText.setError("Ningún paso en la lista puede estar vacío")
                editText.requestFocus()
                obtenido=false
                break
            } else {
                listaPasos[i]=paso
                obtenido=true
            }
        }
        return obtenido
    }

    // Este método va a recorrer todos los elementos del recycler de ingredientes, obteniendo la cantidad de su EditText.
    //Después comprobará que no esté en blanco (de lo contrario, informa del error, retorna true y detiene la ejecución)
    // y lo asignará a la cantidad del ingrediente de su posición correspondiente en la lista de ingredientes.
    private fun obtenerCantidades(): Boolean{
        var obtenido=false
        for(i in 0 until binding.rvIngredientesComida.childCount){
            val view=binding.rvIngredientesComida.getChildAt(i)
            val editText=view.findViewById<EditText>(R.id.etCantidadIngredienteAdd)
            val cantidadStr=editText.text.toString().trim()
            if(cantidadStr.isBlank()){
                editText.setError("La cantidad de un ingrediente no puede estar vacía")
                editText.requestFocus()
                obtenido=false
                break
            } else {
                val cantidad=cantidadStr.toDouble()
                listaIngredientes[i].cantidad=cantidad
                obtenido=true
            }
        }
        return obtenido
    }

    // Este método comprueba que no haya errores en ninguno de los campos y que no se repita el nombre
    //de la comida si no estamos editando (es decir, si no hemos recogido ninguna comida al entrar).
    private fun existeError(): Boolean {
        nombre=binding.etNombreComida.text.toString().trim()
        tag1=binding.etTag1.text.toString().trim()
        tag2=binding.etTag2.text.toString().trim()
        descr=binding.etDescripcionComida.text.toString().trim()
        if(nombre.isBlank()){
            binding.etNombreComida.setError("El nombre de la comida no puede estar vacío")
            binding.etNombreComida.requestFocus()
            return true
        }
        else if(tag1.isBlank()){
            binding.etTag1.setError("No puede haber ningún tag vacío")
            binding.etTag1.requestFocus()
            return true
        }
        else if(tag2.isBlank()){
            binding.etTag2.setError("No puede haber ningún tag vacío")
            binding.etTag2.requestFocus()
            return true
        }
        else if(descr.isBlank()){
            binding.etDescripcionComida.setError("Lad descripción de la comida no puede estar vacía")
            binding.etDescripcionComida.requestFocus()
            return true
        } else if(binding.etNombreComida.isEnabled && vm.getListaComidas().contains(Comida(nombre))){
            binding.etNombreComida.setError("Ya existe una comida con este nombre")
            binding.etNombreComida.requestFocus()
            return true
        } else {
            return false
        }
    }

    private fun borrarComida() {
        if(binding.etNombreComida.isEnabled){
            Toast.makeText(this,"Error: No se puede borrar una comida que aún no ha sido creada", Toast.LENGTH_LONG).show()
        } else {
            // TODO agregar lógica para borrar comida
        }
    }

    //Método que agrega el ingrediente del parámetro a la lista y notifica al recycler
    private fun agregarIngrediente(ingrediente: Ingrediente) {
        listaIngredientes.add(ingrediente)
        ingredientesAdapter.notifyItemInserted(listaIngredientes.size-1)
        binding.scrollView.smoothScrollTo(0, binding.btAddIngredientesComida.top) //Scrolleamos hacia el botón
        binding.rvIngredientesComida.scrollToPosition(listaIngredientes.size-1) //Bajamos el recycler
        binding.tvSinIngredientes.isVisible=false
    }

    //Método que agrega un paso en blanco al recycler y a la lista
    private fun agregarPaso() {
        listaPasos.add("")
        pasosAdapter.notifyItemInserted(listaPasos.size-1)
        binding.rvPasos.scrollToPosition(listaPasos.size-1) //Bajamos el recycler
        binding.scrollView.smoothScrollTo(0, binding.btAdPasos.top) //Scrolleamos hacia el botón
    }

    //Elimina un paso del reycler y de la lista
    private fun onIngrDelete(posicion: Int){
        listaIngredientes.removeAt(posicion)
        ingredientesAdapter.notifyItemRemoved(posicion)
        if(listaIngredientes.size==0){
            binding.tvSinIngredientes.isVisible=true
        }
    }


    private fun onIngrUpdate(ingrediente: Ingrediente){
        val i=Intent(this,AddIngredienteActivity::class.java).apply {
            putExtra("ingrediente",ingrediente)
        }
        responseLauncherUpdate.launch(i)
    }
}