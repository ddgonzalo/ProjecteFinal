package com.example.dana.projectefinal.inventari;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dana.projectefinal.Objectes;
import com.example.dana.projectefinal.R;

import java.util.List;

public class AdapterClients extends BaseAdapter {

//-- data ------------------------------------------------------------------------------------------

    private List<String> llistaClients;
    private Context context;
    private TextView tvClient;
    private Dialog dialog;
    private Objectes.Lloguer lloguer;

//-- constructor -----------------------------------------------------------------------------------

    public AdapterClients(Context context, List<String> llistaClients, TextView tvClient, Dialog dialog, Objectes.Lloguer lloguer) {
        this.context = context;
        this.llistaClients = llistaClients;
        this.tvClient = tvClient;
        this.dialog = dialog;
        this.lloguer = lloguer;
    }

//-- mètodes ----------------------------------------------------------------------------------

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {

            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_lloguers_clients, null);

            holder.dniClient = convertView.findViewById(R.id.dni_client);
            holder.nomClient = convertView.findViewById(R.id.nom_client);

            convertView.setTag(holder);
        }

        else holder = (ViewHolder) convertView.getTag();

        final String client = getItem(position);

        if (client.split("-")[0].equals("null")) {
            holder.dniClient.setText("Sense client");
            holder.dniClient.setTextColor(Color.BLACK);
        }
        else {
            holder.dniClient.setText(client.split("-")[0]);
            holder.nomClient.setText(client.split("-")[1]);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position==0) {
                    tvClient.setText("Sense client");
                    lloguer.setClient("null");
                }
                else {
                    tvClient.setText(client.split("-")[1] + " (" + client.split("-")[0] + ")");
                    lloguer.setClient(client.split("-")[0]);
                }
                dialog.dismiss();

            }
        });

        return convertView;
    }

//--------------------------------------------------------------------------------------------------

    private class ViewHolder {
        TextView dniClient;
        TextView nomClient;
    }

//--------------------------------------------------------------------------------------------------

    //Auto-generated methods
    @Override
    public int getCount() {return this.llistaClients.size();}

    @Override
    public String getItem(int position){return llistaClients.get(position);}

    @Override
    public long getItemId(int position){return position;}
}
