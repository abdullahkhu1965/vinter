package com.example.vinter.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.vinter.Domain.ItemsDomain;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ManagmentWishlist {
    private static final String PREFS_NAME = "WishlistPrefs";
    private static final String WISHLIST_KEY = "WishlistItems";
    private SharedPreferences sharedPreferences;
    private Gson gson;

    public ManagmentWishlist(Context context) {
        // Initialize SharedPreferences using the application context
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    /**
     * Retrieves the current list of items from local storage.
     * @return List of ItemsDomain objects.
     */
    public List<ItemsDomain> getList() {
        String json = sharedPreferences.getString(WISHLIST_KEY, null);
        if (json == null) {
            return new ArrayList<>();
        }

        // Use TypeToken to correctly deserialize List<ItemsDomain>
        Type type = new TypeToken<ArrayList<ItemsDomain>>() {}.getType();
        return gson.fromJson(json, type);
    }

    /**
     * Saves the provided list of items back to local storage.
     * @param list The list to be saved.
     */
    private void saveList(List<ItemsDomain> list) {
        String json = gson.toJson(list);
        sharedPreferences.edit().putString(WISHLIST_KEY, json).apply();
    }

    /**
     * Adds an item to the local wishlist or updates it if it already exists.
     * Assumes the ItemsDomain object's ID is used as the unique identifier.
     * @param item The item to insert.
     */
    public void insertItem(ItemsDomain item) {
        List<ItemsDomain> list = getList();

        if (item.getId() == null || item.getId().isEmpty()) {
            // Handle case where item ID is missing, though previous fixes should prevent this
            return;
        }

        // Check if item already exists (based on ID)
        boolean exists = false;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(item.getId())) {
                // Item found, no quantity update is needed for wishlist, just marking as exists.
                exists = true;
                break;
            }
        }

        if (!exists) {
            list.add(item);
        }

        saveList(list);
    }

    /**
     * Removes an item from the local wishlist by its ID.
     * @param itemId The unique ID of the item to remove.
     */
    public void removeItem(String itemId) {
        if (itemId == null || itemId.isEmpty()) {
            return;
        }

        List<ItemsDomain> list = getList();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(itemId)) {
                list.remove(i);
                break;
            }
        }

        saveList(list);
    }

    /**
     * Checks if a specific item is already in the wishlist.
     * @param itemId The unique ID of the item to check.
     * @return true if the item exists, false otherwise.
     */
    public boolean isItemInWishlist(String itemId) {
        if (itemId == null || itemId.isEmpty()) {
            return false;
        }

        List<ItemsDomain> list = getList();
        for (ItemsDomain item : list) {
            if (item.getId().equals(itemId)) {
                return true;
            }
        }
        return false;
    }
}