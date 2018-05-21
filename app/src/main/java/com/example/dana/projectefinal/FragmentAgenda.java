package com.example.dana.projectefinal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class FragmentAgenda extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.agenda, null);

        ImageButton btAfegirRecordatori = view.findViewById(R.id.afegir_recordatori);

        btAfegirRecordatori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, new FragmentAgendaAfegirRecordatori())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
}