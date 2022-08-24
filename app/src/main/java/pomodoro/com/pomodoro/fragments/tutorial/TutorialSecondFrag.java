package pomodoro.com.pomodoro.fragments.tutorial;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import pomodoro.com.pomodoro.R;
import pomodoro.com.pomodoro.fragments.BaseFragment;

/**
 * Created by ema on 2/16/18.
 */

public class TutorialSecondFrag extends BaseFragment {

    ViewPager pager;
    TutorialDialogFragment dialog;

    public TutorialSecondFrag(){

    }

    @SuppressLint("ValidFragment")
    public TutorialSecondFrag(TutorialDialogFragment dialog, ViewPager pager){
        this.pager = pager;
        this.dialog = dialog;
    }

    @Override
    protected int setResourceId() {
        return R.layout.tutorial_second;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

//    @OnClick(R.id.nextBtn)
//    public void onNextBtnClick(){
//        pager.setCurrentItem(2);
//    }
//
//    @OnClick(R.id.skipTv)
//    public void onSkipTvClick(){
//        dialog.dismiss();
//    }
}
