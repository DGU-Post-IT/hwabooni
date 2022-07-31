package com.postit.hwabooni.presentation.friendplant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.postit.hwabooni.R;
import com.postit.hwabooni.databinding.PlantListBinding;
import com.postit.hwabooni.model.PlantData;
import com.postit.hwabooni.presentation.plant.PlantAdapter;

import java.util.ArrayList;

public class FriendPlantAdapter extends RecyclerView.Adapter<FriendPlantAdapter.CustomViewHolder>{
    private ArrayList<PlantData> arrayList;

    FriendPlantAdapter.OnClickListener listener;

    public FriendPlantAdapter(ArrayList<PlantData> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public FriendPlantAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FriendPlantAdapter.CustomViewHolder(PlantListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull FriendPlantAdapter.CustomViewHolder holder, int position) {

        holder.bind(arrayList.get(position));

        holder.id = arrayList.get(position).getId();


        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener((v)->{
            if(listener!=null) listener.onClick(holder.id, arrayList.get(position));
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                remove(holder.getAdapterPosition());
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public void remove(int position) {
        try{
            arrayList.remove(position);
            notifyItemRemoved(position);
        } catch (IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        String id;

        PlantListBinding binding;

        public CustomViewHolder(@NonNull PlantListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(PlantData data){
            binding.tvPlant.setText(data.getName());
        }
    }

    interface OnClickListener{
        void onClick(String id,PlantData plant);
    }
}
