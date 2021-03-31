package neuberfran.com.jfran.model
import neuberfran.com.jfran.viewmodel.FireViewModel

class FireFran(
    var alarmstate: Boolean  = false,
    var garagestate: Boolean = false,
    var id: String? = null,
    var userId: String? = null,
    var owner: String? = null,
    var value: FireFranValue = FireFranValue(false),
    var valorb: FireFranValueB = FireFranValueB(openPercent = 0)

){
    companion object Factory {
        fun create() :FireViewModel = FireViewModel()
        var COLLECTION = "device-configs"
        var DOCUMENT = "alarme"
        var FIELD_userId = "userId"
    }
}

data class FireFranValue(
    var on: Boolean // = false
)

data class FireFranValueB(
    var openPercent: Number // = 0
)