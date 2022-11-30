package com.example.examenfinalcalidad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.examenfinalcalidad.database.AppDatabase;
import com.example.examenfinalcalidad.entities.Cuenta;
import com.example.examenfinalcalidad.services.CuentaServices;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void listarCuentas(View view){
        Intent intent = new Intent(getApplicationContext(), ListarCuentasActivity.class);
        startActivity(intent);
    }

    public void sincronizarCuentas(View view){
        AppDatabase db = AppDatabase.getInstance(this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://6352ca44a9f3f34c3749009a.mockapi.io/")// -> Aquí va la URL sin el Path
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CuentaServices services = retrofit.create(CuentaServices.class);
        services.getAll().enqueue(new Callback<List<Cuenta>>() {
            @Override
            public void onResponse(Call<List<Cuenta>> call, Response<List<Cuenta>> response) {
                List<Cuenta> cuentas = response.body();

                for(int i=0;i<cuentas.size();i++){
                    Cuenta cuentaAux = cuentas.get(i);
                    Cuenta cuenta2 = db.cuentaDao().find(cuentaAux.id);
                    if(cuenta2 != null) {
                        db.cuentaDao().update(cuentaAux);
                        services.getMovimientos(cuentaAux.id).enqueue(new Callback<List<Cuenta.Movimiento>>() {
                            @Override
                            public void onResponse(Call<List<Cuenta.Movimiento>> call, Response<List<Cuenta.Movimiento>> response) {
                                List<Cuenta.Movimiento> movimientos =  response.body();

                                for(int i=0;i<movimientos.size();i++){
                                    Cuenta.Movimiento movimientoAux = movimientos.get(i);
                                    Cuenta.Movimiento movimientoAux2 = db.movimientoDao().find(movimientoAux.id);
                                    if(movimientoAux2 != null) db.movimientoDao().update(movimientoAux);
                                    else db.movimientoDao().create(movimientoAux);;
                                }

                                Log.i("MAIN_APP", new Gson().toJson(movimientos));
                                Toast.makeText(getApplicationContext(), "Movimientos Sincronizadas", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<List<Cuenta.Movimiento>> call, Throwable t) {

                            }
                        });
                    }
                    else {
                        db.cuentaDao().create(cuentaAux);
                        services.getMovimientos(cuentaAux.id).enqueue(new Callback<List<Cuenta.Movimiento>>() {
                            @Override
                            public void onResponse(Call<List<Cuenta.Movimiento>> call, Response<List<Cuenta.Movimiento>> response) {
                                List<Cuenta.Movimiento> movimientos =  response.body();

                                for(int i=0;i<movimientos.size();i++){
                                    Cuenta.Movimiento movimientoAux = movimientos.get(i);
                                    Cuenta.Movimiento movimientoAux2 = db.movimientoDao().find(movimientoAux.id);
                                    if(movimientoAux2 != null) db.movimientoDao().update(movimientoAux);
                                    else db.movimientoDao().create(movimientoAux);;
                                }

                                Log.i("MAIN_APP", new Gson().toJson(movimientos));
                                Toast.makeText(getApplicationContext(), "Movimientos Sincronizadas", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<List<Cuenta.Movimiento>> call, Throwable t) {

                            }
                        });
                    }
                }

                Log.i("MAIN_APP", new Gson().toJson(cuentas));
                Toast.makeText(getApplicationContext(), "Cuentas Sincronizadas", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onFailure(Call<List<Cuenta>> call, Throwable t) {

            }
        });
    }


}