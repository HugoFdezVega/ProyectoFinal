package com.example.proyectofinal.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.R
import com.example.proyectofinal.model.Comida
import com.example.proyectofinal.model.Ingrediente
import com.example.proyectofinal.model.adapters.listaComidas.ListaComidasAdapter
import com.example.proyectofinal.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

/**
 * Home fragment
 *
 * @constructor Create empty Home fragment
 */
@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val vm: MainViewModel by viewModels()
    private var listaMenu= mutableListOf<Comida>()
    private var listaRaciones= mutableListOf<Int>()

    lateinit var cbVeganoMenu: CheckBox
    lateinit var cbGlutenFreeMenu: CheckBox
    lateinit var btGenerarMenu: Button
    lateinit var btCarrito: ImageButton
    lateinit var recMenu: RecyclerView
    lateinit var btGenerarListaCompra: Button
    lateinit var pbHome: ProgressBar
    lateinit var ivNotificacion: ImageView
    lateinit var tvVacio: TextView
    lateinit var adapter: ListaComidasAdapter

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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inicializar(view)
        setRecycler(view)
        observarMenu()
        setListeners()
    }

    /**
     * Set listeners
     *
     */
    private fun setListeners() {
        btGenerarMenu.setOnClickListener {
            vm.generarMenu(cbVeganoMenu.isChecked,cbGlutenFreeMenu.isChecked)
        }
        btCarrito.setOnClickListener {
            ivNotificacion.isGone=true
            startActivity(Intent(btCarrito.context,ListaCompraActivity::class.java))
        }
        btGenerarListaCompra.setOnClickListener {
            guardarListaCompra()
            ivNotificacion.isGone=false
        }
    }

    /**
     * Guardar lista compra
     *
     * Crea una copia del menu, le asigna sus raciones correspondientes y luego ejecuta los metodos
     * para generar el String de la lista de la compra con todos los ingredientes y cantidades sumados
     *
     */
    private fun guardarListaCompra() {
        var copiaMenu= mutableListOf<Comida>()
        for(i in 0 until listaMenu.size){
            copiaMenu.add(Comida(listaMenu[i]))
            copiaMenu[i].raciones=listaRaciones[i]
        }
        vm.guardarMenu(copiaMenu)
        val ingredientesTotales=obtenerIngredientesTotales(copiaMenu)
        val listaCompra=generarListaCompra(ingredientesTotales)
        vm.guardarListaCompra(listaCompra)
    }

    /**
     * Generar lista compra
     *
     * A partir de la lista que se le pasa, crea la lista en String con el formato deseado
     *
     * @param ingredientesTotales
     * @return
     */
    private fun generarListaCompra(ingredientesTotales: MutableList<Ingrediente>): String {
        var listaCompra=""
        for(i in ingredientesTotales){
            listaCompra+="- ${i.nombre}: ${DecimalFormat("#.#").format(i.cantidad)} ${i.medida}\n"
        }
        return listaCompra
    }

    /**
     * Obtener ingredientes totales
     *
     * A partir del menu, lo va recorriendo y guardando los ingredientes en un array, sumando sus
     * cantidades
     *
     * @param copiaMenu
     * @return
     */
    private fun obtenerIngredientesTotales(copiaMenu: MutableList<Comida>): MutableList<Ingrediente> {
        var ingredientesTotales= mutableListOf<Ingrediente>()
        for(c in copiaMenu){
            for(i in c.ingredientes!!){
                if(ingredientesTotales.contains(i)){
                    val index=ingredientesTotales.indexOf(i)
                    ingredientesTotales[index].cantidad=ingredientesTotales[index].cantidad!!+i.cantidad!!*c.raciones
                } else {
                    val nuevoIngr=Ingrediente(i)
                    nuevoIngr.cantidad=nuevoIngr.cantidad!!*c.raciones
                    if(nuevoIngr.cantidad!!>0){
                        ingredientesTotales.add(nuevoIngr)
                    }
                }
            }
        }
        return ingredientesTotales
    }

    /**
     * Set recycler
     *
     * @param view
     */
    private fun setRecycler(view: View) {
        recMenu.layoutManager=LinearLayoutManager(view.context)
        adapter= ListaComidasAdapter(listaMenu,"menu",{onComidaDelete(it)},{onComidaUpdate(it)},{onComidaOtra(it)},{onComidaParecida(it)},{posicion, raciones -> onComidaRaciones(posicion, raciones)})
        recMenu.adapter=adapter
    }

    private fun onComidaDelete(posicion: Int) {
    }

    /**
     * On comida update
     *
     * Abre la vista detalled e una comida
     *
     * @param comida
     */
    private fun onComidaUpdate(comida: Comida) {
        val intent=Intent(pbHome.context, AddComidaActivity::class.java).apply {
            putExtra("comida", comida)
        }
        startActivity(intent)
    }

    /**
     * On comida raciones
     *
     * Aumenta o disminuye las raciones de una comida en funcion del boton que se pulse
     *
     * @param posicion
     * @param raciones
     */
    private fun onComidaRaciones(posicion: Int, raciones: Int) {
        listaRaciones[posicion]=raciones
    }

    /**
     * On comida parecida
     *
     * // Recibimos la posición del adapter en que se ha pulsado el botón y mandamos la comida en función
     * //de dicha posición, la posición y si están o no pulsamos los checkboxes. La comida devuelta la
     * //ponemos en la posición que corresponde de nuestra lista, se la pasamos al adapter y notificamos.
     *
     * @param posicion
     */
    private fun onComidaParecida(posicion: Int) {
        val comidaParecida=vm.comidaParecida(listaMenu[posicion],posicion,cbVeganoMenu.isChecked,cbGlutenFreeMenu.isChecked)
        listaMenu[posicion]=comidaParecida
        adapter.lista=listaMenu
        adapter.notifyItemChanged(posicion)
    }

    /**
     * On comida otra
     *
     * // Recibimos la posición del adapter en que se ha pulsado el botón y mandamos la posición y si
     * // están o no pulsamos los checkboxes. La comida devuelta la ponemos en la posición que corresponde
     * // de nuestra lista, se la pasamos al adapter y notificamos.
     *
     * @param posicion
     */
    private fun onComidaOtra(posicion: Int) {
        val otraComida=vm.otraComida(posicion, cbVeganoMenu.isChecked,cbGlutenFreeMenu.isChecked)
        listaMenu[posicion]=otraComida
        adapter.lista=listaMenu
        adapter.notifyItemChanged(posicion)
    }

    /**
     * Observar menu
     *
     * // Observamos el LiveData que se nos manda desde el repositorio. Si su tamaño es inferior a 5
     * //establecemos los controles en su estado inicial. De lo contrario, pintamos lo necesario,
     * //asignamos el valor del LiveData a nuestra lista, se la pasamos al adapter y notificamos.
     *
     */
    private fun observarMenu() {
        pbHome.isVisible=true
        vm.ldListaMenu.observeForever{
            pbHome.isVisible=false
            if(it.size<5){
                tvVacio.isGone=false
                recMenu.isVisible=false
                btGenerarListaCompra.isVisible=false
            } else {
                tvVacio.isGone=true
                recMenu.isVisible=true
                btGenerarListaCompra.isVisible=true
                listaMenu=it
                adapter.lista=listaMenu
                adapter.notifyDataSetChanged()
                obtenerRaciones()
            }
        }
        if(listaMenu.isNullOrEmpty()){
            vm.readMenu()
        } else {
            listaMenu= vm.ldListaMenu.value!!
            adapter.lista=listaMenu
            adapter.notifyDataSetChanged()
        }
    }

    /**
     * Obtener raciones
     *
     */
    private fun obtenerRaciones() {
        listaRaciones.clear()
        for(i in listaMenu){
            listaRaciones.add(i.raciones)
        }
    }

    /**
     * Inicializar
     *
     * Inicializa todos los controles
     *
     * @param view
     */
    private fun inicializar(view: View) {
        cbVeganoMenu=view.findViewById(R.id.cbVeganoMenu)
        cbGlutenFreeMenu=view.findViewById(R.id.cbGlutenFreeMenu)
        btGenerarMenu=view.findViewById(R.id.btGenerarMenu)
        btCarrito=view.findViewById(R.id.btCarrito)
        recMenu=view.findViewById(R.id.recMenu)
        btGenerarListaCompra=view.findViewById(R.id.btGenerarLista)
        pbHome=view.findViewById(R.id.pbHome)
        ivNotificacion=view.findViewById(R.id.ivNotificacion)
        tvVacio=view.findViewById(R.id.tvVacio)
    }




}