package com.example.proyectofinal.model.storage

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Prefs
 *
 * @constructor
 *
 * @param c
 */
@Singleton
class Prefs @Inject constructor(@ApplicationContext c: Context){
    val storage=c.getSharedPreferences("USUARIOS",0)

    /**
     * Get user
     *
     * @return
     */
    fun getUser(): String?{
        return storage.getString("USUARIO", null)
    }

    /**
     * Set user
     *
     * @param usuario
     */
    fun setUser(usuario: String){
        storage.edit().putString("USUARIO", usuario).apply()
    }

    /**
     * Borrar todo
     *
     */
    fun borrarTodo(){
        storage.edit().clear().apply()
    }
}
