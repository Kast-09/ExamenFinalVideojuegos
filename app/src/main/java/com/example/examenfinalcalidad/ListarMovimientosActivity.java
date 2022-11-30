package com.example.examenfinalcalidad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.examenfinalcalidad.adapters.CuentaAdapter;
import com.example.examenfinalcalidad.adapters.MovimientosAdapter;
import com.example.examenfinalcalidad.entities.Cuenta;
import com.example.examenfinalcalidad.services.CuentaServices;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListarMovimientosActivity extends AppCompatActivity {

    Cuenta cuenta;
    RecyclerView rvMovimientos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_movimientos);

        rvMovimientos = findViewById(R.id.rvMovimientos);

        Intent intent = getIntent();
        String cuentaJson = intent.getStringExtra("ID_CUENTA");
        Log.i("MAIN_APP", new Gson().toJson(cuentaJson));

        if(cuentaJson!=null){
            cuenta = new Gson().fromJson(cuentaJson, Cuenta.class);
        }
        else return;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://6352ca44a9f3f34c3749009a.mockapi.io/")// -> Aquí va la URL sin el Path
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CuentaServices services = retrofit.create(CuentaServices.class);
        services.getMovimientos(cuenta.id).enqueue(new Callback<List<Cuenta.Movimiento>>() {
            @Override
            public void onResponse(Call<List<Cuenta.Movimiento>> call, Response<List<Cuenta.Movimiento>> response) {
                List<Cuenta.Movimiento> data = response.body();
                rvMovimientos.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                rvMovimientos.setAdapter(new MovimientosAdapter(data));
                Log.i("MAIN_APP", "Response: "+response.body().size());
                Log.i("MAIN_APP", new Gson().toJson(data).toString());
                Toast.makeText(getApplicationContext(), "Se cargo todas las cuentas", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Cuenta.Movimiento>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "No se cargaron las cuentas", Toast.LENGTH_SHORT).show();
            }
        });
    }
}