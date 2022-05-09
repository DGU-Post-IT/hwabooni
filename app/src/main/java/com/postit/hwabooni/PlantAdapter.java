package com.postit.hwabooni;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.CustomViewHolder> {

    private ArrayList<PlantData> arrayList;

    public PlantAdapter(ArrayList<PlantData> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public PlantAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plant_list, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlantAdapter.CustomViewHolder holder, int position) {
        holder.tv_plant.setText(arrayList.get(position).getTv_plant());


        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String plantName = holder.tv_plant.getText().toString();
                Toast.makeText(v.getContext(), plantName, Toast.LENGTH_SHORT).show();
            }
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

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            this.tv_plant = (TextView) itemView.findViewById(R.id.tv_plant);
        }
    }
}
