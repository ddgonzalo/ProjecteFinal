package com.example.dana.projectefinal;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class ConnexioClients {

    private static boolean primeraVegada = true;
    private RequestQueue queue;
    private Context context;

    private static final String URL_CLIENTS = ConnexioDades.SERVIDOR + "clients.php";

    public static HashMap<String, Objectes.Client> llistaClients;

    public ConnexioClients(Context context) {
        this.context = context;
        queue = Volley.newRequestQueue(context);
    }


    public void carregarDades() {
        if (primeraVegada) {
            llistaClients = new HashMap<>();
            carregarClients();
        }
    }

    public void carregarClients() {
        final JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET, URL_CLIENTS, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(0);

                            Objectes.Client client = new Objectes.Client();
                            client.setDni(jsonObject.getString("dni"));
                            client.setNom(jsonObject.getString("nom"));
                            client.setDataNaixement(jsonObject.getString("dataNaixement"));
                            client.setTelefon(jsonObject.getString("telefon"));
                            client.setMovil(jsonObject.getString("movil"));
                            client.setCorreu(jsonObject.getString("correu"));
                            client.setAdreça(jsonObject.getString("adreça"));
                            client.setPoblacio(jsonObject.getString("poblacio"));
                            client.setCodiPostal(jsonObject.getString("codiPostal"));

                            llistaClients.put(client.getDni(), client);

                        } catch (Exception ignored) {}
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        queue.add(jsonRequest);
    }

}
