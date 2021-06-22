package com.example.simpletodo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    // we make an interface to help the MainActivity file communicate with the ItemAdapter
    // helps with editing items by sending position of the item
    public interface OnClickListener   {
        void onItemClicked(int position);
    }

    // helps with removing items from the Recycler by sending in the position of the item
    public interface OnLongClickListener {
        void onItemLongClicked(int position);
    }

    List<String> items;
    OnLongClickListener longClickListener;
    OnClickListener clickListener;

    // constructor!
    public ItemAdapter(List<String> items, OnLongClickListener longClickListener, OnClickListener clickListener) {
        this.items = items;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    // we create a new view and wrap it inside a ViewHolder
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate a view using layout inflater
        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(todoView);
    }

    @Override
    //binds data to particular view holder
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // grab item at the given position
        String item = items.get(position);
        // bind item into specified ViewHolder
        holder.bind(item);

    }

    @Override
    // returns the number of items currently in the list
    public int getItemCount() {
        return items.size();
    }

    // container to provide easy access to views that represent each row
    // of the list
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }

        // update the view inside of the ViewHolder with the given data
        public void bind(String item) {
            tvItem.setText(item);

            // listen for a single, short click on any item
            tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });

            // we listen for a long click on any one of the items
            // allows user to remove items from the to-do list
            tvItem.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v){
                    // notify the listener of the position of the long press
                    longClickListener.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });
        }

    }
}
