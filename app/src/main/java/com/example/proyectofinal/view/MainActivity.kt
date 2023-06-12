package com.example.proyectofinal.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.airbnb.lottie.LottieAnimationView
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.ActivityMainBinding
import com.example.proyectofinal.viewmodel.MainViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main activity
 *
 * @constructor Create empty Main activity
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    //Este responseLauncher será el que vuelva del intento de registro con google
    private val responseLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode== RESULT_OK){
            val task= GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try{
                val cuenta=task.getResult(ApiException::class.java) // Aqui tengo la cuenta con la que entro
                if(cuenta!=null){
                    val credenciales= GoogleAuthProvider.getCredential(cuenta.idToken,null)
                    FirebaseAuth.getInstance().signInWithCredential(credenciales).addOnCompleteListener {
                        if(it.isSuccessful){
                            vm.guardarUsuario(cuenta.email?:"") //Si es nulo, pasa ""
                            startActivity(Intent(this, DosActivity::class.java))
                        } else {
                            Toast.makeText(this, "Error en el login de google", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }catch (e: ApiException){
                println(e.message)
            }
        }
    }
    private var email = ""
    private var pass = ""
    private val vm: MainViewModel by viewModels()

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inicializarListas()
        setListeners()
        comprobarEmail()
        lottieAnimation(R.raw.randomeater30fps)
    }

    private fun lottieAnimation(animation: Int){
        binding.ivAnimacion.setAnimation(animation)
        binding.ivAnimacion.playAnimation()
    }

    /**
     * Inicializar listas
     *
     * Ejecuta los metodos para poner las listas en observacion, haciendo que se carguen antes
     *
     */
    private fun inicializarListas() {
        observarIngredientes()
        observarComidas()
    }

    /**
     * Comprobar email
     *
     * Comprueba las sharedPreferences para mandar a la pantalla principal si detecta que el usuario
     * ya estaba logeado con anterioridad
     *
     */
    private fun comprobarEmail() {
        if (vm.obtenerUsuario() != null) {
            startActivity(Intent(this, DosActivity::class.java))
        }
    }

    /**
     * Set listeners
     *
     */
    private fun setListeners() {
        binding.btRegistro.setOnClickListener {
            if (!existeError()) {
                registrarUsuario()
            }
        }
        binding.btEntrar.setOnClickListener {
            if(!existeError()){
                entrar()
            }
        }
        binding.btGoogle.setOnClickListener {
                entrarGoogle()
        }

    }

    /**
     * Entrar google
     *
     * Inicia sesion con google
     *
     */
    private fun entrarGoogle() {
        val googleConf= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("441056481709-d2p0622m198o3goj2u09fv3duo7gndu0.apps.googleusercontent.com") //en el proyecto->app->google-services->oath_client
            .requestEmail()
            .build()
        val googleClient= GoogleSignIn.getClient(this,googleConf)
        //Para que si cierro sesión me de a elegir un usuario y no me valide con el último
        googleClient.signOut()
        responseLauncher.launch(googleClient.signInIntent)
    }

    /**
     * Entrar
     *
     * Inicia sesion con correo/contraseña
     *
     */
    private fun entrar() {
        vm.entrar(email,pass){
            if(it){
                vm.guardarUsuario(email)
                startActivity(Intent(this, DosActivity::class.java))
            } else {
                Toast.makeText(this, "Error al entrar", Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * Registrar usuario
     *
     * Registra al usuario con correo/contraseña
     *
     */
    private fun registrarUsuario() {
        vm.registrarUsuario(email, pass) {
            if (it) {
                vm.guardarUsuario(email)
                startActivity(Intent(this, DosActivity::class.java))
            } else {
                Toast.makeText(this, "Error en el registro", Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * Existe error
     *
     * Comprueba si existe algun error en correo/contraseña segun lo asignado
     *
     * @return
     */
    private fun existeError(): Boolean {
        email = binding.etEmail.text.toString().trim()
        pass = binding.etPass.text.toString().trim()
        if (email.isBlank()) {
            binding.etEmail.setError("El email no puede estar vacío")
            binding.etEmail.requestFocus()
            return true
        } else if (pass.isBlank()) {
            binding.etPass.setError("La contraseña no puede estar vacía")
            binding.etPass.requestFocus()
            return true
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.etEmail.setError("El formato del email es incorrecto")
            binding.etEmail.requestFocus()
            return true
        } else if (pass.length < 6) {
            binding.etPass.setError("La contraseña debe tener al menos 6 caracteres")
            binding.etPass.requestFocus()
            return true
        }
        return false
    }

    //Sobreescribir el método para que no haga nada al pulsar el botón de retroceso
    override fun onBackPressed() {

    }

    private fun observarComidas() {
        vm.readComidas().observe(this, Observer {

        })
    }

    private fun observarIngredientes(){
        vm.readIngredientes().observe(this, Observer {

        })
    }


}