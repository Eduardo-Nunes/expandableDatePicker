package com.nunes.eduardo.expandabledatepicker

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import kotlinx.android.synthetic.main.date_toolbar_layout.view.*

/**
 * TODO: document your custom view class.
 */
class ToolbarDatePicker : Toolbar {

    private var _backgroundColor: Int = 0
    private var _contentColor: Int = 0

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val attributeSet = context.obtainStyledAttributes(
                attrs, R.styleable.ToolbarDatePicker, defStyle, 0)

        _backgroundColor = attributeSet.getColor(R.styleable.ToolbarDatePicker_toolbarBackgroundColor,
                ContextCompat.getColor(context, R.color.background_material_dark))
        _contentColor = attributeSet.getColor(R.styleable.ToolbarDatePicker_toolbarContentColor,
                ContextCompat.getColor(context, R.color.background_material_light))

        setBackgroundColor(_backgroundColor)

        attributeSet.recycle()

        inflate(context, R.layout.date_toolbar_layout, this)

        setContentColor()

        initListeners()
    }

    private fun initListeners() {

        expandCollapseDate.setOnClickListener {
            expandableDatePicker.toggle(it)
        }

        dateLayout.setOnClickListener {
            expandableDatePicker.toggle(expandCollapseDate)
        }
    }

    private fun setContentColor() {
        dateTitle.setTextColor(_contentColor)
        expandCollapseDate.setColorFilter(_contentColor)
    }

    fun setHomeAsUp(activity: AppCompatActivity){
        homeButton.setColorFilter(_contentColor)

        homeButton.visibility = VISIBLE
        homeButton.setOnClickListener {
            activity.onBackPressed()
        }
    }
}
