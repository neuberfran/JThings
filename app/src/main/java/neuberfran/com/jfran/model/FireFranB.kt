package neuberfran.com.jfran.model

class FireFranB {
    var value: FireFranBValue = FireFranBValue(false, 0)
}
data class FireFranBValue(
    val on: Boolean,
    val openPercent: Number
)


//data class FireFranB(
//
//    var id: String? = null,
//    var userId: String? = null,
//    var alarmstate: Boolean  = false,
//    var garagestate: Boolean = false,
//    val value.on : Boolean  = false,
//    val value.openPercent: Number  = 0
//)