package com.example.dana.projectefinal.inventari;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.dana.projectefinal.R;
import com.example.dana.projectefinal.agenda.FragmentAgenda;

public class FragmentInventariVeure extends Fragment {

    ImageButton btTornarEnrere;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inventari_veure, null);

        btTornarEnrere = (ImageButton) view.findViewById(R.id.tornar_enrere);

        btTornarEnrere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack(); //perquè no es vagin acumulant fragments

                getFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, new FragmentInventari())
                        .commit();
            }
        });


        return view;
    }
}

