package com.hagenberg.needy.Adapters;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hagenberg.needy.Entity.Recipe;
import com.hagenberg.needy.Entity.RecipeBook;
import com.hagenberg.needy.Fragments.RecipeFinderFragment;
import com.hagenberg.needy.Fragments.ShowAllRecipeBooksFragment;
import com.hagenberg.needy.Fragments.ShowAllRecipesByBookFragment;
import com.hagenberg.needy.Fragments.ShowAllRecipesFragment;
import com.hagenberg.needy.ViewModel.RecipeBookViewModel;
import com.hagenberg.needy.ViewModel.RecipeViewModel;

import java.util.LinkedList;
import java.util.List;

public class MainPagerAdapter extends FragmentPagerAdapter {

    LinkedList<RecipeBook> rBooks = new LinkedList<>();

    private String searchString = "";

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public MainPagerAdapter(FragmentManager fm, FragmentActivity fa) {
        super(fm);

        RecipeBookViewModel recipeBookViewModel = ViewModelProviders.of(fa).get(RecipeBookViewModel.class);
        final LiveData<List<RecipeBook>> allRecipeBooks = recipeBookViewModel.getAllLiveRecipeBooks();

        allRecipeBooks.observe(fa, new Observer<List<RecipeBook>>() {
            @Override
            public void onChanged(@Nullable final List<RecipeBook> recipebooks) {
                rBooks = new LinkedList<>();

                if(recipebooks != null){
                    rBooks.addAll(recipebooks);
                }

                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        /*try {
            ShowAllRecipesFragment fragment = (ShowAllRecipesFragment) object;
            fragment.setSearchString(searchString);
        } catch(Exception ex) {
            ShowAllRecipeBooksFragment fragment = (ShowAllRecipeBooksFragment) object;
            fragment.setSearchString(searchString);
        }*/

        return super.getItemPosition(object);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return RecipeFinderFragment.newInstance();
            case 1:
                return ShowAllRecipesFragment.newInstance();
            case 2:
                return ShowAllRecipeBooksFragment.newInstance();
            default:
                if(rBooks.size() >= (position - 2)){
                    return ShowAllRecipesByBookFragment.newInstance(rBooks.get(position-3));
                }

                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Finder";
            case 1:
                return "All Recipes";
            case 2:
                return "All Books";
            default:
                if(rBooks.size() >= (position - 2)){
                    return rBooks.get(position-3).getName();
                }

                return "";
        }
    }

    @Override
    public int getCount() {
        return rBooks.size() + 3;
    }
}
