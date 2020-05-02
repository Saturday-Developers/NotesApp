package itmo.notes.ui.dashboard

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import itmo.notes.R
import lecho.lib.hellocharts.model.*
import lecho.lib.hellocharts.view.LineChartView

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

        populateTestChart()
    }

    private fun populateTestChart(){
        val lineChartView: LineChartView = requireView().findViewById(R.id.chart)
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
