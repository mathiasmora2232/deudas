package com.example.deudas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class detalle_factura : AppCompatActivity() {

    private lateinit var listViewDeudas: ListView
    private val listaFacturas = ArrayList<Factura>()
    private var clienteNombre: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_factura)

        val mainView = findViewById<android.view.View>(R.id.main)
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }

        clienteNombre = intent.getStringExtra("CLIENTE_NOMBRE") ?: ""

        val tvFacturas = findViewById<TextView>(R.id.facturas)
        if (clienteNombre.isNotEmpty()) {
            tvFacturas.text = "Facturas de $clienteNombre"
        } else {
            tvFacturas.text = "Facturas"
        }

        listViewDeudas = findViewById(R.id.listViewDeudas)
        val btnAgregar = findViewById<Button>(R.id.btnAgregar)
        val btnRegresar = findViewById<Button>(R.id.btnRegresar)

        btnRegresar?.setOnClickListener {
            finish()
        }

        btnAgregar.setOnClickListener {
            val intent = Intent(this, AgregarFactura::class.java)
            if (clienteNombre.isNotEmpty()) {
                intent.putExtra("CLIENTE_NOMBRE", clienteNombre)
            }
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        cargarFacturasDelCliente()
    }

    private fun cargarFacturasDelCliente() {
        listaFacturas.clear()

        val db = deudadbhelper(this).readableDatabase

        val cursor = if (clienteNombre.isNotEmpty()) {
            db.rawQuery("SELECT * FROM facturas WHERE cliente = ?", arrayOf(clienteNombre))
        } else {
            db.rawQuery("SELECT * FROM facturas", null)
        }

        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex("id")
            val nombreIndex = cursor.getColumnIndex("nombre")
            val clienteIndex = cursor.getColumnIndex("cliente")
            val montoIndex = cursor.getColumnIndex("monto")

            do {
                val id = if (idIndex != -1) cursor.getInt(idIndex) else -1
                val nombre = if (nombreIndex != -1) cursor.getString(nombreIndex) ?: "" else ""
                val cliente = if (clienteIndex != -1) cursor.getString(clienteIndex) ?: "" else ""
                val monto = if (montoIndex != -1) cursor.getDouble(montoIndex) else 0.0

                val miDeuda = Factura(id, nombre, cliente, monto)
                listaFacturas.add(miDeuda)

            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        val adaptadorPersonalizado = FacturaAdapter(this, listaFacturas)
        listViewDeudas.adapter = adaptadorPersonalizado
    }
}