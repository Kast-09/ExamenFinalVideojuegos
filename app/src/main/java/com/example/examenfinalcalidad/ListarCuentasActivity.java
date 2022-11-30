package com.example.examenfinalcalidad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.examenfinalcalidad.adapters.CuentaAdapter;
import com.example.examenfinalcalidad.entities.Cuenta;
import com.example.examenfinalcalidad.services.CuentaServices;
import com.example.examenfinalcalidad.services.ImageService;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListarCuentasActivity extends AppCompatActivity {

    RecyclerView rvCuentas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_cuentas);

        rvCuentas = findViewById(R.id.rvCuentas);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://6352ca44a9f3f34c3749009a.mockapi.io/")// -> Aquí va la URL sin el Path
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CuentaServices service = retrofit.create(CuentaServices.class);
        service.getAll().enqueue(new Callback<List<Cuenta>>() {
            @Override
            public void onResponse(Call<List<Cuenta>> call, Response<List<Cuenta>> response) {
                List<Cuenta> data = response.body();
                rvCuentas.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                rvCuentas.setAdapter(new CuentaAdapter(data));
                Log.i("MAIN_APP", "Response: "+response.body().size());
                Log.i("MAIN_APP", new Gson().toJson(data).toString());
                Toast.makeText(getApplicationContext(), "Se cargo toda la data", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Cuenta>> call, Throwable t) {
                Log.i("MAIN_APP", "Fallo al cargar datos");
                Toast.makeText(getApplicationContext(), "No se cargo la data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void crearCuenta(View view){
        Intent intent = new Intent(getApplicationContext(), CrearCuentaActivity.class);
        startActivity(intent);
    }
}