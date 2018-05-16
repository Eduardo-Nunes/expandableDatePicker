package com.nunes.eduardo.expandabledatepicker

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.week_day_picker_item.view.*
import org.joda.time.DateTime
import java.util.*

class WeekDaysListAdapter(
        private val items: ArrayList<DateTime>,
        private val onClickListener: (DateTime) -> Unit)
    : RecyclerView.Adapter<WeekDaysListAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position], onClickListener)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.week_day_picker_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(date: DateTime, onClickListener: (DateTime) -> Unit) = with(itemView) {
            textViewDay.text = date.dayOfMonth().asText
            textViewWeek.text = date.dayOfWeek().getAsShortText(Locale(context.getString(R.string.language), context.getString(R.string.country)))

            setOnClickListener {
                onClickListener(date)
            }
        }
    }
}