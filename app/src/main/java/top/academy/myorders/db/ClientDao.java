package top.academy.myorders.db;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import top.academy.myorders.model.Client;
import java.util.List;

@Dao
public interface ClientDao {
    @Insert
    void insert(Client client);

    @Query("SELECT * FROM clients")
    LiveData<List<Client>> getAll();

    @Query("SELECT * FROM clients")
    List<Client> getAllCheck(); // ← для фоновой логики (проверок, генерации)

    @Query("SELECT * FROM clients WHERE id = :id")
    LiveData<Client> findById(int id);

    @Query("SELECT * FROM clients WHERE id = :id")
    Client findByIdSync(int id); // 👈 синхронный метод

    @Delete
    void delete(Client client);
}