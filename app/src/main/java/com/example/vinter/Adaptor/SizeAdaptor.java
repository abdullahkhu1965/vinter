package com.example.vinter.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vinter.R;
import com.example.vinter.databinding.ViewholderSizeBinding;

import java.util.ArrayList;

public class SizeAdaptor extends RecyclerView.Adapter<SizeAdaptor.Viewholder> {
    ArrayList<String> items;
    Context context;
    int selectedPosition=-1;
    int lastSelectedPosition=-1;

    public SizeAdaptor(ArrayList<String> items){
        this.items=items;
    }
    @NonNull
    @Override
    public SizeAdaptor.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        ViewholderSizeBinding binding=ViewholderSizeBinding.inflate(LayoutInflater.from(context),parent,false);
        
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SizeAdaptor.Viewholder holder, int position) {
holder.binding.sizeTxt.setText((items.get(position)));

holder.binding.getRoot().setOnClickListener(v->{
    lastSelectedPosition=selectedPosition;
    selectedPosition=holder.getAdapterPosition();
    notifyItemChanged(lastSelectedPosition);
    notifyItemChanged(selectedPosition);
});
if(selectedPosition==holder.getAdapterPosition()){
    holder.binding.sizeLayout.setBackgroundResource(R.drawable.size_selected);
    holder.binding.sizeTxt.setTextColor(context.getResources().getColor(R.color.green));
}else{
    holder.binding.sizeLayout.setBackgroundResource(R.drawable.size_unselected);
    holder.binding.sizeTxt.setTextColor(context.getResources().getColor(R.color.black));

}
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        ViewholderSizeBinding binding;

        public Viewholder(ViewholderSizeBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
