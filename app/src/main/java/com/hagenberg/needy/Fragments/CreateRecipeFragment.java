package com.hagenberg.needy.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hagenberg.needy.Entity.Ingredient;
import com.hagenberg.needy.Entity.Recipe;
import com.hagenberg.needy.Entity.Unit;
import com.hagenberg.needy.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateRecipeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateRecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateRecipeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CreateRecipeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateReceiptFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateRecipeFragment newInstance(String param1, String param2) {
        CreateRecipeFragment fragment = new CreateRecipeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    TextView tv_create_recipe_name;
    Button bt_new_ingredient, bt_save;
    LinearLayout ll_ingredients, ll_first_ingredient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_create_recipe, container, false);
        tv_create_recipe_name = rootView.findViewById(R.id.tv_create_recipe_name);
        bt_new_ingredient = rootView.findViewById(R.id.bt_create_recipe_new_ingredient);
        bt_save = rootView.findViewById(R.id.bt_create_recipe_saverecipe);
        ll_ingredients = rootView.findViewById(R.id.ll_create_recipe_ingredients);

        CreateNewIngredientView();


        bt_new_ingredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewIngredientView();
            }
        });

        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_create_recipe_name.getText() != null && !tv_create_recipe_name.getText().equals(""))
                Toast.makeText(getActivity(), "Rezept erstellt", Toast.LENGTH_SHORT).show();
            }
        });




        return rootView;
    }

    private void CreateNewIngredientView() {
        //create vertical layout to fit 2 horizontal layout
        final LinearLayout llnewIngredientVertical = new LinearLayout(getActivity());
        llnewIngredientVertical.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        llnewIngredientVertical.setOrientation(LinearLayout.VERTICAL);
        llnewIngredientVertical.setPadding(0,15,0,15);

        //first horizontal layout fitting name and amount
        LinearLayout llnewIngredientHorizontal1 = new LinearLayout(getActivity());
        llnewIngredientHorizontal1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        llnewIngredientHorizontal1.setOrientation(LinearLayout.HORIZONTAL);
        llnewIngredientHorizontal1.setPadding(0,0,0,0);

        //second horizontal layout fitting unit and delete
        LinearLayout llnewIngredientHorizontal2 = new LinearLayout(getActivity());
        llnewIngredientHorizontal2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        llnewIngredientHorizontal2.setOrientation(LinearLayout.HORIZONTAL);
        llnewIngredientHorizontal2.setPadding(0,0,0,0);

        EditText etnewIngredientName = new EditText(getActivity());
        etnewIngredientName.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 4f));
        etnewIngredientName.setHint("Name");

        EditText etNewIngredientAmount = new EditText(getActivity());
        etNewIngredientAmount.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 2f));
        etNewIngredientAmount.setHint("Menge");

        ArrayList<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add(Unit.CoffeeSpoon.toString());
        spinnerArray.add(Unit.Gram.toString());
        spinnerArray.add(Unit.Liter.toString());
        spinnerArray.add(Unit.Unit.toString());

        Spinner etNewIngredientSpinner = new Spinner(getActivity());
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        etNewIngredientSpinner.setAdapter(spinnerArrayAdapter);
        etNewIngredientSpinner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 2f));
        etNewIngredientSpinner.setPrompt("Einheit");

        Button btDeleteIngredient = new Button(getActivity());
        btDeleteIngredient.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
        btDeleteIngredient.setText("Delete");

        llnewIngredientHorizontal1.addView(etnewIngredientName);
        llnewIngredientHorizontal1.addView(etNewIngredientAmount);
        llnewIngredientHorizontal2.addView(etNewIngredientSpinner);
        llnewIngredientHorizontal2.addView(btDeleteIngredient);

        llnewIngredientVertical.addView(llnewIngredientHorizontal1);
        llnewIngredientVertical.addView(llnewIngredientHorizontal2);

        ll_ingredients.addView(llnewIngredientVertical);

        btDeleteIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_ingredients.removeView(llnewIngredientVertical);
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
