package itmo.notes.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

import itmo.notes.R
import itmo.notes.models.Note
import java.text.SimpleDateFormat
class NoteAdapter(private val context: Context,
                    private val dataList: ArrayList<Note>) : BaseAdapter() {

    private val inflater: LayoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    override fun getCount(): Int { return dataList.size }
    override fun getItem(position: Int): Int { return position }
    override fun getItemId(position: Int): Long { return position.toLong() }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var dataitem = dataList[position]

        val rowView = inflater.inflate(R.layout.fragment_home_list_row, parent, false)
        rowView.findViewById<TextView>(R.id.row_description).text = dataitem.description
        rowView.findViewById<TextView>(R.id.row_date).text = dataitem.date_time.toString()

        var mood_resource = when(dataitem.mood_rating) {
            1 -> R.drawable.angry_emoji
            2 -> R.drawable.sad_emoji
            3 -> R.drawable.cry_emoji
            4 -> R.drawable.neutral_emoji
            5 -> R.drawable.smile_emoji
            6 -> R.drawable.big_smile_emoji
            else -> R.drawable.angry_emoji
        }
        rowView.findViewById<ImageView>(R.id.mood_image).setImageResource(mood_resource)

        var weather_resource = when(dataitem.weather_rating) {
            1 -> R.drawable.storm
            2 -> R.drawable.rain
            3 -> R.drawable.clouds
            4 -> R.drawable.cloud_rain_sun
            5 -> R.drawable.cloud_sun
            6 -> R.drawable.sun
            else -> R.drawable.storm
        }
        rowView.findViewById<ImageView>(R.id.weather_image).setImageResource(weather_resource)

        rowView.tag = position
        return rowView
    }
}