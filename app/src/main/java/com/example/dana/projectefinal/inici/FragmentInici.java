package com.example.dana.projectefinal.inici;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dana.projectefinal.MainActivity;
import com.example.dana.projectefinal.R;
import com.example.dana.projectefinal.agenda.ConnexioAgenda;
import com.example.dana.projectefinal.inventari.ConnexioInventari;

public class FragmentInici extends Fragment {

    View view;
    ConnexioAgenda connexioAgenda;
    ConnexioInventari connexioInventari;
    ConnexioInici connexioInici;

    LinearLayout llistaAgenda;
    LinearLayout llistaLloguersEnCurs;

    TextView tvTotalLloguers, tvTotalCompres, tvGuanysMes, tvGuanysMesPassat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.inici, null);
        connexioAgenda = new ConnexioAgenda(getContext());
        connexioInventari = new ConnexioInventari(getContext());
        connexioInici = new ConnexioInici(getContext());

        inicialitzarViews();

        connexioInici.mostrarDades(tvTotalLloguers, tvTotalCompres, tvGuanysMes, tvGuanysMesPassat);
        connexioAgenda.mostrarRecordatoris(getFragmentManager(),"avui", llistaAgenda);
        connexioInventari.mostrarLloguers(getFragmentManager(), "enCurs", llistaLloguersEnCurs);


        return view;
    }


//-- fi onCreateView() -----------------------------------------------------------------------------

    private void inicialitzarViews() {
        llistaAgenda            = view.findViewById(R.id.llista_agenda_avui);
        llistaLloguersEnCurs    = view.findViewById(R.id.llista_lloguers_en_curs);

        tvTotalLloguers         = view.findViewById(R.id.total_lloguers);
        tvTotalCompres          = view.findViewById(R.id.total_compres);

        tvGuanysMes             = view.findViewById(R.id.guanys_mes);
        tvGuanysMesPassat       = view.findViewById(R.id.guanys_mes_passat);
    }
}
