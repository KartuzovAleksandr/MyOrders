package top.academy.myorders.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import com.google.gson.Gson;
import top.academy.myorders.R;
import top.academy.myorders.db.AppDatabase;
import top.academy.myorders.model.Order;
import top.academy.myorders.utils.DataGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class AboutFragment extends Fragment {

    private ActivityResultLauncher<Intent> exportDbLauncher;
    private ActivityResultLauncher<Intent> exportJsonLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        exportDbLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        if (uri != null) {
                            exportFile("Orders.db", uri, true);
                        }
                    }
                });

        exportJsonLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        if (uri != null) {
                            exportFile("Orders.json", uri, false);
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        view.findViewById(R.id.buttonRegenerate).setOnClickListener(v -> regenerateData());
        view.findViewById(R.id.buttonExport).setOnClickListener(v -> exportDatabase());

        return view;
    }

    private void regenerateData() {
        AppDatabase.recreateDatabase(requireContext());
        DataGenerator.generateData(requireContext(), () ->
                Toast.makeText(getContext(), "✅ Данные пересозданы!", Toast.LENGTH_SHORT).show()
        );
    }

    private void exportDatabase() {
        // 👇 Экспорт Orders.db — с подсказками для выбора Downloads
        Intent dbIntent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        dbIntent.addCategory(Intent.CATEGORY_OPENABLE);
        dbIntent.setType("*/*");
        dbIntent.putExtra(Intent.EXTRA_TITLE, "Orders.db");

        // ✅ Добавляем подсказки для файлового менеджера (работает не везде, но где работает — помогает)
        dbIntent.putExtra("android.content.extra.SHOW_ADVANCED", true);
        dbIntent.putExtra("android.content.extra.FANCY", true);

        exportDbLauncher.launch(dbIntent);
    }

    private void exportFile(String filename, Uri uri, boolean isDb) {
        new Thread(() -> {
            try {
                if (isDb) {
                    File dbFile = AppDatabase.getDatabasePath(requireContext());
                    if (!dbFile.exists()) {
                        showToast("❌ База данных не найдена");
                        return;
                    }
                    copyFileToUri(dbFile, uri);
                } else {
                    List<Order> orders = AppDatabase.getDatabase(requireContext()).orderDao().getAllCheck();
                    String json = new Gson().toJson(orders);
                    copyStringToUri(json, uri);
                }

                showToast("✅ " + filename + " экспортирован!");

                // Если это был DB — теперь запускаем JSON
                if (isDb) {
                    Intent jsonIntent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                    jsonIntent.addCategory(Intent.CATEGORY_OPENABLE);
                    jsonIntent.setType("application/json");
                    jsonIntent.putExtra(Intent.EXTRA_TITLE, "Orders.json");

                    // ✅ Тоже добавляем подсказки
                    jsonIntent.putExtra("android.content.extra.SHOW_ADVANCED", true);
                    jsonIntent.putExtra("android.content.extra.FANCY", true);

                    exportJsonLauncher.launch(jsonIntent);
                }

            } catch (Exception e) {
                Log.e("Export", "Ошибка при экспорте " + filename, e);
                showToast("❌ Ошибка: " + e.getMessage());
            }
        }).start();
    }

    private void copyFileToUri(File src, Uri destUri) throws Exception {
        try (FileInputStream in = new FileInputStream(src);
             OutputStream out = requireContext().getContentResolver().openOutputStream(destUri)) {
            if (out == null) throw new Exception("Не удалось открыть поток для записи");
            byte[] buffer = new byte[4096];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        }
    }

    private void copyStringToUri(String content, Uri destUri) throws Exception {
        try (OutputStream out = requireContext().getContentResolver().openOutputStream(destUri)) {
            if (out == null) throw new Exception("Не удалось открыть поток для записи");
            out.write(content.getBytes(StandardCharsets.UTF_8));
        }
    }

    private void showToast(String message) {
        requireActivity().runOnUiThread(() ->
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show());
    }
}