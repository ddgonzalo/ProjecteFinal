package com.example.dana.projectefinal.agenda;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.dana.projectefinal.ConnexioDades;
import com.example.dana.projectefinal.Objectes.*;
import com.example.dana.projectefinal.R;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConnexioAgenda {
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
    public ConnexioAgenda(Context context) {
        this.context = context;
        queue = Volley.newRequestQueue(context);
    }



    /**
     * Agafa de la base de dades els recordatoris pertenyents a avui, a demà, als pròxims 7 dies o a aquest mes.
     * Es passa un per paràmetre el valor:
     * - "avui" si es volen els recordatoris d'avui
     * - "dema" per els recordatoris de demà
     * - "setmana" per els recordatoris dels pròxims 7 dies (sense incloure els d'"avui" i "demà")
     * - "mes" per els recordatoris d'aquest mes (sense inclouse els d'"avui", "dema" i "setmana")
     *
     * @param quan "avui", "dema", "setmana" o "mes", depenent de quins recordatoris es volen veure
     * @param llistaRecordatoris LinearLayout on es mostraran els recordatoris
     */
    public void mostrarRecordatoris(String quan, final LinearLayout llistaRecordatoris) {
        JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET, URL_AGENDA + "?" + quan + "=1", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i=0; i<response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);

                                Recordatori rec = new Recordatori();

                                rec.setData(jsonObject.getString("data"));
                                rec.setTitol(jsonObject.getString("titol"));
                                rec.setText(jsonObject.getString("text"));
                                rec.setUbicacio(jsonObject.getString("ubicacio"));
                                rec.setTipusRecordatori(jsonObject.getString("tipusRecordatori"));

                                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(rec.getData());
                                String horaRecordatori = new SimpleDateFormat("HH:mm").format(date);

                                View fila = LayoutInflater.from(context).inflate(R.layout.row_agenda, llistaRecordatoris, false);

                                ImageView iconaCercle = fila.findViewById(R.id.cercle);
                                TextView hora = fila.findViewById(R.id.hora);
                                TextView recordatori = fila.findViewById(R.id.recordatori);
                                Drawable background = iconaCercle.getDrawable();

                                //canvia el color del cercle en funció del tipus de recordatori (lloguer, personal...)
                                String colorRecordatori = ConnexioDades.llistaEtiquetes.get(rec.getTipusRecordatori());
                                background.setColorFilter(new PorterDuffColorFilter(Color.parseColor(colorRecordatori), PorterDuff.Mode.SRC_IN));
                                hora.setText(horaRecordatori);
                                recordatori.setText(rec.getTitol());

                                llistaRecordatoris.addView(fila);
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

//--------------------------------------------------------------------------------------------------


    /**
     * Afegeix un recordatori nou a la base de dades
     * @param recordatori Recordatori que es vol guardar a l'agenda
     */
    public void guardarRecordatori(Recordatori recordatori) {
        Gson gson = new Gson();
        final String recordatoriJson = gson.toJson(recordatori);

        Log.i("VOLLEYPLS", URL_GUARDAR_RECORDATORI + "?recordatori=" + recordatoriJson);


        StringRequest string_request = new StringRequest(Request.Method.GET, URL_GUARDAR_RECORDATORI + "?recordatori=" + recordatoriJson,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("VOLLEYPLS", "yay");
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("VOLLEYPLS", "Error response");
                        error.printStackTrace();
                    }
                }
        );

        queue.add(string_request);
    }


    public void mostrarRecordatorisEtiqueta(final String nomEtiqueta, final LinearLayout llistaRecordatoris) {
        JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET, URL_AGENDA + "?etiqueta=" + nomEtiqueta, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i=0; i<response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);

                                Recordatori rec = new Recordatori();

                                rec.setData(jsonObject.getString("data"));
                                rec.setTitol(jsonObject.getString("titol"));
                                rec.setText(jsonObject.getString("text"));
                                rec.setUbicacio(jsonObject.getString("ubicacio"));

                                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(rec.getData());

                                int mesRecordatori = Integer.parseInt(new SimpleDateFormat("MM").format(date));
                                String mesRecordatoriStr = "";

                                switch (mesRecordatori) {
                                    case 1: mesRecordatoriStr =  "gener";   break;
                                    case 2: mesRecordatoriStr =  "febrer";  break;
                                    case 3: mesRecordatoriStr =  "març";    break;
                                    case 4: mesRecordatoriStr =  "abril";   break;
                                    case 5: mesRecordatoriStr =  "maig";    break;
                                    case 6: mesRecordatoriStr =  "juny";    break;
                                    case 7: mesRecordatoriStr =  "juliol";  break;
                                    case 8: mesRecordatoriStr =  "agost";   break;
                                    case 9: mesRecordatoriStr =  "setembre";break;
                                    case 10: mesRecordatoriStr = "octubre"; break;
                                    case 11: mesRecordatoriStr = "novembre";break;
                                    case 12: mesRecordatoriStr = "desembre";break;
                                }

                                String dataRecordatori = new SimpleDateFormat("dd").format(date) + " " + mesRecordatoriStr + ", ";
                                dataRecordatori += new SimpleDateFormat("HH:mm").format(date);

                                View fila = LayoutInflater.from(context).inflate(R.layout.row_agenda, llistaRecordatoris, false);

                                ImageView iconaCercle = fila.findViewById(R.id.cercle);
                                TextView dataHora = fila.findViewById(R.id.hora);
                                TextView recordatori = fila.findViewById(R.id.recordatori);
                                Drawable background = iconaCercle.getDrawable();

                                //canvia el color del cercle en funció del tipus de recordatori (lloguer, personal...)
                                String colorRecordatori = ConnexioDades.llistaEtiquetes.get(nomEtiqueta);
                                background.setColorFilter(new PorterDuffColorFilter(Color.parseColor(colorRecordatori), PorterDuff.Mode.SRC_IN));
                                dataHora.setText(dataRecordatori);
                                recordatori.setText(rec.getTitol());

                                llistaRecordatoris.addView(fila);
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
}
