// MainActivity.java
package top.academy.myorders;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import top.academy.myorders.db.AppDatabase;
import top.academy.myorders.fragment.AboutFragment;
import top.academy.myorders.fragment.OrdersFragment;
import top.academy.myorders.fragment.ProductsFragment;
import top.academy.myorders.utils.DataGenerator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            if (item.getItemId() == R.id.nav_about) {
                fragment = new AboutFragment();
            } else if (item.getItemId() == R.id.nav_products) {
                fragment = new ProductsFragment();
            } else if (item.getItemId() == R.id.nav_orders) {
                fragment = new OrdersFragment();
            }
            if (fragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit();
            }
            return true;
        });

        // Запуск первой вкладки
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AboutFragment())
                    .commit();
        }

        // ✅ Проверка: если БД пустая (или не существует) — генерируем данные
        checkAndGenerateData();
    }

    private void checkAndGenerateData() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            boolean hasClients = !db.clientDao().getAllCheck().isEmpty();
            boolean hasProducts = !db.productDao().getAllCheck().isEmpty();

            if (!hasClients || !hasProducts) {
                // Пересоздаём БД и генерируем данные
                AppDatabase.recreateDatabase(this);
                DataGenerator.generateData(this, () -> {
                    // Можно показать тост или лог
                    runOnUiThread(() -> {
                        // Уведомление
                        Toast.makeText(this, "База данных успешно создана", Toast.LENGTH_LONG).show();
                    });
                });
            }
        });
    }
}