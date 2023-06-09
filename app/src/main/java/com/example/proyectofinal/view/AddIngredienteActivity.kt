package com.example.proyectofinal.view

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.ActivityAddComidaBinding
import com.example.proyectofinal.databinding.ActivityAddIngredienteBinding
import com.example.proyectofinal.model.Comida
import com.example.proyectofinal.model.Ingrediente
import com.example.proyectofinal.viewmodel.MainViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

/**
 * Add ingrediente activity
 *
 * @constructor Create empty Add ingrediente activity
 */
@AndroidEntryPoint
class AddIngredienteActivity : AppCompatActivity() {
    private val pickMedia=registerForActivityResult(ActivityResultContracts.PickVisualMedia()){
        if(it!=null){
            binding.ivIngrediente.setImageURI(it)
            img=it
        }
    }

    private val SIN_FOTO="https://firebasestorage.googleapis.com/v0/b/randomeater-e0c93.appspot.com/o/ingredientes%2Fingrediente.png?alt=media&token=698498aa-9d2a-49c4-940e-03f1578cec5f"
    private val vm: MainViewModel by viewModels()
    private var nombre=""
    private var medida="Gramos"
    private var admin=false
    private var datos: Bundle? =null
    private var img: Uri?=null
    private var nuevoIngr: Ingrediente?=null
    private var ingrediente: Ingrediente?=null

    lateinit var binding: ActivityAddIngredienteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddIngredienteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inicializar()
        setListeners()
    }

    /**
     * Set listeners
     *
     */
    private fun setListeners() {
        binding.btVolverAddIngrediente.setOnClickListener {
            finish()
        }
        binding.btGuardarIngrediente.setOnClickListener {
            guardarIngrediente()
        }
        binding.spMedidaIngrediente.onItemSelectedListener=object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                medida= parent!!.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        binding.btBorrarIngrediente.setOnClickListener {
            borrarIngrediente()
        }
        if(admin){
            binding.ivIngrediente.setOnClickListener {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            binding.ivIngrediente.setOnLongClickListener {
                ampliarImagen()
            }
        } else {
            binding.ivIngrediente.setOnClickListener {
                ampliarImagen()
            }
        }
    }

    /**
     * Ampliar imagen
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
            Picasso.get().load(ingrediente!!.imagen).into(ivAmpliada)
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
     * Borrar ingrediente
     *
     */
    private fun borrarIngrediente() {
        if(ingrediente==null){
            Toast.makeText(this,"Error: No se puede borrar un ingrediente que aún no ha sido creado", Toast.LENGTH_LONG).show()
        } else {
            val builder= AlertDialog.Builder(this)
            builder.setTitle("Borrar ingrediente")
                .setMessage("¿Seguro que desea borrar este ingrediente?")
                .setPositiveButton("Aceptar"){ dialog, wich->
                    vm.borrarIngrediente(ingrediente!!)
                    val i=Intent()
                    i.apply {
                        putExtra("retorno", ingrediente)
                        putExtra("borrado", true)
                    }
                    setResult(RESULT_OK,i)
                    Toast.makeText(this, "Ingrediente borrado correctamente", Toast.LENGTH_LONG).show()
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
     * Guardar ingrediente
     *
     */
    private fun guardarIngrediente() {
        if(!existeError()){
            // Si no hemos recogido datos, es un ingrediente nuevo, así que lo creamos pasándole null
            //a la imagen y lo mandamos al vm junto con la img, que será null o no. De lo contrario,
            //estamos editando un ingrediente así que lo creamos con todos sus datos y lo pasamos con img.
            if(datos==null){
                nuevoIngr=Ingrediente(nombre,medida,SIN_FOTO,binding.cbVeganoAddIngrediente.isChecked,binding.cbGlutenFreeAddIngrediente.isChecked)
                Toast.makeText(this, "Ingrediente creado correctamente", Toast.LENGTH_LONG).show()
            } else {
                nuevoIngr=Ingrediente(nombre,medida, ingrediente!!.imagen,binding.cbVeganoAddIngrediente.isChecked,binding.cbGlutenFreeAddIngrediente.isChecked)
                val i=Intent()
                i.apply {
                    putExtra("retorno", nuevoIngr)
                    putExtra("borrado", false)
                }
                setResult(RESULT_OK,i)
                Toast.makeText(this, "Ingrediente editado correctamente", Toast.LENGTH_LONG).show()
            }
            vm.crearIngrediente(nuevoIngr!!, img)
        }
        finish()
    }

    /**
     * Existe error
     *
     * @return
     */
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

    /**
     * Inicializar
     *
     */
    private fun inicializar() {
        recogerDatos()
        comprobarAdmin()
    }

    /**
     * Comprobar admin
     *
     */
    private fun comprobarAdmin() {
        // Cogemos el array de admins y comprobamos si el correod el usuario actual está en dicho
        //array. De ser así, es un admin y activamos el modo admin
        var admins=resources.getStringArray(R.array.admins)
        if(admins.contains(vm.obtenerUsuario())){
            modoAdmin()
        } else {
            binding.spMedidaIngrediente.isEnabled=false
        }
    }

    /**
     * Modo admin
     *
     */
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

    /**
     * Recoger datos
     *
     */
    private fun recogerDatos() {
        datos=intent.extras
        if(datos!=null) {
            //ingrediente = datos?.get("ingrediente") as Ingrediente
            ingrediente=getSerializable(intent,"ingrediente",Ingrediente::class.java)
            //Pintamos los datos obtenidos
            binding.etNombreIngrediente.setText(ingrediente!!.nombre)
            binding.cbVeganoAddIngrediente.isChecked = ingrediente!!.vegano!!
            binding.cbGlutenFreeAddIngrediente.isChecked = ingrediente!!.glutenFree!!
            var listaMedida = resources.getStringArray(R.array.Unidades)
            var index = listaMedida.indexOf(ingrediente!!.medida)
            binding.spMedidaIngrediente.setSelection(index)
            Picasso.get().load(ingrediente!!.imagen).into(binding.ivIngrediente)
        }
    }

    /**
     * Get serializable
     *
     * @param T
     * @param intent
     * @param key
     * @param clase
     * @return
     */
    private fun <T: java.io.Serializable?>getSerializable(intent: Intent, key: String, clase: Class<T>): T {
        return if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU){
            intent.getSerializableExtra(key,clase)!!
        } else {
            intent.getSerializableExtra(key) as T
        }
    }


}