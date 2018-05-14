package com.android.teaching.miprimeraapp.presenters;

import com.android.teaching.miprimeraapp.interactors.GamesInteractor;
import com.android.teaching.miprimeraapp.model.GameModel;
import com.android.teaching.miprimeraapp.view.GameDetailView;

import java.util.ArrayList;

public class GameDetailPresenter {

    private GamesInteractor interactor;
    private GameDetailView view;

    public void startPresenting(GameDetailView view) {
        this.view = view;
        interactor = new GamesInteractor();
    }

    public void loadGameWithId(int id) {
        GameModel game = interactor.getGameWithId(id);
        view.onGameLoaded(game);
    }

    public ArrayList<GameModel> getGames() {
        return interactor.getGames();
    }
}













