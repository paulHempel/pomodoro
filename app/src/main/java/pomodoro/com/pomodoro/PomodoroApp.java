package pomodoro.com.pomodoro;

import android.app.Application;

import io.realm.Realm;
import pomodoro.com.pomodoro.realm.RealmController;
import pomodoro.com.pomodoro.util.AppHelper;

public class PomodoroApp extends Application {

    @Override
    public void onCreate(){
        super.onCreate();

        AppHelper.getInstance().init(this);
        Realm.init(this);
        RealmController.getInstance().init(this);
    }
}
