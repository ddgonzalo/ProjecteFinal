package com.example.dana.projectefinal.inventari;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dana.projectefinal.ConnexioClients;
import com.example.dana.projectefinal.ConnexioDades;
import com.example.dana.projectefinal.FilterWithSpaceAdapter;
import com.example.dana.projectefinal.MainActivity;
import com.example.dana.projectefinal.Objectes;
import com.example.dana.projectefinal.R;
import com.example.dana.projectefinal.Utilitats;
import com.example.dana.projectefinal.PlaceArrayAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class InventariVeureAfegirLloguer extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    final boolean NOU_LLOGUER;
    boolean lloguerEditat;

    //variables per si venim de "DISPONIBILITAT" després d'haver comprovat si un article estava disponible
    boolean venimDesdeDisponibilitat;
    String articleInicial;

    Objectes.Lloguer lloguerActual;
    ConnexioInventari connexio;
    View view;

    TextView titol;
    ImageButton btTornarEnrere;
    Button btRealitzarAccio;

    ConstraintLayout contenidorDataInici, contenidorDataFi, contenidorClient;
    TextView tvDataInici, tvHoraInici, tvDataFi, tvHoraFi, tvPreu, tvClient;
    AutoCompleteTextView acLlocEntrega, acLlocRecollida;

    LinearLayout llistaArticles;
    ConstraintLayout layoutAfegirArticle;
    TextView btAfegirArticle;
    AutoCompleteTextView acArticleNou;


    FilterWithSpaceAdapter<String> adapter;
    List<String> llistaBicicletesScooters;

    //List<String> llistaBicicletesLlogades, llistaScootersLlogats;

    String strDataInici, strDataFi, strHoraInici, strHoraFi, strDniClient;

    TimePickerDialog timePicker;
    DatePickerDialog datePicker;
    GoogleApiClient googleApiClient;
    PlaceArrayAdapter placesArrayAdapter;
    final LatLngBounds BOUNDS = new LatLngBounds(new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    String motiuErrorGuardar;


    /**
     * Constructor per quan es vol crear un nou recordatori
     */
    public InventariVeureAfegirLloguer() {
        this.lloguerActual = new Objectes.Lloguer();
        NOU_LLOGUER = true;
        lloguerEditat = false;
        venimDesdeDisponibilitat = false;
    }


    /**
     * Constructor per quan es vol veure un nou recordatori
     * @param lloguerActual Lloguer que es vol veure
     */
    @SuppressLint("ValidFragment")
    public InventariVeureAfegirLloguer(Objectes.Lloguer lloguerActual) {
        this.lloguerActual = lloguerActual;
        NOU_LLOGUER = false;
        lloguerEditat = false;
        venimDesdeDisponibilitat = false;
    }

    /**
     * Constructor per quan venim de la pantalla "DISPONIBILITAT"
     * @param esBici <code>true</code> si l'article que es vol llogar és una bicicleta o un scooter
     * @param idArticle ID de l'article que es vol llogar
     * @param dataHoraInici Data i hora de l'inici del lloguer en format "yyyy-MM-dd HH:mm:ss"
     * @param dataHoraFi Data i hora del fi del lloguer en format "yyyy-MM-dd HH:mm:ss"
     */
    @SuppressLint("ValidFragment")
    public InventariVeureAfegirLloguer(boolean esBici, String idArticle, String dataHoraInici, String dataHoraFi) {
        this.lloguerActual = new Objectes.Lloguer();
        NOU_LLOGUER = true;
        lloguerEditat = false;
        venimDesdeDisponibilitat = true;

        if (esBici) articleInicial = idArticle + " ";
        else articleInicial = idArticle;

        lloguerActual.setDataInici(dataHoraInici);
        lloguerActual.setDataFi(dataHoraFi);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.lloguers_afegir_lloguer, null);
        connexio = new ConnexioInventari(getContext());

        inicialitzarViews();

        //Es podrà afegir qualsevol article, sigui bicicleta o scooter
        llistaBicicletesScooters = new LinkedList<>();

        for (Integer idBici : ConnexioDades.magatzemBicis.keySet()) {
            Objectes.Article bici =  ConnexioDades.llistaBicicletes.get(ConnexioDades.magatzemBicis.get(idBici));
            llistaBicicletesScooters.add (idBici + " - " + bici.getMarca() + " " + bici.getModel() + " "); //si té un espai al final, vol dir que és una bicicleta
        }

        for (Integer idScooter: ConnexioDades.magatzemScooters.keySet()) {
            Objectes.Article scooter = ConnexioDades.llistaScooters.get(ConnexioDades.magatzemScooters.get(idScooter));
            llistaBicicletesScooters.add(idScooter + " - " + scooter.getMarca() + " " + scooter.getModel());
        }

        adapter = new FilterWithSpaceAdapter<>(getContext(),android.R.layout.simple_list_item_1, llistaBicicletesScooters);
        acArticleNou.setThreshold(1);
        acArticleNou.setAdapter(adapter);

        mostrarDades();

        //per triar dates i hores
        Calendar calendarNow = Calendar.getInstance();

        datePicker = DatePickerDialog.newInstance(
                InventariVeureAfegirLloguer.this,
                calendarNow.get(Calendar.YEAR),
                calendarNow.get(Calendar.MONTH),
                calendarNow.get(Calendar.DAY_OF_MONTH)
        );

        timePicker = TimePickerDialog.newInstance(
                InventariVeureAfegirLloguer.this,
                calendarNow.get(Calendar.HOUR),
                calendarNow.get(Calendar.MINUTE),
                true
        );


        //per strDataFi l'AutoCompleteTextView
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(getActivity(), 0, this)
                .addConnectionCallbacks(this)
                .build();

        acLlocEntrega.setThreshold(3);
        acLlocEntrega.setOnItemClickListener(autocompleteClickListener);

        acLlocRecollida.setThreshold(3);
        acLlocRecollida.setOnItemClickListener(autocompleteClickListener);


        //el primer item de la llista sempre serà "A la botiga"
        //això està definit dins la classe PlaceArrayAdapter
        placesArrayAdapter = new PlaceArrayAdapter(getContext(), android.R.layout.simple_list_item_1, BOUNDS, null);
        acLlocRecollida.setAdapter(placesArrayAdapter);
        acLlocEntrega.setAdapter(placesArrayAdapter);


        btTornarEnrere.setOnClickListener(this);
        btAfegirArticle.setOnClickListener(this);
        btRealitzarAccio.setOnClickListener(this);
        contenidorDataInici.setOnClickListener(this);
        contenidorDataFi.setOnClickListener(this);
        contenidorClient.setOnClickListener(this);

        btTornarEnrere.setOnTouchListener(Utilitats.onTouchListener(btTornarEnrere));
        btRealitzarAccio.setOnTouchListener(Utilitats.onTouchListener(btRealitzarAccio));
        btAfegirArticle.setOnTouchListener(Utilitats.onTouchListener(btAfegirArticle));

        return view;
    }

//-- fi onCreateView() -----------------------------------------------------------------------------

    /**
     * Enllaça les variables View amb la seva vista corresponent strDataFi l'arxiu XML
     */
    private void inicialitzarViews() {

        titol                   = view.findViewById(R.id.titol);
        btTornarEnrere          = view.findViewById(R.id.tornar_enrere);
        btRealitzarAccio        = view.findViewById(R.id.realitzar_accio);

        contenidorDataInici     = view.findViewById(R.id.contenidor_data_hora_inici);
        contenidorDataFi        = view.findViewById(R.id.contenidor_data_hora_fi);
        contenidorClient        = view.findViewById(R.id.contenidor_client);

        tvDataInici             = view.findViewById(R.id.data_inici);
        tvHoraInici             = view.findViewById(R.id.hora_inici);
        tvDataFi                = view.findViewById(R.id.data_fi);
        tvHoraFi                = view.findViewById(R.id.hora_fi);

        tvClient                = view.findViewById(R.id.client);
        acLlocEntrega           = view.findViewById(R.id.lloc_entrega);
        acLlocRecollida         = view.findViewById(R.id.lloc_recollida);

        tvPreu                  = view.findViewById(R.id.preu_lloguer);

        llistaArticles          = view.findViewById(R.id.llista_articles_llogar);
        layoutAfegirArticle = view.findViewById(R.id.ultim_row);

        btAfegirArticle         = view.findViewById(R.id.bt_afegir_article);
        acArticleNou            = view.findViewById(R.id.autocomplete_article_afegir);
    }


    private void guardarOEditar() {
        if (NOU_LLOGUER || lloguerEditat)  guardarLloguer();
        else editarLloguer();
    }


    /**
     * Mira si estem creant un lloguer o veient un i, depenent de quin dels dos sigui,
     * habilita i deshabilita vistes diferents
     */
    private void mostrarDades() {

        if (NOU_LLOGUER) {
            Date dateAvui = new Date();
            strDataInici = new SimpleDateFormat("yyyy-MM-dd").format(dateAvui);
            strDataFi = strDataInici;
            strHoraInici = "09:00:00";
            strHoraFi = "10:00:00";


            btRealitzarAccio.setText("Guardar");
            layoutAfegirArticle.setVisibility(VISIBLE);
            acArticleNou.setVisibility(GONE);
            tvPreu.setVisibility(GONE);

            if (venimDesdeDisponibilitat) {

                mostrarDadesInicials();

                acArticleNou.setVisibility(VISIBLE);
                acArticleNou.setText(articleInicial);
                afegirArticleAlLloguer();
            }
        }
        else {
            titol.setText("Vista lloguer");
            btRealitzarAccio.setText("Editar");
            btRealitzarAccio.setVisibility(GONE);

            tvClient.setFocusableInTouchMode(false);
            acLlocEntrega.setFocusableInTouchMode(false);
            acLlocRecollida.setFocusableInTouchMode(false);
            layoutAfegirArticle.setVisibility(GONE);
            tvPreu.setVisibility(VISIBLE);

            connexio.mostrarBicicletesLloguer(llistaArticles, lloguerActual.getId());
            connexio.mostrarScootersLloguer(llistaArticles, lloguerActual.getId());

            mostrarDadesInicials();
        }
    }

//--------------------------------------------------------------------------------------------------


    private void mostrarDadesInicials() {

        tvClient.setText(lloguerActual.getClient().equals("null") ? "Sense client" :
                ConnexioClients.llistaClients.get(lloguerActual.getClient()).getNom() + " (" + lloguerActual.getClient() + ")");

        tvPreu.setText("Preu: " + String.format("%.2f", lloguerActual.getPreu()) + "€");
        tvDataInici.setText(Utilitats.getDia(lloguerActual.getDataInici()));
        tvHoraInici.setText(Utilitats.getHora(lloguerActual.getDataInici()));
        tvDataFi.setText(Utilitats.getDia(lloguerActual.getDataFi()));
        tvHoraFi.setText(Utilitats.getHora(lloguerActual.getDataFi()));
    }


//--------------------------------------------------------------------------------------------------


    /**
     * Afegeix un article nou strDataFi la llista d'articles que es volen llogar
     */
    private void afegirArticleAlLloguer() {

        if (acArticleNou.getVisibility() == GONE) {
            acArticleNou.setVisibility(VISIBLE);

        }
        else {
            if (potAfegirArticle()) {
                final View filaArticle = LayoutInflater.from(getContext()).inflate(R.layout.row_lloguers_articles, llistaArticles, false);

                ImageButton btEliminarArticle   = filaArticle.findViewById(R.id.eliminar_article);
                TextView idArticle              = filaArticle.findViewById(R.id.id_article);
                TextView nomArticle             = filaArticle.findViewById(R.id.marca_model_article);

                final String article = acArticleNou.getText().toString();
                idArticle.setText(article.split(" - ")[0]);
                nomArticle.setText(article.split(" - ")[1]);


                final List<String> llistaAfegir;

                if (article.charAt(article.length()-1)==' ') {
                    llistaAfegir = lloguerActual.getLlistaBicicletes();
                    String aux = nomArticle.getText().toString();
                    nomArticle.setText(aux.substring(0, aux.length()-1));
                }
                else {
                    llistaAfegir = lloguerActual.getLlistaScooters();
                }


                acArticleNou.setText("");
                llistaArticles.addView(filaArticle);

                adapter.remove(article);
                adapter.notifyDataSetInvalidated();

                llistaAfegir.add(article.split( " - ")[0]);

                btEliminarArticle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        llistaArticles.removeView(filaArticle);
                        llistaAfegir.remove(article.split(" - ")[0]);
                        adapter.add(article);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }


    /**
     * Comprova si l'article triat compleix els requisits per poder llogar-se. Els requisits són:
     * - Ha d'existir strDataFi la base de dades
     * - No ha d'estar buit
     * @return <code>true</code> si l'article compleix els requisits
     */
    private boolean potAfegirArticle() {

        if (!llistaBicicletesScooters.contains(acArticleNou.getText().toString())) {
            Utilitats.mostrarMissatgeError(getContext(), "L'article no existeix,", "si us plau, tria un altre.");
            return false;
        }

        return true;
    }


    /**
     * Si estem veient un lloguer, torna strDataFi la pantalla anterior ("LLOGUERS").
     * Si estem creant o editant un lloguer, comprova si es compleixen els requisists per tornar enrere
     * (especificats strDataFi la funció <code>potTornarEnrere</code>).
     * Si compleix els requisits, mostra un missatge de confirmació per sortir i, si no, mostra un missatge
     * informant de l'error.
     */
    private void tornarEnrere() {
        if (NOU_LLOGUER || lloguerEditat) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


            View dview = inflater.inflate(R.layout.popup_confirmacio, null);

            final Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(dview);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            dialog.show();


            Button btCancelar = (Button) dview.findViewById(R.id.cancelar);
            Button btAcceptar = (Button) dview.findViewById(R.id.acceptar);

            btCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });


            btAcceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Tenca la connexió amb Google API
                    googleApiClient.stopAutoManage(getActivity());
                    googleApiClient.disconnect();

                    getFragmentManager().popBackStack(); //perquè no es vagin acumulant fragments

                    getFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, new InventariLloguersVentes())
                            .commit();


                    dialog.dismiss();
                }
            });
        }
        else {
            MainActivity.canviarPestaña(3);
            getFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, new InventariLloguersVentes())
                    .commit();
        }
    }



//-- guardar lloguer -------------------------------------------------------------------------------

    private void guardarLloguer() {
        lloguerActual.setDataInici(strDataInici + " " + strHoraInici);
        lloguerActual.setDataFi(strDataFi + " " + strHoraFi);
        lloguerActual.setLlocRecollida(acLlocRecollida.getText().toString());
        lloguerActual.setLlocEntrega(acLlocEntrega.getText().toString());

        if (potGuardar()) {

            /* La base de dades comprovarà, en el següent ordre:
             *    - que totes  les bicicletes que es volen llogar estiguin disponibles per el període triat
             *    - que tots els scooters que es volen llogar també estiguin disponibles
             *    - preu total del lloguer
             * Si tot és correcte, mostrarà un missatge informant del preu del lloguer i la seva totalització.
             * Si alguna bicicleta o scooter no està disponible, mostrarà un popup per pantalla informant de l'error.*/
            connexio.guardarLloguer(getActivity().getSupportFragmentManager(), lloguerActual, tvPreu);
        }
        else {
            Utilitats.mostrarMissatgeError(getContext(), "No s'ha pogut realitzar el lloguer,", motiuErrorGuardar);
        }
    }


    private boolean potGuardar() {

        if (lloguerActual.getLlistaBicicletes().isEmpty() && lloguerActual.getLlistaScooters().isEmpty()) {
            motiuErrorGuardar = "no has triat cap article.";
            return false;
        }

        return true;
    }


//-- editar lloguer --------------------------------------------------------------------------------

    private void editarLloguer() {
        lloguerEditat = true;
        btRealitzarAccio.setText("Guardar");
        contenidorClient.setFocusableInTouchMode(true);
        acLlocEntrega.setFocusableInTouchMode(true);
        acLlocRecollida.setFocusableInTouchMode(true);
        layoutAfegirArticle.setVisibility(VISIBLE);

        //Permet eliminar els articles
        for (int i=0; i<llistaArticles.getChildCount(); i++) {
            ConstraintLayout filaArticle = (ConstraintLayout) llistaArticles.getChildAt(i);

            ImageButton btEliminar = filaArticle.findViewById(R.id.eliminar_article);
            btEliminar.setVisibility(VISIBLE);
        }
    }


//-- Triar client ----------------------------------------------------------------------------------

    private void mostrarClients() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View dview = inflater.inflate(R.layout.lloguers_triar_client, null);

        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dview);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.show();

        EditText buscadorClients    = dview.findViewById(R.id.buscador_client);
        final ListView listviewClients    = dview.findViewById(R.id.listview_clients);

        final List<String> llistatClients = new LinkedList<>();
        llistatClients.add("null-");
        for (String dniClient : ConnexioClients.llistaClients.keySet()) {
            if (!dniClient.equals("null"))
                llistatClients.add(dniClient + "-" + ConnexioClients.llistaClients.get(dniClient).getNom());
        }


        //L'adapter de la listview de l'inventari també serveix per aquí, ja que només té un ID (dni), i un nom d'article (nom de client)
        //Estat inicial de la llista, mostrant tots els clients
        AdapterClients adapterInicial = new AdapterClients(getContext(), llistatClients, tvClient, dialog, lloguerActual);
        listviewClients.setAdapter(adapterInicial);

        //Cerca dinàmica de bicicletes
        final ArrayList<String> temp = new ArrayList<>();
        final AdapterClients adapter = new AdapterClients(getContext(), temp, tvClient, dialog, lloguerActual);

        buscadorClients.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                int textLength = charSequence.length();
                temp.clear(); //cada vez que se escribe/borra, la lista de vendedores va cambiando

                for (String v : llistatClients) {
                    //Se puede buscar por nomClient o código de vendedor
                    if (textLength <= v.length()) {
                        if (v.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            temp.add(v);
                        }
                    }
                }

                listviewClients.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

//-- Triar data i hora -----------------------------------------------------------------------------

    /**
     * Què fer quan llistaBicicletesLlogades'ha triat una hora per el recordatori
     * @param view Dialog on es mostra el Timepicker
     * @param hourOfDay Hora triada (0-24)
     * @param minute Minuts triats (0-60)
     * @param second Segons triats. Sempre és "0"
     */
    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

        //horaModificada  = true;

        String strHora = (hourOfDay < 10) ? "0" + String.valueOf(hourOfDay) : String.valueOf(hourOfDay);
        String strMinuts = (minute < 10) ? "0" + String.valueOf(minute) : String.valueOf(minute);
        String strSegons = (second < 10) ? "0" + String.valueOf(second) : String.valueOf(second);


        if (timePicker.getTag().equals("inici")) {
            tvHoraInici.setText(strHora + ":" + strMinuts);
            strHoraInici = strHora + ":" + strMinuts + ":" + strSegons;
        }
        else {
            tvHoraFi.setText(strHora + ":" + strMinuts);
            strHoraFi = strHora + ":" + strMinuts + ":" + strSegons;
        }
    }



    /**
     * Què fer quan llistaBicicletesLlogades'ha triat una data per el recordatori
     * @param view Dialog on es mostra el DatePicker
     * @param year Any triat
     * @param monthOfYear Més triat, amb índex 0 (0-11)
     * @param dayOfMonth Día del mes triat (0-31 depenent del mes)
     */
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        String mes = Utilitats.getNomMes(++monthOfYear);
        String data;

        //per poder-ho comparar amb la data actual, el camp "mes" ha de tenir dos dígits
        if (monthOfYear<10) data = year + "-0" + monthOfYear + "-" + dayOfMonth;
        else data = year + "-" + monthOfYear + "-" + dayOfMonth; //format que es guardarà strDataFi la base de dades


        String aux = data;

        //per saber si la data triada és la data actual ("Avui")
        if (data.equals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))) {
            data = "Avui";
        }
        else {
            data = dayOfMonth + " " + mes + " del " + year;
        }


        //indica si estem canviant la data d'inici o la de fi del lloguer
        if (datePicker.getTag().equals("inici")) {
            strDataInici = aux;
            tvDataInici.setText(data);
        }
        else {
            strDataFi = aux;
            tvDataFi.setText(data);
        }

        //després de triar una data, automáticament llistaBicicletesLlogades'obre un PopUp per triar una hora
        timePicker.show(getActivity().getFragmentManager(), datePicker.getTag());
    }


//-- Per strDataFi l'AutoCompleteTextView ------------------------------------------------------------------

    private AdapterView.OnItemClickListener autocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            final PlaceArrayAdapter.PlaceAutocomplete item = placesArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(googleApiClient, placeId);
            placeResult.setResultCallback(updatePlaceDetailsCallback);
        }
    };


    private ResultCallback<PlaceBuffer> updatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) return;
        }
    };

//-- Necessaris per strDataFi Google Places API ------------------------------------------------------------

    @Override
    public void onConnected(Bundle bundle) {
        placesArrayAdapter.setGoogleApiClient(googleApiClient);
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    @Override
    public void onConnectionSuspended(int i) {
        placesArrayAdapter.setGoogleApiClient(null);
    }



//--------------------------------------------------------------------------------------------------

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tornar_enrere: tornarEnrere();  break;
            case R.id.bt_afegir_article: afegirArticleAlLloguer(); break;
            case R.id.realitzar_accio: guardarOEditar(); break;

            case R.id.contenidor_data_hora_inici: datePicker.show(getActivity().getFragmentManager(), "inici"); break;
            case R.id.contenidor_data_hora_fi: datePicker.show(getActivity().getFragmentManager(), "fi"); break;

            case R.id.contenidor_client: mostrarClients(); break;

        }
    }

//-- Cicle de vida del fragment --------------------------------------------------------------------

    @Override
    public void onPause() {
        super.onPause();

        //Tenca la connexió amb Google API
        googleApiClient.stopAutoManage(getActivity());
        googleApiClient.disconnect();
    }
}
