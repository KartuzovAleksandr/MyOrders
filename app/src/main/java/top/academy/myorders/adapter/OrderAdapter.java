package top.academy.myorders.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import top.academy.myorders.databinding.ItemOrderBinding;
import top.academy.myorders.model.Client;
import top.academy.myorders.model.Order;
import top.academy.myorders.model.Product;
import top.academy.myorders.viewmodel.OrderViewModel;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private Context context;
    private List<Order> orders;
    private final OrderViewModel viewModel;

    public OrderAdapter(Context context, OrderViewModel viewModel) {
        this.context = context;
        this.viewModel = viewModel;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderBinding binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new OrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.bind(orders.get(position));
    }

    @Override
    public int getItemCount() {
        return orders == null ? 0 : orders.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        private final ItemOrderBinding binding;

        public OrderViewHolder(@NonNull ItemOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Order order) {
            binding.textOrderId.setText("Заказ #" + order.getId());

            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy, HH:mm", Locale.getDefault());
            binding.textOrderDate.setText(sdf.format(order.getDatetime()));

            // 👇 Загружаем связанные данные в фоновом потоке
            new Thread(() -> {
                Client client = viewModel.getClientById(order.getClientId());
                Product product = viewModel.getProductById(order.getProductId());

                String clientName = client != null ? client.getName() : "—";
                String productName = product != null ? product.getName() : "—";
                double total = product != null ? product.getPrice() * order.getQuantity() : 0.0;

                String formattedTotal = NumberFormat.getCurrencyInstance(Locale.US).format(total);

                // 👇 Обновляем UI в основном потоке
                binding.getRoot().post(() -> {
                    binding.textOrderClientName.setText(clientName);
                    binding.textOrderProductName.setText(productName);
                    binding.textOrderTotal.setText("Сумма: " + formattedTotal);
                });
            }).start();

            // Удалить
            binding.buttonDeleteOrder.setOnClickListener(v -> {
                viewModel.deleteOrderById(order.getId());
                Toast.makeText(context, "Заказ #" + order.getId() + " удален", Toast.LENGTH_SHORT).show();
            });

            // Редактировать (заглушка)
            binding.buttonEditOrder.setOnClickListener(v -> {
                Toast.makeText(context, "Редактирование заказа #" + order.getId(), Toast.LENGTH_SHORT).show();
            });
        }
    }
}