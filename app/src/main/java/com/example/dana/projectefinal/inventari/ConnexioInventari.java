package com.example.dana.projectefinal.inventari;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.dana.projectefinal.ConnexioDades;

public class ConnexioInventari {

    RequestQueue queue;
    Context context;

    //URLs
    final String SERVIDOR = ConnexioDades.SERVIDOR;
    final String URL_AGENDA = SERVIDOR + "agenda.php";
    final String URL_GUARDAR_RECORDATORI = SERVIDOR + "guardar_recordatori.php";


    /**
     * Constructor
     * @param context Contexte on es troba la classe que l'ha inicialitzat
     */
    public ConnexioInventari(Context context) {
        this.context = context;
        queue = Volley.newRequestQueue(context);
    }
}
