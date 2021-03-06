package itmo.notes.ui.login

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.navigation.Navigation
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import itmo.notes.R
import itmo.notes.models.Note
import itmo.notes.ui.home.NoteAdapter
import kotlinx.android.synthetic.main.fragment_home.*


class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val login = findViewById<Button>(R.id.login)
        val loading = findViewById<ProgressBar>(R.id.loading)

        loginViewModel = ViewModelProviders.of(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                setResult(Activity.RESULT_OK)
                finish()

            }
        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginRequest(username.text.toString(), password.text.toString())
//---                        loginViewModel.login(
//                            username.text.toString(),
//                            password.text.toString()
//                        )

                }
                false
            }

            login.setOnClickListener {
//---                loading.visibility = View.VISIBLE
//---                loginViewModel.login(username.text.toString(), password.text.toString())


                loginRequest(username.text.toString(), password.text.toString())
            }
        }
    }

    private fun loginRequest(username: String, password: String) {
        val loading = findViewById<ProgressBar>(R.id.loading)

        loading.visibility = View.VISIBLE
        Log.d("GOOD", "notes list request")
        val queue = Volley.newRequestQueue(applicationContext)
        val url = "https://saturday-developers-app.herokuapp.com/notes/"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
//                        val listType = object : TypeToken<ArrayList<Note>>() { }.type
//                        val noteList = Gson().fromJson<ArrayList<Note>>(response, listType)
//                        listView?.adapter = NoteAdapter(requireContext(), noteList)
            },
            Response.ErrorListener { error ->
                val errorResponse = "Error while sending request to ${url}: ${error.message}"
                Log.d("BAD", "HERE")
            })
        queue.add(stringRequest)

        loading.visibility = View.INVISIBLE
    }


    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
