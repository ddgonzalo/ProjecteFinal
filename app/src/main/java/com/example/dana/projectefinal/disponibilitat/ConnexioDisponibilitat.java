package com.example.dana.projectefinal.disponibilitat;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.dana.projectefinal.ConnexioDades;
import com.example.dana.projectefinal.Objectes;
import com.example.dana.projectefinal.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ConnexioDisponibilitat {

    RequestQueue queue;
    Context context;

    //URLs
    final String SERVIDOR = ConnexioDades.SERVIDOR;
    final String URL_DISPONIBILITAT = SERVIDOR + "disponibilitat.php";


    /**
     * Constructor
     * @param context Contexte on es troba la classe que l'ha inicialitzat
     */
    public ConnexioDisponibilitat(Context context) {
        this.context = context;
        queue = Volley.newRequestQueue(context);
    }

    public void comprovarDisponibilitatBicicleta (final TextView text, final Button btLlogar, final LottieAnimationView animation, String idBici, String dataInici, String dataFi) {

        String url = URL_DISPONIBILITAT + "?bicicleta=" + idBici + "&dataInici=" + dataInici + "&dataFi=" + dataFi;
        Log.i("VOLLEYPLS", url);
        final JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //La consulta ha retornat algún resultat, per tant la bicicleta no està disponible
                        animation.setAnimation(R.raw.error_animation);
                        btLlogar.setVisibility(GONE);
                        text.setVisibility(VISIBLE);
                        animation.playAnimation();

                        try {
                            JSONObject jsonObject = response.getJSONObject(0);

                            Date dateDataInici = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(jsonObject.getString("dataInici"));
                            String dataIniciLloguer = new SimpleDateFormat("dd/MM/yyyy").format(dateDataInici) + " a les " + new SimpleDateFormat("HH:mm").format(dateDataInici);

                            Date dateDataFi = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(jsonObject.getString("dataFi"));
                            String dataFiLloguer = new SimpleDateFormat("dd/MM/yyyy").format(dateDataFi) + " a les " + new SimpleDateFormat("HH:mm").format(dateDataFi);

                            text.setText("Llogat del " + dataIniciLloguer + " fins el " + dataFiLloguer);

                        } catch (Exception ignored) {}
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //La consulta ha retornat "null", per tant, la bicicleta està disponible
                        btLlogar.setVisibility(VISIBLE);
                        text.setVisibility(GONE);
                        animation.setAnimation(R.raw.success_animation);
                        animation.playAnimation();

                    }
                });

        queue.add(jsonRequest);
    }

    public void comprovarDisponibilitatScooter (TextView text, final Button btLlogar, final LottieAnimationView animation, String idScooter, String dataInici, String dataFi) {

        String url = URL_DISPONIBILITAT + "?scooter=" + idScooter + "&dataInici=" + dataInici + "&dataFi=" + dataFi;
        Log.i("VOLLEYPLS", url);
        JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //La consulta ha retornat algún resultat, per tant la bicicleta no està disponible
                        animation.setAnimation(R.raw.error_animation);
                        btLlogar.setVisibility(GONE);
                        animation.playAnimation();
                        for (int i=0; i<response.length(); i++) {
                            try {

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //La consulta ha retornat "null", per tant, la bicicleta està disponible
                        btLlogar.setVisibility(VISIBLE);
                        animation.setAnimation(R.raw.success_animation);
                        animation.playAnimation();
                    }
                });

        queue.add(jsonRequest);
    }

}
