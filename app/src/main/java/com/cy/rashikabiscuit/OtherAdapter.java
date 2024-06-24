package com.cy.rashikabiscuit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class OtherAdapter extends RecyclerView.Adapter<OtherAdapter.OtherViewHolder> {
    private Context mContext;
    private List<Other> mOtherProductList;

    public OtherAdapter(Context context, List<Other> otherProductList) {
        mContext = context;
        mOtherProductList = otherProductList;
    }

    @NonNull
    @Override
    public OtherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_other_menu_card, parent, false);
        return new OtherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OtherViewHolder holder, int position) {
        Other otherProduct = mOtherProductList.get(position);

        holder.otherProductName.setText(otherProduct.getName());

        // Load image using Glide into otherProductImage
        Glide.with(mContext)
                .load(otherProduct.getImageUrl())
                .apply(RequestOptions.placeholderOf(R.drawable.loading_icon)) // Placeholder image
                .into(holder.otherProductImage); // Use holder.otherProductImage to reference the ImageView

        // Set up RadioGroup with listener
        holder.otherProductRadioGroup.setOnCheckedChangeListener(null); // Clear existing listener
        holder.otherProductRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.other_radioButton_100g) {
                holder.otherProductPrice.setText("Rs " + otherProduct.getPrice().get_100g() + "/-");
            } else if (checkedId == R.id.other_radioButton_250g) {
                holder.otherProductPrice.setText("Rs " + otherProduct.getPrice().get_250g() + "/-");
            } else if (checkedId == R.id.other_radioButton_1Kg) {
                holder.otherProductPrice.setText("Rs " + otherProduct.getPrice().get_1kg() + "/-");
            }
        });

        // Set up plus and minus button listeners
        holder.plusButton.setOnClickListener(v -> {
            int quantity = Integer.parseInt(holder.quantityText.getText().toString());
            quantity++;
            holder.quantityText.setText(String.valueOf(quantity));
            updateQuantityPrice(holder);
        });
        holder.minusButton.setOnClickListener(v -> {
            int quantity = Integer.parseInt(holder.quantityText.getText().toString());
            if (quantity > 1) {
                quantity--;
                holder.quantityText.setText(String.valueOf(quantity));
                updateQuantityPrice(holder);
            }
        });

        holder.addToCartButton.setOnClickListener(v -> {
            // Check if a radio button is selected
            int selectedRadioButtonId = holder.otherProductRadioGroup.getCheckedRadioButtonId();
            if (selectedRadioButtonId == -1) {
                Toast.makeText(mContext, "Please select a pack size", Toast.LENGTH_SHORT).show();
                return;
            }

            // Retrieve the price based on the selected radio button
            int price;
            if (selectedRadioButtonId == R.id.other_radioButton_100g) {
                price = otherProduct.getPrice().get_100g();
            } else if (selectedRadioButtonId == R.id.other_radioButton_250g) {
                price = otherProduct.getPrice().get_250g();
            } else {
                price = otherProduct.getPrice().get_1kg();
            }

            // Get the quantity, default to 1 if the quantity text is empty or invalid
            String quantityText = holder.quantityText.getText().toString();
            int quantity;
            if (quantityText.isEmpty()) {
                quantity = 1;
            } else {
                try {
                    quantity = Integer.parseInt(quantityText);
                } catch (NumberFormatException e) {
                    quantity = 1; // Default to 1 if parsing fails
                }
            }

            // Validate quantity is greater than 0
            if (quantity <= 0) {
                Toast.makeText(mContext, "Please enter a valid quantity", Toast.LENGTH_SHORT).show();
                return;
            }

            // Calculate total price
            int totalPrice = price * quantity;

            
            CartItem cartItem = new CartItem(otherProduct.getName(), quantity, totalPrice);
            CartManager.getInstance().addItem(cartItem);

            // Show success message
            Toast.makeText(mContext, otherProduct.getName() + " added to cart", Toast.LENGTH_SHORT).show();
        });



    }

    @Override
    public int getItemCount() {
        return mOtherProductList.size();
    }

    public static class OtherViewHolder extends RecyclerView.ViewHolder {
        TextView otherProductName, otherProductPrice, quantityText, quantityPrice;
        RadioGroup otherProductRadioGroup;
        RadioButton radioButton100g, radioButton250g, radioButton1kg;
        ImageView otherProductImage;

        Button addToCartButton;

        ImageView plusButton, minusButton;

        public OtherViewHolder(@NonNull View itemView) {
            super(itemView);
            otherProductName = itemView.findViewById(R.id.other_heading);
            otherProductPrice = itemView.findViewById(R.id.other_price);
            otherProductRadioGroup = itemView.findViewById(R.id.other_radio_grp);
            radioButton100g = itemView.findViewById(R.id.other_radioButton_100g);
            radioButton250g = itemView.findViewById(R.id.other_radioButton_250g);
            radioButton1kg = itemView.findViewById(R.id.other_radioButton_1Kg);
            otherProductImage = itemView.findViewById(R.id.other_img);
           addToCartButton = itemView.findViewById(R.id.add_to_cart_btn);
            plusButton = itemView.findViewById(R.id.plus_button);
            minusButton = itemView.findViewById(R.id.minus_button);
            quantityText = itemView.findViewById(R.id.quantity_text);
            quantityPrice = itemView.findViewById(R.id.quantity_price);
        }
    }

    private void updateQuantityPrice(OtherViewHolder holder) {
        int quantity = Integer.parseInt(holder.quantityText.getText().toString());
        String priceText = holder.otherProductPrice.getText().toString();
        int price = Integer.parseInt(priceText.replaceAll("[^0-9]", ""));
        int totalPrice = price * quantity;
        holder.quantityPrice.setText("Rs " + totalPrice + "/-");
    }
}