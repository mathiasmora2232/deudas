package com.example.deudas

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class FacturasActivity : AppCompatActivity() {

    private lateinit var listViewDeudas: ListView
    private val listaClientes = ArrayList<Cliente>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deudas)

        listViewDeudas = findViewById(R.id.listViewDeudas)
        val btnAgregar = findViewById<Button>(R.id.btnAgregar)

        btnAgregar.setOnClickListener {
            mostrarDialogoNuevoCliente()
        }

        listViewDeudas.setOnItemClickListener { _, _, position, _ ->
            val clienteElegido = listaClientes[position]
            val intent = Intent(this, detalle_factura::class.java)
            intent.putExtra("CLIENTE_NOMBRE", clienteElegido.nombre)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        cargarDatosDesdeBaseDeDatos()
    }

    private fun mostrarDialogoNuevoCliente() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Nuevo Cliente")

        val input = EditText(this)
        input.hint = "Nombre del cliente"
        builder.setView(input)

        builder.setPositiveButton("Guardar") { _, _ ->
            val nombreCliente = input.text.toString().trim()
            if (nombreCliente.isNotEmpty()) {
                guardarClienteEnBD(nombreCliente)
                cargarDatosDesdeBaseDeDatos()
            } else {
                Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancelar") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    private fun guardarClienteEnBD(nombre: String) {
        val db = deudadbhelper(this).writableDatabase
        val valores = ContentValues()
        valores.put("nombre", nombre)
        db.insert("clientes", null, valores)
        db.close()
    }

    private fun calcularDeudaTotalCliente(db: SQLiteDatabase, clienteNombre: String): Double {
        var total = 0.0
        val cursor = db.rawQuery("SELECT SUM(monto) FROM facturas WHERE cliente = ?", arrayOf(clienteNombre))
        if (cursor.moveToFirst() && !cursor.isNull(0)) {
            total = cursor.getDouble(0)
        }
        cursor.close()
        return total
    }

    private fun cargarDatosDesdeBaseDeDatos() {
        listaClientes.clear()

        val db = deudadbhelper(this).readableDatabase

        val cursorClientes = db.rawQuery("SELECT * FROM clientes", null)
        if (cursorClientes.moveToFirst()) {
            val idIndex = cursorClientes.getColumnIndex("id")
            val nombreIndex = cursorClientes.getColumnIndex("nombre")
            do {
                val id = if (idIndex != -1) cursorClientes.getInt(idIndex) else -1
                val nombre = if (nombreIndex != -1) cursorClientes.getString(nombreIndex) ?: "" else ""
                if (nombre.isNotEmpty()) {
                    val deudaTotal = calcularDeudaTotalCliente(db, nombre)
                    listaClientes.add(Cliente(id, nombre, deudaTotal))
                }
            } while (cursorClientes.moveToNext())
        }
        cursorClientes.close()

        val cursorFacturas = db.rawQuery("SELECT DISTINCT cliente FROM facturas", null)
        if (cursorFacturas.moveToFirst()) {
            val clienteIndex = cursorFacturas.getColumnIndex("cliente")
            do {
                val clienteNombre = if (clienteIndex != -1) cursorFacturas.getString(clienteIndex) ?: "" else ""
                if (clienteNombre.isNotEmpty() && listaClientes.none { it.nombre.equals(clienteNombre, ignoreCase = true) }) {
                    val deudaTotal = calcularDeudaTotalCliente(db, clienteNombre)
                    listaClientes.add(Cliente(-1, clienteNombre, deudaTotal))
                }
            } while (cursorFacturas.moveToNext())
        }
        cursorFacturas.close()
        db.close()

        val adaptador = ClienteAdapter(this, listaClientes)
        listViewDeudas.adapter = adaptador
    }
}