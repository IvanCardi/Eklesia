package io.eklesia.eklesia;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Santa Caterina on 08/03/2018.
 */

public class ChurchProfileImageBehavior extends AppBarLayout.ScrollingViewBehavior {
    private AppBarLayout appBarLayout;
    public ChurchProfileImageBehavior() {
        super();
    }

    public ChurchProfileImageBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        if (appBarLayout == null) {
            appBarLayout = (AppBarLayout) dependency;
        }
        final boolean result = super.onDependentViewChanged(parent, child, dependency);
        final int totalScrollRange = appBarLayout.getTotalScrollRange();
        child.setPadding(totalScrollRange,0,0,0);

        return result;
    }

}

