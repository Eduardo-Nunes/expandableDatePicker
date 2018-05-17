package com.nunes.eduardo.expandabledatepicker

import android.animation.ValueAnimator
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.*

/**
 * TODO: document your custom view class.
 */

const val ZERO_HEIGHT: Int = 0
const val MINIMIUM_HEIGHT: Int = 1

class ExpandableDatePicker : ConstraintLayout {

    private var _isAnimating: Boolean = false
    private var _isExpanded: Boolean = false
    private var _targetHeight: Int = 0
    private var _savedDuration: Int = 300
    private var _interpolator: Interpolator = DecelerateInterpolator()

    var isExpanded: Boolean
        get() = _isExpanded
        set(value) {
            _isExpanded = value
        }

    constructor(context: Context) : super(context) {
        initView(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        if (isInEditMode) return

        attrs?.let {
            initAttrs(context, it)
        }

        if (!_isExpanded) {
            collapse(null)
        }
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        // Load attributes
        val attributeSet = context.obtainStyledAttributes(
                attrs, R.styleable.ExpandableDatePicker)

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

    fun toggle(rotatingViews: View) {
        if (isExpanded) {
            collapse(rotatingViews)
        } else {
            expand(rotatingViews)
        }
    }

    private fun expand(rotatingViews: View?) {
        if (_isAnimating) return

        if (_targetHeight == ZERO_HEIGHT) {
            measure(
                    MeasureSpec.makeMeasureSpec((parent as View).width, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(ZERO_HEIGHT, MeasureSpec.UNSPECIFIED)
            )
            _targetHeight = measuredHeight
        }

        // Older versions of android (pre API 21) cancel animations for views with a verticalBlank of 0 so use 1 instead.
        layoutParams.height = MINIMIUM_HEIGHT
        visibility = VISIBLE


        val animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                val newHeight =
                        if (interpolatedTime == 1f) {
                            _isExpanded = true
                            _isAnimating = false
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        } else {
                            (_targetHeight * interpolatedTime).toInt()
                        }

                layoutParams.height = newHeight

                requestLayout()
            }

            override fun willChangeBounds(): Boolean = false
        }
        animation.interpolator = _interpolator
        animation.duration = _savedDuration.toLong()
        startAnimation(animation)

        rotatingViews?.let { view ->
            val valueAnimator = ValueAnimator.ofFloat(0f, 180f)
            valueAnimator.addUpdateListener {
                view.rotationX = it.animatedValue as Float
            }

            valueAnimator.interpolator = LinearInterpolator()
            valueAnimator.duration = _savedDuration.toLong()
            valueAnimator.start()
        }

        _isAnimating = true
    }

    private fun collapse(rotatingViews: View?) {
        if (_isAnimating) return

        if (_targetHeight == ZERO_HEIGHT) {
            _targetHeight = measuredHeight
        }

        val animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                val newHeight = if (interpolatedTime == 1f) {
                    visibility = GONE
                    _isAnimating = false
                    isExpanded = false
                    ZERO_HEIGHT
                } else {
                    ((_targetHeight) - (_targetHeight * interpolatedTime).toInt())
                }

                layoutParams.height = newHeight

                requestLayout()
            }

            override fun willChangeBounds(): Boolean = true
        }
        animation.interpolator = _interpolator
        animation.duration = _savedDuration.toLong()
        startAnimation(animation)

        rotatingViews?.let { view ->
            val valueAnimator = ValueAnimator.ofFloat(180f, 360f)
            valueAnimator.addUpdateListener {
                view.rotationX = it.animatedValue as Float
            }

            valueAnimator.interpolator = LinearInterpolator()
            valueAnimator.duration = _savedDuration.toLong()
            valueAnimator.start()
        }

        _isAnimating = true

    }
}
