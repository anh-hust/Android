package com.example.midtermfinal.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midtermfinal.R;
import com.example.midtermfinal.data.GroupInfo;

public class MemberInfoAdapter extends RecyclerView.Adapter<MemberInfoAdapter.ViewHolder> {
    private final Context context;
    private final GroupInfo groupInfo;
    private final OnItemClickListener itemClickListener;

    public MemberInfoAdapter(Context context, GroupInfo groupInfo, OnItemClickListener itemClickListener) {
        this.context = context;
        this.groupInfo = groupInfo;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_member, parent, false);


        return new ViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("MemberInfoAdapter", "Click at " + position);
        holder.memberName.setText(groupInfo.getGroupMembers().get(position));
        holder.memberID.setText(groupInfo.getGroupMembersID().get(position));
        holder.memberAbsent.setText("Absent: " + groupInfo.getGroupMembersAbsent().get(position));

        if (groupInfo.getGroupMembersRole().get(position) == GroupInfo.GROUP_LEADER)
            holder.memberRole.setImageResource(R.drawable.leader_icon);
        else
            holder.memberRole.setImageResource(R.drawable.member_icon);


    }

    @Override
    public int getItemCount() {
        return groupInfo.getGroupMembers().size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView memberName;
        private final TextView memberID;
        private final TextView memberAbsent;
        private final ImageView memberRole;
        OnItemClickListener onItemClickListener;

        public ViewHolder(@NonNull View itemView, MemberInfoAdapter.OnItemClickListener onItemClickListener) {
            super(itemView);

            memberName = itemView.findViewById(R.id.memberName);
            memberID = itemView.findViewById(R.id.memberID);
            memberAbsent = itemView.findViewById(R.id.memberAbsent);
            memberRole = itemView.findViewById(R.id.memberRole);

            /* From here, listen the click */
            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }

    }

}
