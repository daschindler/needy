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
import android.util.Log;
import android.widget.Toast;

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

/**
 * Adapter Class for ViewPager in the MainActivity
 */
public class MainPagerAdapter extends FragmentPagerAdapter {

    LinkedList<RecipeBook> rBooks = new LinkedList<>();

    private String searchString = "";

    /**
     * Setter for searchstring
     * @param searchString
     */
    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public MainPagerAdapter(FragmentManager fm, FragmentActivity fa) {
        super(fm);

        /*RecipeBookViewModel recipeBookViewModel = ViewModelProviders.of(fa).get(RecipeBookViewModel.class);
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
        });*/
    }

    /**
     * Returns position of each fragment, used for setting the searchstring when loading fragments new.
     * @param object
     * @return position of fragment in adapter.
     */
    @Override
    public int getItemPosition(@NonNull Object object) {
        if (object.getClass() == ShowAllRecipeBooksFragment.class) {
            ShowAllRecipeBooksFragment fragment = (ShowAllRecipeBooksFragment) object;
            fragment.setSearchString(searchString);
        } else if (object.getClass() == ShowAllRecipesFragment.class) {
            ShowAllRecipesFragment fragment = (ShowAllRecipesFragment) object;
            fragment.setSearchString(searchString);
        } else if (object.getClass() == ShowAllRecipesByBookFragment.class) {
            return super.getItemPosition(object);
        } else if (object.getClass() == RecipeFinderFragment.class) {
            return super.getItemPosition(object);
        }
        return super.getItemPosition(object);
    }

    /**
     * Returns the fragment for the given position parameter.
     * @param position
     * @return Fragment at given position.
     */
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
                /*if(rBooks.size() >= (position - 2)){
                    return ShowAllRecipesByBookFragment.newInstance(rBooks.get(position-3));
                }*/

                return null;
        }
    }

    /**
     * Returns a different page title for each Page of the ViewPager
     * @param position
     * @return Title for the given position.
     */
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
                /*if(rBooks.size() >= (position - 2)){
                    return rBooks.get(position-3).getName();
                }
*/
                return "";
        }
    }

    /**
     * Returns the amount of pages in the view pager.
     * @return Page amount in viewpager.
     */
    @Override
    public int getCount() {
        return 3; //rBooks.size() + 3;
    }
}
