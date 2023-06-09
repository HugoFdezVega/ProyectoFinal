package com.example.proyectofinal.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
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

/**
 * Add comida activity
 *
 * @constructor Create empty Add comida activity
 */
@AndroidEntryPoint
class AddComidaActivity : AppCompatActivity() {
    /**
     * Response launcher select
     *
     * Recoge el ingrediente que seleccionemos en la activity para seleccionar ingredientes
     */
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

    /**
     * Response launcher update
     *
     * Recoge el ingrediente (y la posicion) del ingrediente que actualicemos/borremos al hacerle click
     */
    private val responseLauncherUpdate=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode==RESULT_OK){
            val ingredienteRetorno=it.data?.getSerializableExtra("retorno") as Ingrediente
            val ingredienteBorrado=it.data?.getSerializableExtra("borrado") as Boolean
            val indice=listaIngredientes.indexOf(ingredienteRetorno)
            val cantidad=listaIngredientes[indice].cantidad
            listaIngredientes.removeAt(indice)
            if(!ingredienteBorrado){
                ingredienteRetorno.cantidad=cantidad
                listaIngredientes.add(indice,ingredienteRetorno)
                ingredientesAdapter.lista=listaIngredientes
                ingredientesAdapter.notifyItemChanged(indice)
            } else {
                ingredientesAdapter.notifyItemRemoved(indice)
            }
        }
    }

    /**
     * Pick media
     *
     * Recoge la imagen que selecciona el usuario
     */
    private val pickMedia=registerForActivityResult(ActivityResultContracts.PickVisualMedia()){
        if(it!=null){
            binding.ivComida.setImageURI(it)
            img=it
        }
    }

    private val SIN_FOTO="https://firebasestorage.googleapis.com/v0/b/randomeater-e0c93.appspot.com/o/comidas%2Fcomida.png?alt=media&token=f677154c-3aa8-4f30-a320-4606ff385bcf"
    private val vm: MainViewModel by viewModels()
    private var listaPasos= mutableListOf<String>()
    private var listaIngredientes= mutableListOf<Ingrediente>()
    private var datos: Bundle? =null
    private var admin=false
    private var nombre=""
    private var tag1=""
    private var tag2=""
    private var descr=""
    private var img: Uri?=null
    private var nuevaComida: Comida?=null

    lateinit var comidaRecogida: Comida
    lateinit var pasosAdapter: ListaPasosAdapter
    lateinit var ingredientesAdapter: ListaIngredientesAdapter
    lateinit var binding: ActivityAddComidaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddComidaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inicializar()
        setListeners()
    }

    /**
     * Inicializar
     *
     * Comprueba si el usuario es admin, setea los recyclers y recoge los posibles datos
     *
     */
    private fun inicializar() {
        datos=intent.extras
        comprobarAdmin()
        setRecyclers()
        recogerDatos()
    }

    /**
     * Recoger datos
     *
     * Recogera los datos(es decir, comprobara si estamos editando una comida) y en funcion de que
     * haya o no datos, los rellenara
     *
     */
    private fun recogerDatos() {
        if(datos!=null){
            comidaRecogida=datos?.get("comida") as Comida
            Picasso.get().load(comidaRecogida.imagen).into(binding.ivComida)
            binding.etNombreComida.setText(comidaRecogida.nombre)
            binding.etTag1.setText(comidaRecogida.tags!![0])
            binding.etTag2.setText(comidaRecogida.tags!![1])
            binding.etDescripcionComida.setText(comidaRecogida.descripcion)
            listaIngredientes= comidaRecogida.ingredientes!!
            listaPasos= comidaRecogida.preparacion!!
            ingredientesAdapter.lista=listaIngredientes
            ingredientesAdapter.notifyDataSetChanged()
            binding.tvSinIngredientes.isGone=true
            pasosAdapter.lista=listaPasos
            pasosAdapter.notifyDataSetChanged()
            binding.tvSinPasos.isGone=true
        }
    }

    /**
     * Comprobar admin
     *
     * Comprueba si somos usuario administrador y en funcion de activara el modo admin
     *
     */
    private fun comprobarAdmin() {
        var admins=resources.getStringArray(R.array.admins)
        if(admins.contains(vm.obtenerUsuario())){
            modoAdmin()
        }
    }

    /**
     * Modo admin
     *
     * Modo que muestra u oculta controles
     *
     */
    private fun modoAdmin() {
        admin=true
        binding.etTag1.isEnabled=true
        binding.etTag2.isEnabled=true
        binding.etDescripcionComida.isEnabled=true
        binding.btAddIngredientesComida.isVisible=true
        binding.btAdPasos.isGone=false
        binding.btGuardarComida.isGone=false
        binding.btBorrarComida.isGone=false
        if(datos==null){
            binding.etNombreComida.isEnabled=true
        } else {
            binding.btGuardarComida.setText("Editar")
        }
    }

    /**
     * Ampliar imagen
     *
     * Muestra un alertDialog con una imagen ampliada
     *
     * @return
     */
    private fun ampliarImagen():Boolean {
        val builder=AlertDialog.Builder(this)
        val inflater=layoutInflater
        val dialogLayout=inflater.inflate(R.layout.dialog_imagen,null)
        val ivAmpliada=dialogLayout.findViewById<ImageView>(R.id.ivAmpliada)
        if(img!=null){
            Picasso.get().load(img).into(ivAmpliada)
        }
        else if(datos!=null){
            Picasso.get().load(comidaRecogida.imagen).into(ivAmpliada)
        } else {
            Picasso.get().load(SIN_FOTO).into(ivAmpliada)
        }
        with(builder){
            setPositiveButton("Cerrar"){dialog, wich->
                dialog.dismiss()
            }
            setCancelable(true)
            setView(dialogLayout)
            show()
        }
        return true
    }

    /**
     * Set recyclers
     *
     */
    private fun setRecyclers() {
        //Recycler de los pasos
        binding.rvPasos.layoutManager=LinearLayoutManager(this)
        pasosAdapter= ListaPasosAdapter(listaPasos,{onPasoDelete(it)},admin,{onPasoUpdate(it)})
        binding.rvPasos.adapter=pasosAdapter
        //Recycler de los ingredientes
        binding.rvIngredientesComida.layoutManager=LinearLayoutManager(this)
        ingredientesAdapter= ListaIngredientesAdapter(listaIngredientes,"add",{onIngrDelete(it)},{onIngrUpdate(it)}, admin, {onCantidadUpdate(it)})
        binding.rvIngredientesComida.adapter=ingredientesAdapter
    }

    /**
     * Set listeners
     *
     */
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
        if(admin){
            binding.ivComida.setOnClickListener {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            binding.ivComida.setOnLongClickListener {
                ampliarImagen()
            }
        } else {
            binding.ivComida.setOnClickListener {
                ampliarImagen()
            }
        }
    }

    /**
     * Crear comida
     *
     * Si no tenemos datos de otra activity, es que estamos creando un nuevo ingrediente,
     * por tanto, le asignamos la imagen a null provisionalmente. Si tenemos datos, es que
     * estamos editando una comida y, por tanto, le asignamos la imagen que tenía ya.
     *
     */
    private fun crearComida() {
        val tags= arrayListOf<String>()
        tags.add(tag1)
        tags.add(tag2)
        // Si no tenemos datos de otra activity, es que estamos creando un nuevo ingrediente,
        //por tanto, le asignamos la imagen a null provisionalmente. Si tenemos datos, es que
        //estamos editando una comida y, por tanto, le asignamos la imagen que tenía ya.
        if(datos==null){
            nuevaComida=Comida(nombre,descr,tags,SIN_FOTO,listaIngredientes,listaPasos)
        } else {
            nuevaComida=Comida(nombre,descr,tags,comidaRecogida.imagen,listaIngredientes,listaPasos)
        }
        //Creamos la nueva comida con la comida y la img que se haya seleccionado
        vm.crearComida(nuevaComida!!,img)
        if(datos==null){
            Toast.makeText(this, "Comida creada correctamente", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Comida editada correctamente", Toast.LENGTH_LONG).show()
        }
        finish()
    }

    /**
     * Existe error
     *
     * Este método comprueba que no haya errores en ninguno de los campos y que no se repita el nombre
     * de la comida si no estamos editando (es decir, si no hemos recogido ninguna comida al entrar).
     *
     * @return
     */
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
        }
        else if(binding.etNombreComida.isEnabled && vm.getListaComidas().contains(Comida(nombre))){
            binding.etNombreComida.setError("Ya existe una comida con este nombre")
            binding.etNombreComida.requestFocus()
            return true
        }
        else if(listaIngredientes.isEmpty()){
            Toast.makeText(this, "ERROR: La lista de ingredientes no puede estar vacía",Toast.LENGTH_LONG).show()
            return true
        }
        else if(listaPasos.isEmpty()){
            Toast.makeText(this, "ERROR: La lista de pasos no puede estar vacía",Toast.LENGTH_LONG).show()
            return true
        } else {
            return false
        }
    }

    /**
     * Borrar comida
     *
     * Borra la comida de la base de datos tras una confirmacion alertDialog mediante tras comprobar
     * que la comida este creada previamente en BBDD
     *
     */
    private fun borrarComida() {
        if(binding.etNombreComida.isEnabled){
            Toast.makeText(this,"Error: No se puede borrar una comida que aún no ha sido creada", Toast.LENGTH_LONG).show()
        } else {
            val builder=AlertDialog.Builder(this)
            builder.setTitle("Borrar comida")
                .setMessage("¿Seguro que desea borrar esta comida?")
                .setPositiveButton("Aceptar"){ dialog, wich->
                    vm.borrarComida(comidaRecogida)
                    finish()
                }
                .setNegativeButton("Cancelar") { dialog, wich->
                    dialog.dismiss()
                }
                .setCancelable(true)
                .show()
        }
    }

    /**
     * Agregar ingrediente
     *
     * Nos lleva al activity para que seleccionemos el ingrediente
     *
     * @param ingrediente
     */
    private fun agregarIngrediente(ingrediente: Ingrediente) {
        listaIngredientes.add(ingrediente)
        ingredientesAdapter.notifyItemInserted(listaIngredientes.size-1)
        binding.rvIngredientesComida.smoothScrollToPosition(listaIngredientes.size-1) //Bajamos el recycler
        binding.scrollView.smoothScrollTo(0, binding.btAddIngredientesComida.bottom) //Scrolleamos hacia el botón
        binding.tvSinIngredientes.isVisible=false
    }

    /**
     * Agregar paso
     *
     * Nos abre el alertDialog para introducir el texto del paso de la receta
     *
     */
    private fun agregarPaso() {
        val builder = AlertDialog.Builder(this)
        val inflater=layoutInflater
        val dialogLayout=inflater.inflate(R.layout.dialog_paso,null)
        val etPaso=dialogLayout.findViewById<TextView>(R.id.etDialogPaso)
        with(builder){
            setTitle("Introduzca el paso que desea agregar a la receta:")
            setPositiveButton("Aceptar"){dialog, wich->
                val textoPaso=etPaso.text.toString().trim()
                if(textoPaso.isBlank()){
                    Toast.makeText(this@AddComidaActivity,"Error: El contenido del paso no puede estar vacío", Toast.LENGTH_LONG).show()
                } else {
                    listaPasos.add(textoPaso)
                    pasosAdapter.notifyItemInserted(listaPasos.size-1)
                    binding.tvSinPasos.isGone=true
                    binding.rvPasos.scrollToPosition(listaPasos.size-1) //Bajamos el recycler
                    //binding.scrollView.smoothScrollTo(0, binding.btVolverAddComida.bottom) //Scrolleamos hacia el botón
                    dialog.dismiss()
                }
            }
            setNegativeButton("Cancelar"){dialog, wich->
                dialog.dismiss()
            }
            setCancelable(false)
            setView(dialogLayout)
            show()
        }

    }

    /**
     * On ingr delete
     *
     * Borra el ingrediente indicado de la lista de ingredientes
     *
     * @param posicion
     */
    private fun onIngrDelete(posicion: Int){
        listaIngredientes.removeAt(posicion)
        ingredientesAdapter.notifyItemRemoved(posicion)
        if(listaIngredientes.size==0){
            binding.tvSinIngredientes.isGone=false
        }
    }

    /**
     * On paso delete
     *
     * Borra el paso indicado de la lista de pasos
     *
     * @param posicion
     */
    private fun onPasoDelete(posicion: Int) {
        listaPasos.removeAt(posicion)
        pasosAdapter.notifyDataSetChanged()
        if(listaPasos.size==0){
            binding.tvSinPasos.isGone=false
        }
    }

    /**
     * On ingr update
     *
     * Abre la vista detalle del ingrediente para poder modificarla
     *
     * @param ingrediente
     */
    private fun onIngrUpdate(ingrediente: Ingrediente){
        val i=Intent(this,AddIngredienteActivity::class.java).apply {
            putExtra("ingrediente",ingrediente)
        }
        responseLauncherUpdate.launch(i)
    }

    /**
     * On cantidad update
     *
     * Abre el alertDialog de la cantidad para que podamos editarla
     *
     * @param ingr
     */
    private fun onCantidadUpdate(ingr: Ingrediente) {
        val builder=AlertDialog.Builder(this)
        val inflater=layoutInflater
        val dialogLayout=inflater.inflate(R.layout.dialog_cantidad,null)
        val etCantidad=dialogLayout.findViewById<EditText>(R.id.etDialogCantidad)
        val tvMedida=dialogLayout.findViewById<TextView>(R.id.tvDialogMedida)
        tvMedida.text=ingr.medida
        etCantidad.setText(ingr.cantidad.toString())
        with(builder){
            setTitle("Introduzca la cantidad de ${ingr.nombre} que desea agregar:")
            setPositiveButton("Aceptar") { dialog, which ->
                val textoCantidad = etCantidad.text.toString().trim()
                if(textoCantidad.isBlank()){
                    Toast.makeText(this@AddComidaActivity,"Error: La cantidad no puede estar vacía", Toast.LENGTH_LONG).show()
                } else {
                    val cantidad=textoCantidad.toDouble()
                    if(cantidad<=0.0){
                        Toast.makeText(this@AddComidaActivity,"Error: La cantidad debe ser mayor que 0", Toast.LENGTH_LONG).show()
                    } else {
                        val cantidad=textoCantidad.toDouble()
                        ingr.cantidad=cantidad
                        val posicion=listaIngredientes.indexOf(ingr)
                        listaIngredientes[posicion]=ingr
                        ingredientesAdapter.lista=listaIngredientes
                        ingredientesAdapter.notifyItemChanged(posicion)
                        dialog.dismiss()
                    }
                }
            }
            setNegativeButton("Cancelar") { dialog, which ->
                dialog.dismiss()
            }
            setCancelable(false)
            setView(dialogLayout)
            show()
        }
    }

    /**
     * On paso update
     *
     * Abre el alertDialog del texto del paso para que podamos editarlo
     *
     * @param oldPaso
     */
    private fun onPasoUpdate(oldPaso: String) {
        val builder = AlertDialog.Builder(this)
        val inflater=layoutInflater
        val dialogLayout=inflater.inflate(R.layout.dialog_paso,null)
        val etPaso=dialogLayout.findViewById<TextView>(R.id.etDialogPaso)
        etPaso.setText(oldPaso)
        with(builder){
            setTitle("Introduzca el paso que desea agregar a la receta:")
            setPositiveButton("Aceptar"){dialog, wich->
                val textoPaso=etPaso.text.toString().trim()
                if(textoPaso.isBlank()){
                    Toast.makeText(this@AddComidaActivity,"Error: El contenido del paso no puede estar vacío", Toast.LENGTH_LONG).show()
                } else {
                    val posicion=listaPasos.indexOf(oldPaso)
                    listaPasos[posicion]=textoPaso
                    pasosAdapter.lista=listaPasos
                    pasosAdapter.notifyItemChanged(posicion)
                    dialog.dismiss()
                }
            }
            setNegativeButton("Cancelar"){dialog, wich->
                dialog.dismiss()
            }
            setCancelable(false)
            setView(dialogLayout)
            show()
        }
    }
}