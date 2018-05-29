package com.example.dana.projectefinal.inventari;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
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
     * @param fragmentManager Fragment Manager per canviar de fragment quan es fa click a un lloguer
     * @param estat "enCurs", "pendents" o "finalitzats"
     * @param llistaLloguers LinearLayout on es mostraran els lloguers
     */
    public void mostrarLloguers(final FragmentManager fragmentManager, String estat, LinearLayout llistaLloguers) {
        List<Objectes.Lloguer> llista = null;

        if (estat.equals("enCurs")) llista = llistaLloguersEnCurs;
        else if (estat.equals("pendents")) llista = llistaLloguersPendents;
        else if (estat.equals("finalitzats")) llista = llistaLloguersFinalitzats;

        for (Objectes.Lloguer lloguer : llista) {
            try {

                View fila = LayoutInflater.from(context).inflate(R.layout.row_inventari_lloguers, llistaLloguers, false);

                TextView idLloguer = fila.findViewById(R.id.id_lloguer);
                TextView dataLloguer = fila.findViewById(R.id.data_lloguer);
                TextView articlesLloguer = fila.findViewById(R.id.articles_lloguer);


                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(lloguer.getDataInici());
                int mes = Integer.parseInt(new SimpleDateFormat("MM").format(date));
                String dataInici = new SimpleDateFormat("dd").format(date) + " " + Utilitats.getNomMes(mes) + ", " + new SimpleDateFormat("HH:mm").format(date);

                date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(lloguer.getDataFi());
                mes = Integer.parseInt(new SimpleDateFormat("MM").format(date));
                String dataFi = new SimpleDateFormat("dd").format(date) + " " + Utilitats.getNomMes(mes) + ", " + new SimpleDateFormat("HH:mm").format(date);

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
                                .replace(R.id.frameLayout, new InventariVeureAfegirLloguer())
                                .addToBackStack(null)
                                .commit();
                    }
                });

            } catch (Exception ignored) {}
        }
    }


    /**
     * Actualitza les dades d'un article existent a la base de dades
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
