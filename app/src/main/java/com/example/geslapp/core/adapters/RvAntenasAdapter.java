package com.example.geslapp.core.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geslapp.R;
import com.example.geslapp.core.camara.CamaraX;
import com.example.geslapp.core.clases.Antenas;
import com.example.geslapp.core.clases.ConfigPreferences;
import com.example.geslapp.core.databaseInvent.Antenas_Invent_Local_DB;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RvAntenasAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<RvAntenasAdapter.MyViewHolder> {
    Context context;

    ArrayList<Antenas> listaantenas = new ArrayList<>();
    int id_antena;
    int id_inventario;
    private static boolean saved = false;
    private static int contsaves = 0;
    private static boolean open;
    private int selectedItem = -1;

    public RvAntenasAdapter(Context context,ArrayList<Antenas> listaantenas, int id_inventario,boolean open) {
        this.context = context;
        this.listaantenas = listaantenas;
        this.open = open;
        this.id_inventario = id_inventario;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_antenas,parent,false);
        return new MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        id_antena = listaantenas.get(position).getId_antenas();
        Antenas_Invent_Local_DB antenas_invent_local_db = new Antenas_Invent_Local_DB(context);
        String ubi = antenas_invent_local_db.getUbi(id_antena,id_inventario);
        holder.edtubi.setText(ubi);
        if(open)
        {

           holder.butfoto.setEnabled(false);
           holder.edtubi.setEnabled(false);
           holder.butsave.setEnabled(false);
           holder.butsave.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.dialogverde)));

        }





        holder.butfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(saved){
                    id_antena = listaantenas.get(position).getId_antenas();
                    Intent t = new Intent(context, CamaraX.class);
                    t.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    t.putExtra("mode","antenas");
                    t.putExtra("id_antena",id_antena);
                    t.putExtra("id_invent",id_inventario);
                    context.startActivity(t);
                    holder.butfoto.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.dialogverde)));


                }else Toast.makeText(context, "Es necesario guardar la ubicaci??n antes que la foto", Toast.LENGTH_SHORT).show();
                
                


            }
        });

        holder.butsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_antena = listaantenas.get(position).getId_antenas();
                Antenas_Invent_Local_DB antenas_invent_local_db = new Antenas_Invent_Local_DB(context);

                String ubi= holder.edtubi.getText().toString();

                if(ubi.equals("") || ubi == null)
                {
                    Toast.makeText(context, "No se pueden tener campos vac??os", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(context, "Se ha guardado la ubicaci??n de la antena", Toast.LENGTH_SHORT).show();
                    antenas_invent_local_db.saveAntenas(id_antena,ubi,id_inventario);
                    contsaves++;
                    saved = true;
                    holder.butsave.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.dialogverde)));




                }
            }
        });

        holder.butview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_antena = listaantenas.get(position).getId_antenas();
                Antenas_Invent_Local_DB antenas_invent_local_db = new Antenas_Invent_Local_DB(context);
                String filename = antenas_invent_local_db.getFoto(id_antena,id_inventario);
                if(filename.equals("NOFOTO") || filename.equals(""))
                {
                    Toast.makeText(context, "No hay foto para mostrar", Toast.LENGTH_SHORT).show();
                }
                else {

                    File image = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), filename);
                    Bitmap bmp = BitmapFactory.decodeFile(image.getPath());
                    holder.imgfoto.setBackgroundResource(R.color.white);
                    holder.imgfoto.setImageBitmap(bmp);

                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return listaantenas.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        //aqui referenciamos los items de la vista
        EditText edtubi;
        Button butfoto,butsave, butview;
        ImageView imgfoto;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            edtubi = itemView.findViewById(R.id.edtubiantenas);
            butfoto = itemView.findViewById(R.id.butfotoantenas);
            butsave = itemView.findViewById(R.id.butsaveantenas);
            butview = itemView.findViewById(R.id.butAviewfoto);
            imgfoto = itemView.findViewById(R.id.imgAfoto);







        }
    }

    public int getContsaves(){return contsaves;}



    
}
