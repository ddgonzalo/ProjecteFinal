package com.example.dana.projectefinal;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class Utilitats {


    /**
     * Mostra per pantalla un missatge d'error de dues línies
     * @param context Contexte on es troba la classe que ha cridat la funció
     * @param strPrimeraLinia Primera línia del missatge
     * @param strSegonaLinia Segona línia del missatge
     */
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


    /**
     * Retorna el nom d'un mes en format String donada la seva posició al calendari
     * @param numeroMes Posició del mes al calendari (1-12)
     * @return Nom del mes en format String
     */
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


    /**
     * Retorna un OnTouchListener que fa que, al fer-se click a un View passat per paràmetres,
     * aquesta amplii el seu tamany en 25%
     * @param v Vista sobre la que actuarà l'OnTouchListener
     * @return View.OnTouchListener
     */
    public static View.OnTouchListener onTouchListener(final View v) {

        View.OnTouchListener listener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setScaleX((float)1.25);
                    v.setScaleY((float)1.25);

                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    v.setScaleX((float)1);
                    v.setScaleY((float)1);
                }
                return false;
            }
        };

        return listener;
    }

}
