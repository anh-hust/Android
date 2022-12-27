package com.example.midtermfinal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midtermfinal.R;
import com.example.midtermfinal.data.GroupInfo;
import com.example.midtermfinal.firestore.MyFireStore;

import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

    private Context context;
    private ArrayList<GroupInfo> groupInfoArrayList;

    private final OnItemClickListener itemClickListener;

    public GroupAdapter(Context context, ArrayList<GroupInfo> groupInfoArrayList, OnItemClickListener onItemClickListener) {
        setContext(context);
        setGroupInfoArrayList(groupInfoArrayList);
        this.itemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_group, parent, false);

//        Log.d("Group Adapter", "Data of GroupInfo: " + groupInfoArrayList
//                + "\nLength: " + getGroupInfoArrayList().size());
        return new GroupAdapter.ViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GroupInfo groupInfo = getGroupInfoArrayList().get(position);
//        Log.d("onBindViewHolder", "Data: " + groupInfo.getGroupName());

        String groupID = groupInfo.getGroupID();

        /* Take picture from drawable */

        holder.groupImg.setImageResource(context.getResources().getIdentifier(
                groupID.replace("Group_", "group"), // parse image name
                "drawable", context.getPackageName()));

        holder.groupID.setText(groupID.replace(MyFireStore.GROUP_NO_DOCUMENT, "Group "));
        holder.groupName.setText(groupInfo.getGroupName());
        holder.totalMembers.setText(groupInfo.getGroupMembers().size() + " members");


    }

    @Override
    public int getItemCount() {
        return getGroupInfoArrayList().size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView groupID;
        private final TextView groupName;
        private final TextView totalMembers;
        private final ImageView groupImg;
        private final OnItemClickListener onItemClickListener;

        public ViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);

            groupID = itemView.findViewById(R.id.groupID);
            totalMembers = itemView.findViewById(R.id.totalMembers);
            groupImg = itemView.findViewById(R.id.groupImg);
            groupName = itemView.findViewById(R.id.groupName);

            /* From here, listen the click */
            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }
    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<GroupInfo> getGroupInfoArrayList() {
        return groupInfoArrayList;
    }

    public void setGroupInfoArrayList(ArrayList<GroupInfo> groupInfoArrayList) {
        this.groupInfoArrayList = groupInfoArrayList;
    }

}
