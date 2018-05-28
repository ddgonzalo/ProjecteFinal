package com.example.dana.projectefinal.inventari;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dana.projectefinal.ConnexioDades;
import com.example.dana.projectefinal.MainActivity;
import com.example.dana.projectefinal.Objectes;
import com.example.dana.projectefinal.R;
import com.example.dana.projectefinal.Utilitats;
import com.example.dana.projectefinal.agenda.FragmentAgenda;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

public class FragmentInventariDetallArticle extends Fragment {
    View view;
    Objectes.Article articleActual;
    String strIdArticle;

    ImageButton btTornarEnrere;
    Button btEditarGuardar;

    ImageView fotoArticle;
    TextView idArticle, nomArticle;
    MaterialEditText preu, pes, velocitat, autonomia, bateria;

    boolean estaEditant, esBicicleta;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.inventari_detall_article, null);

        estaEditant = false; //comença veient l'article

        inicialitzarViews();

        //agafem l'ID de l'article
        strIdArticle = getArguments().getString("article");
        esBicicleta = getArguments().getBoolean("es_bicicleta");

        if (esBicicleta) {
            articleActual = ConnexioDades.llistaBicicletes.get(ConnexioDades.magatzemBicis.get(Integer.parseInt(strIdArticle)));
        }
        else {
            articleActual = ConnexioDades.llistaScooters.get(ConnexioDades.magatzemScooters.get(Integer.parseInt(strIdArticle)));
        }



        btTornarEnrere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tornarEnrere();
            }
        });


        btEditarGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realitzarAccioEditarOGuardar();
            }
        });

        mostrarDades();
        return view;
    }

//-- fi onCreateView() -----------------------------------------------------------------------------

    private void inicialitzarViews() {
        btTornarEnrere      = (ImageButton) view.findViewById(R.id.tornar_enrere);
        btEditarGuardar     = (Button) view.findViewById(R.id.editar_article);

        fotoArticle         = (ImageView) view.findViewById(R.id.imatge_article);
        idArticle           = (TextView) view.findViewById(R.id.id_article);
        nomArticle          = (TextView) view.findViewById(R.id.marca_model_article);

        preu                = (MaterialEditText) view.findViewById(R.id.preu_article);
        pes                 = (MaterialEditText) view.findViewById(R.id.pes);
        velocitat           = (MaterialEditText) view.findViewById(R.id.velocitat);
        autonomia           = (MaterialEditText) view.findViewById(R.id.autonomia);
        bateria             = (MaterialEditText) view.findViewById(R.id.bateria);
    }

//--------------------------------------------------------------------------------------------------

    private void realitzarAccioEditarOGuardar() {
        if (estaEditant) guardarArticle();
        else editarArticle();
    }


    private void editarArticle() {
        estaEditant = true;
        btEditarGuardar.setText("Guardar");

        nomArticle.setFocusableInTouchMode(true);
        preu.setFocusableInTouchMode(true);
        pes.setFocusableInTouchMode(true);
        velocitat.setFocusableInTouchMode(true);
        autonomia.setFocusableInTouchMode(true);
        bateria.setFocusableInTouchMode(true);
    }

    private void guardarArticle() {

        if (potGuardar()) {

            estaEditant = false;
            btEditarGuardar.setText("Editar");

            nomArticle.setFocusableInTouchMode(false);
            preu.setFocusableInTouchMode(false);
            pes.setFocusableInTouchMode(false);
            velocitat.setFocusableInTouchMode(false);
            autonomia.setFocusableInTouchMode(false);
            bateria.setFocusableInTouchMode(false);


            articleActual.setModel(nomArticle.getText().toString()); //marca i model estan JUNTS
            articleActual.setPreu(Double.parseDouble(preu.getText().toString()));
            articleActual.setPes(Double.parseDouble(pes.getText().toString()));
            articleActual.setVelocitat(velocitat.getText().toString());
            articleActual.setAutonomia(autonomia.getText().toString());
            articleActual.setBateria(bateria.getText().toString());


            //separar la marca del model
            String strModel = articleActual.getModel().substring(articleActual.getMarca().length()+1, articleActual.getModel().length());
            articleActual.setModel(strModel);

            ConnexioInventari connexio = new ConnexioInventari(getContext());
            connexio.actualitzarArticle(articleActual, esBicicleta);

        }
    }

    private boolean potGuardar() {

        String primeraLiniaError = "No s'ha pogut guardar l'article,";

        if (nomArticle.getText().toString().equals(null) || nomArticle.getText().toString().equals("")) {
            Utilitats.mostrarMissatgeError(getContext(), primeraLiniaError, "el títol no pot estar buit.");
            return false;
        }

        if (preu.getText().toString().equals(null) || preu.getText().toString().equals("")) {
            Utilitats.mostrarMissatgeError(getContext(), primeraLiniaError, "el camp preu no pot estar buit.");
            return false;
        }

        if (pes.getText().toString().equals(null) || pes.getText().toString().equals("")) {
            Utilitats.mostrarMissatgeError(getContext(), primeraLiniaError, "el camp pes no pot estar buit.");
            return false;
        }

        if (velocitat.getText().toString().equals(null) || velocitat.getText().toString().equals("")) {
            Utilitats.mostrarMissatgeError(getContext(), primeraLiniaError, "el camp velocitat no pot estar buit.");
            return false;
        }

        if (autonomia.getText().toString().equals(null) || autonomia.getText().toString().equals("")) {
            Utilitats.mostrarMissatgeError(getContext(), primeraLiniaError, "el camp autonomía no pot estar buit.");
            return false;
        }

        if (bateria.getText().toString().equals(null) || bateria.getText().toString().equals("")) {
            Utilitats.mostrarMissatgeError(getContext(), primeraLiniaError, "el camp bateria no pot estar buit.");
            return false;
        }

        return true;
    }

//-- tornar enrere ---------------------------------------------------------------------------------


    private void tornarEnrere() {

        if (estaEditant) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


            View dview = inflater.inflate(R.layout.agenda_afegir_recordatori_confirmacio, null);

            final Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(dview);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            dialog.show();


            Button btCancelar = (Button) dview.findViewById(R.id.cancelar);
            Button btAcceptar = (Button) dview.findViewById(R.id.acceptar);

            btCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });


            btAcceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getFragmentManager().popBackStack(); //perquè no es vagin acumulant fragments

                    getFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, new FragmentInventariVeure())
                            .commit();

                    dialog.dismiss();
                }
            });
        }
        else {

            getFragmentManager().popBackStack(); //perquè no es vagin acumulant fragments

            getFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, new FragmentInventariVeure())
                    .commit();
        }
    }


    /**
     * Mostra per pantalla les dades principals de l'article
     */
    private void mostrarDades() {
        idArticle.setText("ID de l'article: " + strIdArticle);
        nomArticle.setText(articleActual.getMarca() + " " + articleActual.getModel());

        preu.setText(String.valueOf(articleActual.getPreu()));
        pes.setText(String.valueOf(articleActual.getPes()));
        velocitat.setText(articleActual.getVelocitat());
        autonomia.setText(articleActual.getAutonomia());
        bateria.setText(articleActual.getBateria());

        //Imatge de l'article
        Picasso.get().load(ConnexioDades.SERVIDOR + articleActual.getId() + ".jpg").into(fotoArticle);
    }
}
