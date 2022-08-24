package pomodoro.com.pomodoro.fragments.tutorial;

import android.annotation.SuppressLint;
import android.os.Bundle;

import java.util.Calendar;

import butterknife.OnClick;
import pomodoro.com.pomodoro.MainActivity;
import pomodoro.com.pomodoro.R;
import pomodoro.com.pomodoro.fragments.BaseFragment;
import pomodoro.com.pomodoro.util.AppHelper;

/**
 * Created by ema on 2/16/18.
 */

public class TutorialFourtFrag extends BaseFragment {

    TutorialDialogFragment dialog;
    MainActivity activity;


    public TutorialFourtFrag(){

    }

    @SuppressLint("ValidFragment")
    public TutorialFourtFrag(TutorialDialogFragment dialog){
        this.dialog = dialog;
    }

    @Override
    protected int setResourceId() {
        return R.layout.tutorial_fourth;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        activity = getMainActivity();
    }

    @OnClick(R.id.doneBtn)
    public void onSkipTvClick(){
        AppHelper.getInstance().setIsTutorialDone(true);
        activity.setNameAndLevel();
        activity.setLevelImage();
        dialog.dismiss();
    }
//    @OnClick(R.id.nextBtn)
//    public void onNextBtnClick(){
//        dialog.dismiss();
//    }
//

}
