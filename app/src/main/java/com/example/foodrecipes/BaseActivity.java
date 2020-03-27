package com.example.foodrecipes;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

// Activity absract car non instanciée. Inutile de l'ajouter dans AndroidManifest.xml
public abstract class BaseActivity extends AppCompatActivity {

    ProgressBar mProgressBar;

    // layoutResID = R.layout.activity_recipe_list
    @Override
    public void setContentView(int layoutResID) {
        // rappel :
        // inflate() prend un layout XML et en créer un objet View (ici le viewGroup)
        // findViewById() cherche une View à l'intérieur d'un viewGroup

        ConstraintLayout constraintLayout =
                (ConstraintLayout) getLayoutInflater().inflate(R.layout.activity_base, null);

        mProgressBar = constraintLayout.findViewById(R.id.progress_bar);

        FrameLayout frameLayout = constraintLayout.findViewById(R.id.activity_content);

        // ici on définit le frameLayout comme root view pour les activités qui étendent cette
        // BaseActivity (attachToRoot to true). Ce qui signifie que activity_recipe_list sera 'inflaté'
        // dans le frameLayout de activity_base
        // voir signature inflate()
        getLayoutInflater().inflate(layoutResID, frameLayout, true);

        // surtout passer ici le constraintLayout (acitivity_base)
        super.setContentView(constraintLayout);
    }

    public void showProgressBar(boolean visibility) {
        mProgressBar.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
    }
}
