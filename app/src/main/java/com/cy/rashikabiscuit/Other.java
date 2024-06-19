package com.cy.rashikabiscuit;

public class Other {

    private String imageUrl;
    private String name;
    private Price price;

    public Other() {
        // Default constructor required for Firestore
    }

    public Other(String imageUrl, String name, Price price) {
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
        private int _250g;
        private int _1kg;

        public Price() {
            // Default constructor required for Firestore
        }

        public Price(int _100g, int _250g, int _1kg) {
            this._100g = _100g;
            this._250g = _250g;
            this._1kg = _1kg;
        }

        // Getters and setters
        public int get_100g() {
            return _100g;
        }

        public void set_100g(int _100g) {
            this._100g = _100g;
        }

        public int get_250g() {
            return _250g;
        }

        public void set_250g(int _250g) {
            this._250g = _250g;
        }

        public int get_1kg() {
            return _1kg;
        }

        public void set_1kg(int _1kg) {
            this._1kg = _1kg;
        }
    }
}
