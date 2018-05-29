package com.example.dana.projectefinal.inventari;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dana.projectefinal.ConnexioDades;
import com.example.dana.projectefinal.FilterWithSpaceAdapter;
import com.example.dana.projectefinal.Objectes;
import com.example.dana.projectefinal.R;
import com.example.dana.projectefinal.Utilitats;

import java.util.LinkedList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class InventariVeureAfegirLloguer extends Fragment implements View.OnClickListener {

    final boolean NOU_LLOGUER;
    boolean estaEditant;
    Objectes.Lloguer lloguerActual;
    View view;

    ImageButton btTornarEnrere;
    Button btRealitzarAccio;

    ConstraintLayout contenidorDataInici, contenidorDataFi;
    TextView tvDataInici, tvHoraInici, tvDataFi, tvHoraFi, tvPreu;
    AutoCompleteTextView acClient, acLlocEntrega, acLlocRecollida;

    LinearLayout llistaArticles;
    ConstraintLayout ultimaFilaAfegirArticle;
    TextView btAfegirArticle;
    AutoCompleteTextView acArticleNou;


    List<String> llistaBicicletesScooters;

    /**
     * Constructor per quan es vol crear un nou recordatori
     */
    public InventariVeureAfegirLloguer() {
        this.lloguerActual = new Objectes.Lloguer();
        NOU_LLOGUER = true;
        estaEditant = false;
    }


    /**
     * Constructor per quan es vol veure un nou recordatori
     * @param lloguerActual Lloguer que es vol veure
     */
    @SuppressLint("ValidFragment")
    public InventariVeureAfegirLloguer(Objectes.Lloguer lloguerActual) {
        this.lloguerActual = lloguerActual;
        NOU_LLOGUER = false;
        estaEditant = false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.inventari_afegir_lloguer, null);

        inicialitzarViews();
        desactivarViews();

        llistaArticles.addView(ultimaFilaAfegirArticle);

        //Es podrà afegir qualsevol article, sigui bicicleta o scooter
        llistaBicicletesScooters = new LinkedList<>();
        for (Integer idBici : ConnexioDades.magatzemBicis.keySet()) {
            Objectes.Article bici =  ConnexioDades.llistaBicicletes.get(ConnexioDades.magatzemBicis.get(idBici));
            llistaBicicletesScooters.add (idBici + " - " + bici.getMarca() + " " + bici.getModel());
        }

        for (Integer idScooter: ConnexioDades.magatzemScooters.keySet()) {
            Objectes.Article scooter = ConnexioDades.llistaScooters.get(ConnexioDades.magatzemScooters.get(idScooter));
            llistaBicicletesScooters.add(idScooter + " - " + scooter.getMarca() + " " + scooter.getModel());
        }

        FilterWithSpaceAdapter<String> adapter = new FilterWithSpaceAdapter<String>(getContext(),android.R.layout.simple_list_item_1, llistaBicicletesScooters);
        acArticleNou.setThreshold(1);
        acArticleNou.setAdapter(adapter);


        btTornarEnrere.setOnClickListener(this);
        btAfegirArticle.setOnClickListener(this);

        btTornarEnrere.setOnTouchListener(Utilitats.onTouchListener(btTornarEnrere));
        btRealitzarAccio.setOnTouchListener(Utilitats.onTouchListener(btRealitzarAccio));
        btAfegirArticle.setOnTouchListener(Utilitats.onTouchListener(btAfegirArticle));

        return view;
    }

//-- fi onCreateView() -----------------------------------------------------------------------------

    /**
     * Enllaça les variables View amb la seva vista corresponent a l'arxiu XML
     */
    private void inicialitzarViews() {

        btTornarEnrere          = view.findViewById(R.id.tornar_enrere);
        btRealitzarAccio        = view.findViewById(R.id.realitzar_accio);

        contenidorDataInici     = view.findViewById(R.id.contenidor_data_hora_inici);
        contenidorDataFi        = view.findViewById(R.id.contenidor_data_hora_fi);

        tvDataInici             = view.findViewById(R.id.data_inici);
        tvHoraInici             = view.findViewById(R.id.hora_inici);
        tvDataFi                = view.findViewById(R.id.data_fi);
        tvHoraFi                = view.findViewById(R.id.hora_fi);

        acClient                = view.findViewById(R.id.client);
        acLlocEntrega           = view.findViewById(R.id.lloc_entrega);
        acLlocRecollida         = view.findViewById(R.id.lloc_recollida);

        tvPreu                  = view.findViewById(R.id.preu_lloguer);

        llistaArticles          = view.findViewById(R.id.llista_articles_llogar);
        ultimaFilaAfegirArticle = (ConstraintLayout) LayoutInflater.from(getContext()).inflate(R.layout.row_inventari_llista_lloguer_afegir, llistaArticles, false);

        btAfegirArticle         = ultimaFilaAfegirArticle.findViewById(R.id.bt_afegir_article);
        acArticleNou            = ultimaFilaAfegirArticle.findViewById(R.id.autocomplete_article_afegir);
    }



    /**
     * Mira si estem creant un lloguer o veient un i, depenent de quin dels dos sigui,
     * habilita i deshabilita vistes diferents
     */
    private void desactivarViews() {

        if (NOU_LLOGUER) {
            btRealitzarAccio.setText("Guardar");
            ultimaFilaAfegirArticle.setVisibility(VISIBLE);
            acArticleNou.setVisibility(GONE);
            tvPreu.setVisibility(GONE);

        }
        else {
            btRealitzarAccio.setText("Editar");
            acClient.setFocusableInTouchMode(false);
            acLlocEntrega.setFocusableInTouchMode(false);
            acLlocRecollida.setFocusableInTouchMode(false);
            ultimaFilaAfegirArticle.setVisibility(GONE);
            tvPreu.setVisibility(VISIBLE);
        }
    }

//--------------------------------------------------------------------------------------------------

    private void afegirArticleAlLloguer() {

        if (acArticleNou.getVisibility() == GONE) {
            acArticleNou.setVisibility(VISIBLE);

        }
        else {
            if (potAfegirArticle()) {
                final View filaArticle = LayoutInflater.from(getContext()).inflate(R.layout.row_inventari_vista_lloguer, llistaArticles, false);

                ImageButton btEliminarArticle   = filaArticle.findViewById(R.id.eliminar_article);
                TextView idArticle              = filaArticle.findViewById(R.id.id_article);
                TextView nomArticle             = filaArticle.findViewById(R.id.marca_model_article);

                idArticle.setText(acArticleNou.getText().toString().split(" - ")[0]);
                nomArticle.setText(acArticleNou.getText().toString().split(" - ")[1]);


                ConstraintLayout aux = ultimaFilaAfegirArticle;
                acArticleNou.setText("");
                llistaArticles.removeViewAt(llistaArticles.getChildCount()-1);
                llistaArticles.addView(filaArticle);
                llistaArticles.addView(aux);


                btEliminarArticle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        llistaArticles.removeView(filaArticle);
                    }
                });
            }
        }
    }


    /**
     * Comprova si l'article triat compleix els requisits per poder llogar-se. Els requisits són:
     * - Ha d'existir a la base de dades
     * - No ha d'estar buit
     * @return <code>true</code> si l'article compleix els requisits
     */
    private boolean potAfegirArticle() {

        if (!llistaBicicletesScooters.contains(acArticleNou.getText().toString())) {
            Utilitats.mostrarMissatgeError(getContext(), "L'article no existeix,", "si us plau, tria un altre.");
            return false;
        }


        return true;
    }

    /**
     * Si estem veient un lloguer, torna a la pantalla anterior ("LLOGUERS").
     * Si estem creant o editant un lloguer, comprova si es compleixen els requisists per tornar enrere
     * (especificats a la funció <code>potTornarEnrere</code>).
     * Si compleix els requisits, mostra un missatge de confirmació per sortir i, si no, mostra un missatge
     * informant de l'error.
     */
    private void tornarEnrere() {
        getFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, new InventariLloguersVentes())
                .commit();
    }


//-------------------------------------------------------------------------------------------------

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tornar_enrere: tornarEnrere();  break;
            case R.id.bt_afegir_article: afegirArticleAlLloguer(); break;

        }
    }
}
