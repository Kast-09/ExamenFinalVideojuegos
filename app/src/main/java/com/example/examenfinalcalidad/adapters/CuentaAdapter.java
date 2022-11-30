package com.example.examenfinalcalidad.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examenfinalcalidad.DetalleCuentaActivity;
import com.example.examenfinalcalidad.R;
import com.example.examenfinalcalidad.entities.Cuenta;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CuentaAdapter extends RecyclerView.Adapter {

    List<Cuenta> data;

    public CuentaAdapter(List<Cuenta> data){
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View itemView = inflater.inflate(R.layout.item_cuenta , parent, false);

        return new CuentaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Cuenta cuenta = data.get(position);

        TextView tvNombre = holder.itemView.findViewById(R.id.tvNombre);
        tvNombre.setText(data.get(position).nombre);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), DetalleCuentaActivity.class);
                intent.putExtra("CUENTA_DATA", new Gson().toJson(cuenta));
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class CuentaViewHolder extends RecyclerView.ViewHolder {
        public CuentaViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
