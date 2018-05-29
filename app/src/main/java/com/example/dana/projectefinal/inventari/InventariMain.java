package com.example.dana.projectefinal.inventari;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.dana.projectefinal.R;
import com.example.dana.projectefinal.Utilitats;

public class InventariMain extends Fragment implements View.OnClickListener {

    Button veureInventari, lloguers, registrarEntrades;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inventari, null);

        veureInventari      = view.findViewById(R.id.bt_inventari);
        lloguers            = view.findViewById(R.id.bt_lloguers_ventes);
        registrarEntrades   = view.findViewById(R.id.bt_entrades);

        veureInventari.setOnClickListener(this);
        lloguers.setOnClickListener(this);
        registrarEntrades.setOnClickListener(this);

        veureInventari.setOnTouchListener(Utilitats.onTouchListener(veureInventari));
        lloguers.setOnTouchListener(Utilitats.onTouchListener(lloguers));
        registrarEntrades.setOnTouchListener(Utilitats.onTouchListener(registrarEntrades));

        return view;
    }


    @Override
    public void onClick(View view) {
        Fragment fragment = null;

        switch (view.getId()) {
            case R.id.bt_inventari: fragment = new InventariVeureLlistaArticles(); break;
            case R.id.bt_lloguers_ventes: fragment = new InventariLloguersVentes(); break;
            case R.id.bt_entrades: fragment = new InventariRegistrarEntrada(); break;
        }

        getFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }
}