package com.example.examenfinalcalidad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.examenfinalcalidad.adapters.MovimientosAdapter;
import com.example.examenfinalcalidad.entities.Cuenta;
import com.example.examenfinalcalidad.services.CuentaServices;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetalleCuentaActivity extends AppCompatActivity {

    TextView tvNombre, tvSaldoFinal;

    Cuenta cuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_cuenta);

        tvNombre = findViewById(R.id.tvNombreCuenta);
        tvSaldoFinal = findViewById(R.id.tvSaldoFinal);

        Intent intent = getIntent();
        String cuentaJson = intent.getStringExtra("CUENTA_DATA");
        Log.i("MAIN_APP", new Gson().toJson(cuentaJson));

        if(cuentaJson!=null){
            cuenta = new Gson().fromJson(cuentaJson, Cuenta.class);
            tvNombre.setText(cuenta.nombre);
        }
        else return;

        final Double[] saldoF = {0d};

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://6352ca44a9f3f34c3749009a.mockapi.io/")// -> Aquí va la URL sin el Path
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CuentaServices services = retrofit.create(CuentaServices.class);
        services.getMovimientos(cuenta.id).enqueue(new Callback<List<Cuenta.Movimiento>>() {
            @Override
            public void onResponse(Call<List<Cuenta.Movimiento>> call, Response<List<Cuenta.Movimiento>> response) {
                List<Cuenta.Movimiento> data = response.body();
                Log.i("MAIN_APP", "Response: "+response.body().size());
                Log.i("MAIN_APP", new Gson().toJson(data).toString());

                for(int i=0; i<data.size(); i++){
                    Toast.makeText(getApplicationContext(), data.get(i).tipo, Toast.LENGTH_SHORT).show();
                    String aux = data.get(i).tipo.toString();
                    Log.i("MAIN_APP", aux);
                    if(aux.equals("Ingreso")) saldoF[0] += data.get(i).monto;
                    if(aux.equals("Gasto")) saldoF[0] -= data.get(i).monto;
                }

                Log.i("MAIN_APP", new Gson().toJson(saldoF).toString());

                tvSaldoFinal.setText("S/. "+saldoF[0]);

                //Toast.makeText(getApplicationContext(), "Se cargo todas las cuentas", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Cuenta.Movimiento>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "No se cargaron las cuentas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void registrarMovimiento(View view){
        Intent intent = new Intent(getApplicationContext(), MovimientoCuentaActivity.class);
        intent.putExtra("ID_CUENTA", new Gson().toJson(cuenta));
        startActivity(intent);
    }

    public void verMovimientos(View view){
        Intent intent = new Intent(getApplicationContext(), ListarMovimientosActivity.class);
        intent.putExtra("ID_CUENTA", new Gson().toJson(cuenta));
        startActivity(intent);
    }
}