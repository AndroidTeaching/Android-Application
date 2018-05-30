package com.android.teaching.miprimeraapp;

import android.*;
import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.teaching.miprimeraapp.interactors.GamesFirebaseInteractor;
import com.android.teaching.miprimeraapp.interactors.GamesInteractor;
import com.android.teaching.miprimeraapp.interactors.GamesInteractorCallback;
import com.android.teaching.miprimeraapp.login.view.LoginActivity;
import com.android.teaching.miprimeraapp.model.GameModel;
import com.android.teaching.miprimeraapp.view.GameDetailActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    private MyAdapter myAdapter;
    private ListView listView;
    private GamesFirebaseInteractor gamesFirebaseInteractor;
    private MyConnectivityReceiver myConnectivityReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Comprobar si tengo permisos para acceder a la ubicación
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            // Tengo permisos
            obtenerUbicacion();
        } else {
            // No tengo permisos
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    100);
        }

        // Escuchar cambios de conectividad
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        myConnectivityReceiver = new MyConnectivityReceiver();
        registerReceiver(myConnectivityReceiver, myIntentFilter);

        // Detectar si tenemos conectividad
        ConnectivityManager myConnectivtyManager = (ConnectivityManager) getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo myNetworkInfo = myConnectivtyManager.getActiveNetworkInfo();
        boolean hasConnectivity = myNetworkInfo != null
                && myNetworkInfo.isConnectedOrConnecting();

        // Si tengo conectividad, hago las peticiones a Firebase
        if(hasConnectivity) {
            // region Firebase
            String token = FirebaseInstanceId.getInstance().getToken();
            FirebaseDatabase myDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = myDatabase
                    .getReference("device_push_token");
            databaseReference.setValue(token);

            gamesFirebaseInteractor = new GamesFirebaseInteractor();
            gamesFirebaseInteractor.getGames(new GamesInteractorCallback() {
                @Override
                public void onGamesAvailable() {
                    findViewById(R.id.loading).setVisibility(View.GONE);
                    // Aquí, GamesFirebaseInteractor ya tiene la lista de juegos
                    myAdapter = new MyAdapter();
                    listView.setAdapter(myAdapter);
                }
            });
            // endregion
        } else {
            // Si no tengo conectividad, escondo el loading y muestro un error
            findViewById(R.id.loading).setVisibility(View.GONE);
            Toast.makeText(this, "You don't have Internet connection!",
                    Toast.LENGTH_LONG).show();
        }

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        listView = findViewById(R.id.list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // Abrir activity de detalle
                Intent intent = new Intent(ListActivity.this,
                        GameDetailActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            if (permissions.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // TENGO PERMISOS
                obtenerUbicacion();
            } else {
                // NO TENGO PERMISOS
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(myConnectivityReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater myInflater = getMenuInflater();
        myInflater.inflate(R.menu.menu_list_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Open LoginActivity
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater myInflater = getMenuInflater();
        myInflater.inflate(R.menu.delete_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // El usuario ha seleccionado un elemento del menu contextual
        myAdapter.notifyDataSetChanged();
        return super.onContextItemSelected(item);
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return gamesFirebaseInteractor.getGames().size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.list_item, parent, false);

            ImageView icon = rowView.findViewById(R.id.image_view);
            Glide.with(ListActivity.this).load(
                    gamesFirebaseInteractor.getGames().get(position).getIcon())
                    .into(icon);
            //icon.setImageResource(gameIcons.get(position));

            TextView textView = rowView.findViewById(R.id.text_view);
            textView.setText(gamesFirebaseInteractor.getGames()
                    .get(position).getName());

            return rowView;
        }
    }

    @SuppressLint("MissingPermission")
    private void obtenerUbicacion() {
        // 1. Obtener LocationManager
        LocationManager myLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // 2. Crear el Listener
        LocationListener myListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("ListActivity","Location changed: " + location.toString());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        //  3. Escuchar las actualizaciones
        myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                0,0, myListener);
    }
}
