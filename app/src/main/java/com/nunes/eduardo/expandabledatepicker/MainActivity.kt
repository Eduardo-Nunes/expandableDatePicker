package com.nunes.eduardo.expandabledatepicker

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import net.danlew.android.joda.JodaTimeAndroid

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        JodaTimeAndroid.init(this)

        initViews()
    }


    private fun initViews() {
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        toolbar.setHomeAsUp(this)
        toolbar.onDaySelectedListener = { date ->
            date
        }
    }
}
