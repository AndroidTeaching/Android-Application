package com.android.teaching.miprimeraapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.android.teaching.miprimeraapp.fragments.GameDetailFragment;
import com.android.teaching.miprimeraapp.model.GameModel;
import com.android.teaching.miprimeraapp.presenters.GameDetailPresenter;
import com.android.teaching.miprimeraapp.view.GameDetailView;

public class GameDetailActivity extends AppCompatActivity
    implements GameDetailView {

    private GameDetailPresenter presenter;
    private int selectedGamePosition;

    private ViewPager viewPager;
    private MyPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);

        viewPager = findViewById(R.id.view_pager);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        presenter = new GameDetailPresenter();

        selectedGamePosition = getIntent().getIntExtra("game_position", 0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.startPresenting(this);
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(selectedGamePosition);
        getSupportActionBar().setTitle(pagerAdapter.getPageTitle(selectedGamePosition));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                getSupportActionBar().setTitle(pagerAdapter.getPageTitle(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onGameLoaded(GameModel game) {
        // NO-OP
    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            int gameId = presenter.getGames().get(position).getId();
            GameDetailFragment fragment = GameDetailFragment.newInstance(gameId);
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return presenter.getGames().get(position).getName();
        }

        @Override
        public int getCount() {
            return presenter.getGames().size();
        }
    }
}























