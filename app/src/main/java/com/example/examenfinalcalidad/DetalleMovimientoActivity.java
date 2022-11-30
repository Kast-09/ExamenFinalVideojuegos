package com.example.examenfinalcalidad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.examenfinalcalidad.entities.Cuenta;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class DetalleMovimientoActivity extends AppCompatActivity {

    public Cuenta.Movimiento movimiento;
    TextView tvDTipoMovimiento, tvDMonto, tvDMotivo;
    ImageView ivDVoucher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_movimiento);

        tvDTipoMovimiento = findViewById(R.id.tvDTipoMovimiento);
        tvDMonto = findViewById(R.id.tvDMonto);
        tvDMotivo = findViewById(R.id.tvDMotivo);
        ivDVoucher = findViewById(R.id.ivDVoucher);

        Intent intent = getIntent();
        String movimientoJson = intent.getStringExtra("MOVIMIENTO_DATA");
        Log.i("MAIN_APP", new Gson().toJson(movimientoJson));

        if(movimientoJson!=null){
            movimiento = new Gson().fromJson(movimientoJson, Cuenta.Movimiento.class);
            tvDTipoMovimiento.setText(movimiento.tipo);
            tvDMonto.setText(movimiento.monto.toString());
            tvDMotivo.setText(movimiento.descripcion);
            Picasso.get().load(movimiento.imagen).into(ivDVoucher);
        }
        else return;
    }

    public void verUbicacion(View view){
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        intent.putExtra("DATA_MOVIMIENTO", new Gson().toJson(movimiento));
        startActivity(intent);
    }
}