package com.android.teaching.miprimeraapp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.android.teaching.miprimeraapp.R;
import com.android.teaching.miprimeraapp.fragments.GameDetailFragment;
import com.android.teaching.miprimeraapp.model.GameModel;
import com.android.teaching.miprimeraapp.presenters.GameDetailPresenter;

public class GameDetailActivity extends AppCompatActivity
    implements GameDetailView {

    private GameDetailPresenter presenter;
    private int currentPosition;
    private MyPagerAdapter myPagerAdapter;
    private ViewPager myViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        presenter = new GameDetailPresenter();

        currentPosition = getIntent().getIntExtra("position", 0);
        presenter.startPresenting(this);
        myViewPager = findViewById(R.id.view_pager);
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        myViewPager.setAdapter(myPagerAdapter);
        myViewPager.setCurrentItem(currentPosition);
        getSupportActionBar().setTitle(myPagerAdapter.getPageTitle(currentPosition));
    }

    @Override
    protected void onStart() {
        super.onStart();

        myViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // Cambio el titulo de la toolbar con el que obtengo del adapter
                getSupportActionBar().setTitle(myPagerAdapter.getPageTitle(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onGameLoaded(GameModel game) {

    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            int gameId = presenter.getGames().get(position).getId();
            return GameDetailFragment.newInstance(gameId);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return "";//presenter.getGames().get(position).getName();
        }

        @Override
        public int getCount() {
            return presenter.getGames().size();
        }
    }
}























