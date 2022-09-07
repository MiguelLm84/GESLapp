package com.example.geslapp.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geslapp.R;
import com.example.geslapp.core.adapters.RvEtqAdapter;
import com.example.geslapp.core.clases.Etqs_invent;
import com.example.geslapp.core.databaseInvent.Etqs_Invent_Local_DB;

import java.util.ArrayList;
import java.util.List;

public class Rv_etiquetas extends AppCompatActivity {
    private RecyclerView rvetq;
    private RecyclerView.Adapter rvetqadapter;
    private RecyclerView.LayoutManager layoutManageretq;
    Button butitems, butback;
    private int id_invent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_invent);
        rvetq = findViewById(R.id.rvmaterialinvent);
        rvetq.setHasFixedSize(true);
        layoutManageretq = new LinearLayoutManager(getApplicationContext());
        rvetq.setLayoutManager(layoutManageretq);
        id_invent = getIntent().getIntExtra("id_invent",-1);
        Etqs_Invent_Local_DB etqs_invent_local_db = new Etqs_Invent_Local_DB(getApplicationContext());

        ArrayList<Etqs_invent> listaEtqs = new ArrayList<>();
        listaEtqs.addAll(etqs_invent_local_db.fillArray(id_invent));
        rvetqadapter = new RvEtqAdapter(getApplicationContext(),id_invent,listaEtqs,Rv_etiquetas.this);
        rvetq.setAdapter(rvetqadapter);
        butback = findViewById(R.id.butbackmaterial);
        butitems = findViewById(R.id.butToItems);
        butitems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    Intent t = new Intent(Rv_etiquetas.this,Rv_items.class);
                    t.putExtra("id_invent",id_invent);
                    startActivity(t);
                    overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);



            }
        });
        butback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                onBackPressed();
                overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);

            }
        });




    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
    }


}
