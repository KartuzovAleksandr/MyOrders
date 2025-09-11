package top.academy.myorders.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;
import top.academy.myorders.db.AppDatabase;
import top.academy.myorders.model.Client;

public class ClientViewModel extends AndroidViewModel {

    private final LiveData<List<Client>> allClients;

    public ClientViewModel(@NonNull Application application) {
        super(application);
        allClients = AppDatabase.getDatabase(application).clientDao().getAll();
    }

    public LiveData<List<Client>> getAllClients() {
        return allClients;
    }
}
