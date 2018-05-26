package com.example.dana.projectefinal;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class Utilitats {

    public static void mostrarMissatgeError(Context context, String strPrimeraLinia, String strSegonaLinia) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View dview = inflater.inflate(R.layout.popup_error, null);

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dview);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.show();


        Button btAcceptar       = (Button) dview.findViewById(R.id.acceptar);
        TextView primeraLinia   = (TextView) dview.findViewById(R.id.primera_linia);
        TextView segonaLinia    = (TextView) dview.findViewById(R.id.segona_linia);

        primeraLinia.setText(strPrimeraLinia);
        segonaLinia.setText(strSegonaLinia);


        btAcceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


    public static String getNomMes(int numeroMes) {
        String mes = "";

        switch (numeroMes) {
            case 1: mes  =  "gener";    break;
            case 2: mes  =  "febrer";   break;
            case 3: mes  =  "març";     break;
            case 4: mes  =  "abril";    break;
            case 5: mes  =  "maig";     break;
            case 6: mes  =  "juny";     break;
            case 7: mes  =  "juliol";   break;
            case 8: mes  =  "agost";    break;
            case 9: mes  =  "setembre"; break;
            case 10: mes =  "octubre";  break;
            case 11: mes =  "novembre"; break;
            case 12: mes =  "desembre"; break;
        }

        return mes;
    }

}
