package com.android.teaching.miprimeraapp.interactors;

import android.util.Log;

import com.android.teaching.miprimeraapp.model.GameModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GamesFirebaseInteractor {

    private ArrayList<GameModel> games = new ArrayList<>();

    public void getGames(final GamesInteractorCallback callback) {
        // 1- Llamar a Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myReference = firebaseDatabase.getReference("games");
        myReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // 2- Obtener la lista de GameModel
                for(DataSnapshot nodoJuego : dataSnapshot.getChildren()) {
                    GameModel model = nodoJuego.getValue(GameModel.class);
                    Log.d("Firebase Interactor", "Game: " + model.getName());
                    games.add(model);
                }
                // 3- Notificar a callback.onGamesAvailable()
                callback.onGamesAvailable();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public ArrayList<GameModel> getGames() {
        return games;
    }

    public GameModel getGameWithId(int id) {
        // Obtener de 'games' el juego con el identificador 'id'
        for (GameModel game: games) {
            if (game.getId() == id) {
                return game;
            }
        }
        return null;
    }
}
