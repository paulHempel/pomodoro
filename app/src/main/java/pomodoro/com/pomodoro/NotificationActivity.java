package pomodoro.com.pomodoro;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import pomodoro.com.pomodoro.constants.Constants;
import pomodoro.com.pomodoro.fragments.pomodoro.AdviceDialog;
import pomodoro.com.pomodoro.pojo.Pomodoro;
import pomodoro.com.pomodoro.realm.RealmController;
import pomodoro.com.pomodoro.util.AppHelper;

public class NotificationActivity extends BaseActivity {
    public static String TAG = NotificationActivity.class.getName();

    @BindView(R.id.stopBtn)
    Button stopBtn;
    @BindView(R.id.timerTv)
    TextView timerTv;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.nextPomoTitle)
    TextView nextPomoTitle;
    @BindView(R.id.placeholderText)
    TextView placeholderText;

    @BindView(R.id.resultRl)
    RelativeLayout resultRl;
    @BindView(R.id.contentRl)
    RelativeLayout contentRl;

    @BindView(R.id.focusedRb)
    RadioButton focusedRb;
    @BindView(R.id.notFocusedRb)
    RadioButton notFocusedRb;

    @BindView(R.id.finishedRb)
    RadioButton finishedRb;
    @BindView(R.id.notFinishedRb)
    RadioButton notFinishedRb;

    @BindView(R.id.firstGroup)
    RadioGroup firstGroup;
    @BindView(R.id.secondGroup)
    RadioGroup secondGroup;

    @BindView(R.id.label0)
    TextView taskTv;

    @BindView(R.id.sessionTextTv)
    TextView sessionTextTv;

    @BindView(R.id.startSessionBtn)
    Button startSessionBtn;

    CountDownTimer countDownTimer;

    int timeUntilStart = 60*1000; //4*60*1000; //should be 4 min - 4*60*1000
    int pomodoroDuration;
    Realm realm;
    Pomodoro pomodoro;
    boolean isCanceld, isFromMain;

    String id;
    PowerManager.WakeLock wakeLock;

    @Override
    public int getResourceId() {
        return R.layout.activity_notification;
    }

    @Override
    public void init(Bundle savedInstanceState) {

        this.realm = RealmController.getInstance().getRealm();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.FULL_WAKE_LOCK, "Pomodoro:WakeLock");


        id = getIntent().getStringExtra(Constants.POMODORO_ID);
        pomodoroDuration = (int)getIntent().getLongExtra(Constants.DURATION, 0);
        isFromMain = getIntent().getBooleanExtra(Constants.FROM_MAIN, false);

        pomodoro = RealmController.getInstance().getPomodoroById(id);

        if (pomodoro != null) {
            sessionTextTv.setText(pomodoro.getName());
            init2();
        }

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

    }

     void init2() {
        if(isFromMain){
            contentRl.setVisibility(View.GONE);
            resultRl.setVisibility(View.VISIBLE);
            onCreateViewResultFragm();

        }else{
            contentRl.setVisibility(View.VISIBLE);
            resultRl.setVisibility(View.GONE);
            setTimer();
        }
    }

    @OnClick(R.id.stopBtn)
    public void onStopBtnCliked(){

        showAlertDialogButtonClicked();
    }

    @OnClick(R.id.startSessionBtn)
    public void onStartSessionBtn(){
        startSessionBtn.setVisibility(View.INVISIBLE);
        startSessionSetup();
    }


    public void showAlertDialogButtonClicked() {

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(NotificationActivity.this);
        builder.setTitle(getString(R.string.alert));
        builder.setMessage(getString(R.string.are_you_sure_you_want_exit));

        // add the buttons
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressBar.clearAnimation();
                countDownTimer.cancel();
                contentRl.setVisibility(View.GONE);
                resultRl.setVisibility(View.VISIBLE);
                isCanceld = true;
                onCreateViewResultFragm();
            }
        });

        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                // do something like...
            }
        });


        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void startTimer() {

        if (pomodoro != null) {

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    pomodoro.setStarted(true);
                }
            });

        }
        countDownTimer = new CountDownTimer(timeUntilStart, 1000) {
            // 500 means, onTick function will be called at every 500
            // milliseconds

            @Override
            public void onTick(long leftTimeInMilliseconds) {
                long seconds = leftTimeInMilliseconds / 1000;
                progressBar.setProgress((int) (leftTimeInMilliseconds / 1000));


                timerTv.setText(String.format("%02d", seconds / 60)
                        + ":" + String.format("%02d", seconds % 60));

            }

            @Override
            public void onFinish() {
                // this function will be called when the timecount is finished

                pomodoro = RealmController.getInstance().getPomodoroById(id);

                if (pomodoro != null) {

                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            pomodoro.setResultShowed(true);
                            pomodoro.setStayedFocused(false);
                            pomodoro.setTaskFinished(false);
                        }
                    });

                    setAdviceAfterLastPomodoro(AppHelper.getInstance().getLevel(), pomodoro.getNumber());

                }
            }

        }.start();

    }

    private void startSessionSetup() {
//        showStartSessionNotificaion();
        progressBar.clearAnimation();
        countDownTimer.cancel();

        stopBtn.setVisibility(View.VISIBLE);
        nextPomoTitle.setText(getString(R.string.time_left));
        placeholderText.setText(getString(R.string.stay_focused_and_avoid));

        Answers.getInstance().logCustom(new CustomEvent("Preptime  finished")
                .putCustomAttribute("Id", id));

        setPomodoroTimer();
    }

    ObjectAnimator animation;
    private void setTimer() {
        progressBar.setMax(timeUntilStart);

        animation = ObjectAnimator.ofInt(progressBar, "progress", 0, timeUntilStart); // see this max value coming back here, we animate towards that value
        animation.setDuration(timeUntilStart); // in milliseconds
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();

        startTimer();
    }

    ObjectAnimator animationPomoTimer;
    private void setPomodoroTimer() {

        progressBar.setMax(pomodoroDuration);

        animationPomoTimer = ObjectAnimator.ofInt(progressBar, "progress", 0, pomodoroDuration); // see this max value coming back here, we animate towards that value
        animationPomoTimer.setDuration(pomodoroDuration); // in milliseconds
        animationPomoTimer.setInterpolator(new DecelerateInterpolator());
        animationPomoTimer.start();
        startPomodoroTimer();

    }

    private void startPomodoroTimer() {


        if (pomodoro != null) {

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    pomodoro.setStarted(true);
                }
            });

        }

        countDownTimer = new CountDownTimer(pomodoroDuration, 1000) {
            // 500 means, onTick function will be called at every 500
            // milliseconds

            @Override
            public void onTick(long leftTimeInMilliseconds) {
                long seconds = leftTimeInMilliseconds / 1000;
                progressBar.setProgress((int) (leftTimeInMilliseconds / 1000));


                timerTv.setText(String.format("%02d", seconds / 60)
                        + ":" + String.format("%02d", seconds % 60));

            }

            @Override
            public void onFinish() {
                // this function will be called when the timecount is finished

                finishSession();

            }

        }.start();

    }

    private void finishSession() {
        showFinishNotificaion();
        progressBar.clearAnimation();
        contentRl.setVisibility(View.GONE);
        resultRl.setVisibility(View.VISIBLE);
        isCanceld = false;

        Answers.getInstance().logCustom(new CustomEvent("Session  finished")
                .putCustomAttribute("Id", id));

        onCreateViewResultFragm();
    }

    private void onCreateViewResultFragm(){
        pomodoro = RealmController.getInstance().getPomodoroById(id);

        if (pomodoro != null) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    pomodoro.setResultShowed(true);
                }
            });

        }
        if (pomodoro != null) {
            taskTv.setText(pomodoro.getName());
        }
        if(isCanceld){
            notFocusedRb.setChecked(true);
        }
    }

    private void showFinishNotificaion() {
        String channelId = "channel-01";
        String channelName = "Discipline Bee channel";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            mChannel.setVibrationPattern(new long[]{1000, 1000});
            notificationManager.createNotificationChannel(mChannel);
        }

        Intent showIntent = new Intent();
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, showIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);

        Notification notification = builder.setContentTitle(this.getString(R.string.app_name))
                .setContentText(getString(R.string.session_finished))
                .setSmallIcon(R.drawable.ic_stat_assignment)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setVibrate(new long[]{1000, 1000})
                .setOngoing(false)
                .build();

        Answers.getInstance().logCustom(new CustomEvent("Finish notification showed")
                .putCustomAttribute("Id", id));

        notificationManager.notify(0, notification);
    }

    private void showStartSessionNotificaion() {
        String channelId = "channel-01";
        String channelName = "Discipline Bee channel";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            mChannel.setVibrationPattern(new long[]{1000, 1000});
            notificationManager.createNotificationChannel(mChannel);
        }

        Intent showIntent = new Intent();
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, showIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);

        Notification notification = builder.setContentTitle(this.getString(R.string.app_name))
                .setContentText(getString(R.string.your_pomodoro_starts_now))
                .setSmallIcon(R.drawable.ic_stat_assignment)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setVibrate(new long[]{1000, 1000})
                .setOngoing(false)
                .build();

        Answers.getInstance().logCustom(new CustomEvent("Start session notification showed")
                .putCustomAttribute("Id", id));

        notificationManager.notify(0, notification);
    }

    @OnClick(R.id.saveBtn)
    public void onSaveBtnClick(){
        if(firstGroup.getCheckedRadioButtonId() == -1 || secondGroup.getCheckedRadioButtonId() == -1){
            Toast.makeText(this, getString(R.string.please_answer), Toast.LENGTH_SHORT).show();
        }else {

            if (pomodoro != null) {

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        pomodoro.setPomodoroCompleted(!isCanceld);
                        if (focusedRb.isChecked()) {
                            pomodoro.setStayedFocused(true);
                        }
                        if (notFocusedRb.isChecked()) {
                            pomodoro.setStayedFocused(false);
                        }
                        if (finishedRb.isChecked()) {
                            pomodoro.setTaskFinished(true);
                        }
                        if (notFinishedRb.isChecked()) {
                            pomodoro.setTaskFinished(false);
                        }
                    }
                });
                //TODO remove
//                AppHelper.getInstance().setLevel(2);

                setAdviceAfterLastPomodoro(AppHelper.getInstance().getLevel(), pomodoro.getNumber());

//                activity.showNewFragmentAddToBackStack(new OverviewFragment(), R.id.main_container);

            }
        }
    }

    public void calculateLevel(){
        RealmResults<Pomodoro> pomodoros = RealmController.getInstance().getFinishedPomodoros();
//        RealmResults<Pomodoro> pomodoros = RealmController.getInstance().getCompletedPomodoros();

        switch (AppHelper.getInstance().getLevel()){
            case 0:
                AdviceDialog alert1 = new AdviceDialog();
                alert1.showDialog(this, getString(R.string.result_after_level_0), false );
                for(final Pomodoro p : pomodoros){
                    if (p != null) {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                p.setDone(true);
                            }
                        });
                    }
                }
                setPushNotification();
                AppHelper.getInstance().setSessionCount(0);
                AppHelper.getInstance().setIsProgramStarted(false);

                Answers.getInstance().logCustom(new CustomEvent("Level 0 finished")
                        .putCustomAttribute("Id", id));
                break;
            case 1:
                int maxPomo = pomodoros.size();
                ArrayList<Pomodoro> taskFinishedA = new ArrayList<>();
                ArrayList<Pomodoro> successfulPomodorosA = new ArrayList<>();

                for(int i=0; i<pomodoros.size(); i++){
                    if(pomodoros.get(i).isTaskFinished()){
                        taskFinishedA.add(pomodoros.get(i));
                    }
                    if(pomodoros.get(i).isStayedFocused()){
                        successfulPomodorosA.add(pomodoros.get(i));
                    }
                }
                if(AppHelper.getInstance().getSessionCount() != 0 && pomodoros.size() == AppHelper.getInstance().getSessionCount()) {
                    int pomoPercent = Math.round((successfulPomodorosA.size() * 100.0f) / maxPomo);
                    int taskPercent = Math.round((taskFinishedA.size() * 100.0f) / maxPomo);
                    if (pomoPercent >= 100) {
                        AppHelper.getInstance().setLevel(2);
                        AppHelper.getInstance().setLevel1Pomo(pomoPercent);
                        AppHelper.getInstance().setLevel1Tasks(taskPercent);
                        AppHelper.getInstance().setSessionCount(0);
                        AppHelper.getInstance().setIsProgramStarted(false);

                        AdviceDialog alert = new AdviceDialog();
                        alert.showDialog(this, getString(R.string.result_after_level_1), false, AppHelper.getInstance().getLevel());
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
                        Answers.getInstance().logCustom(new CustomEvent("Level 1 successfully finished")
                                .putCustomAttribute("Id", id));
                    } else {
//                        activity.startActivity(new Intent(activity, MainActivity.class));
                        //fail
                        AppHelper.getInstance().setSessionCount(0);
                        AppHelper.getInstance().setIsProgramStarted(false);

                        AdviceDialog alert = new AdviceDialog();
                        alert.showDialog(this, getString(R.string.level_1_fail), false);
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
                        Answers.getInstance().logCustom(new CustomEvent("Level 1 failed finished")
                                .putCustomAttribute("Id", id));
                    }
                    setPushNotification();
                }else{
                    this.startActivity(new Intent(this, MainActivity.class));
                    finish();
                }
                break;
            case 2:
                int maxPomo2 = pomodoros.size();
                ArrayList<Pomodoro> taskFinishedB = new ArrayList<>();
                ArrayList<Pomodoro> successfulPomodorosB = new ArrayList<>();

                for(int i=0; i<pomodoros.size(); i++){
                    if(pomodoros.get(i).isTaskFinished()){
                        taskFinishedB.add(pomodoros.get(i));
                    }
                    if(pomodoros.get(i).isStayedFocused()){
                        successfulPomodorosB.add(pomodoros.get(i));
                    }
                }
                if(AppHelper.getInstance().getSessionCount() != 0 && pomodoros.size() == AppHelper.getInstance().getSessionCount()) {
                    int pomoPercent = Math.round((successfulPomodorosB.size() * 100.0f) / maxPomo2);
                    int taskPercent = Math.round((taskFinishedB.size() * 100.0f) / maxPomo2);
                    if (pomoPercent >= 87) { //TODO 10
                        AppHelper.getInstance().setLevel(3);
                        AppHelper.getInstance().setLevel2Pomo(pomoPercent);
                        AppHelper.getInstance().setLevel2Tasks(taskPercent);
                        AppHelper.getInstance().setSessionCount(0);
                        AppHelper.getInstance().setIsProgramStarted(false);

                        AdviceDialog alert = new AdviceDialog();
                        alert.showDialog(this, getString(R.string.result_after_level_2), false, AppHelper.getInstance().getLevel());

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
                        Answers.getInstance().logCustom(new CustomEvent("Level 2 successfully finished")
                                .putCustomAttribute("Id", id));
                    } else {
//                        activity.startActivity(new Intent(activity, MainActivity.class));
                        //fail
                        AppHelper.getInstance().setSessionCount(0);
                        AppHelper.getInstance().setIsProgramStarted(false);

                        AdviceDialog alert = new AdviceDialog();
                        alert.showDialog(this, getString(R.string.level_2_fail), false);
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
                        Answers.getInstance().logCustom(new CustomEvent("Level 2 failed finished")
                                .putCustomAttribute("Id", id));
                    }
                    setPushNotification();
                }else{
                    this.startActivity(new Intent(this, MainActivity.class));
                    finish();
                }
                break;
            case 3:
                int maxPomo3 = pomodoros.size();

                ArrayList<Pomodoro> taskFinished = new ArrayList<>();
                ArrayList<Pomodoro> successfulPomodoros = new ArrayList<>();

                for(int i=0; i<pomodoros.size(); i++){
                    if(pomodoros.get(i).isTaskFinished()){
                        taskFinished.add(pomodoros.get(i));
                    }
                    if(pomodoros.get(i).isStayedFocused()){
                        successfulPomodoros.add(pomodoros.get(i));
                    }
                }
                if(AppHelper.getInstance().getSessionCount() != 0 && pomodoros.size() == AppHelper.getInstance().getSessionCount()) {
                    int pomoPercent = Math.round((successfulPomodoros.size() * 100.0f) / maxPomo3);
                    int taskPercent = Math.round((taskFinished.size() * 100.0f) / maxPomo3);
                    if (pomoPercent >= 75 && taskPercent >= 12) { //TODO 13
                        AppHelper.getInstance().setLevel(4);
                        AppHelper.getInstance().setLevel3Pomo(pomoPercent);
                        AppHelper.getInstance().setLevel3Tasks(taskPercent);
                        AppHelper.getInstance().setSessionCount(0);
                        AppHelper.getInstance().setIsProgramStarted(false);

                        AdviceDialog alert = new AdviceDialog();
                        alert.showDialog(this, getString(R.string.result_after_level_3), false, AppHelper.getInstance().getLevel());

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
                        Answers.getInstance().logCustom(new CustomEvent("Level 3 successfully finished")
                                .putCustomAttribute("Id", id));
                    } else {
//                        activity.startActivity(new Intent(activity, MainActivity.class));
                        //fail
                        AppHelper.getInstance().setSessionCount(0);
                        AppHelper.getInstance().setIsProgramStarted(false);

                        AdviceDialog alert = new AdviceDialog();
                        alert.showDialog(this, getString(R.string.level_3_fail), false);
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
                        Answers.getInstance().logCustom(new CustomEvent("Level 3 failed finished")
                                .putCustomAttribute("Id", id));
                    }
                    setPushNotification();
                }else{
                    this.startActivity(new Intent(this, MainActivity.class));
                    finish();
                }
                break;
            case 4:
                int maxPomo4 = pomodoros.size();

                ArrayList<Pomodoro> taskFinished2 = new ArrayList<>();
                ArrayList<Pomodoro> successfulPomodoros2 = new ArrayList<>();

                for(int i=0; i<pomodoros.size(); i++){
                    if(pomodoros.get(i).isTaskFinished()){
                        taskFinished2.add(pomodoros.get(i));
                    }
                    if(pomodoros.get(i).isStayedFocused()){
                        successfulPomodoros2.add(pomodoros.get(i));
                    }
                }
                if(AppHelper.getInstance().getSessionCount() != 0 && pomodoros.size() == AppHelper.getInstance().getSessionCount()) {
                    int pomoPercent = Math.round((successfulPomodoros2.size() * 100.0f) / maxPomo4);
                    int taskPercent = Math.round((taskFinished2.size() * 100.0f) / maxPomo4);
                    if (pomoPercent >= 75 && taskPercent >= 25) {
                        AppHelper.getInstance().setLevel(5);
                        AppHelper.getInstance().setLevel4Pomo(pomoPercent);
                        AppHelper.getInstance().setLevel4Tasks(taskPercent);
                        AppHelper.getInstance().setSessionCount(0);
                        AppHelper.getInstance().setIsProgramStarted(false);

                        AdviceDialog alert = new AdviceDialog();
                        alert.showDialog(this, getString(R.string.result_after_level_4), false, AppHelper.getInstance().getLevel());

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
                        Answers.getInstance().logCustom(new CustomEvent("Level 4 successfully finished")
                                .putCustomAttribute("Id", id));
                    } else {
//                        activity.startActivity(new Intent(activity, MainActivity.class));
                        //fail
                        AppHelper.getInstance().setSessionCount(0);
                        AppHelper.getInstance().setIsProgramStarted(false);

                        AdviceDialog alert = new AdviceDialog();
                        alert.showDialog(this, getString(R.string.level_4_fail), false);
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
                        Answers.getInstance().logCustom(new CustomEvent("Level 4 failed finished")
                                .putCustomAttribute("Id", id));
                    }
                    setPushNotification();
                }else{
                    this.startActivity(new Intent(this, MainActivity.class));
                    finish();
                }
                break;
            case 5:
                int maxPomo5 = pomodoros.size();

                ArrayList<Pomodoro> taskFinished3 = new ArrayList<>();
                ArrayList<Pomodoro> successfulPomodoros3 = new ArrayList<>();

                for(int i=0; i<pomodoros.size(); i++){
                    if(pomodoros.get(i).isTaskFinished()){
                        taskFinished3.add(pomodoros.get(i));
                    }
                    if(pomodoros.get(i).isStayedFocused()){
                        successfulPomodoros3.add(pomodoros.get(i));
                    }
                }
                if(AppHelper.getInstance().getSessionCount() != 0 && pomodoros.size() == AppHelper.getInstance().getSessionCount()) {
                    int pomoPercent = Math.round((successfulPomodoros3.size() * 100.0f) / maxPomo5);
                    int taskPercent = Math.round((taskFinished3.size() * 100.0f) / maxPomo5);
                    if (pomoPercent >= 75 && taskPercent >= 50) {
                        AppHelper.getInstance().setLevel(6);
                        AppHelper.getInstance().setLevel5Pomo(pomoPercent);
                        AppHelper.getInstance().setLevel5Tasks(taskPercent);
                        AppHelper.getInstance().setSessionCount(0);
                        AppHelper.getInstance().setIsProgramStarted(false);

                        AdviceDialog alert = new AdviceDialog();
                        alert.showDialog(this, getString(R.string.result_after_level_5), false, AppHelper.getInstance().getLevel());

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
                        Answers.getInstance().logCustom(new CustomEvent("Level 5 successfully finished")
                                .putCustomAttribute("Id", id));
                    } else {
//                        activity.startActivity(new Intent(activity, MainActivity.class));
                        //fail
                        AppHelper.getInstance().setSessionCount(0);
                        AppHelper.getInstance().setIsProgramStarted(false);

                        AdviceDialog alert = new AdviceDialog();
                        alert.showDialog(this, getString(R.string.level_5_fail), false);
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
                        Answers.getInstance().logCustom(new CustomEvent("Level 5 failed finished")
                                .putCustomAttribute("Id", id));
                    }
                    setPushNotification();
                }else{
                    this.startActivity(new Intent(this, MainActivity.class));
                    finish();
                }
                break;
            case 6:
                int maxPomo6 = pomodoros.size();

                ArrayList<Pomodoro> taskFinished4 = new ArrayList<>();
                ArrayList<Pomodoro> successfulPomodoros4 = new ArrayList<>();

                for(int i=0; i<pomodoros.size(); i++){
                    if(pomodoros.get(i).isTaskFinished()){
                        taskFinished4.add(pomodoros.get(i));
                    }
                    if(pomodoros.get(i).isStayedFocused()){
                        successfulPomodoros4.add(pomodoros.get(i));
                    }
                }
                if(AppHelper.getInstance().getSessionCount() != 0 && pomodoros.size() == AppHelper.getInstance().getSessionCount()) {
                    int pomoPercent = Math.round((successfulPomodoros4.size() * 100.0f) / maxPomo6);
                    int taskPercent = Math.round((taskFinished4.size() * 100.0f) / maxPomo6);
                    if (pomoPercent >= 75 && taskPercent >= 50) {
                        AppHelper.getInstance().setLevel(7);
                        AppHelper.getInstance().setLevel6Pomo(pomoPercent);
                        AppHelper.getInstance().setLevel6Tasks(taskPercent);
                        AppHelper.getInstance().setSessionCount(0);
                        AppHelper.getInstance().setIsProgramStarted(false);

                        AdviceDialog alert = new AdviceDialog();
                        alert.showDialog(this, getString(R.string.result_after_level_6), false, AppHelper.getInstance().getLevel());

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
                        Answers.getInstance().logCustom(new CustomEvent("Level 6 successfully finished")
                                .putCustomAttribute("Id", id));
                    } else {
//                        activity.startActivity(new Intent(activity, MainActivity.class));
                        //fail
                        AppHelper.getInstance().setSessionCount(0);
                        AppHelper.getInstance().setIsProgramStarted(false);

                        AdviceDialog alert = new AdviceDialog();
                        alert.showDialog(this, getString(R.string.level_6_fail), false);
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
                        Answers.getInstance().logCustom(new CustomEvent("Level 6 failed finished")
                                .putCustomAttribute("Id", id));
                    }
                    setPushNotification();
                }else{
                    this.startActivity(new Intent(this, MainActivity.class));
                    finish();
                }
                break;
            case 7:
                int maxPomo7 = pomodoros.size();

                ArrayList<Pomodoro> taskFinished5 = new ArrayList<>();
                ArrayList<Pomodoro> successfulPomodoros5 = new ArrayList<>();

                for(int i=0; i<pomodoros.size(); i++){
                    if(pomodoros.get(i).isTaskFinished()){
                        taskFinished5.add(pomodoros.get(i));
                    }
                    if(pomodoros.get(i).isStayedFocused()){
                        successfulPomodoros5.add(pomodoros.get(i));
                    }
                }
                if(AppHelper.getInstance().getSessionCount() != 0 && pomodoros.size() == AppHelper.getInstance().getSessionCount()) {
                    int pomoPercent = Math.round((successfulPomodoros5.size() * 100.0f) / maxPomo7);
                    int taskPercent = Math.round((taskFinished5.size() * 100.0f) / maxPomo7);
                    if (pomoPercent >= 75 && taskPercent >= 62) {
                        AppHelper.getInstance().setLevel(8);
                        AppHelper.getInstance().setLevel7Pomo(pomoPercent);
                        AppHelper.getInstance().setLevel7Tasks(taskPercent);
                        AppHelper.getInstance().setSessionCount(0);
                        AppHelper.getInstance().setIsProgramStarted(false);

                        AdviceDialog alert = new AdviceDialog();
                        alert.showDialog(this, getString(R.string.result_after_level_7), false, AppHelper.getInstance().getLevel());

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
                        Answers.getInstance().logCustom(new CustomEvent("Level 7 successfully finished")
                                .putCustomAttribute("Id", id));
                    } else {
//                        activity.startActivity(new Intent(activity, MainActivity.class));
                        //fail
                        AppHelper.getInstance().setSessionCount(0);
                        AppHelper.getInstance().setIsProgramStarted(false);

                        AdviceDialog alert = new AdviceDialog();
                        alert.showDialog(this, getString(R.string.level_7_fail), false);
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
                        Answers.getInstance().logCustom(new CustomEvent("Level 7 failed finished")
                                .putCustomAttribute("Id", id));
                    }
                    setPushNotification();
                }else{
                    this.startActivity(new Intent(this, MainActivity.class));
                    finish();
                }
                break;
            case 8:
                int maxPomo8 = pomodoros.size();

                ArrayList<Pomodoro> taskFinished6 = new ArrayList<>();
                ArrayList<Pomodoro> successfulPomodoros6 = new ArrayList<>();

                for(int i=0; i<pomodoros.size(); i++){
                    if(pomodoros.get(i).isTaskFinished()){
                        taskFinished6.add(pomodoros.get(i));
                    }
                    if(pomodoros.get(i).isStayedFocused()){
                        successfulPomodoros6.add(pomodoros.get(i));
                    }
                }
                if(AppHelper.getInstance().getSessionCount() != 0 && pomodoros.size() == AppHelper.getInstance().getSessionCount()) {
                    int pomoPercent = Math.round((successfulPomodoros6.size() * 100.0f) / maxPomo8);
                    int taskPercent = Math.round((taskFinished6.size() * 100.0f) / maxPomo8);
                    if (pomoPercent >= 75 && taskPercent >= 75) {
                        AppHelper.getInstance().setLevel(9);
                        AppHelper.getInstance().setLevel8Pomo(pomoPercent);
                        AppHelper.getInstance().setLevel8Tasks(pomoPercent);
                        AppHelper.getInstance().setSessionCount(0);
                        AppHelper.getInstance().setIsProgramStarted(false);

                        AdviceDialog alert = new AdviceDialog();
                        alert.showDialog(this, getString(R.string.result_after_level_8), false, AppHelper.getInstance().getLevel());

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

                        Answers.getInstance().logCustom(new CustomEvent("Level 8 successfully finished")
                                .putCustomAttribute("Id", id));
                    } else {
//                        activity.startActivity(new Intent(activity, MainActivity.class));
                        //fail
                        AppHelper.getInstance().setSessionCount(0);
                        AppHelper.getInstance().setIsProgramStarted(false);

                        AdviceDialog alert = new AdviceDialog();
                        alert.showDialog(this, getString(R.string.level_8_fail), false);
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

                        Answers.getInstance().logCustom(new CustomEvent("Level 8 successfully finished")
                                .putCustomAttribute("Id", id));
                    }
                    setPushNotification();
                }else{
                    this.startActivity(new Intent(this, MainActivity.class));
                    finish();
                }
                break;
        }
    }

    private void setAdviceAfterLastPomodoro(int level, int number){
        RealmResults<Pomodoro> pomodoros = RealmController.getInstance().getFinishedPomodoros();

        switch (level){
            case 0:
                calculateLevel();
            case 1:
                if(pomodoros.size() == 1 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_1_level_1), true);
                }else if(pomodoros.size() == 2 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_2_level_1), true );
                }else if(pomodoros.size() == 3 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_3_level_1), true  );
                }else if(pomodoros.size() == 4 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_4_level_1), true  );
                }else if(pomodoros.size() == 5 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_5_level_1), true  );
                }else if(pomodoros.size() == 6 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_6_level_1), true  );
                }else{
                    calculateLevel();
                }
                break;
            case 2:
                if(pomodoros.size() == 2 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_1_level_2), true  );
                }else if(pomodoros.size() == 4 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_2_level_2), true  );
                }else if(pomodoros.size() == 6 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_3_level_2), true  );
                }else if(pomodoros.size() == 8 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_4_level_2), true  );
                }else if(pomodoros.size() == 10 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_5_level_2), true  );
                }else if(pomodoros.size() == 12 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_6_level_2), true  );
                }else {
                    calculateLevel();
                }
                break;
            case 3:
                if(pomodoros.size() == 3 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_1_level_3), true  );
                }else if(pomodoros.size() == 6 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_2_level_3), true  );
                }else if(pomodoros.size() == 9 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_3_level_3), true  );
                }else if(pomodoros.size() == 12 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_4_level_3), true  );
                }else if(pomodoros.size() == 15 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_5_level_3), true  );
                }else if(pomodoros.size() == 18 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_6_level_3), true  );
                }else{
                    calculateLevel();
                }
                break;
            case 4:
                if(pomodoros.size() == 4 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_1_level_4), true  );
                }else if(pomodoros.size() == 8 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_2_level_4), true  );
                }else if(pomodoros.size() == 12 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_3_level_4), true  );
                }else if(pomodoros.size() == 18 ){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_4_level_4), true  );
                }else if(pomodoros.size() == 20 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_5_level_4), true  );
                }else if(pomodoros.size() == 24 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_6_level_4), true  );
                }else{
                    calculateLevel();
                }
                break;
            case 5:
                if(pomodoros.size() == 5 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_1_level_5), true  );
                }else if(pomodoros.size() == 10 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_2_level_5), true  );
                }else if(pomodoros.size() == 15 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_3_level_5), true  );
                }else if(pomodoros.size() == 20 ){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_4_level_5), true  );
                }else if(pomodoros.size() == 25 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_5_level_5), true  );
                }else if(pomodoros.size() == 30 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_6_level_5), true  );
                }else{
                    calculateLevel();
                }
                break;
            case 6:
                if(pomodoros.size() == 6 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_1_level_6), true  );
                }else if(pomodoros.size() == 12 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_2_level_6), true  );
                }else if(pomodoros.size() == 18 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_3_level_6), true  );
                }else if(pomodoros.size() == 24 ){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_4_level_6), true  );
                }else if(pomodoros.size() == 30 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_5_level_6), true  );
                }else if(pomodoros.size() == 36 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_6_level_6), true  );
                }else{
                    calculateLevel();
                }
                break;
            case 7:
                if(pomodoros.size() == 7 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_1_level_7), true  );
                }else if(pomodoros.size() == 14 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_2_level_7), true  );
                }else if(pomodoros.size() == 21 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_3_level_7), true  );
                }else if(pomodoros.size() == 28 ){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_4_level_7), true  );
                }else if(pomodoros.size() == 35 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_5_level_7), true  );
                }else if(pomodoros.size() == 42 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_6_level_7), true  );
                }else{
                    calculateLevel();
                }
                break;
            case 8:
                if(pomodoros.size() == 8 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_1_level_8), true  );
                }else if(pomodoros.size() == 16 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_2_level_8), true  );
                }else if(pomodoros.size() == 24 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_3_level_8), true  );
                }else if(pomodoros.size() == 32 ){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_4_level_8), true  );
                }else if(pomodoros.size() == 40 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_5_level_8), true  );
                }else if(pomodoros.size() == 48 && level == number){
                    AdviceDialog alert = new AdviceDialog();
                    alert.showDialog(this, getString(R.string.advice_day_6_level_8), true  );
                }else{
                    calculateLevel();
                }
                break;
        }
    }


    private void setPushNotification(){
        Calendar calendar = Calendar.getInstance(Locale.GERMAN);
        AppHelper.getInstance().setNextWeek(calendar.get(Calendar.WEEK_OF_YEAR) + 1);
        String channelId = "channel-01";
        String channelName = "Discipline Bee channel";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            mChannel.setVibrationPattern(new long[]{1000, 1000});
            notificationManager.createNotificationChannel(mChannel);
        }

        Intent showIntent = new Intent();
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, showIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);

        Notification notification = builder.setContentTitle(this.getString(R.string.app_name))
                .setContentText(getString(R.string.set_sessions_next_week))
                .setSmallIcon(R.drawable.ic_stat_assignment)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setVibrate(new long[]{1000, 1000})
                .setOngoing(false)
                .build();

        notificationManager.notify(0, notification);
    }

    @Override
    public void onResume() {
        super.onResume();
        wakeLock.acquire();
    }

    @Override
    protected void onPause() {
        super.onPause();
        wakeLock.release();
    }
}
