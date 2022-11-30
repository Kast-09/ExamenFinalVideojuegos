package com.example.examenfinalcalidad;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.examenfinalcalidad.entities.Cuenta;
import com.example.examenfinalcalidad.entities.ImageResponse;
import com.example.examenfinalcalidad.entities.Imagen;
import com.example.examenfinalcalidad.services.CuentaServices;
import com.example.examenfinalcalidad.services.ImageService;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovimientoCuentaActivity extends AppCompatActivity {

    private Spinner spTipo;
    public ImageView ivTakeVoucher;
    String encoded;
    Double latitud, longitud;
    Cuenta cuentaData;
    public EditText etMontoMovimiento, etMotivoMovimiento;
    String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movimiento_cuenta);

        spTipo = findViewById(R.id.spTipo);
        ivTakeVoucher = findViewById(R.id.ivTakeVoucher);
        etMontoMovimiento = findViewById(R.id.etMontoMovimiento);
        etMotivoMovimiento = findViewById(R.id.etMotivoMovimiento);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.combo_tipos, android.R.layout.simple_spinner_dropdown_item);

        spTipo.setAdapter(adapter);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1001);
        }
        else {
            locationStart();
        }

        Intent intent = getIntent();
        String cuentaJson = intent.getStringExtra("ID_CUENTA");
        Log.i("MAIN_APP", new Gson().toJson(cuentaJson));

        if(cuentaJson!=null){
            cuentaData = new Gson().fromJson(cuentaJson, Cuenta.class);
        }
        else return;
    }

    public void selectFoto(View view){
        if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            abrirCamara();
        }
        else{
            requestPermissions(new String[] {Manifest.permission.CAMERA}, 100);//un número cualquiera
        }
    }

    public void abrirCamara(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//con esto es lo mínimo necesario para abrir la cámara
        startActivityForResult(intent, 1000);//se le pone cualquier número, sirve como código de respeusta

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1000 && resultCode == RESULT_OK){// el CAMERA_REQUEST es para validar que sea una petición de abrir la cámara y el RESULT_OK es para validar que al abrir la cámara todo salio bien y no hubo errores
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ivTakeVoucher.setImageBitmap(imageBitmap);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

            Log.i("MAIN_APP", encoded);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.imgur.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Imagen image = new Imagen();
            image.image = encoded;

            ImageService service = retrofit.create(ImageService.class);
            service.create(image).enqueue(new Callback<ImageResponse>() {
                @Override
                public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                    Log.i("MAIN_APP", String.valueOf(response.code()));
                    ImageResponse data = response.body();
                    link = data.data.link;
                    Log.i("MAIN_APP", new Gson().toJson(data));
                    Log.i("MAIN_APP", link);
                }
                @Override
                public void onFailure(Call<ImageResponse> call, Throwable t) {
                    Log.i("MAIN_APP", "NO SE OBTUVO ENLACE");
                }
            });
        }
    }

    public void guardarMovimiento(View view){

        Cuenta.Movimiento movimiento = new Cuenta.Movimiento();
        movimiento.tipo = spTipo.getSelectedItem().toString();
        Double monto = Double.valueOf(etMontoMovimiento.getText().toString());
        if(monto < 0){
            Toast.makeText(getApplicationContext(), "Monto inválido", Toast.LENGTH_SHORT).show();
        }
        else{
            movimiento.monto = monto;
            movimiento.descripcion = etMotivoMovimiento.getText().toString();
            movimiento.latitud = latitud;
            movimiento.longitud = longitud;
            movimiento.imagen = link;
            movimiento.cuentaId = cuentaData.id;

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://6352ca44a9f3f34c3749009a.mockapi.io/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            CuentaServices services = retrofit.create(CuentaServices.class);
            services.createMovimiento(cuentaData.id, movimiento).enqueue(new Callback<Cuenta.Movimiento>() {
                @Override
                public void onResponse(Call<Cuenta.Movimiento> call, Response<Cuenta.Movimiento> response) {
                    Log.i("MAIN_APP", String.valueOf(response.code()));
                    Toast.makeText(getApplicationContext(), "Se guardo correctamente", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), DetalleCuentaActivity.class);
                    intent.putExtra("CUENTA_DATA", new Gson().toJson(cuentaData));
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<Cuenta.Movimiento> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "No se guardo", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setMainActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);
    }

    public class Localizacion implements LocationListener {

        MovimientoCuentaActivity movimientoCuentaActivity;
        public MovimientoCuentaActivity getMainActivity() {
            return movimientoCuentaActivity;
        }

        public void setMainActivity(MovimientoCuentaActivity mainActivity) {
            this.movimientoCuentaActivity = mainActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion
            loc.getLatitude();
            loc.getLongitude();
            latitud = loc.getLatitude();
            longitud = loc.getLongitude();
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            Toast.makeText(getApplicationContext(), "GPS Desactivado", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            Toast.makeText(getApplicationContext(), "GPS Activado", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }
}