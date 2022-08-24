package pomodoro.com.pomodoro;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import butterknife.ButterKnife;

/**
 * Created by ema on 10/25/16.
 */

public abstract class BaseActivity extends AppCompatActivity{

    abstract public int getResourceId();
    abstract public void init(Bundle savedInstanceState);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResourceId());
        ButterKnife.bind(this);

//        if (getResources().getBoolean(R.bool.phone)) {
//            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }else{
//            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        }
        init(savedInstanceState);

    }

    //Hide keyboard when user clicks on view that is not EditText
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (view instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            if (event.getAction() == MotionEvent.ACTION_UP
                    && (x < w.getLeft() || x >= w.getRight()
                    || y < w.getTop() || y > w.getBottom()) ) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }

    public void showNewFragment(Fragment frag, int container)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(container);
        if(currentFragment != null && currentFragment.getClass().equals(frag.getClass()))
            return;
        fragmentManager.beginTransaction().replace(container, frag)
                .commit();
    }

    public void showNewFragment(Fragment frag, int container, Bundle bundle){
        frag.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(container, frag)
                .commit();
    }

    public void showNewFragmentAddToBackStack(Fragment frag, int container)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(container);
        if(currentFragment != null && currentFragment.getClass().equals(frag.getClass()))
            return;
        fragmentManager.beginTransaction().replace(container, frag).addToBackStack(null)
                .commit();
    }

    public void showNewFragmentAddToBackStack(Fragment frag, int container, Bundle bundle)
    {
        frag.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(container);
        if(currentFragment != null && currentFragment.getClass().equals(frag.getClass()))
            return;
        fragmentManager.beginTransaction().replace(container, frag).addToBackStack(null)
                .commit();
    }

    public void clearBackstack(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }

}
