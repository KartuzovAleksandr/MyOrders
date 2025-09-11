package top.academy.myorders.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import top.academy.myorders.db.AppDatabase;
import top.academy.myorders.db.ClientDao;
import top.academy.myorders.db.OrderDao;
import top.academy.myorders.db.ProductDao;
import top.academy.myorders.model.Client;
import top.academy.myorders.model.Order;
import top.academy.myorders.model.Product;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppRepository {
    private final AppDatabase database;
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    private final ClientDao clientDao;
    private final ProductDao productDao;
    private final OrderDao orderDao;

    public AppRepository(Application application) {
        database = AppDatabase.getDatabase(application);
        clientDao = database.clientDao();
        productDao = database.productDao();
        orderDao = database.orderDao();
    }

    // Clients
    public LiveData<List<Client>> getAllClients() { return clientDao.getAll(); }
    public LiveData<Client> getClientById(int id) { return clientDao.findById(id); }
    public void save(Client client) { executor.execute(() -> clientDao.insert(client)); }
    public void delete(Client client) { executor.execute(() -> clientDao.delete(client)); }

    // Products
    public LiveData<List<Product>> getAllProducts() { return productDao.getAll(); }
    public LiveData<Product> getProductById(int id) { return productDao.findById(id); }
    public void save(Product product) { executor.execute(() -> productDao.insert(product)); }

    // Orders
    public LiveData<List<Order>> getAllOrders() { return orderDao.getAll(); }
    public LiveData<Order> getOrderById(int id) { return orderDao.findById(id); }
    public void save(Order order) { executor.execute(() -> orderDao.insert(order)); }
    public void delete(Order order) { executor.execute(() -> orderDao.delete(order)); }
    public void deleteOrderById(int id) {
        executor.execute(() -> {
            Order order = orderDao.findByIdSync(id);
            if (order != null) orderDao.delete(order);
        });
    }

    // 👇 Синхронные методы для использования в фоновых потоках (например, в адаптере)

    public Client getClientByIdSync(int id) {
        try {
            return executor.submit(() -> clientDao.findByIdSync(id)).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Product getProductByIdSync(int id) {
        try {
            return executor.submit(() -> productDao.findByIdSync(id)).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}