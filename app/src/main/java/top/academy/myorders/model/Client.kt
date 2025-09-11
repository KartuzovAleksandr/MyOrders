package top.academy.myorders.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clients")
data class Client(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = "",
    val address: String = "",
    val email: String = "",
    val phone: String = "",
    val login: String = "",
    val password: String = ""
)