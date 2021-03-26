package neuberfran.com.jfran.model

import neuberfran.com.jfran.viewmodel.FireViewModel

class FireFran {
    var value: FireFranValue = FireFranValue(false, 0)
    companion object Factory {
        fun create() :FireViewModel = FireViewModel()
        var COLLECTION = "device-configs"
        var DOCUMENT = "alarme"
        var FIELD_userId = "userId"
    }


    var alarmstate: Boolean  = false
    var garagestate: Boolean = false
    var id: String? = null
    var userId: String? = null
}
data class FireFranValue(
    val on: Boolean,
    val openPercent: Number
)