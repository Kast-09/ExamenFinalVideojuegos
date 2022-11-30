package com.example.examenfinalcalidad.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.examenfinalcalidad.entities.Cuenta;

import java.util.List;

@Dao
public interface CuentaDao {
    @Query("SELECT * FROM cuenta")
    List<Cuenta> getAll();

    @Query("SELECT * FROM cuenta where id = :id")//el nombre del id debe ser igual en el método y en el Query
    Cuenta find(int id);

    @Insert
    void create(Cuenta cuenta);//para enviar varios usuario podemos usar -> void create(User... user); // y se debe pasar los parametros por coma

    @Update
    void update(Cuenta cuenta);

    @Delete
    void delete(Cuenta cuenta);
}
