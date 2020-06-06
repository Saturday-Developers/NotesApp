package itmo.notes.ui.dashboard

import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import itmo.notes.R
import itmo.notes.models.MOOD
import itmo.notes.models.Note
import itmo.notes.models.WEATHER
import lecho.lib.hellocharts.formatter.SimpleAxisValueFormatter
import lecho.lib.hellocharts.model.*
import lecho.lib.hellocharts.view.LineChartView
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
//        val textView: TextView = root.findViewById(R.id.text_dashboard)
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val response = GetNotesTask().execute().get()

        val listType = object : TypeToken<List<Note>>() { }.type
        var notes= Gson().fromJson<List<Note>>(response, listType)

        if(notes!=null)
        {
            notes = notes.sortedBy { it.date_time }
            getDataForWeatherChart(notes)
            getDataForMoodChart(notes)
            getDataForWeatherMoodChart(notes)
            getDataForWeatherMoodDateChart(notes)
        }
    }

    private fun getDataForWeatherChart(notes: List<Note>)
    {
        val weatherList:MutableList<Int> = mutableListOf<Int>()
        for(i:Int in notes.indices)
        {
            weatherList.add(notes[i].weather_rating)
        }

        val datetimeList:MutableList<String> = mutableListOf<String>()
        for(i:Int in notes.indices)
        {
            val format = SimpleDateFormat("dd/MM/yyy")
            val value = format.format(notes[i].date_time);
            datetimeList.add(value)
        }

        val weatherChartView: LineChartView = requireView().findViewById(R.id.weatherChart)
        populateChart(weatherChartView, datetimeList, weatherList, 1, 0)
    }

    private fun getDataForMoodChart(notes: List<Note>)
    {
        val moodList:MutableList<Int> = mutableListOf<Int>()
        for(i:Int in notes.indices)
        {
            moodList.add(notes[i].mood_rating)
        }

        val datetimeList:MutableList<String> = mutableListOf<String>()
        for(i:Int in notes.indices)
        {
            val format = SimpleDateFormat("dd/MM/yyy")
            val value = format.format(notes[i].date_time);
            datetimeList.add(value)
        }

        val moodChartView: LineChartView = requireView().findViewById(R.id.moodChart)
        populateChart(moodChartView, datetimeList, moodList, 0, 0)
    }

    private fun getDataForWeatherMoodChart(notes: List<Note>)
    {
        val moodList:MutableList<Int> = mutableListOf<Int>()
        for(i:Int in notes.indices)
        {
            moodList.add(notes[i].mood_rating)
        }

        val weatherList:MutableList<String> = mutableListOf<String>()
        for(i:Int in notes.indices)
        {
            weatherList.add("" + notes[i].weather_rating)
        }

        val weatherMoodChartView: LineChartView = requireView().findViewById(R.id.weatherMoodChart)
        populateChart(weatherMoodChartView, weatherList, moodList, 0, 1)
    }

    private fun getDataForWeatherMoodDateChart(notes: List<Note>)
    {
        val moodList:MutableList<Int> = mutableListOf<Int>()
        for(i:Int in notes.indices)
        {
            moodList.add(notes[i].mood_rating)
        }

        val weatherList:MutableList<Int> = mutableListOf<Int>()
        for(i:Int in notes.indices)
        {
            weatherList.add(notes[i].weather_rating)
        }

        val datetimeList:MutableList<String> = mutableListOf<String>()
        for(i:Int in notes.indices)
        {
            val format = SimpleDateFormat("dd/MM/yyy")
            val value = format.format(notes[i].date_time);
            datetimeList.add(value)
        }

        val weatherMoodDateChartView: LineChartView = requireView().findViewById(R.id.weatherMoodDateChart)
        populateChartWithTwoLines(weatherMoodDateChartView, datetimeList, moodList, weatherList)
    }

    private fun populateChart(lineChartView: LineChartView, axisData:List<String>, yAxisData:List<Int>, yType: Int, xType: Int){

        val yAxisValues: MutableList<PointValue> = mutableListOf<PointValue>()
        val axisValues: MutableList<AxisValue> = mutableListOf<AxisValue>()

        val line = Line(yAxisValues).setColor(Color.parseColor("#9C27B0"))

        if(xType==0)
        {
            for (i in axisData.indices) {
                axisValues.add(AxisValue(i.toFloat()).setLabel(axisData[i]))
            }
        }
        else{
            for (i in axisData.indices) {
                var value=axisData[i].toInt();
                axisValues.add(AxisValue(i.toFloat()).setLabel(WEATHER.values()[value-1].name))
            }
        }

        for (i in axisData.indices) {
            yAxisValues.add(PointValue(i.toFloat(), yAxisData[i].toFloat()))
        }

        val lines: MutableList<Line> = mutableListOf<Line>()
        lines.add(line)

        val data = LineChartData()
        data.lines = lines

        lineChartView.setLineChartData(data);

        val axis = Axis()
        axis.setValues(axisValues)
        data.axisXBottom = axis

        val yAxis = Axis()
        data.axisYLeft = yAxis

        axis.setTextSize(16);
        axis.setHasTiltedLabels(true)
        if(xType==0){
            axis.setName("Date");
        }
        else{
            axis.setName("Weather");
        }

        val formatter = SimpleAxisValueFormatter()
        formatter.decimalDigitsNumber = 0
        yAxis.formatter = formatter
        val yValues: MutableList<AxisValue> = mutableListOf<AxisValue>()
        for (i in 1..6) {
            if(yType==0)
            {
                yValues.add(AxisValue(i.toFloat()).setLabel(MOOD.values()[i-1].name))
            }
            else{
                yValues.add(AxisValue(i.toFloat()).setLabel(WEATHER.values()[i-1].name))
            }
        }

        yAxis.setValues(yValues)
        yAxis.setTextSize(16);
        if(yType==0){
            yAxis.setName("Mood");
        }
        else{
            yAxis.setName("Weather");
        }

        yAxis.setHasTiltedLabels(true)
    }

    private fun populateChartWithTwoLines(lineChartView: LineChartView, axisData:List<String>, yAxisDataOne:List<Int>, yAxisDataTwo:List<Int>){

        val yAxisValuesOne: MutableList<PointValue> = mutableListOf<PointValue>()
        val yAxisValuesTwo: MutableList<PointValue> = mutableListOf<PointValue>()
        val axisValues: MutableList<AxisValue> = mutableListOf<AxisValue>()

        val colorOne = "#9C27B0";
        val colorTwo = "#778a75";
        val lineOne = Line(yAxisValuesOne).setColor(Color.parseColor(colorOne))
        val lineTwo = Line(yAxisValuesTwo).setColor(Color.parseColor(colorTwo))

        for (i in axisData.indices) {
            axisValues.add(AxisValue(i.toFloat()).setLabel(axisData[i]))
        }

        for (i in axisData.indices) {
            yAxisValuesOne.add(PointValue(i.toFloat(), yAxisDataOne[i].toFloat()))
        }

        for (i in axisData.indices) {
            yAxisValuesTwo.add(PointValue(i.toFloat(), yAxisDataTwo[i].toFloat()))
        }

        val lines: MutableList<Line> = mutableListOf<Line>()
        lines.add(lineOne)
        lines.add(lineTwo)

        val data = LineChartData()
        data.lines = lines

        lineChartView.setLineChartData(data);

        val axis = Axis()
        axis.setValues(axisValues)
        data.axisXBottom = axis

        val yAxis = Axis()
        data.axisYLeft = yAxis

        axis.setTextSize(16);
        axis.setHasTiltedLabels(true)
        axis.setName("Date");

        val formatter = SimpleAxisValueFormatter()
        formatter.decimalDigitsNumber = 0
        yAxis.formatter = formatter
        val yValues: MutableList<AxisValue> = mutableListOf<AxisValue>()
        for (i in 1..6) {
            yValues.add(AxisValue(i.toFloat()).setLabel(MOOD.values()[i-1].name + ", " + WEATHER.values()[i-1].name))
        }
        yAxis.setValues(yValues)
        yAxis.setTextSize(16);
        yAxis.setHasTiltedLabels(true)
        yAxis.setName("Mood, Weather");

        val moodLegend: TextView = requireView().findViewById(R.id.legendMood);
        moodLegend.setBackgroundColor(Color.parseColor(colorOne));

        val weatherLegend: TextView = requireView().findViewById(R.id.legendWeather);
        weatherLegend.setBackgroundColor(Color.parseColor(colorTwo));
    }

}
