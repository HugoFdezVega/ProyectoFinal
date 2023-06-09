package com.example.proyectofinal.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.ActivityDosBinding
import com.example.proyectofinal.model.Comida
import com.example.proyectofinal.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

/**
 * Dos activity
 *
 * @constructor Create empty Dos activity
 */
@AndroidEntryPoint
class DosActivity : AppCompatActivity(), InterfazMenuBar {
    private val fragments= arrayOf(HomeFragment(),ListasFragment())
    private val vm: MainViewModel by viewModels()
    private var admin=false

    lateinit var binding: ActivityDosBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        comprobarAdmin()
    }

    /**
     * Boton pulsado
     *
     * @param boton
     */
    override fun botonPulsado(boton: Int) {
        if(boton!=2){
            cargarFragment(fragments[boton] as Fragment)
        } else {
            val builder= AlertDialog.Builder(this)
            builder.setTitle("Cerrar sesión")
                .setMessage("¿Seguro que desea cerrar sesión?")
                .setPositiveButton("Aceptar"){ dialog, wich->
                    FirebaseAuth.getInstance().signOut()
                    vm.cerrarSesion()
                    startActivity(Intent(this, MainActivity::class.java))
                }
                .setNegativeButton("Cancelar") { dialog, wich->
                    dialog.dismiss()
                }
                .setCancelable(true)
                .show()
        }
    }

    /**
     * Cargar fragment
     *
     * @param fragment
     */
    private fun cargarFragment(fragment: Fragment){
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fcGenerico,fragment)
            addToBackStack(null)
        }
    }

    /**
     * Comprobar admin
     *
     */
    private fun comprobarAdmin() {
        var admins=resources.getStringArray(R.array.admins)
        if(admins.contains(vm.obtenerUsuario())){
            admin=true
        }
    }

    /**
     * On create options menu
     *
     * @param menu
     * @return
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_opciones, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * On options item selected
     *
     * @param item
     * @return
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.itemSugerencias->{
                if(admin){
                    startActivity(Intent(this, SugerenciasAdminActivity::class.java))
                } else {
                    startActivity(Intent(this, SugerenciasUserActivity::class.java))
                }
            }
            R.id.itemSalir->{
                finishAffinity()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //Sobreescribir el método para que no haga nada al pulsar el botón de retroceso
    override fun onBackPressed() {

    }


}