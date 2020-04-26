package itmo.notes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import itmo.notes.activities.ChartsActivity
import itmo.notes.activities.TestEmojiActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonViewCharts.text = "Charts"
        buttonViewTestEmoji.text = "Show icons"
    }

    fun viewCharts(view: View) {
        val intent = Intent(this, ChartsActivity::class.java).apply {}
        startActivity(intent)
    }

    fun viewEmoji(view: View) {
        val intent = Intent(this, TestEmojiActivity::class.java).apply {}
        startActivity(intent)
    }
}
