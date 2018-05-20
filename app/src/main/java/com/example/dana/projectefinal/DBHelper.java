package com.example.dana.projectefinal;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.v4.content.ContextCompat;
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
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.dana.projectefinal.Objectes.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class DBHelper {
    RequestQueue queue;
    Context context;

    //URLs
    final String SERVIDOR = "http://192.168.1.14/";
    final String URL_AGENDA = SERVIDOR + "agenda.php";


    //constructor
    public DBHelper(Context context) {
        this.context = context;
        queue = Volley.newRequestQueue(context);
    }


    public void carregarAgendaAvui(final LinearLayout llistaRecordatoris) {
        JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET, URL_AGENDA + "?avui=1", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i=0; i<response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);

                                Recordatori rec = new Recordatori();

                                rec.setData(jsonObject.getString("data"));
                                rec.setConcepte(jsonObject.getString("concepte"));
                                rec.setText(jsonObject.getString("text"));
                                rec.setUbicacio(jsonObject.getString("ubicacio"));
                                rec.setColor(jsonObject.getString("color"));

                                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(rec.getData());
                                String horaRecordatori = new SimpleDateFormat("HH:mm").format(date);

                                View fila = LayoutInflater.from(context).inflate(R.layout.row_agenda_inici, llistaRecordatoris, false);

                                ImageView iconaCercle = fila.findViewById(R.id.cercle);
                                TextView hora = fila.findViewById(R.id.hora);
                                TextView recordatori = fila.findViewById(R.id.recordatori);
                                Drawable background = iconaCercle.getDrawable();

                                //canvia el color del cercle en funció del tipus de recordatori (lloguer, personal...)
                                background.setColorFilter(new PorterDuffColorFilter(Color.parseColor(rec.getColor()), PorterDuff.Mode.OVERLAY));
                                hora.setText(horaRecordatori);
                                recordatori.setText(rec.getConcepte());

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
