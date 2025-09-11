package top.academy.myorders.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = "",
    val model: String = "",
    val image: String = "",
    val description: String = "",
    val price: Double = 0.0
)