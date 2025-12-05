package com.example.vinter.Activity;

import android.os.Bundle;
import android.view.View;
// 🗑️ REMOVED UNUSED IMPORTS
// import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.vinter.Adaptor.WishlistAdapter;
import com.example.vinter.Domain.ItemsDomain;
import com.example.vinter.Helper.ManagmentWishlist; // 🔑 ADDED: Local Wishlist Manager
import com.example.vinter.databinding.ActivityWishlistBinding;

// 🗑️ REMOVED ALL FIREBASE IMPORTS
// import com.google.firebase.auth.FirebaseAuth;
// import com.google.firebase.auth.FirebaseUser;
// import com.google.firebase.database.DataSnapshot;
// import com.google.firebase.database.DatabaseError;
// import com.google.firebase.database.DatabaseReference;
// import com.google.firebase.database.FirebaseDatabase;
// import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WishlistActivity extends AppCompatActivity {

    private ActivityWishlistBinding binding;
    // 🔑 ADDED: Local Wishlist Manager
    private ManagmentWishlist managmentWishlist;

    // 🗑️ REMOVED FIREBASE FIELDS
    // private FirebaseAuth mAuth;
    // private FirebaseDatabase database;
    // private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWishlistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 🔑 INITIALIZE LOCAL MANAGER
        managmentWishlist = new ManagmentWishlist(this);

        // 🗑️ REMOVED AUTHENTICATION CHECK - LOCAL STORAGE DOESN'T NEED IT
        // mAuth = FirebaseAuth.getInstance();
        // database = FirebaseDatabase.getInstance();
        // FirebaseUser currentUser = mAuth.getCurrentUser();

        // if (currentUser != null) {
        //     userId = currentUser.getUid();
        //     initWishlist();
        // } else {
        //     Toast.makeText(this, "Please sign in to view your Wishlist.", Toast.LENGTH_LONG).show();
        //     finish();
        // }

        initWishlistLocal(); // 🔑 CALL NEW LOCAL FUNCTION

        binding.backBtn.setOnClickListener(v -> finish());
    }

    /**
     * Loads the wishlist items from local storage.
     */
    private void initWishlistLocal() {
        binding.progressBarWishlist.setVisibility(View.VISIBLE);

        // 🔑 LOAD items directly from local manager
        List<ItemsDomain> items = managmentWishlist.getList();

        binding.progressBarWishlist.setVisibility(View.GONE);

        if (!items.isEmpty()) {
            binding.txtEmptyWishlist.setVisibility(View.GONE);
            setupRecyclerView(items);
        } else {
            binding.txtEmptyWishlist.setVisibility(View.VISIBLE);
        }
    }

    // 🗑️ REMOVED THE ENTIRE FIREBASE LOADING LOGIC: initWishlist() and fetchItemDetails()

    private void setupRecyclerView(List<ItemsDomain> items) {
        binding.progressBarWishlist.setVisibility(View.GONE);
        if (!items.isEmpty()) {
            binding.recyclerViewWishlist.setLayoutManager(new LinearLayoutManager(this));
            // 🔑 PASS THE LOCAL MANAGER TO THE ADAPTER
            binding.recyclerViewWishlist.setAdapter(new WishlistAdapter(items, managmentWishlist));
        } else {
            binding.txtEmptyWishlist.setVisibility(View.VISIBLE);
        }
    }
}