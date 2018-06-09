package com.hagenberg.needy.Adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hagenberg.needy.Activities.ShowAllRecipeBooksFragment;
import com.hagenberg.needy.Activities.ShowAllRecipesFragment;

public class MainPagerAdapter extends FragmentPagerAdapter {
    private static int page_count = 2;

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ShowAllRecipesFragment.newInstance();
            case 1:
                return ShowAllRecipeBooksFragment.newInstance();
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Recipes";
            case 1:
                return "Recipe Books";
            default:
                return "";
        }
    }

    @Override
    public int getCount() {
        return page_count;
    }
}
