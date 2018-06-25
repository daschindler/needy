package com.hagenberg.needy.Activities;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hagenberg.needy.R;

import java.io.File;

public class ImportRecipeActivity extends AppCompatActivity {

    private LinearLayout ll_explorer;
    private Button bt_level_up;

    private File actPathFile;
    private int stageCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_recipe);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ll_explorer = findViewById(R.id.ll_import_recipe_explorer);
        bt_level_up = findViewById(R.id.bt_import_recipe_folder_level_up);

        bt_level_up.setVisibility(View.GONE);

        final File sdcard = Environment.getExternalStorageDirectory();

        SetupFilebrowser(sdcard);

        bt_level_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stageCounter = stageCounter - 1;

                if (stageCounter == 0) {
                    bt_level_up.setVisibility(View.GONE);
                }
                ll_explorer.removeAllViews();
                SetupFilebrowser(actPathFile.getParentFile());
            }
        });


    }

    //Aufruf des Setups für mitgegebene Ordner"ebene"
    private void SetupFilebrowser(File filepath) {
        actPathFile = filepath;
        File dirs = new File(filepath.getAbsolutePath());

        if(dirs.exists()) {
            final File[] files = dirs.listFiles();
            for (int i = 0; i < files.length; i++){
                //Layout für einzelnen Ordner/File vorbereiten
                final LinearLayout v = new LinearLayout(this);
                v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 110));
                v.setOrientation(LinearLayout.HORIZONTAL);
                TextView tv_type = new TextView(this);
                //Setup wenn es sich um einen Ordner handelt
                if (files[i].isDirectory()){
                    if (!files[i].getName().startsWith(".") && !files[i].getName().contains(".")) {
                        //listItems.add(files[i].getName());
                        TextView tv_foldername = new TextView(this);

                        tv_foldername.setText(files[i].getName());
                        tv_foldername.setId(i+1);
                        tv_foldername.setTextSize(21);
                        //tv_foldername.offsetTopAndBottom(20);
                        tv_type.setText("Folder");
                        tv_type.setId(i+2);

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        params.weight = 1.0f;
                        tv_foldername.setLayoutParams(params);

                        v.setId(i);
                        v.addView(tv_foldername);
                        v.addView(tv_type);


                        ll_explorer.addView(v);

                        //Methode wird erneut mit "tieferem" Pfad aufgerufen
                        v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (stageCounter == 0) {
                                    bt_level_up.setVisibility(View.VISIBLE);
                                }
                                stageCounter = stageCounter + 1;
                                int id = v.getId();
                                ll_explorer.removeAllViews();
                                SetupFilebrowser(files[id]);
                            }
                        });
                    }
                    //Setup wenn es sich um eine Datei handelt
                } else if (files[i].isFile()){
                    if (files[i].getName().contains(".needy") && !files[i].getName().startsWith(".")){
                        //listItems.add(files[i].getName());
                        TextView tv_filename = new TextView(this);
                        tv_filename.setText(files[i].getName());
                        tv_filename.setId(i+1);
                        tv_filename.setTextSize(21);
                        v.setId(i);
                        tv_type.setText("File");
                        tv_type.setId(i+2);
                        tv_filename.setTextColor(getResources().getColor(R.color.colorPrimary));

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        params.weight = 1.0f;
                        tv_filename.setLayoutParams(params);

                        v.addView(tv_filename);
                        v.addView(tv_type);
                        ll_explorer.addView(v);

                        v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            //Pfad wird an andere Activity mitgegeben
                            public void onClick(View view) {
                                StoreRecipe(files[v.getId()]);
                                ImportRecipeActivity.this.finish();
                            }
                        });

                    }
                }
            }
        }
    }

    private void StoreRecipe(File file) {

    }
}
