package com.example.dana.projectefinal.inventari;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dana.projectefinal.ConnexioDades;
import com.example.dana.projectefinal.Objectes;
import com.google.gson.Gson;

public class ConnexioInventari {

    RequestQueue queue;
    Context context;

    //URLs
    final String SERVIDOR = ConnexioDades.SERVIDOR;
    final String URL_INVENTARI = SERVIDOR + "inventari.php";



    /**
     * Constructor
     * @param context Contexte on es troba la classe que l'ha inicialitzat
     */
    public ConnexioInventari(Context context) {
        this.context = context;
        queue = Volley.newRequestQueue(context);
    }


    public void actualitzarArticle(Objectes.Article article, final boolean esBicicleta) {
        Gson gson = new Gson();
        final String articleJson = gson.toJson(article);

        String url = URL_INVENTARI + "?article=" + articleJson + "&actualitzar=1";

        if (esBicicleta) url += "&bicicleta=1";
        else url += "&scooter=1";

        Log.i("VOLLEYPLSPLS", url);

        StringRequest string_request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("VOLLEYPLSPLS", "Yay");
                        //Actualitza la llista de bicicletes o scooters
                        if (esBicicleta) ConnexioDades.carregarBicicletes();
                        else ConnexioDades.carregarScooters();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("VOLLEYPLSPLS", "Error response");
                        error.printStackTrace();
                    }
                }
        );

        queue.add(string_request);
    }
}
