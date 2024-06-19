package com.cy.rashikabiscuit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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

public class SnackAdapter extends RecyclerView.Adapter<SnackAdapter.SnackViewHolder>  {
    private Context mContext;
    private List<Snack> mSnackList;


    public SnackAdapter(Context context, List<Snack> snackList) {
        mContext = context;
        mSnackList = snackList;
    }

    @NonNull
    @Override
    public SnackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_snack_card, parent, false);
        return new SnackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SnackViewHolder holder, int position) {
        Snack snack = mSnackList.get(position);

        holder.snackName.setText(snack.getName());

        // Load image using Glide into snackImage
        Glide.with(mContext)
                .load(snack.getImageUrl())
                .apply(RequestOptions.placeholderOf(R.drawable.profile)) // Placeholder image
                .into(holder.snackImage); // Use holder.snackImage to reference the ImageView

        // Set up RadioGroup with listener
        holder.snackRadioGroup.setOnCheckedChangeListener(null); // Clear existing listener
        holder.snackRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rdio_btn_6_packs) {
                holder.snackPrice.setText("Rs " + snack.getPrice().get_6_packs() + "/-");
            } else if (checkedId == R.id.rdio_btn_12_packs) {
                holder.snackPrice.setText("Rs " + snack.getPrice().get_12_packs() + "/-");
            } else if (checkedId == R.id.rdio_btn_24_packs) {
                holder.snackPrice.setText("Rs " + snack.getPrice().get_24_packs() + "/-");
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
            int selectedRadioButtonId = holder.snackRadioGroup.getCheckedRadioButtonId();
            if (selectedRadioButtonId == -1) {
                Toast.makeText(mContext, "Please select a pack size", Toast.LENGTH_SHORT).show();
                return;
            }

            // Retrieve the price based on the selected radio button
            int price;
            if (selectedRadioButtonId == R.id.rdio_btn_6_packs) {
                price = snack.getPrice().get_6_packs();
            } else if (selectedRadioButtonId == R.id.rdio_btn_12_packs) {
                price = snack.getPrice().get_12_packs();
            } else {
                price = snack.getPrice().get_24_packs();
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


            CartItem cartItem = new CartItem(snack.getName(), quantity, totalPrice);
            CartManager.getInstance().addItem(cartItem);

            // Show success message
            Toast.makeText(mContext, snack.getName() + " added to cart", Toast.LENGTH_SHORT).show();
        });




    }

    @Override
    public int getItemCount() {
        return mSnackList.size();
    }

    public static class SnackViewHolder extends RecyclerView.ViewHolder {
        TextView snackName, snackPrice, quantityText, quantityPrice;
        RadioGroup snackRadioGroup;
        RadioButton radioButton6Packs, radioButton12Packs, radioButton24Packs;
        ImageView snackImage;
        Button addToCartButton;

        ImageView plusButton, minusButton;
        public SnackViewHolder(@NonNull View itemView) {
            super(itemView);
            snackName = itemView.findViewById(R.id.snack_name);
            snackPrice = itemView.findViewById(R.id.snack_price);
            snackRadioGroup = itemView.findViewById(R.id.snack_radio_grp);
            radioButton6Packs = itemView.findViewById(R.id.rdio_btn_6_packs);
            radioButton12Packs = itemView.findViewById(R.id.rdio_btn_12_packs);
            radioButton24Packs = itemView.findViewById(R.id.rdio_btn_24_packs);
            snackImage = itemView.findViewById(R.id.snack_image);
            addToCartButton = itemView.findViewById(R.id.add_to_cart_btn);
            plusButton = itemView.findViewById(R.id.plus_button);
            minusButton = itemView.findViewById(R.id.minus_button);
            quantityText = itemView.findViewById(R.id.quantity_text);
            quantityPrice = itemView.findViewById(R.id.quantity_price);
        }
    }
    private void updateQuantityPrice(SnackAdapter.SnackViewHolder holder) {
        int quantity = Integer.parseInt(holder.quantityText.getText().toString());
        String priceText = holder.snackPrice.getText().toString();
        int price = Integer.parseInt(priceText.replaceAll("[^0-9]", ""));
        int totalPrice = price * quantity;
        holder.quantityPrice.setText("Rs " + totalPrice + "/-");
    }
}