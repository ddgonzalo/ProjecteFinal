package com.example.dana.projectefinal.agenda;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.dana.projectefinal.R;

public class FragmentAgendaFiltrarData extends Fragment {

    ConnexioAgenda bd;

    LinearLayout llistaAvui, llistaDema, llistaProximsDies, llistaAquestMes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.agenda_filtrar_data, null);
        bd = new ConnexioAgenda(getContext());


        llistaAvui          = view.findViewById(R.id.llista_agenda_avui);
        llistaDema          = view.findViewById(R.id.llista_agenda_dema);
        llistaProximsDies   = view.findViewById(R.id.llista_agenda_setmana);
        llistaAquestMes     = view.findViewById(R.id.llista_agenda_mes);

        bd.mostrarRecordatoris(getActivity().getSupportFragmentManager(),"avui", llistaAvui);
        bd.mostrarRecordatoris(getActivity().getSupportFragmentManager(),"dema", llistaDema);
        bd.mostrarRecordatoris(getActivity().getSupportFragmentManager(),"setmana", llistaProximsDies);
        bd.mostrarRecordatoris(getActivity().getSupportFragmentManager(),"mes", llistaAquestMes);


        return view;
    }
}
