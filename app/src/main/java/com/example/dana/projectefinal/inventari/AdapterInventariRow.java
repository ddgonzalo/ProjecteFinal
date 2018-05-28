package com.example.dana.projectefinal.inventari;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dana.projectefinal.ConnexioDades;
import com.example.dana.projectefinal.Objectes;
import com.example.dana.projectefinal.R;
import com.google.gson.Gson;

import java.util.List;

public class AdapterInventariRow extends BaseAdapter {

//-- data ------------------------------------------------------------------------------------------

    private List<String> llistaArticles;
    private Context context;
    private static FragmentManager fragmentManager;

    private boolean esBicicleta;

//-- constructor -----------------------------------------------------------------------------------

    public AdapterInventariRow(boolean esBicicleta, FragmentManager fragmentManager, List<String> llistaArticles, Context context) {
        this.esBicicleta = esBicicleta;
        this.fragmentManager = fragmentManager;
        this.llistaArticles = llistaArticles;
        this.context = context;
    }

    public AdapterInventariRow(boolean esBicicleta, List<String> llistaArticles, Context context) {
        this.esBicicleta = esBicicleta;
        this.llistaArticles = llistaArticles;
        this.context = context;
    }

//-- mètodes ----------------------------------------------------------------------------------

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {

            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_inventari, null);

            holder.idArticle = convertView.findViewById(R.id.id_article);
            holder.nomArticle = convertView.findViewById(R.id.marca_model_article);

            convertView.setTag(holder);
        }

        else holder = (ViewHolder) convertView.getTag();

        final String article = getItem(position);
        holder.idArticle.setText(article.split("-")[0]);
        holder.nomArticle.setText(article.split("-")[1]);


        //quan es fa click a un article, s'obre una altra pantalla mostrant els detalls d'aquest
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentInventariDetallArticle fragment = new FragmentInventariDetallArticle();


                Bundle args = new Bundle();
                args.putString("article", article.split("-")[0]);
                args.putBoolean("es_bicicleta", esBicicleta);

                fragment.setArguments(args);

                fragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return convertView;
    }

//--------------------------------------------------------------------------------------------------

    private class ViewHolder {
        TextView idArticle;
        TextView nomArticle;
    }

//--------------------------------------------------------------------------------------------------

    //Auto-generated methods
    @Override
    public int getCount() {return this.llistaArticles.size();}

    @Override
    public String getItem(int position){return llistaArticles.get(position);}

    @Override
    public long getItemId(int position){return position;}
}
