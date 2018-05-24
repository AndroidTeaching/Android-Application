package com.android.teaching.miprimeraapp.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.teaching.miprimeraapp.R;
import com.android.teaching.miprimeraapp.WebViewActivity;
import com.android.teaching.miprimeraapp.interactors.GamesInteractor;
import com.android.teaching.miprimeraapp.model.GameModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class GameDetailFragment extends Fragment {


    public GameDetailFragment() {
        // Required empty public constructor
    }

    public static GameDetailFragment newInstance(int gameId) {
        GameDetailFragment fragment = new GameDetailFragment();
        Bundle myBundle = new Bundle();
        myBundle.putInt("game_id", gameId);
        fragment.setArguments(myBundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_game_detail, container,
                false);

        // Obtener GameModel de GamesInteractor
        int gameId = getArguments().getInt("game_id", 0);
        final GameModel game = new GamesInteractor().getGameWithId(gameId);

        // UPDATE VIEW WITH GAME MODEL DATA
        ImageView icono = fragmentView.findViewById(R.id.game_icon);
        //icono.setImageResource(game.getIconDrawable());

        // 1. CAMBIAR IMAGEN DE FONDO
        LinearLayout fondoLayout = fragmentView.findViewById(R.id.game_image_container);
        //fondoLayout.setBackgroundResource(game.getBackgroundDrawable());

        // 2. CAMBIAR DESCRIPCION
        TextView descriptionTextView = fragmentView.findViewById(R.id.game_description);
        descriptionTextView.setText(game.getDescription());

        // 3. DEFINIR ACCION PARA EL BOTON
        Button boton = fragmentView.findViewById(R.id.website_button);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webIntent = new Intent(getContext(), WebViewActivity.class);
                webIntent.putExtra("url", game.getOfficialWebsiteUrl());
                startActivity(webIntent);
            }
        });

        return fragmentView;
    }
}
