package com.example.deudas

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class FacturaAdapter(context: Context, private val listaDeudas: List<Factura>) :
    ArrayAdapter<Factura>(context, 0, listaDeudas) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        
        var vistaFila = convertView
        if (vistaFila == null) {
            vistaFila = LayoutInflater.from(context).inflate(R.layout.activity_facturasvista, parent, false)
        }

        val deudaActual = getItem(position)

        val tvNombreItem = vistaFila!!.findViewById<TextView>(R.id.tvNombreItem)
        val tvMontoItem = vistaFila.findViewById<TextView>(R.id.valorfact)

        if (deudaActual != null) {
            tvNombreItem.text = "${deudaActual.nombre} (${deudaActual.cliente})"
            tvMontoItem.text = "$ ${deudaActual.monto}"
        }

        return vistaFila
    }
}