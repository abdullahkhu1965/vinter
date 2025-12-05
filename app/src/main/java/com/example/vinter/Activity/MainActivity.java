package com.example.vinter.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.example.vinter.Adaptor.CategoryAdaptor;
import com.example.vinter.Adaptor.PopularAdaptor;
import com.example.vinter.Adaptor.SlideAdaptor;
import com.example.vinter.Domain.CategoryDomain;
import com.example.vinter.Domain.ItemsDomain;
import com.example.vinter.Domain.SliderItems;
import com.example.vinter.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initBanner();
        initCategory();
        initPopular();
        BottomNavigation();
    }

    private void BottomNavigation() {
        binding.CartBtn.setOnClickListener(v->
                startActivity(new Intent(MainActivity.this,CartActivity.class)));
        binding.ProfileBtn.setOnClickListener(v->
                startActivity(new Intent(MainActivity.this,ProfileActivity.class)));
        binding.WishlistBtn.setOnClickListener(v->
                startActivity(new Intent(MainActivity.this,WishlistActivity.class)));

    }

    private void initPopular() {
        DatabaseReference myref=database.getReference("Items");

        binding.progressBarPopular.setVisibility(View.VISIBLE);
        ArrayList<ItemsDomain> items=new ArrayList<>();

        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot issue:snapshot.getChildren()){
                        ItemsDomain item = issue.getValue(ItemsDomain.class);
                        if (item != null) {
                            // CRITICAL FIX (for DetailActivity ID issue): Set the Firebase Key (ID)
                            item.setId(issue.getKey());
                            items.add(item);
                        }
                    }
                    if(!items.isEmpty()){
                        binding.recyclerViewPopular.setLayoutManager(new GridLayoutManager(MainActivity.this,2));
                        binding.recyclerViewPopular.setAdapter(new PopularAdaptor(items));
                    }
                    binding.progressBarPopular.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBarPopular.setVisibility(View.GONE);
            }
        });
    }

    private void initCategory() {
        DatabaseReference myref=database.getReference("Category");
        // ✅ CORRECT: Use the official progress bar
        binding.progressBarOfficial.setVisibility(View.VISIBLE);
        ArrayList<CategoryDomain> items=new ArrayList<>();
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot issue:snapshot.getChildren()){
                        items.add(issue.getValue(CategoryDomain.class));
                    }
                    if(!items.isEmpty()){
                        binding.recyclerViewOfficial.setLayoutManager((new LinearLayoutManager(MainActivity.this,
                                LinearLayoutManager.HORIZONTAL,false)));
                        binding.recyclerViewOfficial.setAdapter(new CategoryAdaptor(items));
                    }
                    binding.progressBarOfficial.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBarOfficial.setVisibility(View.GONE);
            }


        });
    }

    private void initBanner(){
        DatabaseReference myRef=database.getReference("Banner");
        binding.progressBarBanner.setVisibility(View.VISIBLE);
        ArrayList<SliderItems> items=new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot issue:snapshot.getChildren()){
                        items.add(issue.getValue(SliderItems.class));
                    }
                    banners(items);
                    binding.progressBarBanner.setVisibility((View.GONE));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBarBanner.setVisibility((View.GONE));
            }
        });
    }
    private void banners(ArrayList<SliderItems> items) {
        binding.viewPagerSlider.setAdapter(new SlideAdaptor(items,binding.viewPagerSlider));
        binding.viewPagerSlider.setClipToPadding(false);
        binding.viewPagerSlider.setClipChildren(false);
        binding.viewPagerSlider.setOffscreenPageLimit(3);
        binding.viewPagerSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        CompositePageTransformer compositePageTransformer=new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));

        binding.viewPagerSlider.setPageTransformer(compositePageTransformer);
    }
}