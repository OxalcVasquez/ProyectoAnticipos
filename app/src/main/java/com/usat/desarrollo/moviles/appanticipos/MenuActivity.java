package com.usat.desarrollo.moviles.appanticipos;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.usat.desarrollo.moviles.appanticipos.data.remote.api.ApiAdapter;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.DatosSesion;
import com.usat.desarrollo.moviles.appanticipos.presentation.anticipo.AnticipoFragment;
import com.usat.desarrollo.moviles.appanticipos.presentation.rendicion_gasto.RendicionFragment;
import com.usat.desarrollo.moviles.appanticipos.utils.Config;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    CircleImageView foto;
    TextView txtNombre, txtEmail,txtRol;
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Referenciar a la cabecera del menú
        View cabeceraMenu = navigationView.getHeaderView(0);
        foto = cabeceraMenu.findViewById(R.id.imagenUsuario);
        txtNombre = cabeceraMenu.findViewById(R.id.nombreUsuario);
        txtEmail = cabeceraMenu.findViewById(R.id.loginUsuario);
        txtRol = cabeceraMenu.findViewById(R.id.rolUsuario);
        //Mostrar los datos del usuario: Nombre y su email
        txtNombre.setText(DatosSesion.sesion.getNombres() + " "+ DatosSesion.sesion.getApellidos());
        txtEmail.setText(DatosSesion.sesion.getEmail());
        //Rol
        if (DatosSesion.sesion.getRol().equalsIgnoreCase("Jefe de profesores")) {
            txtRol.setText(getResources().getString(R.string.jefe_profesores));
        } else if (DatosSesion.sesion.getRol().equalsIgnoreCase("Docente")){
            txtRol.setText(getResources().getString(R.string.docente));
        } else {
            txtRol.setText(getResources().getString(R.string.administrativo_rol));
        }
        //Imag
        Glide.with(this)
                .load(ApiAdapter.BASE_URL.toString() +DatosSesion.sesion.getImg())
                .into(foto);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    if (DatosSesion.sesion.getRol_id() == 1) {
                        FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_DOCENTE);

                    } else {
                        FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_ADMIN);
                    }
                }
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(Config.REGISTRATION_COMPLETE));
        //LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(Config.PUSH_NOTIFICATION));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = new Fragment();
        switch (id) {
            case R.id.nav_anticipo:
                fragment = new AnticipoFragment();
                break;

            case R.id.nav_rendicion_gastos:
                fragment = new RendicionFragment();
                break;
            case R.id.nav_logout:
                DatosSesion.clear();
                Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;

        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contenedor, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}