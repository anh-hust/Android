package com.example.midtermfinal.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.widget.CheckBox;

import com.example.midtermfinal.R;
import com.example.midtermfinal.data.GroupProgressItems;

public class GroupItemAdapter extends RecyclerView.Adapter<GroupItemAdapter.ViewHolder> {
    private final Context context;
    private final GroupProgressItems groupProgressItems;
    private final OnItemClickListener itemClickListener;

    public GroupItemAdapter(Context context, GroupProgressItems groupProgressItems, OnItemClickListener itemClickListener) {
        this.context = context;
        this.groupProgressItems = groupProgressItems;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_progress_list, parent, false);

//        Log.d("GroupItemAdapter", "onCreateViewHolder - Data Check: " + groupProgressItems.getItemListContent());
//        Log.d("GroupItemAdapter", "onCreateViewHolder - Data Check: " + groupProgressItems.getItemListStatus());
        return new ViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupItemAdapter.ViewHolder holder, int position) {
//        Log.d("GroupItemAdapter", "onBindViewHolder - Data: " + groupProgressItems.getItemListContent().get(position));
//        Log.d("GroupItemAdapter", "onBindViewHolder - Data: " + groupProgressItems.getItemListStatus().get(position));

        holder.itemCheck.setText(groupProgressItems.getItemListContent().get(position));
        holder.itemCheck.setChecked(groupProgressItems.getItemListStatus().get(position));

        if (groupProgressItems.getItemListStatus().get(position) == GroupProgressItems.COMPLETED) {
            holder.itemBackgroud.setCardBackgroundColor(context.getColor(R.color.less_blue));
            holder.itemCheck.setTextColor(context.getColor(R.color.parakeet));
        } else {
            holder.itemBackgroud.setCardBackgroundColor(context.getColor(R.color.less_pink));
            holder.itemCheck.setTextColor(context.getColor(R.color.black));
        }
    }

    @Override
    public int getItemCount() {
        return this.groupProgressItems.getItemListContent().size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final CheckBox itemCheck;
        private final OnItemClickListener onItemClickListener;
        private final CardView itemBackgroud;

        public ViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            itemCheck = itemView.findViewById(R.id.itemCheck);
            itemBackgroud = itemView.findViewById(R.id.itemBackground);


            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }
    }
}
