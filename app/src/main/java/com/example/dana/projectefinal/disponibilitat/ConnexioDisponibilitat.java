package com.example.dana.projectefinal.disponibilitat;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.dana.projectefinal.ConnexioDades;
import com.example.dana.projectefinal.MainActivity;
import com.example.dana.projectefinal.R;
import com.example.dana.projectefinal.inventari.InventariVeureAfegirLloguer;

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


    /**
     * Comprova si per una data i hora d'inici, i una data i hora de fi, una bicicleta determinada està disponible per llogar-se.
     * @param text TextView que mostrarà el missatge d'error en cas de que no es pugui llogar la bicicleta
     * @param btLlogar Button per anar a la pestanya "LLOGUERS" si la bicicleta està disponible per llogar
     * @param animation Animació
     * @param article Bicicleta que es vol llogar, en format "ID - marca model"
     * @param dataInici Data i hora d'inici del lloguer en format "yyyy-MM-dd HH:mm:ss"
     * @param dataFi Data i hora de fi del lloguer en format "yyyy-MM-dd HH:mm:ss"
     */
    public void comprovarDisponibilitatBicicleta (final FragmentManager fragmentManager, final TextView text, final Button btLlogar,
                                                  final LottieAnimationView animation, final String article, final String dataInici, final String dataFi) {

        String idBici = article.split(" - ")[0];
        String url = URL_DISPONIBILITAT + "?bicicleta=" + idBici + "&dataInici=" + dataInici + "&dataFi=" + dataFi;

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

                        btLlogar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MainActivity.canviarPestaña(3);
                                fragmentManager.beginTransaction()
                                        .replace(R.id.frameLayout, new InventariVeureAfegirLloguer(true, article, dataInici, dataFi))
                                        .addToBackStack(null)
                                        .commit();


                            }
                        });

                    }
                });

        queue.add(jsonRequest);
    }



    /**
     * Comprova si per una data i hora d'inici, i una data i hora de fi, un scooter determinat està disponible per llogar-se.
     * @param text TextView que mostrarà el missatge d'error en cas de que no es pugui llogar l'scooter
     * @param btLlogar Button per anar a la pestanya "LLOGUERS" si l'scooter està disponible per llogar
     * @param animation Animació
     * @param article Scooter que es vol llogar, en format "ID - marca model"
     * @param dataInici Data i hora d'inici del lloguer en format "yyyy-MM-dd HH:mm:ss"
     * @param dataFi Data i hora de fi del lloguer en format "yyyy-MM-dd HH:mm:ss"
     */
    public void comprovarDisponibilitatScooter (final FragmentManager fragmentManager, final TextView text, final Button btLlogar,
                                                final LottieAnimationView animation, final String article, final String dataInici, final String dataFi) {

        String idScooter = article.split(" - ")[0];
        String url = URL_DISPONIBILITAT + "?scooter=" + idScooter + "&dataInici=" + dataInici + "&dataFi=" + dataFi;

        JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET, url, null,
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
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

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

                        btLlogar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MainActivity.canviarPestaña(3);

                                fragmentManager.beginTransaction()
                                        .replace(R.id.frameLayout, new InventariVeureAfegirLloguer(false, article, dataInici, dataFi))
                                        .addToBackStack(null)
                                        .commit();

                            }
                        });
                    }
                });

        queue.add(jsonRequest);
    }

}
