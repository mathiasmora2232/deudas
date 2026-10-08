package com.example.deudas

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class deudadbhelper(context: Context) : SQLiteOpenHelper(context, "deuda.db", null, 2) {

    override fun onCreate(db: SQLiteDatabase?) {
        val orderSql = "CREATE TABLE facturas (id INTEGER PRIMARY KEY AUTOINCREMENT, fecha date, nombre TEXT, cliente TEXT, monto REAL)"
        val clienteSql = "CREATE TABLE clientes (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT)"
        db?.execSQL(orderSql)
        db?.execSQL(clienteSql)
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        if (oldVersion < 2) {
            db?.execSQL("CREATE TABLE IF NOT EXISTS clientes (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT)")
        }
    }
}