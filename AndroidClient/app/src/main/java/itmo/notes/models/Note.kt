package itmo.notes.models

import java.util.*

public class Note {
    var id: Int = 0
    lateinit var date_time: Date
    var weather_rating: Int = 0
    var mood_rating: Int = 0
    var description: String = ""
}