package com.example.examenfinalcalidad.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.examenfinalcalidad.entities.Cuenta;

import java.util.List;

@Dao
public interface MovimientoDao {
    @Query("SELECT * FROM movimiento")
    List<Cuenta.Movimiento> getAll();

    @Query("SELECT * FROM movimiento where id = :id")//el nombre del id debe ser igual en el método y en el Query
    Cuenta.Movimiento find(int id);

    @Insert
    void create(Cuenta.Movimiento movimiento);//para enviar varios usuario podemos usar -> void create(User... user); // y se debe pasar los parametros por coma

    @Update
    void update(Cuenta.Movimiento movimiento);

    @Delete
    void delete(Cuenta.Movimiento movimiento);
}
