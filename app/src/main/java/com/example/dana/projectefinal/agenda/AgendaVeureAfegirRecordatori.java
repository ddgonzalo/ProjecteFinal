package com.example.dana.projectefinal.agenda;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.airbnb.lottie.LottieAnimationView;
import com.example.dana.projectefinal.ConnexioDades;
import com.example.dana.projectefinal.MainActivity;
import com.example.dana.projectefinal.Objectes;
import com.example.dana.projectefinal.PlaceArrayAdapter;
import com.example.dana.projectefinal.R;
import com.example.dana.projectefinal.Utilitats;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.google.android.gms.common.api.GoogleApiClient.*;


public class AgendaVeureAfegirRecordatori extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,
        OnConnectionFailedListener, ConnectionCallbacks {


    boolean horaModificada;

    final boolean NOU_RECORDATORI;
    boolean recordatoriEditat;
    String dataAntigaRecordatori; //per si el recordatori s'edita, saber quin era

    View view;
    ConnexioAgenda bd;
    Objectes.Recordatori recordatoriActual;

    ViewSwitcher switcher;
    ImageButton btEliminar;
    ImageButton btEnrere;
    Button btSuperior;

    ConstraintLayout canviarData, canviarEtiqueta;
    TextView tvData, tvHora, tvEtiqueta;

    MaterialEditText titolRecordatori;
    MaterialEditText textRecordatori;
    AutoCompleteTextView ubicacioRecordatori;
    TextView etiquetaRecordatori;
    TextView etiquetaText;

    GoogleApiClient googleApiClient;
    PlaceArrayAdapter placesArrayAdapter;
    final LatLngBounds BOUNDS = new LatLngBounds(new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    String dataRecordatori, horaRecordatori; //data i hora en el format que es guarda a la base de dades
    String missatgeErrorGuardar;

//--------------------------------------------------------------------------------------------------


    /**
     * Constructor
     * @param nouRecordatori TRUE si estem creant un recordatori nou, FALSE si estem editant un recordatori existent
     */
    public AgendaVeureAfegirRecordatori() {
        NOU_RECORDATORI = true;
        horaModificada = false;
    }


    public AgendaVeureAfegirRecordatori(Objectes.Recordatori recordatori) {
        NOU_RECORDATORI = false;
        recordatoriActual = recordatori;
        horaModificada = false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.agenda_afegir_recordatori, null);
        bd = new ConnexioAgenda(getContext());

        inicialitzarViews();

        //perquè els botons tinguin un efecte de fer-se més grans al fer-lis click
        btEliminar.setOnTouchListener(Utilitats.onTouchListener(btEliminar));
        btEnrere.setOnTouchListener(Utilitats.onTouchListener(btEnrere));
        btSuperior.setOnTouchListener(Utilitats.onTouchListener(btSuperior));

        btEnrere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tornarEnrere(1);
            }
        });


        btEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarConfirmacioEliminarRecordatori();
            }
        });



        btSuperior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realitzarAccioEditarOGuardar();
            }
        });


        canviarEtiqueta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canviarEtiqueta();
            }
        });


        canviarData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar now = Calendar.getInstance();
                DatePickerDialog datePicker = DatePickerDialog.newInstance(
                        AgendaVeureAfegirRecordatori.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                datePicker.show(getActivity().getFragmentManager(), "Datepickerdialog");
            }
        });


        //per a l'AutoCompleteTextView
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(getActivity(), 0, this)
                .addConnectionCallbacks(this)
                .build();

        ubicacioRecordatori.setThreshold(3);
        ubicacioRecordatori.setOnItemClickListener(autocompleteClickListener);


        //el primer item de la llista sempre serà "A la botiga"
        //això està definit dins la classe PlaceArrayAdapter
        placesArrayAdapter = new PlaceArrayAdapter(getContext(), android.R.layout.simple_list_item_1, BOUNDS, null);
        ubicacioRecordatori.setAdapter(placesArrayAdapter);

        if (NOU_RECORDATORI) {
            recordatoriEditat = false;
            btEliminar.setVisibility(GONE);
            recordatoriActual = new Objectes.Recordatori();

            btSuperior.setText("Guardar");
            canviarData.setClickable(true);
            canviarEtiqueta.setClickable(true);
            titolRecordatori.setFocusableInTouchMode(true);
            ubicacioRecordatori.setFocusableInTouchMode(true);
            textRecordatori.setFocusableInTouchMode(true);

            horaRecordatori = new SimpleDateFormat("HH:mm").format(new Date());
            tvHora.setText(horaRecordatori);

            dataRecordatori = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            //Quan s'obre la finestra, per defecte l'etiqueta posada és "PERSONAL"
            Drawable[] drawables = etiquetaText.getCompoundDrawables(); //left, top, right, bottom
            drawables[0].setColorFilter(new PorterDuffColorFilter(Color.parseColor(ConnexioDades.llistaEtiquetes.get("PERSONAL")), PorterDuff.Mode.SRC_IN));

        }
        else {
            recordatoriEditat = false;
            btSuperior.setText("Editar");
            btEliminar.setVisibility(GONE);

            canviarData.setClickable(false);
            canviarEtiqueta.setClickable(false);
            ubicacioRecordatori.setFocusableInTouchMode(false);

            textRecordatori.setFocusableInTouchMode(false);
            titolRecordatori.setFocusableInTouchMode(false);

            titolRecordatori.setText(recordatoriActual.getTitol());
            textRecordatori.setText(recordatoriActual.getText());

            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(recordatoriActual.getData());
                dataRecordatori = new SimpleDateFormat("yyyy-MM-dd").format(date);
                horaRecordatori = Utilitats.getHora(recordatoriActual.getData());


                tvData.setText(Utilitats.getDia(recordatoriActual.getData()));
                tvHora.setText(horaRecordatori);
            } catch (Exception ignored) {}

            ubicacioRecordatori.setText(recordatoriActual.getUbicacio());

            etiquetaRecordatori.setText(recordatoriActual.getTipusRecordatori());

            final String colorEtiqueta = ConnexioDades.llistaEtiquetes.get(recordatoriActual.getTipusRecordatori());
            Drawable[] drawables = etiquetaText.getCompoundDrawables();
            drawables[0].setColorFilter(new PorterDuffColorFilter(Color.parseColor(colorEtiqueta), PorterDuff.Mode.SRC_IN));
        }



        return view;
    }

//-- fi onCreateView() -----------------------------------------------------------------------------


//-- guardar recordatori ---------------------------------------------------------------------------

    /**
     * Enllaça les variables View amb les seves vistes respectives a l'arxiu XML
     */
    private void inicialitzarViews() {
        switcher            = (ViewSwitcher) view.findViewById(R.id.my_switcher);

        btEnrere            = view.findViewById(R.id.tornar_enrere);
        btEliminar          = view.findViewById(R.id.eliminar_recordatori);
        btSuperior          = view.findViewById(R.id.guardar_recordatori);
        canviarData         = view.findViewById(R.id.contenidorDataHora);
        canviarEtiqueta     = view.findViewById(R.id.contenidorEtiqueta);

        tvData              = view.findViewById(R.id.dataRecordatori);
        tvHora              = view.findViewById(R.id.horaRecordatori);
        tvEtiqueta          = view.findViewById(R.id.etiqueta_recordatori);

        etiquetaText        = view.findViewById(R.id.etiqueta_text);
        etiquetaRecordatori = view.findViewById(R.id.etiqueta_recordatori);
        titolRecordatori    = view.findViewById(R.id.titol_recordatori);
        textRecordatori     = view.findViewById(R.id.text);
        ubicacioRecordatori = view.findViewById(R.id.ubicacio_recordatori);
    }


    /**
     * Comprova si estem a un nou recordatori o estem editant un i, en cas afirmatiu, guarda el recordatori.
     * En canvi, si només estem veient un recordatori, mostra la pantalla d'edició del recordatori.
     */
    private void realitzarAccioEditarOGuardar() {
        if (NOU_RECORDATORI || recordatoriEditat) guardarRecordatori();
        else editarRecordatori();
    }


    /**
     * Comprova si el recordatori compleix els requisits per ser guardat a la base de dades i,
     * si els compleix, el guarda, sinó mostra per pantalla un missatge d'error
     */
    private void guardarRecordatori() {
        if (potGuardar()) {
            recordatoriActual.setTitol(titolRecordatori.getText().toString());
            recordatoriActual.setText(textRecordatori.getText().toString());
            recordatoriActual.setData(dataRecordatori + " " + horaRecordatori);
            recordatoriActual.setEstat(1);
            recordatoriActual.setUbicacio(ubicacioRecordatori.getText().toString());
            recordatoriActual.setTipusRecordatori(etiquetaRecordatori.getText().toString());


            if (NOU_RECORDATORI) bd.guardarRecordatori(recordatoriActual);
            else bd.editarRecordatori(recordatoriActual, dataAntigaRecordatori);

            LottieAnimationView lottie = switcher.findViewById(R.id.animation_view);

            //afegim un listener a l'animació perquè torni enrere quan acabi
            lottie.addAnimatorListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {}

                @Override
                public void onAnimationEnd(Animator animator) {

                    try {
                        Thread.sleep(50); //perquè abans de canviar de fragment esperi una mica (sinó acaba molt de sobte)
                    } catch (InterruptedException ignored) {}

                    //Tenca la connexió amb Google API
                    googleApiClient.stopAutoManage(getActivity());
                    googleApiClient.disconnect();

                    //Canvi de fragment
                    getFragmentManager().popBackStack(); //perquè no es vagin acumulant fragments

                    getFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, new AgendaMain())
                            .commit();

                    MainActivity.canviarPestaña(1);
                }

                @Override
                public void onAnimationCancel(Animator animator) {}

                @Override
                public void onAnimationRepeat(Animator animator) {}
            });

            switcher.showNext();
            lottie.playAnimation();
            horaModificada = false;
        }

        else { //No compleix algún requisit per guardar el recordatori
            Utilitats.mostrarMissatgeError(getContext(), "No s'ha pogut guardar el recordatori,", missatgeErrorGuardar);
        }
    }


    /**
     * Comprova si el recordatori compleix els requisits per poder ser guardat a la base de dades
     * Els requisits són:
     * - El títol no pot estar buit
     * - El títol ha de tenir menys de 30 caràcters
     * - El text del recordatori no pot estar buit
     * - La ubicació no pot estar buida
     *
     * @return <code>true</code> si el recordatori compleix els requisits
     */
    private boolean potGuardar() {

        if (titolRecordatori.getText().toString().equals(null) || titolRecordatori.getText().toString().equals("")) {
            missatgeErrorGuardar = "el títol no pot estar buit.";
            return false;
        }

        if (titolRecordatori.getText().toString().length()>30) {
            missatgeErrorGuardar = "el títol no pot tenir més de 30 caràcters.";
            return false;
        }

        if (textRecordatori.getText().toString().equals(null) || textRecordatori.getText().toString().equals("")) {
            missatgeErrorGuardar = "el text del recordatori no pot estar buit.";
            return false;
        }

        if (ubicacioRecordatori.getText().toString().equals(null) || ubicacioRecordatori.getText().toString().equals("")) {
            missatgeErrorGuardar = "l'ubicació no pot estar buida.";
            return false;
        }

        if (horaModificada) {
            List<String> dates = new LinkedList<>();

            for (Objectes.Recordatori r : ConnexioAgenda.llistaAvui) dates.add(r.getData());
            for (Objectes.Recordatori r : ConnexioAgenda.llistaDema) dates.add(r.getData());
            for (Objectes.Recordatori r : ConnexioAgenda.llistaSetmana) dates.add(r.getData());
            for (Objectes.Recordatori r : ConnexioAgenda.llistaMes) dates.add(r.getData());

            if (dates.contains(dataRecordatori + " " + horaRecordatori)) {
                missatgeErrorGuardar = "ja hi ha un recordatori per aquesta data i hora.";
                return false;
            }
        }

        return true;
    }


//-- editar recordatori ----------------------------------------------------------------------------


    /**
     * Permet editar el recordatori fent que el títol, l'ubicació, l'etiqueta, la data i el text
     * del recordatori siguin editables
     */
    private void editarRecordatori() {
        recordatoriEditat = true;
        btEliminar.setVisibility(VISIBLE);
        dataAntigaRecordatori = recordatoriActual.getData();

        btSuperior.setText("Guardar");

        titolRecordatori.setFocusableInTouchMode(true);
        textRecordatori.setFocusableInTouchMode(true);
        ubicacioRecordatori.setFocusableInTouchMode(true);
        canviarData.setClickable(true);
        canviarEtiqueta.setClickable(true);
    }

//-- tornar enrere ---------------------------------------------------------------------------------

    /**
     * Mostra un missatge de confirmació per forçar la sortida, ja que la nota no s'ha guardat
     * @param idPestaña Pestaña a la que es vol canviar
     */
    private void tornarEnrere(final int idPestaña) {
        if (NOU_RECORDATORI || recordatoriEditat) {
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
                            .replace(R.id.frameLayout, new AgendaMain())
                            .commit();

                    MainActivity.canviarPestaña(idPestaña);

                    dialog.dismiss();
                }
            });
        }

        else {
            //Tenca la connexió amb Google API
            googleApiClient.stopAutoManage(getActivity());
            googleApiClient.disconnect();

            getFragmentManager().popBackStack(); //perquè no es vagin acumulant fragments

            getFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, new AgendaMain())
                    .commit();

            MainActivity.canviarPestaña(idPestaña);
        }
    }

//-- Triar una etiqueta per el recordatori ---------------------------------------------------------


    /**
     * Mostra per pantalla un PopUp amb la llista d'etiquetes EXISTENTS i permet triar una per posar-li al recordatori
     */
    private void canviarEtiqueta() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View dview = inflater.inflate(R.layout.agenda_afegir_recordatori_etiquetes, null);

        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dview);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        LinearLayout linearLayoutEtiquetes = (LinearLayout) dview.findViewById(R.id.llista_etiquetes);


        //per a cada etiqueta que hi hagi, afegeix una fila al LinearLayout
        for (final String etiqueta : ConnexioDades.llistaEtiquetes.keySet()) {
            View fila = LayoutInflater.from(getContext()).inflate(R.layout.row_agenda_etiquetes, linearLayoutEtiquetes, false);

            //ImageView iconaEtiqueta = fila.findViewById(R.id.icona_etiqueta);
            TextView nomEtiqueta    = fila.findViewById(R.id.nom_etiqueta);

            final String colorEtiqueta = ConnexioDades.llistaEtiquetes.get(etiqueta);
            Drawable[] drawables = nomEtiqueta.getCompoundDrawablesRelative();
            drawables[0].setColorFilter(new PorterDuffColorFilter(Color.parseColor(colorEtiqueta), PorterDuff.Mode.SRC_IN));
            nomEtiqueta.setText(etiqueta);

            linearLayoutEtiquetes.addView(fila);

            fila.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    etiquetaRecordatori.setText(etiqueta);

                    Drawable[] drawables = etiquetaText.getCompoundDrawables(); //left, top, right, bottom
                    drawables[0].setColorFilter(new PorterDuffColorFilter(Color.parseColor(colorEtiqueta), PorterDuff.Mode.SRC_IN));
                    dialog.dismiss();
                }
            });
        }

        dialog.show();
    }


//-- Eliminar el recordatori -----------------------------------------------------------------------


    /**
     * Mostra per pantalla un popup per confirmar que el recordatori es vol eliminar de la base de dades
     */
    private void mostrarConfirmacioEliminarRecordatori() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View dview = inflater.inflate(R.layout.popup_confirmacio, null);

        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dview);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.show();


        Button btCancelar = (Button) dview.findViewById(R.id.cancelar);
        Button btAcceptar = (Button) dview.findViewById(R.id.acceptar);
        TextView primeraLinia   = (TextView) dview.findViewById(R.id.primera_linia);
        TextView segonaLinia    = (TextView) dview.findViewById(R.id.segona_linia);

        primeraLinia.setText("Segur que vols eliminar aquest recordatori?");
        segonaLinia.setText("Un cop eliminat, no es podrà recuperar.");


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
                        .replace(R.id.frameLayout, new AgendaMain())
                        .commit();

                bd.eliminarRecordatori(recordatoriActual.getData());
                MainActivity.canviarPestaña(1);

                dialog.dismiss();
            }
        });
    }


//-- Triar data i hora del recordatori -------------------------------------------------------------


    /**
     * Què fer quan s'ha triat una hora per el recordatori
     * @param view Dialog on es mostra el Timepicker
     * @param hourOfDay Hora triada (0-24)
     * @param minute Minuts triats (0-60)
     * @param second Segons triats. Sempre és "0"
     */
    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

        horaModificada  = true;

        String strHora = (hourOfDay < 10) ? "0" + String.valueOf(hourOfDay) : String.valueOf(hourOfDay);
        String strMinuts = (minute < 10) ? "0" + String.valueOf(minute) : String.valueOf(minute);
        String strSegons = (second < 10) ? "0" + String.valueOf(second) : String.valueOf(second);

        tvHora.setText(strHora + ":" + strMinuts);
        horaRecordatori = strHora + ":" + strMinuts + ":" + strSegons;
    }



    /**
     * Què fer quan s'ha triat una data per el recordatori
     * @param view Dialog on es mostra el DatePicker
     * @param year Any triat
     * @param monthOfYear Més triat, amb índex 0 (0-11)
     * @param dayOfMonth Día del mes triat (0-31 depenent del mes)
     */
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        String mes = Utilitats.getNomMes(++monthOfYear);


        //per poder-ho comparar amb la data actual, el camp "mes" ha de tenir dos dígits
        if (monthOfYear<10) dataRecordatori = year + "-0" + monthOfYear + "-" + dayOfMonth;
        else dataRecordatori = year + "-" + monthOfYear + "-" + dayOfMonth; //format que es guardarà a la base de dades

        //per saber si la data triada és la data actual ("Avui")
        if (dataRecordatori.equals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))) {
            tvData.setText("Avui");
        }
        else {
            tvData.setText(dayOfMonth + " " + mes + " del " + year);
        }

        //després de triar una data, automáticament s'obre un PopUp per triar una hora
        Calendar now = Calendar.getInstance();

        TimePickerDialog timePicker = TimePickerDialog.newInstance(
                AgendaVeureAfegirRecordatori.this,
                now.get(Calendar.HOUR),
                now.get(Calendar.MINUTE),
                true
        );

        timePicker.show(getActivity().getFragmentManager(), "Timepickerdialog");
    }



//-- Per a l'AutoCompleteTextView ------------------------------------------------------------------

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


//-- Necessaris per a Google Places API ------------------------------------------------------------

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



//-- Cicle de vida del fragment --------------------------------------------------------------------

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this); //comença a rebre notificacions de l'EventBus
    }


    @Override
    public void onPause() {
        super.onPause();

        //Tenca la connexió amb Google API
        googleApiClient.stopAutoManage(getActivity());
        googleApiClient.disconnect();

        EventBus.getDefault().unregister(this); //deixa de rebre notificacions de l'EventBus
    }



//-- EventBus --------------------------------------------------------------------------------------

    @Subscribe
    public void handleEvents(String missatge) {

        if (missatge.contains("sortir")) {
            //Quan es prem algun botó del menú inferior arriba aquest missatge
            tornarEnrere(Integer.parseInt(missatge.split(" ")[1]));
        }
    }
}
