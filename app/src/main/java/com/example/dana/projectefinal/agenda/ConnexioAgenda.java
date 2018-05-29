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
import com.example.dana.projectefinal.Utilitats;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.support.v4.app.FragmentManager;

public class ConnexioAgenda {
    private static boolean primeraVegada = true;
    private static RequestQueue queue;
    private Context context;

    public static List<Recordatori> llistaAvui;
    public static List<Recordatori> llistaDema;
    public static List<Recordatori> llistaSetmana;
    public static List<Recordatori> llistaMes;

    //URLs
    final static String SERVIDOR = ConnexioDades.SERVIDOR;
    final static String URL_AGENDA = SERVIDOR + "agenda.php";
    final static String URL_GUARDAR_RECORDATORI = SERVIDOR + "guardar_recordatori.php";


    /**
     * Constructor
     * @param context Contexte on es troba la classe que l'ha inicialitzat
     */
    public ConnexioAgenda(Context context) {
        this.context = context;
        queue = Volley.newRequestQueue(context);
    }


    /**
     * Carrega tots els recordatoris
     */
    public void carregarDades() {
        if (primeraVegada) {
            llistaAvui = new LinkedList<>();
            llistaDema = new LinkedList<>();
            llistaSetmana = new LinkedList<>();
            llistaMes = new LinkedList<>();

            carregarLlista("avui");
            carregarLlista("dema");
            carregarLlista("setmana");
            carregarLlista("mes");

            primeraVegada = false;
        }
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
     */
    public static void carregarLlista (final String quan) {
        final JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET, URL_AGENDA + "?" + quan + "=1", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i=0; i<response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);

                                final Recordatori rec = new Recordatori();

                                rec.setData(jsonObject.getString("data"));
                                rec.setTitol(jsonObject.getString("titol"));
                                rec.setText(jsonObject.getString("text"));
                                rec.setUbicacio(jsonObject.getString("ubicacio"));
                                rec.setTipusRecordatori(jsonObject.getString("tipusRecordatori"));


                                if (quan.equals("avui")) llistaAvui.add(rec);
                                else if (quan.equals("dema")) llistaDema.add(rec);
                                else if (quan.equals("setmana")) llistaSetmana.add(rec);
                                else if (quan.equals("mes")) llistaMes.add(rec);

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
    public void mostrarRecordatoris(final FragmentManager fragmentManager, final String quan, final LinearLayout llistaRecordatoris) {
        List<Recordatori> llista = new LinkedList<>();

        if (quan.equals("avui")) llista.addAll(llistaAvui);
        else if (quan.equals("dema")) llista = llistaDema;
        else if (quan.equals("setmana")) llista = llistaSetmana;
        else  llista = llistaMes;

        for (int i=0; i<llista.size(); i++) {
            try {

                final Recordatori rec = llista.get(i);


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
                recordatori.setText(rec.getTitol());


                //Si els recordatoris que es mostren són els de els próxims 7 dies o els d'aquest mes,
                //s'ha d'indicar de quin dia es tracta (no només la hora)
                if (quan.equals("setmana") || quan.equals("mes")) {
                    int mesRecordatori = Integer.parseInt(new SimpleDateFormat("MM").format(date));

                    String mesRecordatoriStr = Utilitats.getNomMes(mesRecordatori);

                    String dataRecordatori = new SimpleDateFormat("dd").format(date) + " " + mesRecordatoriStr + ", ";
                    dataRecordatori += new SimpleDateFormat("HH:mm").format(date);

                    hora.setText(dataRecordatori);
                } else hora.setText(horaRecordatori);


                llistaRecordatoris.addView(fila);

                fila.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fragmentManager.beginTransaction()
                                .replace(R.id.frameLayout, new AgendaVeureAfegirRecordatori(rec))
                                .addToBackStack(null)
                                .commit();
                    }
                });
            } catch (Exception ignored) {}
        }
    }

//--------------------------------------------------------------------------------------------------

    /**
     * Afegeix un recordatori nou a la base de dades
     * @param recordatori Recordatori que es vol guardar a l'agenda
     */
    public void guardarRecordatori(Recordatori recordatori) {
        Gson gson = new Gson();
        final String recordatoriJson = gson.toJson(recordatori);


        StringRequest string_request = new StringRequest(Request.Method.GET, URL_GUARDAR_RECORDATORI + "?nouRecordatori=" + recordatoriJson,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        primeraVegada = true;
                        carregarDades();
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
     * Modifica un recordatorin donat la seva data antiga,  actualitzant tots els seus camps a la base de dades
     * @param recordatori Recordatori modificat
     * @param dataAntiga Data que tenia el recordatori abans de ser editat, en format "yyyy-MM-dd HH:mm:ss"
     */
    public void editarRecordatori(Recordatori recordatori, String dataAntiga) {
        Gson gson = new Gson();
        final String recordatoriJson = gson.toJson(recordatori);

        StringRequest string_request = new StringRequest(Request.Method.GET, URL_GUARDAR_RECORDATORI + "?recordatori=" + recordatoriJson + "&dataAntiga=" + dataAntiga,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        primeraVegada = true;
                        carregarDades();
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
     * Elimina un recordatori de la base de dades, donada la seva data i hora
     * @param data Data i hora del recordatori, en format "yyyy-MM-dd HH:mm:ss"
     */
    public void eliminarRecordatori(String data) {

        StringRequest string_request = new StringRequest(Request.Method.GET, URL_AGENDA + "?eliminar=" + data,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        primeraVegada = true;
                        carregarDades();
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


//--------------------------------------------------------------------------------------------------

    /**
     * Mostra per pantalla una llista amb els recordatoris que pertanyen a una etiqueta donada
     * @param nomEtiqueta Nom de l'etiqueta de la qual es volen veure els recordatoris
     * @param llistaRecordatoris LinearLayout on s'afegirà la vista del recordatori
     */
    public void mostrarRecordatorisEtiqueta(final FragmentManager fragmentManager, final String nomEtiqueta, final LinearLayout llistaRecordatoris) {
        JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET, URL_AGENDA + "?etiqueta=" + nomEtiqueta, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i=0; i<response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);

                                final Recordatori rec = new Recordatori();

                                rec.setData(jsonObject.getString("data"));
                                rec.setTitol(jsonObject.getString("titol"));
                                rec.setText(jsonObject.getString("text"));
                                rec.setUbicacio(jsonObject.getString("ubicacio"));
                                rec.setTipusRecordatori(nomEtiqueta);

                                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(rec.getData());

                                int mesRecordatori = Integer.parseInt(new SimpleDateFormat("MM").format(date));

                                String mesRecordatoriStr = Utilitats.getNomMes(mesRecordatori);

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

                                fila.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        fragmentManager.beginTransaction()
                                                .replace(R.id.frameLayout, new AgendaVeureAfegirRecordatori(rec))
                                                .addToBackStack(null)
                                                .commit();
                                    }
                                });
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
