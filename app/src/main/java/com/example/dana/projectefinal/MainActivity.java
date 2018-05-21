package com.example.dana.projectefinal;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationViewEx bottomNavigation = (BottomNavigationViewEx) findViewById(R.id.bnve);

        //atributs perquè només es vegi el text en l'item del menú seleccionat
        bottomNavigation.enableShiftingMode(false);
        bottomNavigation.enableItemShiftingMode(true);
        bottomNavigation.enableAnimation(true);

        bottomNavigation.setOnNavigationItemSelectedListener(this);

        //comença mostrant la pantalla d'inici
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, new FragmentInici())
                .commit();

    }

//-- fi onCreate() ---------------------------------------------------------------------------------

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.action_inici: fragment = new FragmentInici(); break;

            case R.id.action_agenda: fragment = new FragmentAgenda(); break;

            case R.id.action_disponibilitat: fragment = new FragmentDisponibilitat(); break;

            case R.id.action_inventari: fragment = new FragmentInventari();  break;

            case R.id.action_settings: fragment = new FragmentSettings(); break;
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();

        return true;
    }
}
