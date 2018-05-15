package com.nunes.eduardo.expandabledatepicker

import android.content.Context
import android.graphics.Canvas
import android.support.constraint.ConstraintLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator

/**
 * Created by eduardonunes on 15/05/18.
 */

/**
 * TODO: document your custom view class.
 */
class ExpandableDatePicker : ConstraintLayout {

    private var _isAnimating: Boolean = false
    private var _isExpanded: Boolean = false
    private var _targetHeight: Int = 0
    private var _collapseMinHeight: Int = 0
    private var _savedDuration: Int = 300
    private var _interpolator: Interpolator = DecelerateInterpolator()

    private var textPaint: TextPaint? = null
    private var textWidth: Float = 0f
    private var textHeight: Float = 0f

    var isExpanded: Boolean
        get() = _isExpanded
        set(value) {
            _isExpanded = value
        }

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
                attrs, R.styleable.ExpandableDatePicker, defStyle, 0)

        _isExpanded = attributeSet.getBoolean(
                R.styleable.ExpandableDatePicker_expand_isExpanded, _isExpanded)
        _savedDuration = attributeSet.getInt(
                R.styleable.ExpandableDatePicker_expand_duration,
                _savedDuration)
        _interpolator = when(attributeSet.getInt(R.styleable.ExpandableDatePicker_expand_interpolator, 2)){
            1 -> AccelerateInterpolator()
            2 -> DecelerateInterpolator()
            else -> _interpolator
        }
        attributeSet.recycle()
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }
}
