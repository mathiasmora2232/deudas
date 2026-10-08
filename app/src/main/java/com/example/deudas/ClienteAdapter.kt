package com.example.deudas

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ClienteAdapter(context: Context, private val listaClientes: List<Cliente>) :
    ArrayAdapter<Cliente>(context, 0, listaClientes) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var vistaFila = convertView
        if (vistaFila == null) {
            vistaFila = LayoutInflater.from(context).inflate(R.layout.item_cliente, parent, false)
        }

        val clienteActual = getItem(position)

        val tvNombreCliente = vistaFila!!.findViewById<TextView>(R.id.tvNombreCliente)
        val tvDeudaTotal = vistaFila.findViewById<TextView>(R.id.tvDeudaTotal)

        if (clienteActual != null) {
            tvNombreCliente.text = clienteActual.nombre
            tvDeudaTotal.text = "$ ${clienteActual.totalDeuda}"
        }

        return vistaFila
    }
}
