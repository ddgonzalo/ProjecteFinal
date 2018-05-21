package com.example.dana.projectefinal;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;

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

import java.util.Calendar;

import static com.google.android.gms.common.api.GoogleApiClient.*;


public class FragmentAgendaAfegirRecordatori extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,
        OnConnectionFailedListener, ConnectionCallbacks {

    ConstraintLayout canviarData, canviarUbicacio;
    TextView tvData, tvHora;
    AutoCompleteTextView ubicacio;

    GoogleApiClient googleApiClient;
    PlaceArrayAdapter placesArrayAdapter;
    final LatLngBounds BOUNDS = new LatLngBounds(new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    String dataRecordatori, horaRecordatori;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.agenda_afegir_recordatori, null);

        ImageButton btEnrere = view.findViewById(R.id.tornar_enrere);

        btEnrere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (potTornarEnrere()) {
                    View aux = view.findViewById(R.id.text);
                    ViewGroup parent = (ViewGroup) aux.getParent();

                    int index = parent.indexOfChild(aux);
                    parent.removeView(aux);

                    aux = getLayoutInflater().inflate(R.layout.lottie_success, parent, false);
                    parent.addView(aux, index);


                    /*getFragmentManager().popBackStack(); //perquè no es vagin acumulant fragments

                    getFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, new FragmentAgenda())
                            .commit();*/
                }
            }
        });

        canviarData      = view.findViewById(R.id.contenidorDataHora);
        canviarUbicacio  = view.findViewById(R.id.contenidorUbicacio);
        tvData           = view.findViewById(R.id.dataRecordatori);
        tvHora           = view.findViewById(R.id.horaRecordatori);

        ubicacio         = view.findViewById(R.id.autoCompleteTextView);


        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(getActivity(), 0, this)
                .addConnectionCallbacks(this)
                .build();

        ubicacio.setThreshold(3);
        ubicacio.setOnItemClickListener(autocompleteClickListener);

        placesArrayAdapter = new PlaceArrayAdapter(getContext(), android.R.layout.simple_list_item_1, BOUNDS, null);
        ubicacio.setAdapter(placesArrayAdapter);

        canviarData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar now = Calendar.getInstance();
                DatePickerDialog datePicker = DatePickerDialog.newInstance(
                        FragmentAgendaAfegirRecordatori.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                datePicker.show(getActivity().getFragmentManager(), "Datepickerdialog");
            }
        });


        return view;
    }

//-- fi onCreateView() -----------------------------------------------------------------------------


    private boolean potTornarEnrere() {
        googleApiClient.stopAutoManage(getActivity());
        googleApiClient.disconnect();

        return true;
    }


    private void obrirDialegConfirmacio() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View tview = inflater.inflate(R.layout.agenda_afegir_recordatori_confirmacio, null);

        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(tview);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.show();
    }

//-- Triar data i hora del recordatori -------------------------------------------------------------

    //quan s'ha triat una hora per el recordatori
    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        tvHora.setText(hourOfDay + ":" + minute);
        horaRecordatori = hourOfDay + ":" + minute + ":" + second;
    }



    //quan s'ha triat una data per el recordatori
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        String mes = null;

        switch (monthOfYear) {
            case 1: mes  =  "gener";    break;
            case 2: mes  =  "febrer";   break;
            case 3: mes  =  "març";     break;
            case 4: mes  =  "abril";    break;
            case 5: mes  =  "maig";     break;
            case 6: mes  =  "juny";     break;
            case 7: mes  =  "juliol";   break;
            case 8: mes  =  "agost";    break;
            case 9: mes  =  "setembre"; break;
            case 10: mes =  "octubre";  break;
            case 11: mes =  "novembre"; break;
            case 12: mes =  "desembre"; break;
        }

        tvData.setText(dayOfMonth + " " + mes + " del " + year);
        dataRecordatori = dayOfMonth + "/" + monthOfYear + "/" + year; //format que es guardarà a la base de dades

        //després de triar una data, automáticament s'obre un PopUp per triar una hora
        Calendar now = Calendar.getInstance();

        TimePickerDialog timePicker = TimePickerDialog.newInstance(
                FragmentAgendaAfegirRecordatori.this,
                now.get(Calendar.HOUR),
                now.get(Calendar.MINUTE),
                true
        );

        timePicker.show(getActivity().getFragmentManager(), "Datepickerdialog");
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
    public void onPause() {
        super.onPause();
        googleApiClient.stopAutoManage(getActivity());
        googleApiClient.disconnect();
    }
}
