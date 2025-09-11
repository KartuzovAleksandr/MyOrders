package top.academy.myorders.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val clientId: Int = 0,
    val productId: Int = 0,
    val datetime: Date = Date(),
    val quantity: Int = 0
)