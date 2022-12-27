package com.example.midtermfinal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midtermfinal.DataHolder;
import com.example.midtermfinal.R;
import com.example.midtermfinal.data.GroupProgressItems;
import com.example.midtermfinal.firestore.MyFireStore;

import java.util.ArrayList;

public class ProgressMgmtAdapter extends RecyclerView.Adapter<ProgressMgmtAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<GroupProgressItems> groupProgressItemsArrayList;
    private final OnItemClickListener itemClickListener;

    public ProgressMgmtAdapter(Context context, ArrayList<GroupProgressItems> groupProgressItemsArrayList, OnItemClickListener itemClickListener) {
        this.context = context;
        this.groupProgressItemsArrayList = groupProgressItemsArrayList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_progress, parent, false);

        /* Save data */
        DataHolder.getInstance().setGroupProgressItems(this.groupProgressItemsArrayList);
        return new ViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgressMgmtAdapter.ViewHolder holder, int position) {
        GroupProgressItems groupProgressItems = this.groupProgressItemsArrayList.get(position);

        holder.groupID.setText(groupProgressItems.getGroupID()
                .replace(MyFireStore.GROUP_NO_DOCUMENT, "Group "));

        /* Parse Progress items */
        int itemCount = groupProgressItems.getItemListContent().size();
        int completedItemCount = 0;
        for (int i = 0; i < itemCount; i++) {
            if (groupProgressItems.getItemListStatus().get(i) == GroupProgressItems.COMPLETED)
                completedItemCount++;
        }

        holder.progressBar.setMax(itemCount);
        holder.progressBar.setProgress(completedItemCount);
        holder.totalItems.setText(String.valueOf(itemCount));
    }

    @Override
    public int getItemCount() {
        return groupProgressItemsArrayList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView groupID;
        private final ProgressBar progressBar;
        private final TextView totalItems;

        OnItemClickListener onItemClickListener;

        public ViewHolder(@NonNull View itemView, OnItemClickListener itemClickListener) {
            super(itemView);

            groupID = itemView.findViewById(R.id.groupID);
            progressBar = itemView.findViewById(R.id.progressBar);
            totalItems = itemView.findViewById(R.id.totalItems);

            /* For click Listener */
            this.onItemClickListener = itemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }
    }
}
