package top.academy.myorders.db;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import top.academy.myorders.model.Product;
import java.util.List;

@Dao
public interface ProductDao {
    @Insert
    void insert(Product product);

    @Query("SELECT * FROM products")
    LiveData<List<Product>> getAll();

    @Query("SELECT * FROM products")
    List<Product> getAllCheck();

    @Query("SELECT * FROM products WHERE id = :id")
    LiveData<Product> findById(int id);
}