package com.example.geslapp.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geslapp.R;
import com.example.geslapp.core.clases.CheckConnection;
import com.example.geslapp.core.clases.ConfigPreferences;
import com.example.geslapp.core.clases.Update;
import com.example.geslapp.core.database.Cajas_Local_DB;
import com.example.geslapp.core.database.Login_Local_DB;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class MenuActivity extends AppCompatActivity {

    Button btnProcesoLotes, btnCentroActivo, butmenu, btnExpBottomSheet;
    TextView versionTv, trackingTv, tvCaja, txtuser,txtlastcon;
    ArrayList<String> listaCentros, listaIps, listaDominios, listaIdsCentros,listaCecos,listaModelos,listaIdsModelos,listaTamanos;
    ImageView imgcon;
    static String version;
    private static String IP,REC,username;
    private final ConfigPreferences config = new ConfigPreferences();
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);
        IP = config.getIP(getApplicationContext());
        REC = config.getRec(getApplicationContext());
        btnProcesoLotes= findViewById(R.id.btnProcesoPorLotes);
        btnCentroActivo = findViewById(R.id.btnCentroActivo);
        txtuser = findViewById(R.id.txtMuser);
        imgcon = findViewById(R.id.imgcon);
        txtlastcon = findViewById(R.id.txtlastcon);

        requestPermission();

        butmenu = findViewById(R.id.butCmenu);
        versionTv = findViewById(R.id.txtversion);
        trackingTv = findViewById(R.id.tvTracking);
        tvCaja = findViewById(R.id.tvCaja);
        Intent backintent = getIntent();
        listaCentros = backintent.getStringArrayListExtra("listaCentros");
        listaIdsCentros = backintent.getStringArrayListExtra("listaIdsCentros");
        listaCecos = backintent.getStringArrayListExtra("listaCecos");
        listaIps = backintent.getStringArrayListExtra("listaIps");
        listaDominios = backintent.getStringArrayListExtra("listaDominios");
        listaModelos = backintent.getStringArrayListExtra("listaModelos");
        listaIdsModelos = backintent.getStringArrayListExtra("listaIdsModelos");
        listaTamanos = backintent.getStringArrayListExtra("listaTamanos");
        version=config.getVApp(getApplicationContext());
        versionTv.setText(version);
        String password = backintent.getStringExtra("password");
        username = backintent.getStringExtra("username");
        txtuser.setText(username);
        startCheckConnection();

      butmenu.setOnClickListener(v -> showdialog());

        btnProcesoLotes.setOnClickListener(view -> {
            Intent intent= new Intent(MenuActivity.this, ProcesoPorLotesActivity.class);
            intent.putExtra("version", version);
            intent.putExtra("username",username);
            intent.putStringArrayListExtra("listaCentros", listaCentros);
            intent.putStringArrayListExtra("listaIdsCentros", listaIdsCentros);
            intent.putStringArrayListExtra("listaCecos", listaCecos);
            //intent.putStringArrayListExtra("listaIps", listaIps);
            //intent.putStringArrayListExtra("listaDominios", listaDominios);
            intent.putStringArrayListExtra("listaModelos", listaModelos);
            intent.putStringArrayListExtra("listaIdsModelos", listaIdsModelos);
            intent.putStringArrayListExtra("listaTamanos", listaTamanos);

            startActivity(intent);
            overridePendingTransition(R.anim.zoom_back_in,R.anim.zoom_back_out);
        });

        btnCentroActivo.setOnClickListener(view -> {
            Intent intent= new Intent(MenuActivity.this, ElegirCentroActivity.class);
            intent.putExtra("version", version);
            intent.putStringArrayListExtra("listaCentros", listaCentros);
            intent.putStringArrayListExtra("listaIdsCentros", listaIdsCentros);
            intent.putStringArrayListExtra("listaCecos", listaCecos);
            intent.putStringArrayListExtra("listaIps", listaIps);
            intent.putStringArrayListExtra("listaDominios", listaDominios);
            //intent.putStringArrayListExtra("listaModelos", listaModelos);
            //intent.putStringArrayListExtra("listaIdsModelos", listaIdsModelos);
            //intent.putStringArrayListExtra("listaTamanos", listaTamanos);
            intent.putExtra("username",username);
            startActivity(intent);
            overridePendingTransition(R.anim.zoom_back_in,R.anim.zoom_back_out);

        });
    }

    @SuppressLint("SetTextI18n")
    private void startCheckConnection() {
        new Update().cancel(true);
        //new CheckConnection().execute("http://"+IP+"/gesl/"+REC+"/");

        new Handler().postDelayed(() -> new CheckConnection().execute("http://"+IP+"/gesl/"+REC+"/"), 500);
        /*try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        String serverResponse = new CheckConnection().getServerResponse();

        if (serverResponse == null) {
            config.setCon(getApplicationContext(),false);
            txtlastcon.setText("última conexión el "+config.getLastCon(getApplicationContext()));
            int color = Color.parseColor("#737373");
            btnCentroActivo.getBackground().setColorFilter(color, PorterDuff.Mode.ADD);
            btnCentroActivo.setEnabled(false);

        } else {
            txtlastcon.setText("");
            config.setLastCon(getApplicationContext());
            int color = Color.parseColor("#000000");
            config.setCon(getApplicationContext(),true);
            new CheckConnection().setServer_response();
            Login_Local_DB login_local_db = new Login_Local_DB(getApplicationContext());
            Cajas_Local_DB cajas_local_db = new Cajas_Local_DB(getApplicationContext());
            cajas_local_db.deleteRows();
            cajas_local_db.getDBCajas(getApplicationContext(),IP,REC);
            login_local_db.deleteRows();
            login_local_db.getDBUser(getApplicationContext());
            config.setVTables(getApplicationContext());
            btnCentroActivo.setEnabled(true);
            btnCentroActivo.getBackground().setColorFilter(color, PorterDuff.Mode.ADD);
        }

        if(config.getCon(getApplicationContext())) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.online);

            imgcon.setImageBitmap(bitmap);
        }
        else
        {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.offline);

            imgcon.setImageBitmap(bitmap);
        }
    }

    private void showdialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet);
        LinearLayout configlayout = dialog.findViewById(R.id.layoutconfig);
        LinearLayout rechargelayout = dialog.findViewById(R.id.layoutrecharge);
        LinearLayout infolayout = dialog.findViewById(R.id.layoutinfo3);
        LinearLayout exitlayout = dialog.findViewById(R.id.layoutexit);
        LinearLayout actulayout = dialog.findViewById(R.id.layoutactu);
        TextView txtvtables = dialog.findViewById(R.id.txtVtablas);
        TextView txtlastupdate = dialog.findViewById(R.id.txtVapp);
        txtlastupdate.setText(config.getLastAppUpdate(getApplicationContext()));
        txtvtables.setText(config.getVTables(getApplicationContext()));

        actulayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCheckConnection();
                if(config.getCon(getApplicationContext())) {
                    if (checkPermission()) {
                        new CheckConnection().cancel(true);
                        Update atualizaApp = new Update();
                        atualizaApp.setContext(MenuActivity.this);

                        atualizaApp.execute("http://"+IP+"/gesl/"+REC+"/app-release.apk");
                        boolean updated = atualizaApp.getUpdated();
                        try {
                            Thread.sleep(2000);
                            startCheckConnection();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(updated)
                        {
                            config.setLastAppUpdate(getApplicationContext());
                            txtlastupdate.setText(config.getLastAppUpdate(getApplicationContext()));
                        }
                        else
                        {
                            txtlastupdate.setText(config.getLastAppUpdate(getApplicationContext()));
                        }


                    } else {
                        requestPermission();
                    }//end else
                }else
                {
                    new CheckConnection().cancel(true);
                    Toast.makeText(MenuActivity.this, "No hay conexión con el host", Toast.LENGTH_SHORT).show();
                }
            }//end on click
        });



        infolayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://asysgon.com");
                        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });
        rechargelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CheckConnection().execute("http://"+IP+"/gesl/"+REC+"/");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String serverResponse = new CheckConnection().getServerResponse();

                if (serverResponse!= null) {
                    new CheckConnection().setServer_response();
                    Cajas_Local_DB cajas_local_db = new Cajas_Local_DB(getApplicationContext());
                    Login_Local_DB login_local_db = new Login_Local_DB(getApplicationContext());
                    cajas_local_db.deleteRows();
                    cajas_local_db.getDBCajas(getApplicationContext(),IP,REC);
                    login_local_db.deleteRows();
                    login_local_db.getDBUser(getApplicationContext());
                    config.setVTables(getApplicationContext());
                    txtvtables.setText(config.getVTables(getApplicationContext()));
                    Toast.makeText(getApplicationContext(),"LAS TABLAS HAN SIDO CARGADAS",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"NO HAY CONEXION CON EL SERVIDOR",Toast.LENGTH_SHORT).show();
                }
            }
        });
        configlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showconfingdialog();
            }
        });
        exitlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(MenuActivity.this, LoginActivity.class);
                startActivity(t);
                overridePendingTransition(R.anim.zoom_back_in,R.anim.zoom_back_out);
                finish();
            }
        });
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();

    }

    private void showconfingdialog() {
        final Dialog dialog = new Dialog(MenuActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomconfig);
        EditText edtip = dialog.findViewById(R.id.edtip);
        EditText edtrec = dialog.findViewById(R.id.edtrecurso);
        Button butsave = dialog.findViewById(R.id.butCsave);
        edtip.setText(IP);
        edtrec.setText(REC);
        TextView txtversion = dialog.findViewById(R.id.txtVconfig);
        txtversion.setText(version+"");

        butsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = edtip.getText().toString();
                String rec = edtrec.getText().toString();
                if(isValidIPAddress(ip))
                {

                    IP = ip;
                    REC = rec;
                    new CheckConnection().cancel(true);
                    new CheckConnection().execute("http://"+ip+"/gesl/"+rec+"/");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String serverResponse = new CheckConnection().getServerResponse();

                    if (serverResponse == null) {

                        Toast.makeText(MenuActivity.this, "No hay conexión con el host", Toast.LENGTH_SHORT).show();



                    }
                    else {
                        config.createPreferences(getApplicationContext(),ip,rec);
                        Toast.makeText(MenuActivity.this, "Configuración guardada", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
                else Toast.makeText(MenuActivity.this, "Datos erróneos", Toast.LENGTH_SHORT).show();
            }
        });


        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();

    }

    public static boolean isValidIPAddress(String ip)
    {

        String zeroTo255
                = "(\\d{1,2}|(0|1)\\"
                + "d{2}|2[0-4]\\d|25[0-5])";

        String regex
                = zeroTo255 + "\\."
                + zeroTo255 + "\\."
                + zeroTo255 + "\\."
                + zeroTo255;

        Pattern p = Pattern.compile(regex);

        if (ip == null) {
            return false;
        }

        Matcher m = p.matcher(ip);

        return m.matches();
    }
    @Override
    public void onBackPressed() {


    }

    @Override
    protected void onResume() {
        super.onResume();
        startCheckConnection();
    }

    @Override
    protected void onStop() {
        super.onStop();


    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }
}