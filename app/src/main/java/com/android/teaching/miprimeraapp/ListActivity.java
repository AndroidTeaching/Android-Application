package com.android.teaching.miprimeraapp;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

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
}
