package com.cy.rashikabiscuit;

import java.util.Map;

public class Snack {
    private String imageUrl;
    private String name;
    private Price price;

    public Snack() {
        // Default constructor required for Firestore
    }

    public Snack(String imageUrl, String name, Price price) {
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
        private int _6_packs;
        private int _12_packs;
        private int _24_packs;

        public Price() {
            // Default constructor required for Firestore
        }

        public Price(int _6_packs, int _12_packs, int _24_packs) {
            this._6_packs = _6_packs;
            this._12_packs = _12_packs;
            this._24_packs = _24_packs;
        }

        // Getters and setters
        public int get_6_packs() {
            return _6_packs;
        }

        public void set_6_packs(int _6_packs) {
            this._6_packs = _6_packs;
        }

        public int get_12_packs() {
            return _12_packs;
        }

        public void set_12_packs(int _12_packs) {
            this._12_packs = _12_packs;
        }

        public int get_24_packs() {
            return _24_packs;
        }

        public void set_24_packs(int _24_packs) {
            this._24_packs = _24_packs;
        }
    }
}
