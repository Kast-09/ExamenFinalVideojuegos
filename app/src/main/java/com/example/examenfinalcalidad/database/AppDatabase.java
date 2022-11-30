package com.example.examenfinalcalidad.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.examenfinalcalidad.daos.CuentaDao;
import com.example.examenfinalcalidad.daos.MovimientoDao;
import com.example.examenfinalcalidad.entities.Cuenta;

@Database(entities = {Cuenta.class, Cuenta.Movimiento.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CuentaDao cuentaDao();
    public abstract MovimientoDao movimientoDao();

    public static AppDatabase getInstance(Context context){
        return Room.databaseBuilder(context, AppDatabase.class, "EF_VJ2022-2")
                .allowMainThreadQueries()
                .build();
    }
}
