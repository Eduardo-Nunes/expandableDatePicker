package com.nunes.eduardo.expandabledatepicker

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import kotlinx.android.synthetic.main.date_toolbar_layout.view.*
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.util.*

/**
 * TODO: document your custom view class.
 */
const val HUMAN_DATE_PATTERN: String = "dd MMMMM"
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
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    @SuppressLint("PrivateResource")
    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val attributeSet = context.obtainStyledAttributes(
                attrs, R.styleable.ToolbarDatePicker, defStyle, 0)

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

//        datePicker.maxDate = _date.plusDays(_dateRange).millis
//        datePicker.minDate = _date.minusDays(_dateRange).millis

        weekDaysList.adapter = WeekDaysListAdapter(prepareDates(_date, _dateRange), { setDateClicked(it) })
    }

    private fun initListeners() {

        expandCollapseDate.setOnClickListener {
            expandableDatePicker.toggle(it)
        }

        dateLayout.setOnClickListener {
            expandableDatePicker.toggle(expandCollapseDate)
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            datePicker.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
//                setDateClicked(DateTime().withDate(year, monthOfYear, dayOfMonth))
//            }
//        }else{
//            datePicker.init(_date.year, _date.monthOfYear, _date.dayOfMonth,
//                    { _, year, monthOfYear, dayOfMonth ->
//                        setDateClicked(DateTime().withDate(year, monthOfYear, dayOfMonth))
//                    })
//        }
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

        return context.getString(R.string.concateneted_date_text, dateString[0], dateString[1])
    }

    private fun prepareDates(initialDate: DateTime, rangeDays: Int): ArrayList<DateTime> {
        val dateList: ArrayList<DateTime> = arrayListOf()

        for (i in (rangeDays * -1)..rangeDays) {
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
