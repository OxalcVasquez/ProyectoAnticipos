package com.usat.desarrollo.moviles.appanticipos.data.local.datos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Conexion extends SQLiteOpenHelper {

    public static Context contextApp;
    public static String dbName = "BDApp";
    public static int version =1;

    public Conexion() {
        super(contextApp,dbName , null, version);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

//        sqLiteDatabase.execSQL(Tablas.tablas);

//        for (int i = 0; i <  Tablas.tabla.length; i++) {
//                    sqLiteDatabase.execSQL(Tablas.tablas[i]);
//
//        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
