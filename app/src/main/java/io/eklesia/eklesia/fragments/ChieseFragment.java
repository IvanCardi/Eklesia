package io.eklesia.eklesia.fragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import io.eklesia.eklesia.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChieseFragment extends Fragment {


    public ChieseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_chiese,container,false);
        ImageView imageView= (ImageView) getActivity().findViewById(R.id.dashboard_search_icon);

        if(imageView.getVisibility()!=View.VISIBLE){
            imageView.animate()
                    .alpha(1.0f)
                    .translationX(0)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);

                        }
                    });
            imageView.setVisibility(View.VISIBLE);
        }


        return rootView;
    }

}
