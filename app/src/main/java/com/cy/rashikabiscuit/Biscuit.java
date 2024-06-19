package com.cy.rashikabiscuit;

import java.util.Map;

public class Biscuit {
    private String imageUrl;
    private String name;
    private Price price;

    public Biscuit() {
        // Default constructor required for Firestore
    }

    public Biscuit(String imageUrl, String name, Price price) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.price = price;
    }

    // Getters and setters
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    // Nested class for Price
    public static class Price {
        private int _100g;
        private int _350g;
        private int _500g;

        public Price() {
            // Default constructor required for Firestore
        }

        public Price(int _100g, int _350g, int _500g) {
            this._100g = _100g;
            this._350g = _350g;
            this._500g = _500g;
        }

        // Getters and setters
        public int get_100g() {
            return _100g;
        }

        public void set_100g(int _100g) {
            this._100g = _100g;
        }

        public int get_350g() {
            return _350g;
        }

        public void set_350g(int _350g) {
            this._350g = _350g;
        }

        public int get_500g() {
            return _500g;
        }

        public void set_500g(int _500g) {
            this._500g = _500g;
        }
    }
}