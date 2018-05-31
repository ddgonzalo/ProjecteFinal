package com.example.dana.projectefinal;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.dana.projectefinal.agenda.ConnexioAgenda;
import com.example.dana.projectefinal.agenda.AgendaMain;
import com.example.dana.projectefinal.configuracio.ConfiguracioMain;
import com.example.dana.projectefinal.disponibilitat.DisponibilitatMain;
import com.example.dana.projectefinal.inici.FragmentInici;
import com.example.dana.projectefinal.inventari.ConnexioInventari;
import com.example.dana.projectefinal.inventari.InventariMain;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    static boolean agendaPotTornareEnrere;
    int itemAnterior;

    boolean tornarEnrere;

    static BottomNavigationViewEx  bottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //-- Carregar les dades ----
        ConnexioDades conn = new ConnexioDades(this); //perquè les bicicletes i scooters estiguin disponibles a totes les classes

        ConnexioAgenda connAgenda = new ConnexioAgenda(this);
        connAgenda.carregarDades();

        ConnexioInventari connInventari = new ConnexioInventari(this);
        connInventari.carregarDades();

        ConnexioClients connClients = new ConnexioClients(this);
        connClients.carregarDades();


        bottomNavigation = (BottomNavigationViewEx) findViewById(R.id.bnve);

        //atributs perquè només es vegi el text en l'item del menú seleccionat
        bottomNavigation.enableShiftingMode(false);
        bottomNavigation.enableItemShiftingMode(true);
        bottomNavigation.enableAnimation(true);

        bottomNavigation.setOnNavigationItemSelectedListener(this);

        //comença mostrant la pantalla d'inici
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, new FragmentInici())
                .commit();

        agendaPotTornareEnrere = true;
        tornarEnrere = true;
        itemAnterior = bottomNavigation.getSelectedItemId();
    }

//-- fi onCreate() ---------------------------------------------------------------------------------

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //EventBus.getDefault().post("sortir " + bottomNavigation.getMenuItemPosition(item));

        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.action_inici:
                fragment = new FragmentInici();
                break;

            case R.id.action_agenda:
                fragment = new AgendaMain();
                break;

            case R.id.action_disponibilitat:
                fragment = new DisponibilitatMain();
                break;

            case R.id.action_inventari:
                fragment = new InventariMain();
                break;

            case R.id.action_settings:
                fragment = new ConfiguracioMain();
                break;
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();

        return true;
    }

//-- Cicle de vida de l'activitat ------------------------------------------------------------------


    public static void canviarPestaña(int id) {
        bottomNavigation.setCurrentItem(id);
    }

    @Override
    public void onBackPressed() {

        /* Això fa que, si estem en un fragment "secundari", només es pugui tornar enrere
         * si fem click al botó [Enrere] que hi ha a la UI.
         * Per exemple, a la pantalla "Agenda -> Nou recordatori" només podem tornar enrere (cap a "Agenda")
         * si fem click al botó per tornar enrere que apareix adalt a l'esquerra.
         * Està fet així per millorar i simplificar les comprovacions de sortida dels fragments, si en tenen. */
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count==0) super.onBackPressed();
    }


    /*@Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(MainActivity.this); //comença a rebre notificacions de l'EventBus
    }


    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(MainActivity.this); //deixa de rebre notificacions de l'EventBus
    }


    @Subscribe
    public void handleEvents(String missatge) {

        if (missatge.contains("activar menu pestaña")) {
            bottomNavigation.setOnNavigationItemSelectedListener(this);
            //int pestañaId = Integer.parseInt(missatge.substring("activar menu pestaña ".length()-1, missatge.length()-1));

            //if (pestañaId !=-1) onNavigationItemSelected(bottomNavigation.getMenu().findItem(pestañaId));
        }

        if (missatge.contains("cambiar pestaña")) {
            int pestañaId = Integer.parseInt(missatge.substring("cambiar pestaña  ".length()-1, missatge.length()-1));
    onNavigationItemSelected(bottomNavigation.getMenu().findItem(pestañaId));
}
    }*/
}