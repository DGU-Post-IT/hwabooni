package com.postit.hwabooni.presentation.plant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.postit.hwabooni.R;
import com.postit.hwabooni.model.PlantData;

import java.util.ArrayList;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.CustomViewHolder> {

    private ArrayList<PlantData> arrayList;

    OnClickListener listener;

    public PlantAdapter(ArrayList<PlantData> arrayList) {
        this.arrayList = arrayList;
    }
    //Test

    @NonNull
    @Override
    public PlantAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plant_list, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlantAdapter.CustomViewHolder holder, int position) {
        holder.tv_plant.setText(arrayList.get(position).getmyPlantName());
        holder.id = arrayList.get(position).getId();


        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener((v)->{
            if(listener!=null) listener.onClick(holder.id);
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

        protected TextView tv_plant;
        String id;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            this.tv_plant = (TextView) itemView.findViewById(R.id.tv_plant);
        }
    }

    interface OnClickListener{
        void onClick(String id);
    }
}
