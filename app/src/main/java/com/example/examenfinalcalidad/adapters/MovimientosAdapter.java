package com.example.examenfinalcalidad.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examenfinalcalidad.DetalleCuentaActivity;
import com.example.examenfinalcalidad.DetalleMovimientoActivity;
import com.example.examenfinalcalidad.R;
import com.example.examenfinalcalidad.entities.Cuenta;
import com.google.gson.Gson;

import java.util.List;

public class MovimientosAdapter extends RecyclerView.Adapter {

    List<Cuenta.Movimiento> data;

    public MovimientosAdapter(List<Cuenta.Movimiento> data){
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());//aquí llamamos al contexto

        View itemView = inflater.inflate(R.layout.item_movimientos, parent, false);//aquí hacemos referencia al item creado

        return new MovimientosViewHolder(itemView);//aquí retornamos el itemView creado
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Cuenta.Movimiento movimiento = data.get(position);

        TextView tvTipoMovimiento = holder.itemView.findViewById(R.id.tvTipoMovimiento);
        tvTipoMovimiento.setText(data.get(position).tipo);

        TextView tvMontoMovimiento = holder.itemView.findViewById(R.id.tvMontoMovimiento);
        tvMontoMovimiento.setText(data.get(position).monto.toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), DetalleMovimientoActivity.class);
                intent.putExtra("MOVIMIENTO_DATA", new Gson().toJson(movimiento));
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class MovimientosViewHolder extends RecyclerView.ViewHolder{
        public MovimientosViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
