package com.cy.rashikabiscuit;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrderHistryActivity extends AppCompatActivity {
    private RecyclerView orderRecyclerView;
    private OrderHistoryAdapter orderHistoryAdapter;
    private List<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_histry);

        orderRecyclerView = findViewById(R.id.order_history_recycler_view);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderList = new ArrayList<>();
        orderHistoryAdapter = new OrderHistoryAdapter(orderList);
        orderRecyclerView.setAdapter(orderHistoryAdapter);

        fetchOrderHistory();
    }

    private void fetchOrderHistory() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();

        db.collection("orders").document(userId).collection("userOrders")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Order order = document.toObject(Order.class);
                            orderList.add(order);
                        }
                        orderHistoryAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(OrderHistryActivity.this, "Failed to fetch order history", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}