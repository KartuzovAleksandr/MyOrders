package top.academy.myorders.db;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import top.academy.myorders.model.Order;
import java.util.List;

@Dao
public interface OrderDao {
    @Insert
    void insert(Order order);

    @Query("SELECT * FROM orders")
    LiveData<List<Order>> getAll();

    @Query("SELECT * FROM orders")
    List<Order> getAllCheck();

    @Query("SELECT * FROM orders WHERE id = :id")
    LiveData<Order> findById(int id);

    @Query("SELECT * FROM orders WHERE id = :id")
    Order findByIdSync(int id);

    @Delete
    void delete(Order order);
}