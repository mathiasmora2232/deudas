package com.example.deudas

data class Factura(
    var id: Int = -1,
    var nombre: String,
    var cliente: String,
    var monto: Double
)