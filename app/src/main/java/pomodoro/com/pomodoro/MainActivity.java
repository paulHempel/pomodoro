package pomodoro.com.pomodoro;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.jakewharton.threetenabp.AndroidThreeTen;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;

import java.util.Calendar;
import java.util.Locale;
import java.util.logging.Level;

import io.realm.Realm;
import io.realm.RealmResults;
import pomodoro.com.pomodoro.fragments.advice.AdviceFragment;
import pomodoro.com.pomodoro.fragments.dashboard.OverviewFragment;
import pomodoro.com.pomodoro.fragments.pomodoro.NewCalendarFragment;

import pomodoro.com.pomodoro.fragments.report.ReportSummaryFragment;
import pomodoro.com.pomodoro.fragments.tutorial.TutorialDialogFragment;
import pomodoro.com.pomodoro.pojo.Pomodoro;
import pomodoro.com.pomodoro.realm.RealmController;
import pomodoro.com.pomodoro.reciver.AlarmReceiver;
import pomodoro.com.pomodoro.util.AppHelper;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView nameTv, levelTv;
    ImageView levelIv;
    Realm realm;
    int REQUEST_OVERLAY_PERMISSION = 10;

    @Override
    public int getResourceId() {
        return R.layout.activity_main;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        AndroidThreeTen.init(this);

        RequestPermission();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.realm = RealmController.getInstance().getRealm();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        levelTv = (TextView) header.findViewById(R.id.levelTv);
        levelIv = (ImageView) header.findViewById(R.id.levelIv);

        setNameAndLevel();
        setLevelImage();

        if(!AppHelper.getInstance().isTutorialDone()) {
            showTutorial();
            final Calendar mcurrentTime = Calendar.getInstance();
            if(mcurrentTime.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || mcurrentTime.get(Calendar.DAY_OF_WEEK) > Calendar.THURSDAY){
                AppHelper.getInstance().setIsTestTime(true);
                AppHelper.getInstance().setLevel(0);
            }
        }

        LocalDateTime myCalendar = LocalDateTime.now(ZoneId.systemDefault());

        RealmResults<Pomodoro> incompleted = RealmController.getInstance().getIncompletedPomodoros(myCalendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        if(incompleted.size() > 0){
            for (final Pomodoro p : incompleted) {
                Log.d("POMODORO_DATE", p.getDate() + "");
                if (p != null) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            p.setStayedFocused(false);
                            p.setTaskFinished(false);
                            p.setStarted(true);
                        }
                    });

                }
            }

//            LocalDateTime calendar = LocalDateTime.now(ZoneId.systemDefault());
//            RealmResults<Pomodoro> sorted = RealmController.getInstance().getSortedPomodorosByDate(calendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
//
//            if(AppHelper.getInstance().getLevel() == 0 && sorted.size() == 0){
//                Calendar c = Calendar.getInstance(Locale.GERMAN);
//                AppHelper.getInstance().setNextWeek(c.get(Calendar.WEEK_OF_YEAR) + 1);
//            }

        }

            LocalDateTime calendar = LocalDateTime.now(ZoneId.systemDefault());
            RealmResults<Pomodoro> sorted = RealmController.getInstance().getSortedPomodorosByDate(calendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            if(sorted.size() > 0){
                showNewFragment(new OverviewFragment(), R.id.main_container);
            }else{
                showNewFragment(new NewCalendarFragment(), R.id.main_container);
            }

        if(!AppHelper.getInstance().isFirstTimeOpen()){
//            final Pomodoro obj = new Pomodoro();
//            obj.setId(1 + "1.1.2020");
//
//            realm.executeTransaction(new Realm.Transaction() {
//                @Override
//                public void execute(Realm realm) {
//                    realm.copyToRealmOrUpdate(obj);
//                }
//            });
//            clearAllData();
            AppHelper.getInstance().setIsFirstTimeOpen(true);
        }

//            AppHelper.getInstance().setLevel(8);
    }

    private void RequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + this.getPackageName()));
                startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION);
            } else {
                //Permission Granted-System will work
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_OVERLAY_PERMISSION) {
            if (Settings.canDrawOverlays(this)) {
                // permission granted...
            }else{
                // permission not granted...
                finish();

            }
        }
    }

    private void showTutorial() {
        TutorialDialogFragment dialogFragment = new TutorialDialogFragment ();
        dialogFragment.show(getSupportFragmentManager(), "Tutorial Fragment");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            showNewFragmentAddToBackStack(new OverviewFragment(), R.id.main_container);
        } else if (id == R.id.nav_gallery) {
            showNewFragmentAddToBackStack(new NewCalendarFragment(), R.id.main_container);
        } else if (id == R.id.nav_slideshow) {
            showNewFragmentAddToBackStack(new AdviceFragment(), R.id.main_container);
        } else if (id == R.id.reset_level){
            showAlertDialogResetLevel();
        } else if (id == R.id.clear_data){
            showAlertDialogButtonClicked();
        }else if (id == R.id.nav_report){
            showNewFragmentAddToBackStack(new ReportSummaryFragment(), R.id.main_container);
        }else if (id == R.id.nav_feedback){
            openWebSite();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openWebSite() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.haudenverres.de/routines/#contact"));
        startActivity(browserIntent);
    }


    public void showAlertDialogButtonClicked() {

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.alert));
        builder.setMessage(getString(R.string.are_you_sure_reset_data));

        // add the buttons
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // do something like...

                clearAllData();

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

    private void clearAllData() {
        RealmResults<Pomodoro> pomodoros = RealmController.getInstance().getAllPomodoros();
        for (final Pomodoro p : pomodoros) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
            notificationIntent.addCategory("android.intent.category.DEFAULT");
            notificationIntent.setClass(MainActivity.this, AlarmReceiver.class);
            int newId = Integer.parseInt(p.getId().replaceAll("[.]", ""));
            PendingIntent broadcast = PendingIntent.getBroadcast(MainActivity.this, newId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(broadcast);
        }

        RealmController.getInstance().clearAll();
        final Calendar mcurrentTime = Calendar.getInstance(Locale.GERMANY);
        if(mcurrentTime.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || mcurrentTime.get(Calendar.DAY_OF_WEEK) > Calendar.THURSDAY) {
            AppHelper.getInstance().setLevel(0);
            AppHelper.getInstance().setIsTestMessageShowed(false);
            AppHelper.getInstance().setIsTestTime(true);
        }else{
            AppHelper.getInstance().setIsTestTime(false);
            AppHelper.getInstance().setIsTestMessageShowed(true);
            AppHelper.getInstance().setLevel(1);
        }
        AppHelper.getInstance().setNextWeek(0);
        AppHelper.getInstance().setLevel1Pomo(0);
        AppHelper.getInstance().setLevel2Pomo(0);
        AppHelper.getInstance().setLevel3Pomo(0);
        AppHelper.getInstance().setLevel4Pomo(0);
        AppHelper.getInstance().setLevel5Pomo(0);
        AppHelper.getInstance().setLevel6Pomo(0);
        AppHelper.getInstance().setLevel7Pomo(0);
        AppHelper.getInstance().setLevel8Pomo(0);
        AppHelper.getInstance().setLevel1Tasks(0);
        AppHelper.getInstance().setLevel2Tasks(0);
        AppHelper.getInstance().setLevel3Tasks(0);
        AppHelper.getInstance().setLevel4Tasks(0);
        AppHelper.getInstance().setLevel5Tasks(0);
        AppHelper.getInstance().setLevel6Tasks(0);
        AppHelper.getInstance().setLevel7Tasks(0);
        AppHelper.getInstance().setIsProgramStarted(false);
        AppHelper.getInstance().setSessionCount(0);
        finish();
        startActivity(getIntent());
    }


    public void showAlertDialogResetLevel() {

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.alert));
        builder.setMessage(getString(R.string.are_you_sure_reset_level));

        // add the buttons
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                AppHelper.getInstance().setIsProgramStarted(false);
                AppHelper.getInstance().setSessionCount(0);

                Calendar calendar = Calendar.getInstance(Locale.GERMAN);
                AppHelper.getInstance().setNextWeek(calendar.get(Calendar.WEEK_OF_YEAR) + 1);

                RealmResults<Pomodoro> pomodoros = RealmController.getInstance().getAllPomodoros();
                for (final Pomodoro p : pomodoros) {
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                    Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
                    notificationIntent.addCategory("android.intent.category.DEFAULT");
                    notificationIntent.setClass(MainActivity.this, AlarmReceiver.class);
                    int newId = Integer.parseInt(p.getId().replaceAll("[.]", ""));
                    PendingIntent broadcast = PendingIntent.getBroadcast(MainActivity.this, newId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.cancel(broadcast);

                    if (p != null) {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                p.deleteFromRealm();
                            }
                        });

                    }
                }
                finish();
                startActivity(getIntent());
            }
        });



        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });


        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void setNameAndLevel(){
//        nameTv.setText(AppHelper.getInstance().getFutureName());
        levelTv.setText(getString(R.string.level) + " " + AppHelper.getInstance().getLevel());
    }

    public void setLevelImage(){
        switch(AppHelper.getInstance().getLevel()){
            case 0 : levelIv.setImageResource(R.drawable.level_zero_bee);
                break;
            case 1 : levelIv.setImageResource(R.drawable.level_one_bee);
                break;
            case 2 : levelIv.setImageResource(R.drawable.level_two_bee);
                break;
            case 3 : levelIv.setImageResource(R.drawable.level_three_bee);
                break;
            case 4 : levelIv.setImageResource(R.drawable.level_four_bee);
                break;
            case 5 : levelIv.setImageResource(R.drawable.level_five_bee);
                break;
            case 6 : levelIv.setImageResource(R.drawable.level_six_bee);
                break;
            case 7 : levelIv.setImageResource(R.drawable.level_seven_bee);
                break;
            case 8 : levelIv.setImageResource(R.drawable.level_eight_bee);
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

        Answers.getInstance().logCustom(new CustomEvent("Level finished Notification showed")
                .putCustomAttribute("Level", AppHelper.getInstance().getLevel() + ""));

        notificationManager.notify(0, notification);
    }
}
