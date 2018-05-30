package com.example.dana.projectefinal.inventari;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dana.projectefinal.ConnexioDades;
import com.example.dana.projectefinal.Objectes;
import com.example.dana.projectefinal.R;
import com.example.dana.projectefinal.Utilitats;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import okhttp3.internal.Util;

import static android.view.View.GONE;

public class ConnexioInventari {

    private static boolean primeraVegada = true;
    private RequestQueue queue;
    private Context context;

    //URLs
    final static String SERVIDOR = ConnexioDades.SERVIDOR;
    final static String URL_INVENTARI = SERVIDOR + "inventari.php";
    final static String URL_LLOGUERS = SERVIDOR + "lloguers.php";

    public static List<Objectes.Lloguer> llistaLloguersEnCurs;
    public static List<Objectes.Lloguer> llistaLloguersPendents;
    public static List<Objectes.Lloguer> llistaLloguersFinalitzats;


    /**
     * Constructor
     * @param context Contexte on es troba la classe que l'ha inicialitzat
     */
    public ConnexioInventari(Context context) {
        this.context = context;
        queue = Volley.newRequestQueue(context);
    }



    /**
     * Inicialitza i carrega les llistes <code>llistaLloguersEnCurs</code>, <code>llistaLloguersPendents</code> i <code>llistaLlloguersFinalitzats</code>
     * amb les dades de lloguers en curs, pendents o finalitzats, respectivament.
     */
    public void carregarDades() {

        if (primeraVegada) {
            llistaLloguersEnCurs = new LinkedList<>();
            llistaLloguersPendents = new LinkedList<>();
            llistaLloguersFinalitzats = new LinkedList<>();

            carregarLloguers("enCurs");
            carregarLloguers("pendents");
            carregarLloguers("finalitzats");

            primeraVegada = false;
        }
    }

//-- lloguers --------------------------------------------------------------------------------------

    /**
     * Carrega una de les llistes <code>llistaLloguersEnCurs</code>, <code>llistaLloguersPendents</code> o <code>llistaLlloguersFinalitzats</code>
     * amb dades dels lloguers corresponents.
     * @param estat "enCurs", "pendents" o "finalitzats"
     */
    public void carregarLloguers(final String estat) {
        final JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET, URL_LLOGUERS + "?estat=" + estat, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i=0; i<response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);

                                final Objectes.Lloguer lloguer = new Objectes.Lloguer();

                                lloguer.setId(jsonObject.getString("id"));
                                lloguer.setDataInici(jsonObject.getString("dataInici"));
                                lloguer.setDataFi(jsonObject.getString("dataFi"));
                                lloguer.setLlocEntrega(jsonObject.getString("llocEntrega"));
                                lloguer.setLlocRecollida(jsonObject.getString("llocRecollida"));
                                lloguer.setClient(jsonObject.getString("client"));
                                lloguer.setPreu(Double.parseDouble(jsonObject.getString("preu")));
                                lloguer.setTotalArticles(jsonObject.getInt("totalArticles"));


                                if (estat.equals("enCurs")) llistaLloguersEnCurs.add(lloguer);
                                else if (estat.equals("pendents")) llistaLloguersPendents.add(lloguer);
                                else if (estat.equals("finalitzats")) llistaLloguersFinalitzats.add(lloguer);

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
     * Mostra per pantalla una llista de lloguers en curs, pendents o finalitzats
     * @param fragmentManager Fragment Manager per canviar de fragment quan es fa click strDataFi un lloguer
     * @param estat "enCurs", "pendents" o "finalitzats"
     * @param llistaLloguers LinearLayout on es mostraran els lloguers
     */
    public void mostrarLloguers(final FragmentManager fragmentManager, String estat, LinearLayout llistaLloguers) {
        List<Objectes.Lloguer> llista = null;

        if (estat.equals("enCurs")) llista = llistaLloguersEnCurs;
        else if (estat.equals("pendents")) llista = llistaLloguersPendents;
        else if (estat.equals("finalitzats")) llista = llistaLloguersFinalitzats;

        for (final Objectes.Lloguer lloguer : llista) {
            try {

                View fila = LayoutInflater.from(context).inflate(R.layout.row_inventari_lloguers, llistaLloguers, false);

                TextView idLloguer = fila.findViewById(R.id.id_lloguer);
                TextView dataLloguer = fila.findViewById(R.id.data_lloguer);
                TextView articlesLloguer = fila.findViewById(R.id.articles_lloguer);

                String dataInici = Utilitats.getDia(lloguer.getDataInici()) + ", " + Utilitats.getHora(lloguer.getDataInici());

                String dataFi = Utilitats.getDia(lloguer.getDataFi()) + ", " + Utilitats.getHora(lloguer.getDataFi());
                idLloguer.setText("ID " + lloguer.getId());

                if (lloguer.getTotalArticles()==1) articlesLloguer.setText("(" + lloguer.getTotalArticles() + " article)");
                else articlesLloguer.setText("(" + lloguer.getTotalArticles() + " articles)");

                if (estat.equals("enCurs")) dataLloguer.setText("Acaba " + dataFi);
                else if (estat.equals("pendents")) dataLloguer.setText("Comença " + dataInici);
                else if (estat.equals("finalitzats")) dataLloguer.setText("Va acabar " + dataFi);

                llistaLloguers.addView(fila);


                fila.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fragmentManager.beginTransaction()
                                .replace(R.id.frameLayout, new InventariVeureAfegirLloguer(lloguer))
                                .addToBackStack(null)
                                .commit();
                    }
                });

            } catch (Exception ignored) {}
        }
    }


    /**
     * Mostra per pantalla una llista de les bicicletes que s'han llogat en un lloguer concret.
     * @param llistaArticles LinearLayout on es mostrarà la llista de bicicletes
     * @param idLloguer ID del lloguer en el que s'han llogat les bicicletes
     */
    public void mostrarBicicletesLloguer (final LinearLayout llistaArticles, String idLloguer) {
        final JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET, URL_LLOGUERS + "?bicisLloguer=" + idLloguer, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i=0; i<response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);

                                final View fila = LayoutInflater.from(context).inflate(R.layout.row_inventari_vista_lloguer, llistaArticles, false);

                                ImageButton btEliminarArticle   = fila.findViewById(R.id.eliminar_article);
                                TextView idArticle              = fila.findViewById(R.id.id_article);
                                TextView nomArticle             = fila.findViewById(R.id.marca_model_article);

                                idArticle.setText(jsonObject.getString("idBicicleta"));
                                nomArticle.setText(jsonObject.getString("marca") + " " + jsonObject.getString("model"));

                                btEliminarArticle.setVisibility(GONE);

                                btEliminarArticle.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        llistaArticles.removeView(fila);
                                    }
                                });


                                llistaArticles.addView(fila);
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
     * Mostra per pantalla una llista dels scooters que s'han llogat en un lloguer concret.
     * @param llistaArticles LinearLayout on es mostrarà la llista d'scooters
     * @param idLloguer ID del lloguer en el que s'han llogat els scooters
     */
    public void mostrarScootersLloguer(final LinearLayout llistaArticles, String idLloguer) {
        final JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET, URL_LLOGUERS + "?scootersLloguer=" + idLloguer, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i=0; i<response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);

                                final View fila = LayoutInflater.from(context).inflate(R.layout.row_inventari_vista_lloguer, llistaArticles, false);

                                ImageButton btEliminarArticle   = fila.findViewById(R.id.eliminar_article);
                                TextView idArticle              = fila.findViewById(R.id.id_article);
                                TextView nomArticle             = fila.findViewById(R.id.marca_model_article);

                                idArticle.setText(jsonObject.getString("idScooter"));
                                nomArticle.setText(jsonObject.getString("marca") + " " + jsonObject.getString("model"));

                                btEliminarArticle.setVisibility(GONE);


                                llistaArticles.addView(fila);
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
     * Comprova si les bicicletes que es volen llogar estan disponibles per el període de temps triat i,
     * si ho estan, comprova si els scooters estan disponibles.
     * En cas de que algun article ja esitgui llogat per aquell període de temps, mostra un popup
     * per pantalla informant de l'error.
     * @param bicicletesQueEsVolenLlogar Llista amb els IDs de les bicicletes que es volen llogar
     * @param scootersQueEsVolenLlogar Llista amb els IDs dels scooters que es volen llogar
     * @param dataInici Data d'inici del lloguer en format "yyyy-MM-dd HH:mm:ss"
     * @param dataFi Data de fi del lloguer en format "yyyy-MM-dd HH:mm:ss"
     */
    public void comprovarBicicletesScootersDisponibles(final List<String> bicicletesQueEsVolenLlogar, final List<String> scootersQueEsVolenLlogar, final String dataInici, final String dataFi) {
        String url = URL_LLOGUERS + "?comprovarDisponibilitatBicis=1&dataInici=" + dataInici + "&dataFi=" + dataFi;
        final JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        //Llista de les bicicletes llogades entre "dataInici" i "dataFi"
                        List<String> bicicletesLlogades = new LinkedList<>();
                        for (int i=0; i<response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                bicicletesLlogades.add(jsonObject.getString("idBicicleta"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        //Mira si alguna de les bicicletes que volem llogar és una de les bicicletes que ja estan llogades
                        for (String bicicletaQueEsVol : bicicletesQueEsVolenLlogar) {
                            if (bicicletesLlogades.contains(bicicletaQueEsVol)) {
                                Utilitats.mostrarMissatgeError(context, "La bicicleta \"" + bicicletaQueEsVol + "\" no està disponible per aquest període,",
                                        "si us plau, elimina-la de la llista.");
                                return;
                            }
                        }

                        comprovarScootersDisponibles(scootersQueEsVolenLlogar, dataInici, dataFi);
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
     * Comprova si els scooters que es volen llogar estan disponibles per el període de temps triat.
     * @param scootersQueEsVolenLlogar Llista amb els IDs dels scooters que es volen llogar
     * @param dataInici Data d'inici del lloguer en format "yyyy-MM-dd HH:mm:ss"
     * @param dataFi Data de fi del lloguer en format "yyyy-MM-dd HH:mm:ss"
     */
    private void comprovarScootersDisponibles(final List<String> scootersQueEsVolenLlogar, String dataInici, String dataFi) {
        String url = URL_LLOGUERS + "?comprovarDisponibilitatScooters=1&dataInici=" + dataInici + "&dataFi=" + dataFi;
        final JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        //Llista dels scooters llogats entre "dataInici" i "dataFi"
                        List<String> scootersLlogats = new LinkedList<>();
                        for (int i=0; i<response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                scootersLlogats.add(jsonObject.getString("idScooter"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        //Mira si algun dels scooters que volem llogar és un dels scooters que ja estan llogats
                        for (String scooterQueEsVol : scootersQueEsVolenLlogar) {
                            if (scootersLlogats.contains(scooterQueEsVol)) {
                                Utilitats.mostrarMissatgeError(context, "L'scooter \"" + scooterQueEsVol + "\" no està disponible per aquest període,",
                                        "si us plau, elimina'l de la llista.");
                                return;
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
     * Actualitza les dades d'un article existent strDataFi la base de dades
     * @param article Article actualitzat
     * @param esBicicleta <code>true</code> si l'article és una bicicleta, <code>false</code> si és un scooter
     */
    public void actualitzarArticle(Objectes.Article article, final boolean esBicicleta) {
        Gson gson = new Gson();
        final String articleJson = gson.toJson(article);

        String url = URL_INVENTARI + "?article=" + articleJson + "&actualitzar=1";

        if (esBicicleta) url += "&bicicleta=1";
        else url += "&scooter=1";


        StringRequest string_request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Actualitza la llista de bicicletes o scooters
                        if (esBicicleta) ConnexioDades.carregarBicicletes();
                        else ConnexioDades.carregarScooters();
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
