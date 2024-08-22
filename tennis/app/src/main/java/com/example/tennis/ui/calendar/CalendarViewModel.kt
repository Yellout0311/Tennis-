import android.graphics.Color
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

data class Reservation(
    val date: Date,
    val hour: Int,
    val minute: Int,
    val courtNumber: Int,
    val option: String,
    val color: Int
)

class CalendarViewModel : ViewModel() {
    private val reservations = mutableMapOf<String, Reservation>()

    fun addReservation(date: Date, hour: Int, minute: Int, courtNumber: Int, option: String) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateKey = dateFormat.format(date)
        val color = if (option == "예매일") Color.RED else Color.BLUE
        val reservation = Reservation(date, hour, minute, courtNumber, option, color)
        reservations[dateKey] = reservation
    }

    fun getReservation(date: String): Reservation? {
        return reservations[date]
    }

    fun getAllReservations(): Map<String, Reservation> {
        return reservations.toMap()
    }
}