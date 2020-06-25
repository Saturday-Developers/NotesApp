package itmo.notes.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.ResponseDelivery
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import itmo.notes.R
import itmo.notes.models.Note
import kotlinx.android.synthetic.main.fragment_add_note.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddNoteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddNoteFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createMoodSpinner(view)
        createWeatherSpinner(view)

        sendNoteButton.setOnClickListener {
            createNoteRequest()
        }
    }

    fun createMoodSpinner(view: View) {
        val spinner: Spinner = view.findViewById(R.id.moodSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        val categories: MutableList<Int> = ArrayList()
        categories.add(1)
        categories.add(2)
        categories.add(3)
        categories.add(4)
        categories.add(5)

        val dataAdapter: ArrayAdapter<Int> = ArrayAdapter<Int>(requireContext(), android.R.layout.simple_spinner_item, categories)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


        spinner.setPrompt("Выберите оценку настроения")
    }

    fun createWeatherSpinner(view: View) {
        val spinner: Spinner = view.findViewById(R.id.weatherSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        val categories: MutableList<Int> = ArrayList()
        categories.add(1)
        categories.add(2)
        categories.add(3)
        categories.add(4)
        categories.add(5)

        val dataAdapter: ArrayAdapter<Int> = ArrayAdapter<Int>(requireContext(), android.R.layout.simple_spinner_item, categories)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        spinner.setPrompt("Выберите оценку погоды")
    }

    private fun createNoteRequest() {

        Log.d("GOOD", "notes list request")
        val queue = Volley.newRequestQueue(activity?.applicationContext)
        val url = "https://saturday-developers-app.herokuapp.com/notes/"


        val mood_rating = moodSpinner.selectedItem.toString().toInt()
        val weather_rating = weatherSpinner.selectedItem.toString().toInt()
        val description = descriptionText.text
        val jsonObject = JSONObject()
        val date_time = SimpleDateFormat("yyyy-MM-dd").format(Date()) + "T" + SimpleDateFormat("hh:mm:ss").format(Date())

        jsonObject.put("date_time", date_time)
        jsonObject.put("weather_rating", weather_rating)
        jsonObject.put("mood_rating", mood_rating)
        jsonObject.put("description", description)

        Log.d("test", mood_rating.toString())

        val request = JsonObjectRequest(
            Request.Method.POST, url,
            jsonObject, Response.Listener { response ->
                Log.d("GOOD", "Note created")
            },
            Response.ErrorListener { error ->
                val errorResponse = "Error while sending request to ${url}: ${error.message}"
                Log.d("BAD", "Can not create node")
            })
        queue.add(request)
    }




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddNoteFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddNoteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}