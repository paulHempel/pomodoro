package pomodoro.com.pomodoro.reciver;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;
import java.util.Date;

import io.realm.RealmResults;
import pomodoro.com.pomodoro.NotificationActivity;
import pomodoro.com.pomodoro.R;
import pomodoro.com.pomodoro.constants.Constants;
import pomodoro.com.pomodoro.pojo.Pomodoro;
import pomodoro.com.pomodoro.realm.RealmController;

public class AlarmReceiver extends BroadcastReceiver {

    private boolean isStart = false;
    String id;
    long duration;
    long removeMinutes = 60*1000; //4*60*1000; //4 min

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        String text = intent.getStringExtra(Constants.NOTIFICATION_TEXT);
        isStart = intent.getBooleanExtra(Constants.IS_START, false);
        id = intent.getStringExtra(Constants.POMODORO_ID);
        duration = intent.getLongExtra(Constants.DURATION, 15*60*1000);

        if(id == null){
            Calendar calendar = Calendar.getInstance();
            RealmResults<Pomodoro> sorted = RealmController.getInstance().getSortedPomodorosByDate(calendar.getTimeInMillis());
            for(Pomodoro p : sorted){
                int newId = Integer.parseInt(p.getId().replaceAll("[.]", ""));
                setReminderNotificationIntent(p.getDate()-removeMinutes, p.getId(), p.getDuration(), newId, context);
            }
        }else{
            String channelId = "channel-01";
            String channelName = "Discipline Bee channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(
                        channelId, channelName, importance);
                mChannel.setVibrationPattern(new long[] {1000, 1000});
                notificationManager.createNotificationChannel(mChannel);
            }
            Intent notificationIntent = new Intent(context, NotificationActivity.class);
            notificationIntent.putExtra(Constants.POMODORO_ID, id);
            notificationIntent.putExtra(Constants.DURATION, duration);

            Intent showIntent = new Intent();
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, showIntent, 0);


            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId);

            Notification notification = builder.setContentTitle(context.getString(R.string.app_name))
                    .setContentText(text)
//                        .setTicker("New Message Alert!")
                    .setSmallIcon(R.drawable.ic_stat_assignment)
                    .setContentIntent(contentIntent)
                    .setAutoCancel(true)
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setVibrate(new long[] { 1000, 1000})
                    .setOngoing(false)
                    .build();

            notificationManager.notify(0, notification);

            context.startActivity(new Intent(context, NotificationActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra(Constants.POMODORO_ID, id).putExtra(Constants.DURATION, duration));

        }


    }

    private void setReminderNotificationIntent(long date, String id, long duration, int newId, Context context){
        Calendar now = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(date));
        if(calendar.getTimeInMillis() > now.getTimeInMillis()) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
            notificationIntent.addCategory("android.intent.category.DEFAULT");
            notificationIntent.putExtra(Constants.NOTIFICATION_TEXT, context.getString(R.string.your_next_pomodoro_will_start));
            notificationIntent.putExtra(Constants.IS_START, true);
            notificationIntent.putExtra(Constants.POMODORO_ID, id);
            notificationIntent.putExtra(Constants.DURATION, duration);
            notificationIntent.setClass(context, AlarmReceiver.class);
            PendingIntent broadcast = PendingIntent.getBroadcast(context, newId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//            PendingIntent broadcast = PendingIntent.getActivity(context, newId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

//            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), broadcast);
            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), broadcast), broadcast);
        }
    }
}
