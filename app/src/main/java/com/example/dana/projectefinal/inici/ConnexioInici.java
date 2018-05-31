package com.example.dana.projectefinal.inici;

import android.content.Context;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dana.projectefinal.ConnexioDades;
import com.example.dana.projectefinal.R;
import com.example.dana.projectefinal.inventari.InventariLloguersVentes;


public class ConnexioInici {

    public static String SERVIDOR = ConnexioDades.SERVIDOR;
    public static String URL_DADES = SERVIDOR + "dades_guanys.php";

    private static RequestQueue queue;


    /**
     * Constructor
     * @param context Contexte on es troba la classe que l'ha inicialitzat
     */
    public ConnexioInici(Context context) {
        queue = Volley.newRequestQueue(context);
    }


    /**
     * Mostra per pantalla les dades dels guanys d'aquest mes i el mes passat.
     * @param tvLloguers TextView on es mostraran els guanys dels lloguers d'aquest mes
     * @param tvCompres TextView on es mostraran els diners invertits en compres d'aquest mes
     * @param tvGuanysMes TextView on es mostraran els guanys totals d'aquest mes (lloguers-compres)
     * @param tvGuanysMesPassat TextView on es mostraran els guanys que vam tenir el mes passat
     */
    public void mostrarDades(TextView tvLloguers, TextView tvCompres, TextView tvGuanysMes, TextView tvGuanysMesPassat) {
        mostrarDadesGuanysMesPassat(tvGuanysMesPassat);
        mostrarDadesLloguerMes(tvLloguers, tvCompres, tvGuanysMes);
    }


    /**
     * Mostra en un TextView el totla de guanys del mes passat
     * @param tvGuanysMesPassat TextView on es mostrarà l'informació
     */
    public void mostrarDadesGuanysMesPassat(final TextView tvGuanysMesPassat) {

        StringRequest string_request = new StringRequest(Request.Method.GET, URL_DADES + "?dades=guanys_mes_passat",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        double guanysMesPassat = Double.parseDouble(response);
                        tvGuanysMesPassat.setText("Guanys del mes passat: " + String.format("%.2f", guanysMesPassat) + "€");
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );

        queue.add(string_request);
    }


    public void mostrarDadesLloguerMes(final TextView tvLloguers, final TextView tvCompres, final TextView tvGuanys) {
        StringRequest string_request = new StringRequest(Request.Method.GET, URL_DADES + "?dades=lloguers_mes",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        double totalLloguers = Double.parseDouble(response);
                        tvLloguers.setText(String.format("%.2f", totalLloguers) + "€");

                        mostrarDadesCompresMes(totalLloguers, tvCompres, tvGuanys);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );

        queue.add(string_request);
    }

    public void mostrarDadesCompresMes(final double lloguers, final TextView tvCompres, final TextView tvGuanys) {
        StringRequest string_request = new StringRequest(Request.Method.GET, URL_DADES + "?dades=compres_mes",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        double totalCompres = Double.parseDouble(response);
                        tvCompres.setText(String.format("%.2f", totalCompres) + "€");
                        mostrarDadesGuanysMes(tvGuanys, lloguers, totalCompres);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );

        queue.add(string_request);
    }

    public void mostrarDadesGuanysMes(TextView tvGuanys, double lloguers, double compres) {
        tvGuanys.setText("Guanys d'aquest mes: " + String.format("%.2f", lloguers-compres) + "€");
    }

}
