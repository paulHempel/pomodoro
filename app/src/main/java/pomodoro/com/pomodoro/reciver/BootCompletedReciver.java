package pomodoro.com.pomodoro.reciver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import io.realm.RealmResults;
import pomodoro.com.pomodoro.R;
import pomodoro.com.pomodoro.constants.Constants;
import pomodoro.com.pomodoro.pojo.Pomodoro;
import pomodoro.com.pomodoro.realm.RealmController;

public class BootCompletedReciver extends BroadcastReceiver {
    Context context;
    long removeMinutes = 60*1000; //4*60*1000; //4 min

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Calendar calendar = Calendar.getInstance();
            RealmResults<Pomodoro> sorted = RealmController.getInstance().getSortedPomodorosByDate(calendar.getTimeInMillis());

            for(Pomodoro p : sorted){
                int newId = Integer.parseInt(p.getId().replaceAll("[.]", ""));
                setReminderNotificationIntent(p.getDate()-removeMinutes, p.getId(), p.getDuration(), newId);
            }
        }
    }

    private void setReminderNotificationIntent(long date, String id, long duration, int newId){
        Calendar now = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(date));
        if(calendar.getTimeInMillis() > now.getTimeInMillis()) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
            notificationIntent.addCategory("android.intent.category.DEFAULT");
            notificationIntent.putExtra(Constants.NOTIFICATION_TEXT,context.getString(R.string.your_next_pomodoro_will_start));
            notificationIntent.putExtra(Constants.IS_START, true);
            notificationIntent.putExtra(Constants.POMODORO_ID, id);
            notificationIntent.putExtra(Constants.DURATION, duration);
            notificationIntent.setClass(context, AlarmReceiver.class);
            PendingIntent broadcast = PendingIntent.getBroadcast(context, newId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

//            PendingIntent broadcast = PendingIntent.getActivity(context, newId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), broadcast), broadcast);

//            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), broadcast);
        }
    }
}
