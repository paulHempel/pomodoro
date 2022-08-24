package pomodoro.com.pomodoro.fragments.tutorial;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.OnClick;
import pomodoro.com.pomodoro.MainActivity;
import pomodoro.com.pomodoro.R;
import pomodoro.com.pomodoro.fragments.BaseFragment;
import pomodoro.com.pomodoro.util.AppHelper;

public class TutorialFifthFrag extends BaseFragment {

    @BindView(R.id.nameEdt)
    EditText nameEdt;

    TutorialDialogFragment dialog;
    MainActivity activity;

    public TutorialFifthFrag(){

    }

    @SuppressLint("ValidFragment")
    public TutorialFifthFrag(TutorialDialogFragment dialog){
        this.dialog = dialog;
    }

    @Override
    protected int setResourceId() {
        return R.layout.tutorial_fifth;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        activity = getMainActivity();
    }

    //    @OnClick(R.id.nextBtn)
//    public void onNextBtnClick(){
//        dialog.dismiss();
//    }
//
    @OnClick(R.id.doneBtn)
    public void onSkipTvClick(){
        AppHelper.getInstance().setIsTutorialDone(true);
        AppHelper.getInstance().setFutureName(nameEdt.getText().toString());
        activity.setNameAndLevel();
        activity.setLevelImage();
        dialog.dismiss();
    }
}
