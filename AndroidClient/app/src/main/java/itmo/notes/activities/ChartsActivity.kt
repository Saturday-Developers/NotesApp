package itmo.notes.activities

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import itmo.notes.R
import kotlinx.android.synthetic.main.activity_charts.*
import lecho.lib.hellocharts.model.*
import lecho.lib.hellocharts.view.LineChartView


class ChartsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charts)
        textView.text = "Hello, Tanya!"
        populateTestChart()
    }

    private fun populateTestChart(){
        val lineChartView: LineChartView = findViewById(R.id.chart)
        val axisData = arrayOf(
            "Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept",
            "Oct", "Nov", "Dec"
        )
        val yAxisData = intArrayOf(50, 20, 15, 30, 20, 60, 15, 40, 45, 10, 90, 18)

        val yAxisValues: MutableList<PointValue> = mutableListOf<PointValue>()
        val axisValues: MutableList<AxisValue> = mutableListOf<AxisValue>()

        val line = Line(yAxisValues).setColor(Color.parseColor("#9C27B0"))

        for (i in axisData.indices) {
            axisValues.add(AxisValue(i.toFloat()).setLabel(axisData[i]))
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

    }
}