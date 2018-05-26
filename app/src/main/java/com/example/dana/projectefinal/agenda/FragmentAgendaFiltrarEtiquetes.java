package com.example.dana.projectefinal.agenda;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dana.projectefinal.ConnexioDades;
import com.example.dana.projectefinal.R;

public class FragmentAgendaFiltrarEtiquetes extends Fragment{

    LinearLayout llistaCardviews;
    ConnexioAgenda connexio;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.agenda_filtrar_etiquetes, null);
        connexio = new ConnexioAgenda(getContext());

        llistaCardviews =view.findViewById(R.id.llista_cardviews);


        //per a cada etiqueta, afegeix un CardView amb tots els recordatoris que tenen aquesta etiqueta
        for (String nomEtiqueta : ConnexioDades.llistaEtiquetes.keySet()) {

            View cardview = LayoutInflater.from(getContext()).inflate(R.layout.carview_etiquetes, llistaCardviews, false);

            TextView cardviewTitol          = cardview.findViewById(R.id.nom_etiqueta);
            LinearLayout llistaRecordatoris = cardview.findViewById(R.id.llista_agenda);

            cardviewTitol.setText(nomEtiqueta);
            connexio.mostrarRecordatorisEtiqueta(nomEtiqueta, llistaRecordatoris);

            llistaCardviews.addView(cardview);
        }

        return view;

    }

//-- fi onCreateView() -----------------------------------------------------------------------------
}
