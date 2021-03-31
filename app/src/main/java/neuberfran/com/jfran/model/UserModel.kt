package neuberfran.com.jfran.model

class UserModel {
    var userEmail: String? = null
        private set
    var userName: String? = null
        private set
    var admin: Boolean? = null
        private set

    constructor() {}
    constructor(userEmail: String?, userName: String?, admin: Boolean?) {
        this.userEmail = userEmail
        this.userName = userName
        this.admin = admin
    }

}