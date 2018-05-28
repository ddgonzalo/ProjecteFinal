package com.example.dana.projectefinal.disponibilitat;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.dana.projectefinal.ConnexioDades;
import com.example.dana.projectefinal.R;
import com.example.dana.projectefinal.Utilitats;
import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class FragmentDisponibilitat extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, View.OnClickListener{

    View view;
    ConnexioDisponibilitat connexio;

    Calendar calendarNow;
    DatePickerDialog datePicker;
    TimePickerDialog timePicker;

    List<String> llistaNomsBicicletes;
    List<String> llistaNomsScooters;

    MaterialAutoCompleteTextView nomArticle;
    FilterWithSpaceAdapter<String> adapter;

    RadioButton rbBicicleta, rbScooter;

    RadioButton rbDisponibilitatUnDia, rbDisponibilitatMesUnDia;
    Button btComprovarDisponibilitat;

    //Disponibilitat 1 dia
    ConstraintLayout contenidorUnDia;
    ConstraintLayout btCanviarHoraFi, btCanviarHoraInici, btCanviarDia;
    TextView tvDiaDisponibilitat, tvHoraIniciDisponibilitat, tvHoraFiDisponibilitat;


    //Disponibilitat més d'1 dia
    ConstraintLayout contenidorMesDunDia;
    ConstraintLayout btCanviarDataHoraInici, btCanviarDataHoraFi;
    TextView tvDataInici, tvDataFi, tvHoraInici, tvHoraFi;

    String strDataInici, strDataFi, strHoraInici, strHoraFi; //el que es guardarà a la base de dades


    //Resposta (disponible o no)
    ConstraintLayout layoutResposta;
    LottieAnimationView animationView;
    Button btLlogar;
    TextView textLlogat;

//--------------------------------------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.disponibilitat, null);
        connexio = new ConnexioDisponibilitat(getContext());

        inicialitzarViews();

        layoutResposta.setVisibility(GONE);

        strDataInici = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        strHoraInici = "00:00:00";
        strDataFi =  strDataInici;
        strHoraFi = strHoraInici;

        //Carrega la llista de bicicletes i scooters que hi ha al magatzem
        llistaNomsBicicletes = new LinkedList<>();

        for (Integer idBici : ConnexioDades.llistaBicicletes.keySet()) {
            llistaNomsBicicletes.add (idBici + " - " + ConnexioDades.llistaBicicletes.get(idBici));
        }

        llistaNomsScooters = new LinkedList<>();

        for (Integer idScooter: ConnexioDades.llistaScooters.keySet()) {
            llistaNomsScooters.add(idScooter + " - " + ConnexioDades.llistaScooters.get(idScooter));
        }

        adapter = new FilterWithSpaceAdapter<> (getContext(),android.R.layout.simple_list_item_1, llistaNomsBicicletes);
        nomArticle.setThreshold(1);
        nomArticle.setAdapter(adapter);


        //per triar dates i hores
        calendarNow = Calendar.getInstance();

        datePicker = DatePickerDialog.newInstance(
                FragmentDisponibilitat.this,
                calendarNow.get(Calendar.YEAR),
                calendarNow.get(Calendar.MONTH),
                calendarNow.get(Calendar.DAY_OF_MONTH)
        );

        timePicker = TimePickerDialog.newInstance(
                FragmentDisponibilitat.this,
                calendarNow.get(Calendar.HOUR),
                calendarNow.get(Calendar.MINUTE),
                true
        );


        rbDisponibilitatUnDia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contenidorUnDia.setVisibility(VISIBLE);
                contenidorMesDunDia.setVisibility(GONE);
            }
        });

        rbDisponibilitatMesUnDia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contenidorUnDia.setVisibility(GONE);
                contenidorMesDunDia.setVisibility(VISIBLE);
            }
        });


        return view;
    }

//-- fi onCreateView() -----------------------------------------------------------------------------


    public void inicialitzarViews() {

        nomArticle                  = (MaterialAutoCompleteTextView) view.findViewById(R.id.marca_model_article);
        rbBicicleta                 = (RadioButton) view.findViewById(R.id.rb_bicicleta);
        rbScooter                   = (RadioButton) view.findViewById(R.id.rb_scooter);
        btComprovarDisponibilitat   = (Button) view.findViewById(R.id.bt_comprovar);

        rbDisponibilitatUnDia       = (RadioButton) view.findViewById(R.id.rb_un_dia);
        rbDisponibilitatMesUnDia    = (RadioButton) view.findViewById(R.id.rb_mes_dun_dia);


        //Disponibilitat 1 dia
        contenidorUnDia             = (ConstraintLayout) view.findViewById(R.id.disponibilitat_dia);
        btCanviarDia                = (ConstraintLayout) view.findViewById(R.id.contenidor_dia);
        btCanviarHoraInici          = (ConstraintLayout) view.findViewById(R.id.contenidor_hora_inici);
        btCanviarHoraFi             = (ConstraintLayout) view.findViewById(R.id.contenidor_hora_fi);

        tvDiaDisponibilitat         = (TextView) view.findViewById(R.id.dia_disponibilitat);
        tvHoraIniciDisponibilitat   = (TextView) view.findViewById(R.id.hora_inici_disponibilitat);
        tvHoraFiDisponibilitat      = (TextView) view.findViewById(R.id.hora_fi_disponibilitat);

        //Disponibilitat més d'1 dia
        contenidorMesDunDia         = (ConstraintLayout) view.findViewById(R.id.disponibilitat_mes_dun_dia);
        btCanviarDataHoraInici      = (ConstraintLayout) view.findViewById(R.id.contenidor_data_hora_inici);
        btCanviarDataHoraFi         = (ConstraintLayout) view.findViewById(R.id.contenidor_data_hora_fi);

        tvDataInici                 = (TextView) view.findViewById(R.id.data_inici);
        tvHoraInici                 = (TextView) view.findViewById(R.id.hora_inici);
        tvDataFi                    = (TextView) view.findViewById(R.id.data_fi);
        tvHoraFi                    = (TextView) view.findViewById(R.id.hora_fi);

        //Resposta
        layoutResposta              = (ConstraintLayout) view.findViewById(R.id.layout_resposta);
        animationView               = (LottieAnimationView) view.findViewById(R.id.animation_view);
        btLlogar                    = (Button) view.findViewById(R.id.bt_llogar);
        textLlogat                  = (TextView) view.findViewById(R.id.text_llogat);


        //-- OnClick Listeners ----
        btCanviarDia.setOnClickListener(this);
        btCanviarHoraInici.setOnClickListener(this);
        btCanviarHoraFi.setOnClickListener(this);

        btCanviarDataHoraInici.setOnClickListener(this);
        btCanviarDataHoraFi.setOnClickListener(this);

        btComprovarDisponibilitat.setOnClickListener(this);

        rbBicicleta.setOnClickListener(this);
        rbScooter.setOnClickListener(this);
    }

//-- Comprovar disponibilitat ----------------------------------------------------------------------


    /**
     * Comprova si un article està disponible en un dia o en un període de temps determinat
     * Si l'article que es vol buscar no existeix, mostra per pantalla un missatge informant de l'error.
     */
    public void comprobarDisponibilitatArticle() {
        //Primer comprova si l'article triat existeix a la base de dades
        if (rbBicicleta.isChecked()) {
            if (!llistaNomsBicicletes.contains(nomArticle.getText().toString())) {
                Utilitats.mostrarMissatgeError(getContext(), "L'article no existeix,", "si us plau, tria un altre.");
                //mostrarMissatgeError();
                return;
            }
        }
        else {
            if (!llistaNomsScooters.contains(nomArticle.getText().toString())) {
                Utilitats.mostrarMissatgeError(getContext(), "L'article no existeix,", "si us plau, tria un altre.");
                return;
            }
        }

        String idArticle = nomArticle.getText().toString().split(" - ")[0];

        if (rbBicicleta.isChecked()) {
            layoutResposta.setVisibility(VISIBLE);
            connexio.comprovarDisponibilitatBicicleta(textLlogat, btLlogar, animationView, idArticle, strDataInici + " " + strHoraInici, strDataFi + " " + strHoraFi);
        }
        else {
            connexio.comprovarDisponibilitatScooter(textLlogat, btLlogar, animationView, idArticle, strDataInici + " " + strHoraInici, strDataFi + " " + strHoraFi);
        }
    }


//-- Triar data i hora del recordatori -------------------------------------------------------------

    //quan s'ha triat una hora per el recordatori
    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

        //Afegir els "0" necessaris si la hora és abans de les 10
        //per exemple, les "04:04" es veurien com a "4:4"
        String strHora = String.valueOf(hourOfDay);
        String strMinuts = String.valueOf(minute);

        if (hourOfDay<10) strHora = "0" + hourOfDay;
        if (minute<10) strMinuts = "0" + minute;


        if (timePicker.getTag().equals("canviar hora inici")) {
            strHoraInici = strHora + ":" + strMinuts + ":" + second;
            tvHoraIniciDisponibilitat.setText(strHora + ":" + strMinuts);
        }

        else if (timePicker.getTag().equals("canviar hora fi")) {
            strHoraFi = strHora + ":" + strMinuts + ":" + second;
            tvHoraFiDisponibilitat.setText(strHora + ":" + strMinuts);
        }

        else if (timePicker.getTag().equals("canviar data hora inici")) {
            strHoraInici = strHora + ":" + strMinuts + ":" + second;
            tvHoraInici.setText(strHora + ":" + strMinuts);
        }

        else if (timePicker.getTag().equals("canviar data hora fi")) {
            strHoraFi = strHora + ":" + strMinuts + ":" + second;
            tvHoraFi.setText(strHora + ":" + strMinuts);
        }
    }



    //quan s'ha triat una data per el recordatori
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        String mes = null;
        monthOfYear++; //tenen index 0

        switch (monthOfYear) {
            case 1: mes  =  "gener";    break;
            case 2: mes  =  "febrer";   break;
            case 3: mes  =  "març";     break;
            case 4: mes  =  "abril";    break;
            case 5: mes  =  "maig";     break;
            case 6: mes  =  "juny";     break;
            case 7: mes  =  "juliol";   break;
            case 8: mes  =  "agost";    break;
            case 9: mes  =  "setembre"; break;
            case 10: mes =  "octubre";  break;
            case 11: mes =  "novembre"; break;
            case 12: mes =  "desembre"; break;
        }


        String aux;
        //per poder-ho comparar amb la data actual, el camp "mes" ha de tenir dos dígits
        if (monthOfYear<10) aux = year + "-0" + monthOfYear + "-" + dayOfMonth;
        else aux = year + "-" + monthOfYear + "-" + dayOfMonth; //format que es guardarà a la base de dades

        if (datePicker.getTag().equals("canviar dia")) {
            strDataInici = aux;
            strDataFi = aux;
        }
        else if (datePicker.getTag().equals("canviar data hora inici")) {
            strDataInici = aux;
        }
        else if (datePicker.getTag().equals("canviar data hora fi")) {
            strDataFi = aux;
        }



        //per si la data triada és el dia d'avui
        if (aux.equals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))) aux = "Avui";
        else aux = dayOfMonth + " " + mes + " del " + year;

        //per saber a quin TextView s'ha d'escriure la data triada
        if (datePicker.getTag().equals("canviar dia")) {
            tvDiaDisponibilitat.setText(aux);
            //strDataInici = aux;
            //strDataFi = aux;
        }
        else if (datePicker.getTag().equals("canviar data hora inici")) {
            tvDataInici.setText(aux);
            //strDataInici = aux;
        }
        else if (datePicker.getTag().equals("canviar data hora fi")) {
            tvDataFi.setText(aux);
            //strDataFi = aux;
        }


        //si estem en "Disponibilitat més d'1 dia", la hora es tria automàticament després de la data
        if (datePicker.getTag().contains("canviar data hora")) {
            //després de triar una data, automáticament s'obre un PopUp per triar una hora
            timePicker.show(getActivity().getFragmentManager(), datePicker.getTag());
        }
    }

//--------------------------------------------------------------------------------------------------

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.contenidor_dia: datePicker.show(getActivity().getFragmentManager(), "canviar dia"); break;
            case R.id.contenidor_data_hora_inici: datePicker.show(getActivity().getFragmentManager(), "canviar data hora inici"); break;
            case R.id.contenidor_data_hora_fi: datePicker.show(getActivity().getFragmentManager(), "canviar data hora fi"); break;

            case R.id.contenidor_hora_inici: timePicker.show(getActivity().getFragmentManager(), "canviar hora inici"); break;
            case R.id.contenidor_hora_fi: timePicker.show(getActivity().getFragmentManager(), "canviar hora fi"); break;

            case R.id.bt_comprovar: comprobarDisponibilitatArticle(); break;

            //busca l'article triat a la llista de bicicletes
            case R.id.rb_bicicleta:
                adapter = new FilterWithSpaceAdapter<> (getContext(),android.R.layout.simple_list_item_1, llistaNomsBicicletes);
                nomArticle.setThreshold(1);
                nomArticle.setAdapter(adapter);
                break;

            //busca l'article triat a la llista d'scooters
            case R.id.rb_scooter:
                adapter = new FilterWithSpaceAdapter<> (getContext(),android.R.layout.simple_list_item_1, llistaNomsScooters);
                nomArticle.setThreshold(1);
                nomArticle.setAdapter(adapter);
                break;
        }
    }
}