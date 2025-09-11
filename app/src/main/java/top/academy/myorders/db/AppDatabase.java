// db/AppDatabase.java
package top.academy.myorders.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import top.academy.myorders.model.Client;
import top.academy.myorders.model.Order;
import top.academy.myorders.model.Product;
import top.academy.myorders.utils.DateConverter;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
        entities = {Client.class, Product.class, Order.class},
        version = 1,
        exportSchema = false
)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract ClientDao clientDao();
    public abstract ProductDao productDao();
    public abstract OrderDao orderDao();

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "Orders.db")
                            .allowMainThreadQueries() // Только для демо!
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // 👇 Возвращает File объект базы данных — для экспорта
    public static File getDatabasePath(Context context) {
        return context.getDatabasePath("Orders.db");
    }

    // 👇 Удаляет текущую БД и сбрасывает инстанс
    public static void recreateDatabase(Context context) {
        if (INSTANCE != null) {
            INSTANCE.close();
            INSTANCE = null;
        }
        context.deleteDatabase("Orders.db");
    }
}