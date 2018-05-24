package com.android.teaching.miprimeraapp.interactors;

import com.android.teaching.miprimeraapp.R;
import com.android.teaching.miprimeraapp.model.GameModel;

import java.util.ArrayList;

public class GamesInteractor {

    private ArrayList<GameModel> games;

    /**
     * Constructor
     */
    public GamesInteractor() {
        // Si no tengo juegos, los creo
        if (games == null) {
            GameModel overwatchGameModel = new GameModel(0, "Overwatch",
                    "Descripción Overwatch",
                    "https://playoverwatch.com/en-us/",
                    "",
                    "");

            GameModel lolGameModel = new GameModel(1, "League Of Legends",
                    "Descripción LoL",
                    "https://play.euw.leagueoflegends.com/es_ES",
                    "",
                    "");

            games = new ArrayList<>();
            games.add(overwatchGameModel);
            games.add(lolGameModel);
        }
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


























