package com.example.dana.projectefinal.inventari;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.dana.projectefinal.R;
import com.example.dana.projectefinal.Utilitats;

public class InventariLloguersVentes extends Fragment {

    ConnexioInventari connexio;

    ImageButton btTornarEnrere, btAfegirLloguer;
    LinearLayout llistaEnCurs, llistaPendents, llistaFinalitzats;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lloguers_ventes, null);
        connexio = new ConnexioInventari(getContext());

        btTornarEnrere      = view.findViewById(R.id.tornar_enrere);
        btAfegirLloguer     = view.findViewById(R.id.afegir_lloguer);
        llistaEnCurs        = view.findViewById(R.id.llista_en_curs);
        llistaPendents      = view.findViewById(R.id.llista_pendents);
        llistaFinalitzats   = view.findViewById(R.id.llista_finalitzats);


        connexio.mostrarLloguers(getActivity().getSupportFragmentManager(), "enCurs", llistaEnCurs);
        connexio.mostrarLloguers(getActivity().getSupportFragmentManager(), "pendents", llistaPendents);
        connexio.mostrarLloguers(getActivity().getSupportFragmentManager(), "finalitzats", llistaFinalitzats);

        btTornarEnrere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getFragmentManager().popBackStack(); //perquè no es vagin acumulant fragments

                getFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, new InventariMain())
                        .commit();
            }
        });

        btAfegirLloguer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getFragmentManager().popBackStack(); //perquè no es vagin acumulant fragments

                getFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, new InventariVeureAfegirLloguer())
                        .commit();
            }
        });

        btTornarEnrere.setOnTouchListener(Utilitats.onTouchListener(btTornarEnrere));
        btAfegirLloguer.setOnTouchListener(Utilitats.onTouchListener(btAfegirLloguer));


        return view;
    }
}
