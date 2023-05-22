package com.example.proyectofinal.model

data class Sugerencia(
    var peticionario: String?=null,
    var asunto: String?=null,
    var contenido: String?=null,
    var fecha: Long=System.currentTimeMillis()
):java.io.Serializable