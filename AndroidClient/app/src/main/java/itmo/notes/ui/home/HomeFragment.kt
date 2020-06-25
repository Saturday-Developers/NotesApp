package itmo.notes.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import itmo.notes.R
import itmo.notes.models.Note
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
//        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        notesListRequest()
        addNoteButton.setOnClickListener {
            Log.d("hello", "world")
            getActivity()?.getSupportFragmentManager()?.beginTransaction()?.replace(R.id.homeLayout, AddNoteFragment())?.addToBackStack(null)?.commit()
        }

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        notesListRequest()
    }


    private fun notesListRequest() {
        loader.visibility = View.VISIBLE

        Log.d("GOOD", "notes list request")
        val queue = Volley.newRequestQueue(activity?.applicationContext)
        val url = "https://saturday-developers-app.herokuapp.com/notes/"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                val listType = object : TypeToken<ArrayList<Note>>() { }.type
                val utf8String = String(response.toByteArray(charset("ISO-8859-1")), charset("UTF-8"))
                val noteList = Gson().fromJson<ArrayList<Note>>(utf8String, listType)
                listView?.adapter = NoteAdapter(requireContext(), noteList)
            },
            Response.ErrorListener { error ->
                val errorResponse = "Error while sending request to ${url}: ${error.message}"
                Log.d("BAD", "HERE")
            })
        queue.add(stringRequest)

        loader.visibility = View.INVISIBLE
    }
}
