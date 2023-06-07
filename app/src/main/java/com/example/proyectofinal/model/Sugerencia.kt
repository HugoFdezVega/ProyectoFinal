package com.example.proyectofinal.model

/**
 * Sugerencia
 *
 * @property peticionario
 * @property asunto
 * @property contenido
 * @property fecha
 * @constructor Create empty Sugerencia
 */
data class Sugerencia(
    var peticionario: String?=null,
    var asunto: String?=null,
    var contenido: String?=null,
    var fecha: Long=System.currentTimeMillis()
):java.io.Serializable