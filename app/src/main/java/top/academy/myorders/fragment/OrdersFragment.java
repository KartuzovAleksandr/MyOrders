package top.academy.myorders.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import top.academy.myorders.R;
import top.academy.myorders.adapter.OrderAdapter;
import top.academy.myorders.viewmodel.OrderViewModel;

public class OrdersFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        OrderAdapter adapter = new OrderAdapter(requireContext());
        recyclerView.setAdapter(adapter);

        OrderViewModel viewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        viewModel.getAllOrders().observe(getViewLifecycleOwner(), orders -> {
            if (orders != null) {
                adapter.setOrders(orders);
            } else {
                // Данные не заполнены
            }
        });

        return view;
    }
}