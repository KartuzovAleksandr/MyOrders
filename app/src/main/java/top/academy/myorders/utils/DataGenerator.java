// utils/DataGenerator.java
package top.academy.myorders.utils;

import android.content.Context;
import android.util.Log;

import top.academy.myorders.db.AppDatabase;
import top.academy.myorders.model.Client;
import top.academy.myorders.model.Product;
import com.github.javafaker.Faker;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

public class DataGenerator {

    public static void generateData(Context context, Runnable onFinished) {
        AppDatabase db = AppDatabase.getDatabase(context);
        Faker faker = new Faker(new Locale("ru"));

        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                // Генерируем клиентов
                for (int i = 0; i < 10; i++) {
                    Client client = new Client(
                            0,
                            faker.name().fullName(),
                            faker.address().fullAddress(),
                            faker.internet().emailAddress(),
                            faker.phoneNumber().phoneNumber(),
                            faker.name().username(),
                            faker.internet().password(8, 12)
                    );
                    db.clientDao().insert(client);
                }

                // Генерируем товары
                String[] productNames = {
                        "iPhone 15 Pro", "MacBook Air M2", "Samsung Galaxy S24",
                        "PlayStation 5", "Xbox Series X", "iPad Pro",
                        "Apple Watch Ultra", "AirPods Max", "DJI Mavic 3",
                        "Canon EOS R5", "Sony WH-1000XM5", "GoPro HERO12",
                        "Tesla Powerbank 20000mAh", "Logitech MX Master 3S", "Razer Blade 16"
                };

                String[] models = {
                        "Titanium", "M2 Chip", "Snapdragon 8 Gen 3",
                        "Digital Edition", "1TB", "M2, 512GB",
                        "GPS + Cellular", "Space Gray", "Cine",
                        "RF Mount", "Wireless Noise Cancelling", "Black Edition",
                        "Fast Charge", "Graphite", "RTX 4090"
                };

                String[] descriptions = {
                        "Самый мощный iPhone с титановым корпусом",
                        "Тонкий, легкий и невероятно мощный",
                        "Флагманский смартфон с ИИ-камерой",
                        "Игровая консоль нового поколения",
                        "Мощнейшая консоль от Microsoft",
                        "Профессиональный планшет для творчества",
                        "Часы для экстремалов и спортсменов",
                        "Премиальные наушники с пространственным звуком",
                        "Профессиональный дрон для кино",
                        "Зеркальная камера для профессионалов",
                        "Лучшие в мире беспроводные наушники",
                        "Экшн-камера для любых условий",
                        "Портативный аккумулятор для гаджетов",
                        "Лучшая мышь для продуктивности",
                        "Игровой ноутбук с топовой графикой"
                };

                double[] prices = {
                        1199.99, 1099.99, 999.99,
                        699.99, 599.99, 899.99,
                        799.99, 549.99, 3299.99,
                        3899.99, 399.99, 449.99,
                        129.99, 99.99, 3499.99
                };

                String[] images = {
                        "https://images.unsplash.com/photo-1605236453806-6ff3b6a0a554?w=300&h=300&fit=crop", // iPhone 15 Pro
                        "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=300&h=300&fit=crop", // MacBook Air M2
                        "https://images.unsplash.com/photo-1610945265018-739c5c86f805?w=300&h=300&fit=crop", // Samsung Galaxy S24
                        "https://images.unsplash.com/photo-1627857088035-9986a4f9a8d8?w=300&h=300&fit=crop", // PlayStation 5
                        "https://images.unsplash.com/photo-1627857088040-5b2127c2c5e1?w=300&h=300&fit=crop", // Xbox Series X
                        "https://images.unsplash.com/photo-1544345932-9772e1d2e1f0?w=300&h=300&fit=crop", // iPad Pro
                        "https://images.unsplash.com/photo-1579586337278-3befd40fd17a?w=300&h=300&fit=crop", // Apple Watch Ultra
                        "https://images.unsplash.com/photo-1572569511254-95e888f9f2df?w=300&h=300&fit=crop", // AirPods Max
                        "https://images.unsplash.com/photo-1580133653610-bc48-9d11-300d-559b?w=300&h=300&fit=crop", // DJI Mavic 3
                        "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?w=300&h=300&fit=crop", // Canon EOS R5
                        "https://images.unsplash.com/photo-1589003702956-0e1b29a772c8?w=300&h=300&fit=crop", // Sony WH-1000XM5
                        "https://images.unsplash.com/photo-1607748851687-ba38e5b5a75c?w=300&h=300&fit=crop", // GoPro HERO12
                        "https://images.unsplash.com/photo-1587825164653-7e1a32222027?w=300&h=300&fit=crop", // Tesla Powerbank
                        "https://images.unsplash.com/photo-1527864550186-1b7d9e43e46b?w=300&h=300&fit=crop", // Logitech MX Master 3S
                        "https://images.unsplash.com/photo-1587825164653-7e1a32222027?w=300&h=300&fit=crop"  // Razer Blade 16 (можно заменить позже)
                };

                for (int i = 0; i < productNames.length; i++) {
                    Product product = new Product(
                            0,
                            productNames[i],
                            models[i],
                            images[i].trim(),
                            descriptions[i],
                            prices[i]
                    );
                    db.productDao().insert(product);
                }

            } finally {
                if (onFinished != null) {
                    // Возвращаемся на главный поток для callback
                    ((android.app.Activity) context).runOnUiThread(onFinished);
                }
            }
        });
    }}