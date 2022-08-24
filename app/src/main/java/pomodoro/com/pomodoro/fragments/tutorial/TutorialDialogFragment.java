package pomodoro.com.pomodoro.fragments.tutorial;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.viewpagerindicator.CirclePageIndicator;

import pomodoro.com.pomodoro.R;


/**
 * Created by ema on 2/16/18.
 */

public class TutorialDialogFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tutorial_layout, container, false);
        getDialog().setTitle("Tutorial");

        final ViewPager pager =  (ViewPager) rootView.findViewById(R.id.pager);
        pager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
                    @Override
                    public Fragment getItem(int position) {
                        switch (position){
                            case 0: return new TutorialFirstFragment(TutorialDialogFragment.this, pager);
                            case 1: return new TutorialSecondFrag(TutorialDialogFragment.this, pager);
                            case 2: return new TutorialThirdFragment(TutorialDialogFragment.this, pager);
                            case 3: return new TutorialFourtFrag(TutorialDialogFragment.this);
//                            case 4: return new TutorialFifthFrag(TutorialDialogFragment.this);
                        }
                        return new TutorialFirstFragment();
                    }

                    @Override
                    public int getCount() {
                        return 4;
                    }
                });

        CirclePageIndicator titleIndicator = (CirclePageIndicator)rootView.findViewById(R.id.indicator);
        titleIndicator.setViewPager(pager);


        return rootView;
    }

    @Override
    public void onResume() {
        // Sets the height and the width of the DialogFragment
        int width = RelativeLayout.LayoutParams.MATCH_PARENT;
        int height = RelativeLayout.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setLayout(width, height);

        super.onResume();
    }
}
