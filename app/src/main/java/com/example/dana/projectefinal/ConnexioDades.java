package com.example.dana.projectefinal;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.*;

public class ConnexioDades {

    public static String SERVIDOR = "http://192.168.1.11/";

    private static RequestQueue queue;

    public static HashMap<String, String> llistaEtiquetes;

    public static HashMap<Integer, Objectes.Article> llistaBicicletes;
    public static HashMap<Integer, Objectes.Article> llistaScooters;

    public static HashMap<Integer, Integer> magatzemBicis;
    public static HashMap<Integer, Integer> magatzemScooters;

    /**
     * Constructor
     * @param context Contexte on es troba la classe que l'ha inicialitzat
     */
    public ConnexioDades(Context context) {
            queue = Volley.newRequestQueue(context);
            carregarEtiquetes();
            carregarBicicletes();
            carregarScooters();
    }


    /**
     * Carrega en el HashMap <code>llistaEtiquetes</code> una llista amb el nom de totes les etiquetes que pot tenir un recordatori
     * (clau), i el color que té l'etiqueta (valor)
     */
    private void carregarEtiquetes() {
        llistaEtiquetes = new HashMap<>();

        JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET, SERVIDOR + "connexio_principal.php" + "?etiquetes=1", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i=0; i<response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);

                                llistaEtiquetes.put(jsonObject.getString("descripcio"), jsonObject.getString("color"));
                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        queue.add(jsonRequest);
    }


    /**
     * Carrega en el HashMap <code>llistaBicicletes</code> la llista de bicicletes existents al catàleg de bicicletes
     * on la ID de la bicicleta és la clau, i un objecte <code>Article</code> és el valor, que té totes les dades de la bicicleta
     */
    public static void carregarBicicletes() {
        llistaBicicletes = new HashMap<>();

        JsonArrayRequest string_request = new JsonArrayRequest(Method.GET, SERVIDOR + "bicicletes.php", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i=0; i<response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Objectes.Article bici = new Objectes.Article();

                                bici.setId(jsonObject.getString("id"));
                                bici.setMarca(jsonObject.getString("marca"));
                                bici.setModel(jsonObject.getString("model"));

                                bici.setAutonomia(jsonObject.getString("autonomia"));
                                bici.setBateria(jsonObject.getString("bateria"));
                                bici.setVelocitat(jsonObject.getString("velocitat"));
                                bici.setPes(Double.parseDouble(jsonObject.getString("pes")));
                                bici.setPreu(Double.parseDouble(jsonObject.getString("preu")));

                                llistaBicicletes.put(jsonObject.getInt("id"), bici);
                            } catch (Exception ignored) {
                                ignored.printStackTrace();
                            }
                        }

                        carregarMagatzemBicis();
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


    /**
     * Carrega en el HashMap <code>llistaScooters</code> la llista d'scooters existents al catàleg d'scooters
     * on la ID de l'scooter és la clau, i un objecte <code>Article</code> és el valor, que té totes les dades de l'scooter
     */
    public static void carregarScooters() {
        llistaScooters = new HashMap<>();

        JsonArrayRequest string_request = new JsonArrayRequest(Method.GET, SERVIDOR + "scooters.php", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i=0; i<response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Objectes.Article scooter = new Objectes.Article();

                                scooter.setId(jsonObject.getString("id"));
                                scooter.setMarca(jsonObject.getString("marca"));
                                scooter.setModel(jsonObject.getString("model"));

                                scooter.setAutonomia(jsonObject.getString("autonomia"));
                                scooter.setBateria(jsonObject.getString("bateria"));
                                scooter.setVelocitat(jsonObject.getString("velocitat"));
                                scooter.setPes(Double.parseDouble(jsonObject.getString("pes")));
                                scooter.setPreu(Double.parseDouble(jsonObject.getString("preu")));

                                llistaScooters.put(jsonObject.getInt("id"), scooter);


                            } catch (Exception ignored) {}
                        }

                        carregarMagatzemScooters();
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


    /**
     * Carrega en el HashMap <code>magatzemBicis</code> una llista amb l'ID únic de la bicicleta (clau),
     * i l'ID que li correspòn al catàleg (valor)
     */
    public static void carregarMagatzemBicis() {
        magatzemBicis = new HashMap<>();
        JsonArrayRequest string_request = new JsonArrayRequest(Method.GET, SERVIDOR + "bicicletes.php?magatzem=1", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i=0; i<response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                magatzemBicis.put(jsonObject.getInt("id"), jsonObject.getInt("idPare"));
                            } catch (Exception ignored) {

                            }
                        }
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


    /**
     * Carrega en el HashMap <code>magatzemScooters</code> una llista amb l'ID únic de l'scooter (clau),
     * i l'ID que li correspòn al catàleg (valor)
     */
    public static void carregarMagatzemScooters() {
        magatzemScooters = new HashMap<>();

        JsonArrayRequest string_request = new JsonArrayRequest(Method.GET, SERVIDOR + "scooters.php?magatzem=1", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i=0; i<response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                magatzemScooters.put(jsonObject.getInt("id"), jsonObject.getInt("idPare"));
                            } catch (Exception ignored) {}
                        }
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

}
