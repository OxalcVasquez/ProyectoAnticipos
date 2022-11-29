package com.usat.desarrollo.moviles.appanticipos.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

import com.usat.desarrollo.moviles.appanticipos.R;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;



public class Helper {


    public static String convertPassMd5(String pass) {
        String password = null;
        MessageDigest mdEnc;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
            mdEnc.update(pass.getBytes(), 0, pass.length());
            pass = new BigInteger(1, mdEnc.digest()).toString(16);
            while (pass.length() < 32) {
                pass = "0" + pass;
            }
            password = pass;
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        return password;
    }


    private static boolean mResult;

    public static boolean mensajeInformacion(Context context, String title, String message) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message mesg) {
                throw new RuntimeException();
            }
        };

        // make a text input dialog and show it
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setIcon(R.drawable.ic_info);
        alert.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                mResult = true;
                handler.sendMessage(handler.obtainMessage());
            }
        });

        alert.show();

        // loop till a runtime exception is triggered.
        try {
            Looper.loop();
        }
        catch(RuntimeException e2) {

        }

        return mResult;
    }


    public static boolean mensajeError(Context context, String title, String message) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message mesg) {
                throw new RuntimeException();
            }
        };

        // make a text input dialog and show it
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setIcon(R.drawable.ic_error);
        alert.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                mResult = true;
                handler.sendMessage(handler.obtainMessage());
            }
        });

        alert.show();

        // loop till a runtime exception is triggered.
        try {
            Looper.loop();
        }
        catch(RuntimeException e2) {

        }

        return mResult;
    }

    //Gestionar diálogos
    public static void mensajeConfirmacion(Context context, String message, String bt_ok, String bt_cancel, final Runnable if_ok){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(bt_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if_ok.run();
                    }
                }).setNegativeButton(bt_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel(); //close dialog
            }
        });

        //show dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static String imageToBase64(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String imageDecode = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return imageDecode;
    }

    public static Bitmap base64ToImage(String imageString){
        //encode image to base64 string
        byte[] imageBytes = Base64.decode(imageString, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return decodedImage;
    }

    public static String obtenerDireccionMapa(Context context, double latitud, double longitud) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitud, longitud, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            } else {
                Log.w("Dirección", "No se ha retornado la dirección!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd.substring(0,strAdd.length()-1);
    }

    public static String obtenerFechaActual(){
        Date fecha = new Date();
        SimpleDateFormat formatofecha = new SimpleDateFormat("dd/MM/yyyy");
        return formatofecha.format(fecha);
    }

    public static String formatearDMA_to_AMD(String fecha){
        String a = fecha.substring(fecha.length()-4);
        String m = fecha.substring(3,5);
        String d = fecha.substring(0, 2);
        return  a+"-"+m+"-"+d;
    }

    public static String formatearAMD_to_DMA(String fecha){
        String d = fecha.substring(fecha.length()-2);
        String m = fecha.substring(5,7);
        String a = fecha.substring(0,4);
        return  d+"/"+m+"/"+a;
    }


    public static int diasEntreDosFechas(String fecha1,String fecha2){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fechaInicio = LocalDate.parse(fecha1, dtf);
        LocalDate fechaFin = LocalDate.parse(fecha2, dtf);
        return (int) Duration.between(fechaInicio.atStartOfDay(), fechaFin.atStartOfDay()).toDays();
    }

}