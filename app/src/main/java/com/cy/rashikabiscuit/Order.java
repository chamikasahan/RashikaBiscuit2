package com.cy.rashikabiscuit;

import com.google.firebase.Timestamp;

import java.util.List;
import java.util.Map;

public class Order {
    private String userId;
    private List<Map<String, Object>> items;
    private Timestamp timestamp; // Ensure this is com.google.firebase.Timestamp
    private String status;

    public Order() {
        // Default constructor required for calls to DataSnapshot.getValue(Order.class)
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Map<String, Object>> getItems() {
        return items;
    }

    public void setItems(List<Map<String, Object>> items) {
        this.items = items;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}