package com.example.todolist.adapters

import android.view.View
import android.widget.AdapterView

class MySpinnerAdapter: AdapterView.OnItemSelectedListener {
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        if (p0 != null) {
            p0.getItemAtPosition(p2)
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}