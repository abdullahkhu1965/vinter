package com.example.vinter.Activity;

import android.os.Bundle;
import android.view.View;


import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.vinter.Adaptor.CartAdaptor;
import com.example.vinter.Helper.ManagmentCart;

import com.example.vinter.databinding.ActivityCartBinding;

import java.util.Locale; // 🔑 ADDED: For consistent currency formatting

public class CartActivity extends BaseActivity {
    ActivityCartBinding binding;
    private  double tax;
    private  ManagmentCart managmentCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart = new ManagmentCart(this);

        calculatorCart();
        setVariable();
        initCartList();
    }

    private void initCartList() {
        if(managmentCart.getListCart().isEmpty()){
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        }
        else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollViewCart.setVisibility(View.VISIBLE);

        }
        binding.cartView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        // Ensure CartAdaptor is using the fixed calculation logic (from previous response)
        binding.cartView.setAdapter(new CartAdaptor(managmentCart.getListCart(),this,()->calculatorCart()));
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v->finish());
    }

    private void calculatorCart() {
        double percentTax = 0.02; // 2% tax rate
        double delivery = 10.0;
        double subtotal = managmentCart.getTotalFee(); // Subtotal of all items

        // 1. Calculate Tax: Subtotal * Tax Rate
        double calculatedTax = subtotal * percentTax;

        // 2. Calculate Final Total
        double finalTotal = subtotal + delivery + calculatedTax;

        // 3. Rounding: Use Math.round(*100.0)/100.0 to properly round to two decimal places

        double roundedSubtotal = Math.round(subtotal * 100.0) / 100.0;
        double roundedTax = Math.round(calculatedTax * 100.0) / 100.0;
        double roundedTotal = Math.round(finalTotal * 100.0) / 100.0;

        // Update the global tax variable
        this.tax = roundedTax;

        // 🔑 FIX: Use String.format(Locale.US, "$%.2f", ...) for clean currency output
        binding.totalFeeTxt.setText(String.format(Locale.US, "$%.2f", roundedSubtotal));
        binding.taxTxt.setText(String.format(Locale.US, "$%.2f", roundedTax));
        binding.deliveryTxt.setText(String.format(Locale.US, "$%.2f", delivery)); // Delivery assumed to be fixed $10.00
        binding.totalTxt.setText(String.format(Locale.US, "$%.2f", roundedTotal));
    }
}