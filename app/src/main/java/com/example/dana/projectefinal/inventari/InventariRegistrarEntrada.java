package com.example.dana.projectefinal.inventari;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.dana.projectefinal.R;
import com.example.dana.projectefinal.Utilitats;

public class InventariRegistrarEntrada extends Fragment {

    ImageButton btTornarEnrere;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inventari_registrar_entrada, null);

        btTornarEnrere = view.findViewById(R.id.tornar_enrere);

        btTornarEnrere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack(); //perquè no es vagin acumulant fragments

                getFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, new InventariMain())
                        .commit();
            }
        });

        btTornarEnrere.setOnTouchListener(Utilitats.onTouchListener(btTornarEnrere));


        return view;
    }
}
