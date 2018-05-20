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

        //atributs perquè només es vegi el text en l'item seleccionat
        bottomNavigation.enableShiftingMode(false);
        bottomNavigation.enableItemShiftingMode(true);
        bottomNavigation.enableAnimation(true);

        bottomNavigation.setOnNavigationItemSelectedListener(this);

        //------------------------------------------------------------------------------------------

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, new FragmentInici())
                .commit();

        /*bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_inici: break;

                    case R.id.action_agenda: break;

                    case R.id.action_disponibilitat: break;

                    case R.id.action_inventari: break;

                    case R.id.action_settings: break;

                }
                return true;
            }
        });*/

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.action_inici: fragment = new FragmentInici(); Log.i("BOTTOMNAV", "Inici"); break;

            case R.id.action_agenda: fragment = new FragmentAgenda(); Log.i("BOTTOMNAV", "Agenda"); break;

            case R.id.action_disponibilitat: fragment = new FragmentDisponibilitat(); Log.i("BOTTOMNAV", "Disponibilitat"); break;

            case R.id.action_inventari: fragment = new FragmentInventari(); Log.i("BOTTOMNAV", "Inventari"); break;

            case R.id.action_settings: fragment = new FragmentSettings(); Log.i("BOTTOMNAV", "Settings"); break;
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
        return true;
    }
}
