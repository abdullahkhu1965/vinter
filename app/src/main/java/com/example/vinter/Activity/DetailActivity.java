package com.example.vinter.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vinter.Adaptor.SizeAdaptor;
import com.example.vinter.Adaptor.SlideAdaptor;
import com.example.vinter.Domain.ItemsDomain;
import com.example.vinter.Domain.SliderItems;
import com.example.vinter.Fragment.DescriptionFragment;
import com.example.vinter.Fragment.ReviewFragment;
import com.example.vinter.Fragment.SoldFragment;
import com.example.vinter.Helper.ManagmentCart;
import com.example.vinter.Helper.ManagmentWishlist; // 🔑 ADDED: Local Wishlist Manager
import com.example.vinter.R;

import com.example.vinter.databinding.ActivityDetailBinding;

import com.google.firebase.auth.FirebaseAuth;
// 🗑️ REMOVED UNUSED FIREBASE IMPORTS
// import com.google.firebase.auth.FirebaseUser;
// import com.google.firebase.database.DataSnapshot;
// import com.google.firebase.database.DatabaseError;
// import com.google.firebase.database.DatabaseReference;
// import com.google.firebase.database.FirebaseDatabase;
// import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends BaseActivity {
    ActivityDetailBinding binding;
    private ItemsDomain object;
    private int numberOrder = 1;
    private ManagmentCart managmentCart;
    private ManagmentWishlist managmentWishlist; // 🔑 ADDED: Local Wishlist Manager

    private FirebaseAuth mAuth;
    // 🗑️ REMOVED UNUSED FIREBASE FIELDS
    // private FirebaseDatabase database;
    private String currentItemId;

    private Handler slideHandle = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart = new ManagmentCart(this);
        managmentWishlist = new ManagmentWishlist(this); // 🔑 INITIALIZE LOCAL MANAGER

        mAuth = FirebaseAuth.getInstance();
        // database = FirebaseDatabase.getInstance(); // 🗑️ Firebase Database not needed here for local Wishlist

        getBundles();
        initBanners();
        initSize();
        setupViewPager();

        // 🔑 CALL NEW LOCAL SETUP FUNCTION
        setupFavButtonLocal();
    }

    private void initSize() {
        ArrayList<String> list = new ArrayList<>();
        list.add("S");
        list.add("M");
        list.add("L");
        list.add("XL");
        list.add("XXL");

        binding.recyclerSize.setAdapter(new SizeAdaptor(list));
        binding.recyclerSize.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void initBanners() {
        ArrayList<SliderItems> sliderItems = new ArrayList<>();
        if (object.getPicUrl() != null) {
            for (int i = 0; i < object.getPicUrl().size(); i++) {
                sliderItems.add(new SliderItems(object.getPicUrl().get(i)));
            }
        }

        binding.viewpageSlider.setAdapter(new SlideAdaptor(sliderItems, binding.viewpageSlider));
        binding.viewpageSlider.setClipToPadding(false);
        binding.viewpageSlider.setClipChildren(false);
        binding.viewpageSlider.setOffscreenPageLimit(3);

        if (binding.viewpageSlider.getChildAt(0) != null) {
            binding.viewpageSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        }
    }

    private void getBundles() {
        object = (ItemsDomain) getIntent().getSerializableExtra("object");

        // 🔑 FIX: Get Item ID from explicit Intent extra, falling back to object ID
        String explicitId = getIntent().getStringExtra("itemId");

        if (explicitId != null && !explicitId.isEmpty()) {
            currentItemId = explicitId;
        } else {
            currentItemId = object.getId();
        }

        if (object != null && currentItemId != null) {
            object.setId(currentItemId);
        }
        // END FIX

        binding.titleTxt.setText(object.getTitle());
        binding.priceTxt.setText("$" + object.getPrice());
        binding.ratingBar.setRating((float) object.getRating());
        binding.ratingTxt.setText(object.getRating() + " Rating");

        binding.addToCartBar.setOnClickListener(v -> {
            object.setNumberinCart(numberOrder);
            managmentCart.insertItem(object);
            Toast.makeText(this, "Added to Cart!", Toast.LENGTH_SHORT).show();
        });

        binding.backBtn.setOnClickListener(v -> finish());
    }

    /**
     * Handles the logic for checking favorite status and toggling the item LOCALLY.
     */
    private void setupFavButtonLocal() {

        // 1. Initial Check: Load the current favorite status from local storage
        boolean isFavorite = managmentWishlist.isItemInWishlist(currentItemId);

        if (isFavorite) {
            binding.favBtn.setImageResource(R.drawable.fav_filled);
        } else {
            binding.favBtn.setImageResource(R.drawable.fav);
        }

        // 2. Click Listener: Toggle the status
        binding.favBtn.setOnClickListener(v -> {
            boolean currentState = managmentWishlist.isItemInWishlist(currentItemId);

            if (currentItemId == null || currentItemId.isEmpty()) {
                Toast.makeText(this, "Error: Item ID missing. Cannot save.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (currentState) {
                // Toggled state: Favorited -> REMOVE locally
                managmentWishlist.removeItem(currentItemId);
                binding.favBtn.setImageResource(R.drawable.fav);
                Toast.makeText(DetailActivity.this, "Removed from Wishlist", Toast.LENGTH_SHORT).show();
            } else {
                // Toggled state: Not favorited -> ADD locally
                managmentWishlist.insertItem(object);
                binding.favBtn.setImageResource(R.drawable.fav_filled);
                Toast.makeText(DetailActivity.this, "Added to Wishlist", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 🗑️ REMOVE the original Firebase setupFavButton()
    /* private void setupFavButton() {
        // ... (Original Firebase code is removed here) ...
    }
    */


    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        DescriptionFragment tab1 = new DescriptionFragment();
        ReviewFragment tab2 = new ReviewFragment();
        SoldFragment tab3 = new SoldFragment();

        Bundle bundle1 = new Bundle();
        Bundle bundle2 = new Bundle();
        Bundle bundle3 = new Bundle();

        bundle1.putString("description", object.getDescription());

        tab1.setArguments(bundle1);
        tab2.setArguments(bundle2);
        tab3.setArguments(bundle3);

        adapter.addFrag(tab1, "Descriptions");
        adapter.addFrag(tab2, "Reviews");
        adapter.addFrag(tab3, "Sold");

        binding.viewpager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewpager);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();


        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        private void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}