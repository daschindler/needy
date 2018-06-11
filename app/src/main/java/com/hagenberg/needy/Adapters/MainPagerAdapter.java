package com.hagenberg.needy.Adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hagenberg.needy.Fragments.ShowAllRecipeBooksFragment;
import com.hagenberg.needy.Fragments.ShowAllRecipesFragment;

public class MainPagerAdapter extends FragmentPagerAdapter {
    private static int page_count = 2;
    private String searchString = "";

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        try {
            ShowAllRecipesFragment fragment = (ShowAllRecipesFragment) object;
            fragment.setSearchString(searchString);
        } catch(Exception ex) {
            ShowAllRecipeBooksFragment fragment = (ShowAllRecipeBooksFragment) object;
            fragment.setSearchString(searchString);
        }
        return super.getItemPosition(object);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ShowAllRecipesFragment.newInstance();
            case 1:
                //update with searchString newInstance
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
