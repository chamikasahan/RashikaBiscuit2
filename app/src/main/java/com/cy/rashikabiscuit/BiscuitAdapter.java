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
import java.util.Map;

public class BiscuitAdapter extends RecyclerView.Adapter<BiscuitAdapter.BiscuitViewHolder> {
    private Context mContext;
    private List<Biscuit> mBiscuitList;



    public BiscuitAdapter(Context context, List<Biscuit> biscuitList) {
        mContext = context;
        mBiscuitList = biscuitList;
    }

    @NonNull
    @Override
    public BiscuitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_card_menu, parent, false);
        return new BiscuitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BiscuitViewHolder holder, int position) {
        Biscuit biscuit = mBiscuitList.get(position);

        holder.biscuitName.setText(biscuit.getName());

        // Load image using Glide into biscuitImage
        Glide.with(mContext)
                .load(biscuit.getImageUrl())
                .apply(RequestOptions.placeholderOf(R.drawable.profile)) // Placeholder image
                .into(holder.biscuitImage); // Use holder.biscuitImage to reference the ImageView

        // Set up RadioGroup with listener
        holder.biscuitRadioGroup.setOnCheckedChangeListener(null); // Clear existing listener
        holder.biscuitRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.biscuit_radioButton_100g) {
                holder.biscuitPrice.setText("Rs " + biscuit.getPrice().get_100g() + "/-");
            } else if (checkedId == R.id.biscuit_radioButton_350g) {
                holder.biscuitPrice.setText("Rs " + biscuit.getPrice().get_350g() + "/-");
            } else if (checkedId == R.id.biscuit_radioButton_500g) {
                holder.biscuitPrice.setText("Rs " + biscuit.getPrice().get_500g() + "/-");
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
            int selectedRadioButtonId = holder.biscuitRadioGroup.getCheckedRadioButtonId();
            if (selectedRadioButtonId == -1) {
                Toast.makeText(mContext, "Please select a packs", Toast.LENGTH_SHORT).show();
                return;
            }

            // Retrieve the price based on the selected radio button
            int price;
            if (selectedRadioButtonId == R.id.biscuit_radioButton_100g) {
                price = biscuit.getPrice().get_100g();
            } else if (selectedRadioButtonId == R.id.biscuit_radioButton_350g) {
                price = biscuit.getPrice().get_350g();
            } else {
                price = biscuit.getPrice().get_500g();
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

            CartItem cartItem = new CartItem(biscuit.getName(), quantity, totalPrice);
            CartManager.getInstance().addItem(cartItem);

            // Show success message
            Toast.makeText(mContext, biscuit.getName() + " added to cart", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return mBiscuitList.size();
    }

    public static class BiscuitViewHolder extends RecyclerView.ViewHolder {
        TextView biscuitName, biscuitPrice, quantityText, quantityPrice;
        RadioGroup biscuitRadioGroup;
        RadioButton radioButton100g, radioButton350g, radioButton500g;
        ImageView biscuitImage; // Ensure this is properly declared
        ImageView plusButton, minusButton;
        Button addToCartButton;

        public BiscuitViewHolder(@NonNull View itemView) {
            super(itemView);
            biscuitName = itemView.findViewById(R.id.biscuit_heading);
            biscuitPrice = itemView.findViewById(R.id.biscuit_price);
            biscuitRadioGroup = itemView.findViewById(R.id.biscuit_radio_grp);
            radioButton100g = itemView.findViewById(R.id.biscuit_radioButton_100g);
            radioButton350g = itemView.findViewById(R.id.biscuit_radioButton_350g);
            radioButton500g = itemView.findViewById(R.id.biscuit_radioButton_500g);
            biscuitImage = itemView.findViewById(R.id.biscuit_img); // Initialize ImageView here

            plusButton = itemView.findViewById(R.id.plus_button);
            minusButton = itemView.findViewById(R.id.minus_button);
            quantityText = itemView.findViewById(R.id.quantity_text);
            quantityPrice = itemView.findViewById(R.id.quantity_price);
            addToCartButton = itemView.findViewById(R.id.add_to_cart_btn);
        }
    }
    private void updateQuantityPrice(BiscuitViewHolder holder) {
        int quantity = Integer.parseInt(holder.quantityText.getText().toString());
        String priceText = holder.biscuitPrice.getText().toString();
        int price = Integer.parseInt(priceText.replaceAll("[^0-9]", ""));
        int totalPrice = price * quantity;
        holder.quantityPrice.setText("Rs " + totalPrice + "/-");
    }

}