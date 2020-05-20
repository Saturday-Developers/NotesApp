package itmo.notes.ui.login

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.annotation.MainThread
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import itmo.notes.R
import itmo.notes.models.Note
import itmo.notes.ui.home.NoteAdapter
import kotlinx.android.synthetic.main.fragment_home.*
//import ru.fofanko.stocktaking.data.Result


//---class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {
class LoginViewModel() : ViewModel() {
    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    @SuppressLint("CheckResult")
    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
//        Single.fromCallable { loginRepository.login(username, password) }
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe { result ->
//                if (result is Result.Success) {
//                    _loginResult.value = LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
//                } else {
//                    print("error")
//                    _loginResult.value = LoginResult(error = R.string.login_failed)
//                }
//            }


//        val queue = Volley.newRequestQueue(activity?.applicationContext)
//        val url = "https://saturday-developers-app.herokuapp.com/notes/"
//        val stringRequest = StringRequest(
//            Request.Method.GET, url,
//            Response.Listener<String> { response ->
//                val listType = object : TypeToken<ArrayList<Note>>() { }.type
//                val noteList = Gson().fromJson<ArrayList<Note>>(response, listType)
//                listView?.adapter = NoteAdapter(requireContext(), noteList)
//            },
//            Response.ErrorListener { error ->
//                val errorResponse = "Error while sending request to ${url}: ${error.message}"
//                Log.d("BAD", "HERE")
//            })
//        queue.add(stringRequest)

    }


    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }



}
