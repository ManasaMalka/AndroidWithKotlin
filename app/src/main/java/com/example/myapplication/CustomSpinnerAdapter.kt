package com.example.myapplication

// CustomSpinnerAdapter.kt
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ArrayAdapter
//import android.widget.TextView
//
//class CustomSpinnerAdapter(context: Context, resource: Int, objects: Array<String>) :
//    ArrayAdapter<String>(context, resource, objects) {
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        return createView(position, convertView, parent)
//    }
//
//    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
//        return createView(position, convertView, parent)
//    }
//
//    private fun createView(position: Int, convertView: View?, parent: ViewGroup): View {
//        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false)
//        val textView = view.findViewById<TextView>(android.R.id.text1)
//        textView.text = getItem(position)
//        return view
//    }
//}


// CustomSpinnerAdapter.kt// CustomSpinnerAdapter.kt
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat

class CustomSpinnerAdapter(context: Context, resource: Int, objects: Array<String>) :
    ArrayAdapter<String>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createDropDownView(position, convertView, parent)
    }

    private fun createView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_item, parent, false)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = getItem(position)
        textView.setTextColor(ContextCompat.getColor(context, android.R.color.black)) // Set text color to black
        return view
    }

    private fun createDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = getItem(position)
        textView.setTextColor(ContextCompat.getColor(context, android.R.color.black)) // Set text color to black
        view.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white)) // Set background color to white
        return view
    }
}
