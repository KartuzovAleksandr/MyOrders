package top.academy.myorders.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import top.academy.myorders.databinding.ItemOrderBinding;
import top.academy.myorders.model.Order;
import top.academy.myorders.viewmodel.OrderViewModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private Context context;
    private List<Order> orders;

    public OrderAdapter(Context context) {
        this.context = context;
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
            binding.textOrderQuantity.setText("Кол-во: " + order.getQuantity());

            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
            binding.textOrderDate.setText("Дата: " + sdf.format(order.getDatetime()));

            binding.textOrderClient.setText("Клиент ID: " + order.getClientId());
            binding.textOrderProduct.setText("Товар ID: " + order.getProductId());

            // Удалить
            binding.buttonDeleteOrder.setOnClickListener(v -> {
                OrderViewModel viewModel = new ViewModelProvider((androidx.fragment.app.FragmentActivity) context).get(OrderViewModel.class);
                viewModel.deleteOrderById(order.getId());
                Toast.makeText(context, "Заказ #" + order.getId() + " удален", Toast.LENGTH_SHORT).show();
            });

            // Редактировать (пока заглушка)
            binding.buttonEditOrder.setOnClickListener(v -> {
                Toast.makeText(context, "Редактирование заказа #" + order.getId(), Toast.LENGTH_SHORT).show();
                // Здесь можно открыть диалог или фрагмент редактирования
            });
        }
    }
}