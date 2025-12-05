package com.example.vinter.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.vinter.Domain.ItemsDomain;
import com.example.vinter.Helper.ChangeNumberItemsListener;
import com.example.vinter.Helper.ManagmentCart;
import com.example.vinter.databinding.ViewholderCartBinding;

import java.util.ArrayList;
import java.util.Locale; // Added for cleaner currency formatting

public class CartAdaptor extends RecyclerView.Adapter<CartAdaptor.Viewholder> {
    ArrayList<ItemsDomain> listItemSelected;
    ChangeNumberItemsListener changeNumberItemsListener;
    private ManagmentCart managmentCart;

    public CartAdaptor(ArrayList<ItemsDomain> listItemSelected, Context context, ChangeNumberItemsListener changeNumberItemsListener) {
        this.listItemSelected = listItemSelected;
        this.changeNumberItemsListener = changeNumberItemsListener;
        managmentCart=new ManagmentCart(context);
    }

    @NonNull
    @Override
    public CartAdaptor.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderCartBinding binding=ViewholderCartBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdaptor.Viewholder holder, int position) {

        ItemsDomain item = listItemSelected.get(position);

        holder.binding.titleTxt.setText(item.getTitle());

        // 🔑 FIX 1: Display the Base Price (feeEachItem) correctly.
        // Format to two decimal places (e.g., $40.00).
        holder.binding.feeEachItem.setText(String.format(Locale.US, "$%.2f", item.getPrice()));

        // 🔑 FIX 2: Calculate Total Price (totalEachItem) by multiplying Price * Quantity.
        // The previous code incorrectly ADDED them.
        double totalEachItem = item.getPrice() * item.getNumberinCart();
        holder.binding.totalEachItem.setText(String.format(Locale.US, "$%.2f", totalEachItem));

        holder.binding.numberItemTxt.setText(String.valueOf(item.getNumberinCart()));

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop());

        Glide.with(holder.itemView.getContext())
                .load(item.getPicUrl().get(0))
                .apply(requestOptions)
                .into(holder.binding.pic);

        holder.binding.plusCartBtn.setOnClickListener(v ->
                managmentCart.plusItem(listItemSelected, position, () -> {
                            notifyDataSetChanged();
                            changeNumberItemsListener.changed();
                        }
                ));
        holder.binding.minusCartBtn.setOnClickListener(v ->
                managmentCart.minusItem(listItemSelected, position, () -> {
                            notifyDataSetChanged();
                            changeNumberItemsListener.changed();
                        }
                ));
    }

    @Override
    public int getItemCount() {
        return listItemSelected.size();
    }

    public class Viewholder  extends  RecyclerView.ViewHolder{
        ViewholderCartBinding binding;
        public Viewholder(ViewholderCartBinding binding) {

            super(binding.getRoot());
            this.binding=binding;
        }
    }
}