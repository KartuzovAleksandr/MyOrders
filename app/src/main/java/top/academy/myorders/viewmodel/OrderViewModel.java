package top.academy.myorders.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import top.academy.myorders.model.Client;
import top.academy.myorders.model.Order;
import top.academy.myorders.model.Product;
import top.academy.myorders.repository.AppRepository;
import java.util.List;

public class OrderViewModel extends AndroidViewModel {
    private final AppRepository repository;

    public OrderViewModel(Application application) {
        super(application);
        repository = new AppRepository(application);
    }
    public LiveData<List<Order>> getAllOrders() {
        return repository.getAllOrders();
    }
    public void save(Order order) {
        repository.save(order);
    }
    public void delete(Order order) {
        repository.delete(order);
    }
    public void deleteOrderById(int id) {
        repository.deleteOrderById(id);
    }
    // 👇 Методы для адаптера — через репозиторий, синхронные (вызываются в фоне)
    public Client getClientById(int id) {
        return repository.getClientByIdSync(id);
    }

    public Product getProductById(int id) {
        return repository.getProductByIdSync(id);
    }
}