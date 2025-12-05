package com.example.vinter.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.vinter.Domain.SliderItems;
import com.example.vinter.R;

import java.util.ArrayList;

public class SlideAdaptor extends RecyclerView.Adapter<SlideAdaptor.SliderViewHolder> {

    private ArrayList<SliderItems> sliderItems;  // FIXED
    private ViewPager2 viewPager2;
    private Context context;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sliderItems.addAll(sliderItems);
            notifyDataSetChanged();
        }
    };

    public SlideAdaptor(ArrayList<SliderItems> sliderItems, ViewPager2 viewPager2) {
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.slide_item_container, parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.setImage(sliderItems.get(position));  // FIXED (method name)
        if (position == sliderItems.size() - 2) {
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    public class SliderViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlide);
        }

        void setImage(SliderItems sliderItem) {

            RequestOptions requestOptions = new RequestOptions().transform(
                    new com.bumptech.glide.load.resource.bitmap.FitCenter(),
                    new com.bumptech.glide.load.resource.bitmap.RoundedCorners(30)
            );

            Glide.with(context)
                    .load(sliderItem.getUrl())
                    .apply(requestOptions)
                    .into(imageView);
        }

    }
}
