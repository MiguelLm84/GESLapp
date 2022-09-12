package com.example.geslapp.ui;


import android.content.Intent;
import android.os.Bundle;
import android.transition.Explode;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.geslapp.R;
import com.example.geslapp.core.adapters.RvEtqAdapter;
import com.example.geslapp.core.clases.Etqs_invent;
import com.example.geslapp.core.databaseInvent.Etqs_Invent_Local_DB;
import java.util.ArrayList;


public class Rv_etiquetas extends AppCompatActivity {

    private RecyclerView rvEtq;
    private Button butitems, butback;
    private int id_invent;
    private ConstraintLayout constraintLayoutInventarioMaterial;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_invent);

        init();
        getArgsRecyclerViewInventarios();
        buttonItems();
        buttonBack();
    }

    private void init() {

        constraintLayoutInventarioMaterial = findViewById(R.id.constraintLayoutInventarioMaterial);
        butback = findViewById(R.id.butbackmaterial);
        butitems = findViewById(R.id.butToItems);
        rvEtq = findViewById(R.id.rvmaterialinvent);
        rvEtq.setHasFixedSize(true);
    }

    private void getArgsRecyclerViewInventarios() {

        id_invent = getIntent().getIntExtra("id_invent",-1);

        if(id_invent == -1) {
            constraintLayoutInventarioMaterial.setVisibility(View.VISIBLE);
            rvEtq.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "IF -> Id_invent: " + id_invent, Toast.LENGTH_SHORT).show();

        } else {
            rvEtq.setVisibility(View.VISIBLE);
            RecyclerView.LayoutManager layoutManagerEtq = new LinearLayoutManager(this);
            rvEtq.setLayoutManager(layoutManagerEtq);

            Etqs_Invent_Local_DB etqs_invent_local_db = new Etqs_Invent_Local_DB(this);

            ArrayList<Etqs_invent> listaEtqs = new ArrayList<>(etqs_invent_local_db.fillArray(id_invent, Rv_etiquetas.this));
            RecyclerView.Adapter<RvEtqAdapter.MyViewHolder> rvEtqAdapter = new RvEtqAdapter(Rv_etiquetas.this, id_invent, listaEtqs, Rv_etiquetas.this);
            rvEtq.setAdapter(rvEtqAdapter);
            Toast.makeText(this, "ELSE -> Id_invent: " + id_invent, Toast.LENGTH_SHORT).show();
        }
    }

    private void buttonItems() {

        butitems.setOnClickListener(v -> {
            Intent t = new Intent(Rv_etiquetas.this,Rv_items.class);
            t.putExtra("id_invent",id_invent);
            startActivity(t);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
    }

    private void buttonBack() {

        butback.setOnClickListener(v -> {
            onBackPressed();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
