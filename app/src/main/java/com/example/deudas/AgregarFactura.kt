package com.example.deudas

import android.content.ContentValues
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AgregarFactura : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar)

        val titulofactura = findViewById<EditText>(R.id.facttitle)
        val cliente = findViewById<EditText>(R.id.cliente)
        val valorfact = findViewById<EditText>(R.id.valorfact)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)
        val btnRegresar = findViewById<Button>(R.id.btnRegresar)

        btnRegresar?.setOnClickListener {
            finish()
        }

        val clientePrellenado = intent.getStringExtra("CLIENTE_NOMBRE")
        if (!clientePrellenado.isNullOrEmpty()) {
            cliente.setText(clientePrellenado)
        }


        btnGuardar.setOnClickListener {
            val nombre = titulofactura.text.toString()
            val client = cliente.text.toString()
            val monto = valorfact.text.toString().toDoubleOrNull() ?: 0.0


            val miAyudante = deudadbhelper(this)

            val db = miAyudante.writableDatabase

            val valores = ContentValues()
            valores.put("nombre", nombre)
            valores.put("cliente", client)
            valores.put("monto", monto)

            val resultado = db.insert("facturas", null, valores)

            db.close()

            if (resultado != -1L) {
                Toast.makeText(this, "Guardado con éxito!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
            }
        }
    }
}