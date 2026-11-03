package models

class UserData(userName: String, userLastName: String, userBirthday: String, userGmail: String) {
    val nombre: String = userName
    val apellidos: String = userLastName
    val fecha_nac: String = userBirthday
    val email: String = userGmail
}