package com.example.vinter.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vinter.Domain.ItemsDomain;
import com.example.vinter.Helper.ManagmentWishlist; // 🔑 ADDED: Local Wishlist Manager
import com.example.vinter.R;
// 🗑️ REMOVED ALL FIREBASE IMPORTS
// import com.google.firebase.auth.FirebaseAuth;
// import com.google.firebase.auth.FirebaseUser;
// import com.google.firebase.database.DatabaseReference;
// import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Locale; // Used for consistent price formatting

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {

    private final List<ItemsDomain> items;
    private Context context;
    // 🔑 ADDED: Local Wishlist Manager
    private final ManagmentWishlist managmentWishlist;

    // 🗑️ REMOVED FIREBASE FIELDS
    // private final FirebaseDatabase database;
    // private final String userId;

    // 🔑 CONSTRUCTOR FIX: Accept the local manager
    public WishlistAdapter(List<ItemsDomain> items, ManagmentWishlist managmentWishlist) {
        this.items = items;
        this.managmentWishlist = managmentWishlist;
        // 🗑️ REMOVED FIREBASE INITIALIZATION
    }

    // 🗑️ REMOVED THE ORIGINAL CONSTRUCTOR

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        // FIX: Assuming R.layout.viewholder_wishlist_item is created
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_wishlist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemsDomain item = items.get(position);

        // 1. Set Item Details
        holder.titleTxt.setText(item.getTitle());

        // FIX: Use String.format with Locale for consistent price display (resolves Locale warning)
        holder.priceTxt.setText(String.format(Locale.US, "$%.2f", item.getPrice()));

        // 2. Load Image
        if (item.getPicUrl() != null && !item.getPicUrl().isEmpty()) {
            Glide.with(context)
                    .load(item.getPicUrl().get(0))
                    .into(holder.picItem);
        } else {
            // Fallback image if needed
            holder.picItem.setImageResource(R.drawable.btn_3);
        }

        // 3. Set Delete/Remove Button Listener (getId() should work after ItemsDomain fix)
        holder.deleteBtn.setOnClickListener(v -> removeItemFromWishlistLocal(position, item.getId())); // 🔑 CALL LOCAL REMOVAL
    }

    /**
     * Handles the logic to remove an item from both the LOCAL storage and the local list.
     * @param position The position of the item in the local list.
     * @param itemId The unique ID of the item to remove from local storage.
     */
    private void removeItemFromWishlistLocal(int position, String itemId) {
        if (itemId == null || itemId.isEmpty()) {
            Toast.makeText(context, "Item error. Cannot remove.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 🔑 FIX: Remove item using the local manager
        managmentWishlist.removeItem(itemId);

        // 2. Remove from local list and update RecyclerView
        items.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, items.size());
        Toast.makeText(context, "Item removed from Wishlist", Toast.LENGTH_SHORT).show();
    }

    // 🗑️ REMOVED THE ORIGINAL FIREBASE REMOVAL LOGIC: removeItemFromWishlist()

    @Override
    public int getItemCount() {
        return items.size();
    }

    // =========================================================================
    // ViewHolder Class
    // =========================================================================

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleTxt, priceTxt;
        ImageView picItem, deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // These IDs must match the ones in viewholder_wishlist_item.xml
            titleTxt = itemView.findViewById(R.id.titleTxt);
            priceTxt = itemView.findViewById(R.id.priceTxt);
            picItem = itemView.findViewById(R.id.picItem);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);

            // Optional: Implement click listener for the entire item
            // itemView.setOnClickListener(v -> { ... });
        }
    }
}