package com.example.dana.projectefinal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class FragmentInici extends Fragment {

    ConnexioAgenda bd;
    LinearLayout llistaAgenda;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inici, null);
        bd = new ConnexioAgenda(getContext());

        llistaAgenda = view.findViewById(R.id.llista_agenda_avui);

        bd.mostrarRecordatoris("avui", llistaAgenda);

        return view;
    }
}
