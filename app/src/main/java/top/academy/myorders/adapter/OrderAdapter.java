package top.academy.myorders.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import top.academy.myorders.R;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
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
        TextView orderId, client, product, quantity, date;
        Button deleteButton;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.textOrderId);
            client = itemView.findViewById(R.id.textOrderClient);
            product = itemView.findViewById(R.id.textOrderProduct);
            quantity = itemView.findViewById(R.id.textOrderQuantity);
            date = itemView.findViewById(R.id.textOrderDate);
            deleteButton = itemView.findViewById(R.id.buttonDeleteOrder);
        }

        void bind(Order order) {
            orderId.setText("Заказ #" + order.getId());
            quantity.setText("Кол-во: " + order.getQuantity());

            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
            date.setText("Дата: " + sdf.format(order.getDatetime()));

            client.setText("Клиент ID: " + order.getClientId());
            product.setText("Товар ID: " + order.getProductId());

            deleteButton.setOnClickListener(v -> {
                OrderViewModel viewModel = new ViewModelProvider((androidx.fragment.app.FragmentActivity) context).get(OrderViewModel.class);
                viewModel.deleteOrderById(order.getId());
                Toast.makeText(context, "Заказ удален", Toast.LENGTH_SHORT).show();
            });
        }
    }
}