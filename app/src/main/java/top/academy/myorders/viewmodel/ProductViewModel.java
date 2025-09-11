package top.academy.myorders.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import top.academy.myorders.model.Product;
import top.academy.myorders.repository.AppRepository;
import java.util.List;

public class ProductViewModel extends AndroidViewModel {
    private final AppRepository repository;

    public ProductViewModel(Application application) {
        super(application);
        repository = new AppRepository(application);
    }

    public LiveData<List<Product>> getAllProducts() {
        return repository.getAllProducts();
    }

    public LiveData<Product> getProductById(int id) {
        return repository.getProductById(id);
    }

    public void save(Product product) {
        repository.save(product);
    }
}