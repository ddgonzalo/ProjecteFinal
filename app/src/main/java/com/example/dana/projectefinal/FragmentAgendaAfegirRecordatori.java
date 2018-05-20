package com.example.dana.projectefinal;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.nio.channels.UnresolvedAddressException;
import java.util.Calendar;

import static com.google.android.gms.common.api.GoogleApiClient.*;

public class FragmentAgendaAfegirRecordatori extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, OnConnectionFailedListener, ConnectionCallbacks {

    ConstraintLayout canviarData, canviarUbicacio;
    TextView data, hora;
    AutoCompleteTextView at;
    GoogleApiClient mGoogleApiClient;
    String LOG_TAG = "plsplspls";
    PlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.agenda_afegir_recordatori, null);

        ImageButton btEnrere = view.findViewById(R.id.tornar_enrere);

        btEnrere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack(); //perquè no es vagin acumulant fragments

                getFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, new FragmentAgenda())
                        .commit();
            }
        });

        canviarData = view.findViewById(R.id.contenidorDataHora);
        canviarUbicacio = view.findViewById(R.id.contenidorUbicacio);
        data = view.findViewById(R.id.dataRecordatori);
        hora = view.findViewById(R.id.horaRecordatori);

        at = view.findViewById(R.id.autoCompleteTextView);


        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(getActivity(), 0, this)
                .addConnectionCallbacks(this)
                .build();

        at.setThreshold(3);
        at.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(getContext(), android.R.layout.simple_list_item_1, BOUNDS_MOUNTAIN_VIEW, null);

        at.setAdapter(mPlaceArrayAdapter);

        canviarData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        FragmentAgendaAfegirRecordatori.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
            }
        });


        return view;
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                return;
            }
        }
    };

//-- Triar data i hora del recordatori -------------------------------------------------------------

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        hora.setText(hourOfDay + ":" + minute);
    }



    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        String mes = "";
        switch (monthOfYear) {
            case 1: mes = "gener"; break;
            case 2: mes = "febrer"; break;
            case 3: mes = "març"; break;
            case 4: mes = "abril"; break;
            case 5: mes = "maig"; break;
            case 6: mes = "juny"; break;
            case 7: mes = "juliol"; break;
            case 8: mes = "agost"; break;
            case 9: mes = "setembre"; break;
            case 10: mes = "octubre"; break;
            case 11: mes = "novembre"; break;
            case 12: mes = "desembre"; break;
        }

        data.setText(dayOfMonth + " " + mes + " del " + year);

        //després de triar una data, automáticament s'obre un PopUp per triar una hora
        Calendar now = Calendar.getInstance();

        TimePickerDialog dpd = TimePickerDialog.newInstance(
                FragmentAgendaAfegirRecordatori.this,
                now.get(Calendar.HOUR),
                now.get(Calendar.MINUTE),
                true
        );

        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }


//-- Referents a Google Places API -----------------------------------------------------------------

    @Override
    public void onConnected(Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(LOG_TAG, "Google Places API connected.");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(LOG_TAG, "Google Places API connection suspended.");
    }
}
