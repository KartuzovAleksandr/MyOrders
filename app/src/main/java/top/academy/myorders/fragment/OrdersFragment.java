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
import top.academy.myorders.databinding.FragmentOrdersBinding;
import top.academy.myorders.viewmodel.OrderViewModel;

public class OrdersFragment extends Fragment {

    private FragmentOrdersBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOrdersBinding.inflate(inflater, container, false);

        RecyclerView recyclerView = binding.recyclerViewOrders;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        OrderViewModel viewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        OrderAdapter adapter = new OrderAdapter(requireContext(), viewModel);
        recyclerView.setAdapter(adapter);

        viewModel.getAllOrders().observe(getViewLifecycleOwner(), orders -> {
            if (orders != null) {
                adapter.setOrders(orders);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}