package com.cy.rashikabiscuit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder> {

    private List<Order> orderList;

    public OrderHistoryAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView orderItemsTextView;
        TextView orderDateTextView;
        TextView orderStatusTextView;

        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            orderItemsTextView = itemView.findViewById(R.id.order_items);
            orderDateTextView = itemView.findViewById(R.id.order_date);
            orderStatusTextView = itemView.findViewById(R.id.order_status);
        }

        public void bind(Order order) {
            StringBuilder items = new StringBuilder();
            for (Map<String, Object> itemData : order.getItems()) {
                items.append(itemData.get("name")).append(" x ").append(itemData.get("quantity")).append(" - Rs ").append(itemData.get("price")).append("\n");
            }
            orderItemsTextView.setText(items.toString());

            // Assuming getTimestamp() returns a Firebase Timestamp
            Timestamp timestamp = order.getTimestamp();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
            orderDateTextView.setText(sdf.format(timestamp.toDate()));

            orderStatusTextView.setText(order.getStatus());
        }
    }
}