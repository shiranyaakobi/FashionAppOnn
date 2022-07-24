package com.idc.fashion.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.idc.fashion.Model.Item;
import com.idc.fashion.R;
import com.idc.fashion.SecurityHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {
    private List<Item> items;
    private RecyclerViewClickListener itemListener;

    public interface RecyclerViewClickListener {
        void itemClicked(Item item);
    }

    public ItemListAdapter(List<Item> items, RecyclerViewClickListener listener) {
        this.items = items;
        this.itemListener = listener;
    }

    @NonNull
    @Override
    public ItemListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_card, parent, false);
        ItemListAdapter.ViewHolder viewHolder = new ItemListAdapter.ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = items.get(position);
        holder.itemName.setText(item.getName());
        holder.cardViewItem.setOnClickListener(v -> itemListener.itemClicked(item));
        Picasso.get().load(SecurityHelper.Decrypt(item.getImageAddress())).into(holder.itemPhoto);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView itemPhoto;
        TextView itemName;
        CardView cardViewItem;

        public ViewHolder(View itemView) {
            super(itemView);
            itemPhoto = itemView.findViewById(R.id.item_photo);
            itemName = itemView.findViewById(R.id.item_name);
            cardViewItem = itemView.findViewById(R.id.items_card);
        }

    }
}
