package top.academy.myorders.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;
import top.academy.myorders.R;
import top.academy.myorders.adapter.ImageCarouselAdapter;
import top.academy.myorders.databinding.FragmentProductDetailBinding;
import top.academy.myorders.model.Client;
import top.academy.myorders.model.Product;
import top.academy.myorders.viewmodel.ClientViewModel;
import top.academy.myorders.viewmodel.ProductViewModel;
import top.academy.myorders.viewmodel.OrderViewModel;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProductDetailFragment extends Fragment {

    private FragmentProductDetailBinding binding;
    private int productId;
    private Product product;
    private Client selectedClient;
    private List<Client> clientList = new ArrayList<>();

    public static ProductDetailFragment newInstance(int productId) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putInt("productId", productId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productId = getArguments().getInt("productId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false);
        setupViews();
        loadProduct();
        loadClients();
        return binding.getRoot();
    }

    private void setupViews() {
        binding.editTextQuantity.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { updateTotal(); }
            @Override public void afterTextChanged(android.text.Editable s) {}
        });

        binding.buttonPlaceOrder.setOnClickListener(v -> placeOrder());
    }

    private void loadProduct() {
        ProductViewModel viewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        viewModel.getProductById(productId).observe(getViewLifecycleOwner(), p -> {
            if (p != null) {
                product = p;
                binding.textProductName.setText(product.getName());
                binding.textProductModel.setText(product.getModel());
                binding.textProductDescription.setText(product.getDescription());
                binding.textProductPrice.setText(NumberFormat.getCurrencyInstance(Locale.US).format(product.getPrice()));
                binding.viewPagerImages.setAdapter(new ImageCarouselAdapter(product.getImage()));
                updateTotal();
            } else {
                Toast.makeText(getContext(), "Продукт не найден", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadClients() {
        ClientViewModel clientViewModel = new ViewModelProvider(this).get(ClientViewModel.class);
        clientViewModel.getAllClients().observe(getViewLifecycleOwner(), clients -> {
            if (clients != null && !clients.isEmpty()) {
                clientList.clear();
                clientList.addAll(clients);

                List<String> clientNames = new ArrayList<>();
                for (Client client : clients) {
                    clientNames.add(client.getName()); // Можно: client.getName() + " (" + client.getEmail() + ")"
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        requireContext(),
                        R.layout.dropdown_menu_popup_item, // Material 3 стиль
                        clientNames
                );

                AutoCompleteTextView autoCompleteTextView = binding.spinnerClient;
                autoCompleteTextView.setAdapter(adapter);

                autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
                    selectedClient = clientList.get(position);
                });

                // По умолчанию выбираем первого клиента
                if (!clientList.isEmpty()) {
                    selectedClient = clientList.get(0);
                    autoCompleteTextView.setText(clientNames.get(0), false);
                }
            } else {
                Toast.makeText(getContext(), "Нет клиентов. Заполните данные.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void updateTotal() {
        if (product == null) return;
        try {
            int qty = Integer.parseInt(binding.editTextQuantity.getText().toString());
            double total = qty * product.getPrice();
            binding.textTotalPrice.setText("Итого: " + NumberFormat.getCurrencyInstance(Locale.US).format(total));
        } catch (NumberFormatException e) {
            binding.textTotalPrice.setText("Итого: $0.00");
        }
    }

    private void placeOrder() {
        if (selectedClient == null) {
            Toast.makeText(getContext(), "Выберите клиента", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int qty = Integer.parseInt(binding.editTextQuantity.getText().toString());
            if (qty <= 0) throw new NumberFormatException();

            OrderViewModel orderViewModel = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);
            top.academy.myorders.model.Order order = new top.academy.myorders.model.Order(
                    0,
                    selectedClient.getId(),
                    productId,
                    new Date(),
                    qty
            );
            orderViewModel.save(order);

            Toast.makeText(getContext(), "Заказ сохранён!", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();

        } catch (Exception e) {
            Toast.makeText(getContext(), "Введите корректное количество", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}