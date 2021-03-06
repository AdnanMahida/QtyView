package com.ad.qtyview

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val qty: QtyView = findViewById(R.id.qty)
        val txtQty: TextView = findViewById(R.id.txtQty)
        val btnSetQty:Button = findViewById(R.id.btnSetQty)
        btnSetQty.setOnClickListener {
            qty.qty = 1
        }

        txtQty.text = "Current "+qty.qty.toString()+" Qty"

        qty.onQtyChangeListener = object : QtyView.OnQtyChangeListener {
            override fun onQuantityChanged(
                oldQuantity: Int,
                newQuantity: Int
            ) {
                Toast.makeText(applicationContext, "New Qty $newQuantity", Toast.LENGTH_SHORT)
                    .show()
                txtQty.text = "Current "+qty.qty.toString()+" Quantities"
            }

            override fun onLimitReached() {
                Toast.makeText(applicationContext, "On Limit Reached", Toast.LENGTH_SHORT)
                    .show()
                txtQty.text = "Current "+qty.qty.toString()+" Quantities"
            }

        }

    }
}