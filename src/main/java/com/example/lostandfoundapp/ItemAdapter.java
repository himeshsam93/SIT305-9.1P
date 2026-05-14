package com.example.lostandfoundapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemAdapter
        extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    Context context;

    ArrayList<Item> originalList;
    ArrayList<Item> filteredList;

    public ItemAdapter(Context context,
                       ArrayList<Item> list) {

        this.context = context;

        this.originalList = list;

        this.filteredList = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_row,
                        parent,
                        false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull MyViewHolder holder,
            int position) {

        Item item = filteredList.get(position);

        holder.txtName.setText(item.getName());

        holder.txtCategory.setText(item.getCategory());

        holder.txtTimestamp.setText(item.getTimestamp());

        if(item.getImagePath() != null &&
                !item.getImagePath().isEmpty()) {

            holder.imgItem.setImageURI(
                    Uri.parse(item.getImagePath())
            );
        }

        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(
                    context,
                    ItemDetailActivity.class
            );

            intent.putExtra("id", item.getId());

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void filter(String text) {

        filteredList.clear();

        if(text.isEmpty()) {

            filteredList.addAll(originalList);
        }
        else {

            for(Item item : originalList) {

                if(item.getCategory()
                        .toLowerCase()
                        .contains(text.toLowerCase())) {

                    filteredList.add(item);
                }
            }
        }

        notifyDataSetChanged();
    }

    public static class MyViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtName,
                txtCategory,
                txtTimestamp;

        ImageView imgItem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);

            txtCategory = itemView.findViewById(R.id.txtCategory);

            txtTimestamp = itemView.findViewById(R.id.txtTimestamp);

            imgItem = itemView.findViewById(R.id.imgItem);
        }
    }
}
