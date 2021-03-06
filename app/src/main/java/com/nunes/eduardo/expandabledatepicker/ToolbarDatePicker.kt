package com.nunes.eduardo.expandabledatepicker

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import kotlinx.android.synthetic.main.date_toolbar_layout.view.*
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.util.*

/**
 * TODO: document your custom view class.
 */
const val DOUBLE_OFF: Int = 2
const val PLUS_ONE: Int = 1
const val NEGATIVE_NUMBER: Int = -1
const val DEFAULT_DEF_STYLE: Int = 0
const val HUMAN_DATE_PATTERN: String = "dd MMMMM"
const val FISRT_POSITION: Int = 0
const val SECOND_POSITION: Int = 1
class ToolbarDatePicker : Toolbar {

    private var _backgroundColor: Int = 0
    private var _contentColor: Int = 0
    private var _dateRange: Int = 2
    private lateinit var _date: DateTime
    private var _onDaySelectedListener: ((DateTime) -> Unit)? = null

    var onDaySelectedListener: ((DateTime) -> Unit)?
        get() = _onDaySelectedListener
        set(value) {
            _onDaySelectedListener = value
        }

    constructor(context: Context) : super(context) {
        init(null, DEFAULT_DEF_STYLE)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, DEFAULT_DEF_STYLE)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    @SuppressLint("PrivateResource")
    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val attributeSet = context.obtainStyledAttributes(
                attrs, R.styleable.ToolbarDatePicker, defStyle, DEFAULT_DEF_STYLE)

        _backgroundColor = attributeSet.getColor(R.styleable.ToolbarDatePicker_toolbarBackgroundColor,
                ContextCompat.getColor(context, R.color.background_material_dark))
        _contentColor = attributeSet.getColor(R.styleable.ToolbarDatePicker_toolbarContentColor,
                ContextCompat.getColor(context, R.color.background_material_light))
        _dateRange = attributeSet.getInteger(R.styleable.ToolbarDatePicker_dateRange,
                _dateRange)

        attributeSet.recycle()

        initViews()
        initData()
        initListeners()
    }

    private fun initViews() {
        setBackgroundColor(_backgroundColor)

        inflate(context, R.layout.date_toolbar_layout, this)

        setContentColor()
    }

    private fun initData() {
        _date = DateTime.now()
        dateTitle.text = retrieveHumanDate(_date)

        weekDaysList.layoutManager = GridLayoutManager(context, (_dateRange * DOUBLE_OFF) + PLUS_ONE)

        val itemDecoration = ItemOffsetDecoration(context, R.dimen.item_offset)
        weekDaysList.addItemDecoration(itemDecoration)

        weekDaysList.adapter = WeekDaysListAdapter(prepareDates(_date, _dateRange), { setDateClicked(it) })
    }

    private fun initListeners() {

        expandCollapseDate.setOnClickListener {
            expandableDatePicker.toggle(it)
        }

        dateLayout.setOnClickListener {
            expandableDatePicker.toggle(expandCollapseDate)
        }

    }

    private fun setDateClicked(date: DateTime){
        _date = date
        dateTitle.text = retrieveHumanDate(_date)
        expandableDatePicker.toggle(expandCollapseDate)
        _onDaySelectedListener?.invoke(_date)
    }

    private fun setContentColor() {
        dateTitle.setTextColor(_contentColor)
        expandCollapseDate.setColorFilter(_contentColor)
    }

    private fun retrieveHumanDate(date: DateTime): String {
        val dateString = DateTimeFormat
                .forPattern(HUMAN_DATE_PATTERN)
                .withLocale(Locale(context.getString(R.string.language), context.getString(R.string.country)))
                .print(date)
                .toUpperCase()
                .split(' ')

        return context.getString(R.string.concateneted_date_text, dateString[FISRT_POSITION], dateString[SECOND_POSITION])
    }

    private fun prepareDates(initialDate: DateTime, rangeDays: Int): ArrayList<DateTime> {
        val dateList: ArrayList<DateTime> = arrayListOf()

        for (i in (rangeDays * NEGATIVE_NUMBER)..rangeDays) {
            dateList.add(initialDate.plusDays(i))
        }

        return dateList
    }

    fun setHomeAsUp(activity: AppCompatActivity){
        homeButton.setColorFilter(_contentColor)

        homeButton.visibility = VISIBLE
        homeButton.setOnClickListener {
            activity.onBackPressed()
        }
    }
}
