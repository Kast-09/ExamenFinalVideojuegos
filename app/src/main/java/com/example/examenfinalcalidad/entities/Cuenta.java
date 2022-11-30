package com.example.examenfinalcalidad.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cuenta")
public class Cuenta {
    @PrimaryKey
    public int id;
    @ColumnInfo(name = "nombre")
    public String nombre;
    //@ColumnInfo(name = "movimientos")
    //public Movimiento movimiento;

    @Entity(tableName = "movimiento")
    public static class Movimiento{
        @PrimaryKey
        public int id;
        @ColumnInfo(name = "cuentaId")
        public int cuentaId;
        @ColumnInfo(name = "descripcion")
        public String descripcion;
        @ColumnInfo(name = "monto")
        public Double monto;
        @ColumnInfo(name = "tipo")
        public String tipo;
        @ColumnInfo(name = "latitud")
        public Double latitud;
        @ColumnInfo(name = "longitud")
        public Double longitud;
        @ColumnInfo(name = "imagen")
        public String imagen;
    }
}
