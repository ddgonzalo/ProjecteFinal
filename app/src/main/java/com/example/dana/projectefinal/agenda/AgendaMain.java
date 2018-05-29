package com.example.dana.projectefinal.agenda;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;

import com.example.dana.projectefinal.R;
import com.example.dana.projectefinal.Utilitats;

public class AgendaMain extends Fragment {

    SharedPreferences sharedPreferences;

    ImageButton afegirRecordatori;

    RadioButton filtrarPerData, filtrarPerEtiquetes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.agenda, null);
        sharedPreferences  = getActivity().getSharedPreferences("my_preferences", Context.MODE_PRIVATE);

        //setFiltreInicial(); //indica si els recordatoris es veuran agrupats per data o per categories

        afegirRecordatori       = view.findViewById(R.id.afegir_recordatori);
        filtrarPerData          = view.findViewById(R.id.filtrar_per_data);
        filtrarPerEtiquetes     = view.findViewById(R.id.filtrar_per_etiquetes);

        afegirRecordatori.setOnTouchListener(Utilitats.onTouchListener(afegirRecordatori));

        afegirRecordatori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, new AgendaVeureAfegirRecordatori())
                        .addToBackStack(null)
                        .commit();
            }
        });


        filtrarPerData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canviarFiltre(true, new AgendaFiltrarData());
            }
        });

        filtrarPerEtiquetes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canviarFiltre(false, new AgendaFiltrarEtiquetes());
            }
        });

        return view;
    }

//-- fi onCreateView() -----------------------------------------------------------------------------


    private void canviarFiltre(boolean filtrarPerData, Fragment fragment) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("agenda_filtrar_data",filtrarPerData);
        editor.commit();


        getChildFragmentManager().beginTransaction()
                .replace(R.id.agenda_frame_layout, fragment)
                .addToBackStack(null)
                .commit();
    }


//-- Cicle de vida del fragment --------------------------------------------------------------------

    @Override
    public void onResume() {
        super.onResume();

        //indica com es veuran agrupats els recordatoris quan es mostri la pantalla "AGENDA",
        //si per data o per categories

        if (sharedPreferences.getBoolean("agenda_filtrar_data", true)) {

            getChildFragmentManager().beginTransaction()
                    .replace(R.id.agenda_frame_layout, new AgendaFiltrarData())
                    .commit();

            filtrarPerData.setChecked(true);
        }
        else {

            getChildFragmentManager().beginTransaction()
                    .replace(R.id.agenda_frame_layout, new AgendaFiltrarEtiquetes())
                    .commit();

            filtrarPerEtiquetes.setChecked(true);
        }
    }
}