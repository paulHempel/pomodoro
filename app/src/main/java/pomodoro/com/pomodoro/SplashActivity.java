package pomodoro.com.pomodoro;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.google.gson.Gson;

import io.realm.Realm;
import io.realm.RealmResults;
import pomodoro.com.pomodoro.pojo.Pomodoro;
import pomodoro.com.pomodoro.realm.RealmController;
import pomodoro.com.pomodoro.util.AppHelper;

public class SplashActivity extends AppCompatActivity {

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        navigateToNextActvity();
    }

    private void navigateToNextActvity() {

        if(!AppHelper.getInstance().isDoneDeleted()) {
            AppHelper.getInstance().setDoneDeleted(true);
            this.realm = RealmController.getInstance().getRealm();
            RealmResults<Pomodoro> pomodoros = RealmController.getInstance().getDonePomodoros();
            for (final Pomodoro p : pomodoros) {
                if (p != null) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            p.deleteFromRealm();
                        }
                    });
                }
            }

            Answers.getInstance().logCustom(new CustomEvent("Done sessions deleted")
                    .putCustomAttribute("Done deleted", "true")
                    .putCustomAttribute("Level", AppHelper.getInstance().getLevel()));
        }


        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                SplashActivity.this.finish();
            }
        }, 1500);

    }
}
