package com.nunes.eduardo.expandabledatepicker

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        initListeners()
    }

    private fun initListeners() {
        expandCollapseDate.setOnClickListener {
            expandableDatePicker.toggle(it)
        }
    }

    private fun initViews() {
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
    }
}
