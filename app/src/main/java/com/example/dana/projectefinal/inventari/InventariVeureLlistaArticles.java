package com.example.dana.projectefinal.inventari;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;

import com.example.dana.projectefinal.ConnexioDades;
import com.example.dana.projectefinal.MainActivity;
import com.example.dana.projectefinal.Objectes;
import com.example.dana.projectefinal.R;
import com.example.dana.projectefinal.Utilitats;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InventariVeureLlistaArticles extends Fragment {

    ImageButton btTornarEnrere;

    EditText buscadorArticle;
    RadioButton rbVeureBicicletes, rbVeureScooters;
    ListView llistaArticles;

    List<String> llistaBicicletes, llistaScooters;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inventari_veure, null);

        btTornarEnrere      = (ImageButton) view.findViewById(R.id.tornar_enrere);
        buscadorArticle     = (EditText) view.findViewById(R.id.buscador_article);
        rbVeureBicicletes   = (RadioButton) view.findViewById(R.id.rb_bicicletes);
        rbVeureScooters     = (RadioButton) view.findViewById(R.id.rb_scooters);

        //llistaArticles és Listview i no LinearLayout perquè sinó no es podria fer la cerca dinàmica
        llistaArticles      = (ListView) view.findViewById(R.id.llista_articles);

        carregarLlistes();
        veureBicicletes();


        rbVeureBicicletes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                veureBicicletes();
            }
        });

        rbVeureScooters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                veureScooters();
            }
        });

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


    private void carregarLlistes() {
        llistaBicicletes = new LinkedList<>();
        llistaScooters = new LinkedList<>();

        for (Integer idBici : ConnexioDades.magatzemBicis.keySet()) {
            Objectes.Article bicicleta = ConnexioDades.llistaBicicletes.get(ConnexioDades.magatzemBicis.get(idBici));
            llistaBicicletes.add(idBici + "-" + bicicleta.getMarca() + " " + bicicleta.getModel());
        }

        for (Integer idScooter : ConnexioDades.magatzemScooters.keySet()) {
            Objectes.Article scooters = ConnexioDades.llistaScooters.get(ConnexioDades.magatzemScooters.get(idScooter));
            llistaScooters.add(idScooter + "-" + scooters.getMarca() + " " + scooters.getModel());
        }
    }


    private void veureBicicletes() {
        //Estat inicial de la llista
        AdapterInventariRow adapterInicial = new AdapterInventariRow(true, getFragmentManager(), llistaBicicletes, getContext());
        llistaArticles.setAdapter(adapterInicial);

        //Cerca dinàmica de bicicletes
        final ArrayList<String> temp = new ArrayList<>();
        final AdapterInventariRow adapter = new AdapterInventariRow(true, temp, getContext());

        buscadorArticle.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                int textLength = charSequence.length();
                temp.clear(); //cada vez que se escribe/borra, la lista de vendedores va cambiando

                for (String v : llistaBicicletes) {
                    //Se puede buscar por nomClient o código de vendedor
                    if (textLength <= v.length()) {
                        if (v.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            temp.add(v);
                        }
                    }
                }

                llistaArticles.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }


    private void veureScooters() {

        //Estat inicial de la llista
        AdapterInventariRow adapterInicial = new AdapterInventariRow(false, llistaScooters, getContext());
        llistaArticles.setAdapter(adapterInicial);

        final ArrayList<String> temp = new ArrayList<>();
        final AdapterInventariRow adapter = new AdapterInventariRow(false, temp, getContext());

        buscadorArticle.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                int textLength = charSequence.length();
                temp.clear(); //cada vez que se escribe/borra, la lista de vendedores va cambiando

                for (String v : llistaScooters) {
                    //Se puede buscar por nomClient o código de vendedor
                    if (textLength <= v.length()) {
                        if (v.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            temp.add(v);
                        }
                    }
                }

                llistaArticles.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }
}

