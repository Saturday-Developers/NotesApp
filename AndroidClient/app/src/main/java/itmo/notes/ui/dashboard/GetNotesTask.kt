package itmo.notes.ui.dashboard

import android.os.AsyncTask
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class GetNotesTask : AsyncTask<Unit, Unit, String>() {

    override fun doInBackground(vararg params: Unit?): String? {
        val url = URL("https://saturday-developers-app.herokuapp.com/notes/")
        val httpClient = url.openConnection() as HttpURLConnection
        if (httpClient.responseCode == HttpURLConnection.HTTP_OK) {
            try {
                val stream = BufferedInputStream(httpClient.inputStream)
                val data: String = readStream(inputStream = stream)
                return data
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                httpClient.disconnect()
            }
        } else {
            println("ERROR ${httpClient.responseCode}")
        }
        return null
    }

    fun readStream(inputStream: BufferedInputStream): String {
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        bufferedReader.forEachLine { stringBuilder.append(it) }
        return stringBuilder.toString()
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
    }
}