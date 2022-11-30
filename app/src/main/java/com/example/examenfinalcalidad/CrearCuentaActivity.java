package com.example.examenfinalcalidad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.examenfinalcalidad.entities.Cuenta;
import com.example.examenfinalcalidad.services.CuentaServices;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CrearCuentaActivity extends AppCompatActivity {

    EditText etNombreCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);

        etNombreCuenta = findViewById(R.id.etNombreCuenta);
    }

    public void guardarCuenta(View view){
        Cuenta cuenta = new Cuenta();
        cuenta.nombre = etNombreCuenta.getText().toString();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://6352ca44a9f3f34c3749009a.mockapi.io/")// -> Aquí va la URL sin el Path
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CuentaServices services = retrofit.create(CuentaServices.class);
        services.create(cuenta).enqueue(new Callback<Cuenta>() {
            @Override
            public void onResponse(Call<Cuenta> call, Response<Cuenta> response) {
                Log.i("MAIN_APP", "Response: "+response.code());
                Toast.makeText(getApplicationContext(), "Se creo la cuenta", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), ListarCuentasActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<Cuenta> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "No se creo la cuenta", Toast.LENGTH_SHORT).show();
            }
        });
    }
}