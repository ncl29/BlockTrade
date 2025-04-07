package com.example.blocktradefinal;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.nameTextView.setText(product.getName());
        holder.priceTextView.setText(product.getTradeType() + ": " + product.getPrice());
        holder.locationTextView.setText(product.getLocation());
        holder.ownerTextView.setText(product.getOwner());

        if (product.getImageUriString() != null && !product.getImageUriString().isEmpty()) {
            Glide.with(context)
                    .load(Uri.parse(product.getImageUriString()))
                    .into(holder.imageView);
        } else if (product.getImageResId() != 0) {
            holder.imageView.setImageResource(product.getImageResId());
        } else {
            holder.imageView.setImageResource(R.drawable.ic_launcher_background);
        }

        // ✅ Handle item click to open ProductDetailsActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailsActivity.class);
            intent.putExtra("imageUri", product.getImageUriString()); // can be null
            intent.putExtra("imageResId", product.getImageResId()); // drawable ID
            intent.putExtra("caption", product.getName());
            intent.putExtra("location", product.getLocation());
            intent.putExtra("type", product.getTradeType());
            intent.putExtra("owner", product.getOwner());
            intent.putExtra("price", product.getPrice());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView, priceTextView, locationTextView, ownerTextView;

        public ProductViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView2);
            nameTextView = itemView.findViewById(R.id.productName);
            priceTextView = itemView.findViewById(R.id.productPrice);
            locationTextView = itemView.findViewById(R.id.productLocation);
            ownerTextView = itemView.findViewById(R.id.productOwner);
        }
    }
}
