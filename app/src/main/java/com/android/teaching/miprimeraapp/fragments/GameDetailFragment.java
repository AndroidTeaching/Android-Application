package com.android.teaching.miprimeraapp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.teaching.miprimeraapp.R;
import com.android.teaching.miprimeraapp.interactors.GamesInteractor;
import com.android.teaching.miprimeraapp.model.GameModel;

public class GameDetailFragment extends Fragment {
    private String currentGameWebsite;

    public static GameDetailFragment newInstance(int gameId) {
        GameDetailFragment fragment = new GameDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("game_id", gameId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_detail, container, false);;

        int gameId = getArguments().getInt("game_id", 0);
        GameModel game = new GamesInteractor().getGameWithId(gameId);
        this.currentGameWebsite = game.getOfficialWebsiteUrl();

        // UPDATE VIEW WITH GAME MODEL DATA
        ImageView icono = view.findViewById(R.id.game_icon);
        icono.setImageResource(game.getIconDrawable());

        // 1. CAMBIAR IMAGEN DE FONDO
        LinearLayout fondoLayout = view.findViewById(R.id.game_image_container);
        fondoLayout.setBackgroundResource(game.getBackgroundDrawable());

        // 2. CAMBIAR DESCRIPCION
        TextView descriptionTextView = view.findViewById(R.id.game_description);
        descriptionTextView.setText(game.getDescription());

        view.findViewById(R.id.website_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(currentGameWebsite));
                startActivity(websiteIntent);
            }
        });
        return view;
    }
}
