package com.example.proyectofinal.model

data class Comida(
    var nombre: String?=null,
    var descripcion: String?=null,
    var tags: MutableList<String>?=null,
    var imagen: String?=null,
    var ingredientes: MutableList<Ingrediente>?=null,
    var preparacion: MutableList<String>?=null,
    var raciones: Int=1
    ):java.io.Serializable{

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Comida
        if (nombre!!.lowercase() != other.nombre!!.lowercase()) return false
        return true
    }
    override fun hashCode(): Int {
        return nombre!!.lowercase().hashCode()
    }

    //Constructor copia para evitar aliasing
    constructor(comida: Comida): this (comida.nombre, comida.descripcion, comida.tags, comida.imagen, comida.ingredientes, comida.preparacion, comida.raciones)

}
