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
    public static HashMap<Integer, String> llistaEstatsRecordatoris;

    public static HashMap<Integer, Objectes.Article> llistaBicicletes;
    public static HashMap<Integer, Objectes.Article> llistaScooters;

    public static HashMap<Integer, Integer> magatzemBicis;
    public static HashMap<Integer, Integer> magatzemScooters;

    public ConnexioDades(Context context) {
            queue = Volley.newRequestQueue(context);
            carregarEtiquetes();
            //carregarEstatsRecordatoris();
            carregarBicicletes();
            carregarScooters();
    }


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


    private void carregarEstatsRecordatoris() {
        llistaEstatsRecordatoris = new HashMap<>();

        JsonArrayRequest string_request = new JsonArrayRequest(Method.POST, SERVIDOR + "connexio_principal.php", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i=0; i<response.length(); i++) {
                            try {
                                JSONObject jsonObject = (JSONObject) response.getJSONObject(i);

                                llistaEstatsRecordatoris.put(jsonObject.getInt("id"), jsonObject.getString("descripcio"));
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
        ) {
            @Override
            public Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("estats", "");
                return params;
            }

        };

        queue.add(string_request);
    }


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
