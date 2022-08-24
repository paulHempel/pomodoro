package pomodoro.com.pomodoro.fragments.pomodoro;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.jakewharton.threetenabp.AndroidThreeTen;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.temporal.ChronoUnit;
import org.threeten.bp.temporal.WeekFields;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import pomodoro.com.pomodoro.MainActivity;
import pomodoro.com.pomodoro.R;
import pomodoro.com.pomodoro.constants.Constants;
import pomodoro.com.pomodoro.fragments.BaseFragment;
import pomodoro.com.pomodoro.fragments.dashboard.OverviewFragment;
import pomodoro.com.pomodoro.pojo.Pomodoro;
import pomodoro.com.pomodoro.realm.RealmController;
import pomodoro.com.pomodoro.reciver.AlarmReceiver;
import pomodoro.com.pomodoro.util.AppHelper;

public class NewCalendarFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.time1Tv)
    TextView time1Tv;
    @BindView(R.id.time2Tv)
    TextView time2Tv;
    @BindView(R.id.time3Tv)
    TextView time3Tv;
    @BindView(R.id.time4Tv)
    TextView time4Tv;
    @BindView(R.id.time5Tv)
    TextView time5Tv;
    @BindView(R.id.time6Tv)
    TextView time6Tv;
    @BindView(R.id.time7Tv)
    TextView time7Tv;
    @BindView(R.id.time8Tv)
    TextView time8Tv;


    @BindView(R.id.container1)
    LinearLayout container1;
    @BindView(R.id.container2)
    LinearLayout container2;
    @BindView(R.id.container3)
    LinearLayout container3;
    @BindView(R.id.container4)
    LinearLayout container4;
    @BindView(R.id.container5)
    LinearLayout container5;
    @BindView(R.id.container6)
    LinearLayout container6;
    @BindView(R.id.container7)
    LinearLayout container7;
    @BindView(R.id.container8)
    LinearLayout container8;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.mondayTv)
    TextView mondayTv;
    @BindView(R.id.tuesdayTv)
    TextView tuesdayTv;
    @BindView(R.id.wednsdayTv)
    TextView wednsdayTv;
    @BindView(R.id.thuesdayTv)
    TextView thuesdayTv;
    @BindView(R.id.fridayTv)
    TextView fridayTv;
    @BindView(R.id.saturdayTv)
    TextView saturdayTv;
    @BindView(R.id.sundayTv)
    TextView sundayTv;

    @BindView(R.id.pomodoro1Edt)
    TextInputEditText pomodoro1Edt;
    @BindView(R.id.pomodoro2Edt)
    TextInputEditText pomodoro2Edt;
    @BindView(R.id.pomodoro3Edt)
    TextInputEditText pomodoro3Edt;
    @BindView(R.id.pomodoro4Edt)
    TextInputEditText pomodoro4Edt;
    @BindView(R.id.pomodoro5Edt)
    TextInputEditText pomodoro5Edt;
    @BindView(R.id.pomodoro6Edt)
    TextInputEditText pomodoro6Edt;
    @BindView(R.id.pomodoro7Edt)
    TextInputEditText pomodoro7Edt;
    @BindView(R.id.pomodoro8Edt)
    TextInputEditText pomodoro8Edt;

    @BindView(R.id.timerIv1)
    ImageView timerIv1;
    @BindView(R.id.timerIv2)
    ImageView timerIv2;
    @BindView(R.id.timerIv3)
    ImageView timerIv3;
    @BindView(R.id.timerIv4)
    ImageView timerIv4;
    @BindView(R.id.timerIv5)
    ImageView timerIv5;
    @BindView(R.id.timerIv6)
    ImageView timerIv6;
    @BindView(R.id.timerIv7)
    ImageView timerIv7;
    @BindView(R.id.timerIv8)
    ImageView timerIv8;

    @BindView(R.id.weekNumberTv)
            TextView weekNumberTv;
    @BindView(R.id.weekRangeTv)
            TextView weekRangeTv;

    @BindView(R.id.exitCalendatBtn)
    Button exitCalendatBtn;

    MainActivity activity;

    int level, hour, minute;
    Realm realm;

    Pomodoro first;
    Pomodoro second;
    Pomodoro third;
    Pomodoro fourth;
    Pomodoro fifth;
    Pomodoro sixth;
    Pomodoro seventh;
    Pomodoro eight;

    long removeMinutes = 60*1000; //4*60*1000; //4 min
    private static final int THIRTY_MINUTES = 30 * 60 * 1000;


    String myFormat = "dd.MM.yyyy"; //In which you need put here
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.GERMANY);

    String currentDate, today;
    long monday;

    LocalDateTime myCalendar, mCurrentTime, selectedDateTime;
    WeekFields weekFields;

    @Override
    protected int setResourceId() {
        return R.layout.new_calendar_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        activity = getMainActivity();
        AndroidThreeTen.init(activity);

        this.realm = RealmController.getInstance().getRealm();

        timerIv1.setOnClickListener(this);
        timerIv2.setOnClickListener(this);
        timerIv3.setOnClickListener(this);
        timerIv4.setOnClickListener(this);
        timerIv5.setOnClickListener(this);
        timerIv6.setOnClickListener(this);
        timerIv7.setOnClickListener(this);
        timerIv8.setOnClickListener(this);

        time1Tv.setOnClickListener(this);
        time2Tv.setOnClickListener(this);
        time3Tv.setOnClickListener(this);
        time4Tv.setOnClickListener(this);
        time5Tv.setOnClickListener(this);
        time6Tv.setOnClickListener(this);
        time7Tv.setOnClickListener(this);
        time8Tv.setOnClickListener(this);

        myCalendar = LocalDate.now(ZoneId.systemDefault()).atTime(8, 0);
        mCurrentTime = LocalDateTime.now(ZoneId.systemDefault());

        weekFields = WeekFields.of(Locale.GERMANY);

        int currentDayOfWeek = mCurrentTime.get(weekFields.dayOfWeek());

        today = sdf.format(myCalendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

        hour = mCurrentTime.getHour();
        minute = mCurrentTime.getMinute();

        if(AppHelper.getInstance().getNextWeek() == myCalendar.get(weekFields.weekOfYear())){
            AppHelper.getInstance().setNextWeek(0);
        }


        if(AppHelper.getInstance().getNextWeek() != 0){
            //subtract day of week to monday
             mCurrentTime = mCurrentTime.minus((Long.valueOf(currentDayOfWeek)-1),ChronoUnit.DAYS);
            //add week starting from monday
            mCurrentTime = mCurrentTime.plus(1,ChronoUnit.WEEKS);
            //subtract day of week to monday
            myCalendar = myCalendar.minus((Long.valueOf(currentDayOfWeek)-1),ChronoUnit.DAYS);
            //add week starting from monday
            myCalendar = myCalendar.plus(1,ChronoUnit.WEEKS);
        }


        if(AppHelper.getInstance().getLevel() == 0 &&
                (myCalendar.getDayOfWeek() == DayOfWeek.MONDAY
                        || myCalendar.getDayOfWeek() == DayOfWeek.TUESDAY
                        || myCalendar.getDayOfWeek() == DayOfWeek.WEDNESDAY
                        || myCalendar.getDayOfWeek() == DayOfWeek.THURSDAY)){
            AppHelper.getInstance().setLevel(1);
            AppHelper.getInstance().setIsTestTime(false);
            activity.setLevelImage();
            activity.setNameAndLevel();
        }

        level = AppHelper.getInstance().getLevel();

        setLayoutBasedOnLevel(level);

        DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        LocalDateTime calendar = LocalDate.now(ZoneId.systemDefault()).atTime(0,0, 0);

        if(AppHelper.getInstance().getNextWeek() != 0){
            calendar = calendar.minus((Long.valueOf(currentDayOfWeek)-1),ChronoUnit.DAYS);
            calendar = calendar.plus(1,ChronoUnit.WEEKS);
        }
        calendar = calendar.with(DayOfWeek.MONDAY);
        monday = calendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        String start = format.format(calendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        calendar = calendar.plusDays(Long.valueOf(6));
        String end = format.format(calendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

        if(!AppHelper.getInstance().isTestMessageShowed() && AppHelper.getInstance().isTestTime()){
            if(mCurrentTime.getDayOfWeek() == DayOfWeek.SUNDAY || mCurrentTime.getDayOfWeek() == DayOfWeek.THURSDAY
                    || mCurrentTime.getDayOfWeek() == DayOfWeek.FRIDAY
                    || mCurrentTime.getDayOfWeek() == DayOfWeek.SATURDAY) {
                AdviceDialog alert = new AdviceDialog();
                alert.showDialog(activity, getString(R.string.test_level_messege), getString(R.string.info));
            }
        }

        if(AppHelper.getInstance().isProgramStarted()){
            exitCalendatBtn.setVisibility(View.INVISIBLE);
        }

        int weekOfCurrentTime=mCurrentTime.get(weekFields.weekOfYear());

        setCurrentTime();

        weekNumberTv.setText(weekOfCurrentTime +"");
        weekRangeTv.setText(start + " - " + end);
        updateLabel();
        Log.d("MONDAY", monday +"");

    }

    private void setSavedPomodoros(){
        boolean isToday = currentDate.equals(today);
        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault()).withNano(0);
        boolean isPast = selectedDateTime.isBefore(now);


        first = RealmController.getInstance().getPomodoroForDay(currentDate, 1);
        if(first != null){
            if(first.isStarted() || (isToday && AppHelper.getInstance().isProgramStarted())){
                pomodoro1Edt.setEnabled(false);
                timerIv1.setOnClickListener(null);
                time1Tv.setOnClickListener(null);
            }else{
                pomodoro1Edt.setEnabled(true);
                timerIv1.setOnClickListener(this);
                time1Tv.setOnClickListener(this);
            }
            pomodoro1Edt.setText(first.getName());
            time1Tv.setText(first.getTextTime());
            time1Tv.setTag(first.getDate());
        }else{
            if(AppHelper.getInstance().isProgramStarted()){
                pomodoro1Edt.setEnabled(false);
                timerIv1.setOnClickListener(null);
                time1Tv.setOnClickListener(null);
                pomodoro1Edt.setText("");
                time1Tv.setText("08:00");
                time1Tv.setTag(myCalendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            }else {
                if(isPast){
                    pomodoro1Edt.setEnabled(false);
                    timerIv1.setOnClickListener(null);
                    time1Tv.setOnClickListener(null);
                    pomodoro1Edt.setText("");
                    time1Tv.setText("08:00");
                    time1Tv.setTag(myCalendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
                }else {
                    pomodoro1Edt.setText("");
                    time1Tv.setText("08:00");
                    time1Tv.setTag(myCalendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
                    pomodoro1Edt.setEnabled(true);
                    timerIv1.setOnClickListener(this);
                    time1Tv.setOnClickListener(this);
                }
            }
        }

        second = RealmController.getInstance().getPomodoroForDay(currentDate, 2);
        if(second != null){
            if(second.isStarted() || (isToday && AppHelper.getInstance().isProgramStarted())){
                pomodoro2Edt.setEnabled(false);
                timerIv2.setOnClickListener(null);
                time2Tv.setOnClickListener(null);

            }else{
                pomodoro2Edt.setEnabled(true);
                timerIv2.setOnClickListener(this);
                time2Tv.setOnClickListener(this);
            }
            pomodoro2Edt.setText(second.getName());
            time2Tv.setText(second.getTextTime());
            time2Tv.setTag(second.getDate());
        }else{
            if(AppHelper.getInstance().isProgramStarted()) {
                pomodoro2Edt.setEnabled(false);
                timerIv2.setOnClickListener(null);
                time2Tv.setOnClickListener(null);
                pomodoro2Edt.setText("");
                time2Tv.setText("08:00");
                time2Tv.setTag(myCalendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            }else {
                if(isPast){
                    pomodoro2Edt.setEnabled(false);
                    timerIv2.setOnClickListener(null);
                    time2Tv.setOnClickListener(null);
                    pomodoro2Edt.setText("");
                    time2Tv.setText("08:00");
                    time2Tv.setTag(myCalendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
                }else{
                    pomodoro2Edt.setText("");
                    time2Tv.setText("08:00");
                    time2Tv.setTag(myCalendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
                    pomodoro2Edt.setEnabled(true);
                    timerIv2.setOnClickListener(this);
                    time2Tv.setOnClickListener(this);
                }
            }
        }

        third = RealmController.getInstance().getPomodoroForDay(currentDate, 3);
        if(third != null){
            if(third.isStarted() || (isToday && AppHelper.getInstance().isProgramStarted())){
                pomodoro3Edt.setEnabled(false);
                timerIv3.setOnClickListener(null);
                time3Tv.setOnClickListener(null);
            }else{
                pomodoro3Edt.setEnabled(true);
                timerIv3.setOnClickListener(this);
                time3Tv.setOnClickListener(this);
            }
            pomodoro3Edt.setText(third.getName());
            time3Tv.setText(third.getTextTime());
            time3Tv.setTag(third.getDate());
        }else{
            if(AppHelper.getInstance().isProgramStarted()) {
                pomodoro3Edt.setEnabled(false);
                timerIv3.setOnClickListener(null);
                time3Tv.setOnClickListener(null);
                pomodoro3Edt.setText("");
                time3Tv.setText("08:00");
                time3Tv.setTag(myCalendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            }else {
                if (isPast) {
                    pomodoro3Edt.setEnabled(false);
                    timerIv3.setOnClickListener(null);
                    time3Tv.setOnClickListener(null);
                    pomodoro3Edt.setText("");
                    time3Tv.setText("08:00");
                    time3Tv.setTag(myCalendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
                }else {
                    pomodoro3Edt.setText("");
                    time3Tv.setText("08:00");
                    time3Tv.setTag(myCalendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
                    pomodoro3Edt.setEnabled(true);
                    timerIv3.setOnClickListener(this);
                    time3Tv.setOnClickListener(this);
                }
            }
        }

        fourth = RealmController.getInstance().getPomodoroForDay(currentDate, 4);

        if(fourth != null){
            if(fourth.isStarted() || (isToday && AppHelper.getInstance().isProgramStarted())){
                pomodoro4Edt.setEnabled(false);
                timerIv4.setOnClickListener(null);
                time4Tv.setOnClickListener(null);
            }else{
                pomodoro4Edt.setEnabled(true);
                timerIv4.setOnClickListener(this);
                time4Tv.setOnClickListener(this);
            }
            pomodoro4Edt.setText(fourth.getName());
            time4Tv.setText(fourth.getTextTime());
            time4Tv.setTag(fourth.getDate());
        }else{
            if(AppHelper.getInstance().isProgramStarted()) {
                pomodoro4Edt.setEnabled(false);
                timerIv4.setOnClickListener(null);
                time4Tv.setOnClickListener(null);
                pomodoro4Edt.setText("");
                time4Tv.setText("08:00");
                time4Tv.setTag(myCalendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            }else {
                if(isPast){
                    pomodoro4Edt.setEnabled(false);
                    timerIv4.setOnClickListener(null);
                    time4Tv.setOnClickListener(null);
                    pomodoro4Edt.setText("");
                    time4Tv.setText("08:00");
                    time4Tv.setTag(myCalendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
                }else {
                    pomodoro4Edt.setText("");
                    time4Tv.setText("08:00");
                    time4Tv.setTag(myCalendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
                    pomodoro4Edt.setEnabled(true);
                    timerIv4.setOnClickListener(this);
                    time4Tv.setOnClickListener(this);
                }
            }
        }

        fifth = RealmController.getInstance().getPomodoroForDay(currentDate, 5);

        if(fifth != null){
            if(fifth.isStarted() || (isToday && AppHelper.getInstance().isProgramStarted())){
                pomodoro5Edt.setEnabled(false);
                timerIv5.setOnClickListener(null);
                time5Tv.setOnClickListener(null);

            }else{
                pomodoro5Edt.setEnabled(true);
                timerIv5.setOnClickListener(this);
                time5Tv.setOnClickListener(this);

            }
            pomodoro5Edt.setText(fifth.getName());
            time5Tv.setText(fifth.getTextTime());
            time5Tv.setTag(fifth.getDate());
        }else{
            if(AppHelper.getInstance().isProgramStarted()) {
                pomodoro5Edt.setEnabled(false);
                timerIv5.setOnClickListener(null);
                time5Tv.setOnClickListener(null);
                pomodoro5Edt.setText("");
                time5Tv.setText("08:00");
                time5Tv.setTag(myCalendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            }else{
                if(isPast){
                    pomodoro5Edt.setEnabled(false);
                    timerIv5.setOnClickListener(null);
                    time5Tv.setOnClickListener(null);
                    pomodoro5Edt.setText("");
                    time5Tv.setText("08:00");
                    time5Tv.setTag(myCalendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
                }else {
                    pomodoro5Edt.setText("");
                    time5Tv.setText("08:00");
                    time5Tv.setTag(myCalendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
                    pomodoro5Edt.setEnabled(true);
                    timerIv5.setOnClickListener(this);
                    time5Tv.setOnClickListener(this);
                }
            }
        }

        sixth = RealmController.getInstance().getPomodoroForDay(currentDate, 6);
        if(sixth != null){
            if(sixth.isStarted() || (isToday && AppHelper.getInstance().isProgramStarted())){
                pomodoro6Edt.setEnabled(false);
                timerIv6.setOnClickListener(null);
                time6Tv.setOnClickListener(null);
            }else{
                pomodoro6Edt.setEnabled(true);
                timerIv6.setOnClickListener(this);
                time6Tv.setOnClickListener(this);
            }
            pomodoro6Edt.setText(sixth.getName());
            time6Tv.setText(sixth.getTextTime());
            time6Tv.setTag(sixth.getDate());
        }else{
            if(AppHelper.getInstance().isProgramStarted()) {
                pomodoro6Edt.setEnabled(false);
                timerIv6.setOnClickListener(null);
                time6Tv.setOnClickListener(null);
                pomodoro6Edt.setText("");
                time6Tv.setText("08:00");
                time6Tv.setTag(myCalendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            }else {
                if(isPast) {
                    pomodoro6Edt.setEnabled(false);
                    timerIv6.setOnClickListener(null);
                    time6Tv.setOnClickListener(null);
                    pomodoro6Edt.setText("");
                    time6Tv.setText("08:00");
                    time6Tv.setTag(myCalendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
                }else {
                    pomodoro6Edt.setText("");
                    time6Tv.setText("08:00");
                    time6Tv.setTag(myCalendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
                    pomodoro6Edt.setEnabled(true);
                    timerIv6.setOnClickListener(this);
                    time6Tv.setOnClickListener(this);
                }
            }
        }

        seventh = RealmController.getInstance().getPomodoroForDay(currentDate, 7);

        if(seventh != null){
            if(seventh.isStarted() || (isToday && AppHelper.getInstance().isProgramStarted())){
                pomodoro7Edt.setEnabled(false);
                timerIv7.setOnClickListener(null);
                time7Tv.setOnClickListener(null);
            }else{
                pomodoro7Edt.setEnabled(true);
                timerIv7.setOnClickListener(this);
                time7Tv.setOnClickListener(this);
            }
            pomodoro7Edt.setText(seventh.getName());
            time7Tv.setText(seventh.getTextTime());
            time7Tv.setTag(seventh.getDate());
        }else{
            if(AppHelper.getInstance().isProgramStarted()) {
                pomodoro7Edt.setEnabled(false);
                timerIv7.setOnClickListener(null);
                time7Tv.setOnClickListener(null);
                pomodoro7Edt.setText("");
                time7Tv.setText("08:00");
                time7Tv.setTag(myCalendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            }else {
                if(isPast){
                    pomodoro7Edt.setEnabled(false);
                    timerIv7.setOnClickListener(null);
                    time7Tv.setOnClickListener(null);
                    pomodoro7Edt.setText("");
                    time7Tv.setText("08:00");
                    time7Tv.setTag(myCalendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
                }else {
                    pomodoro7Edt.setText("");
                    time7Tv.setText("08:00");
                    time7Tv.setTag(myCalendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
                    pomodoro7Edt.setEnabled(true);
                    timerIv7.setOnClickListener(this);
                    time7Tv.setOnClickListener(this);
                }
            }
        }

        eight = RealmController.getInstance().getPomodoroForDay(currentDate, 8);
        if(eight != null){
            if(eight.isStarted() || (isToday && AppHelper.getInstance().isProgramStarted())){
                pomodoro8Edt.setEnabled(false);
                timerIv8.setOnClickListener(null);
                time8Tv.setOnClickListener(null);
            }else{
                pomodoro8Edt.setEnabled(true);
                timerIv8.setOnClickListener(this);
                time8Tv.setOnClickListener(this);
            }
            pomodoro8Edt.setText(eight.getName());
            time8Tv.setText(eight.getTextTime());
            time8Tv.setTag(eight.getDate());
        }else{
            if(AppHelper.getInstance().isProgramStarted()) {
                pomodoro8Edt.setEnabled(false);
                timerIv8.setOnClickListener(null);
                time8Tv.setOnClickListener(null);
                pomodoro8Edt.setText("");
                time8Tv.setText("08:00");
                time8Tv.setTag(myCalendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            }else {
                if(isPast){
                    pomodoro8Edt.setEnabled(false);
                    timerIv8.setOnClickListener(null);
                    time8Tv.setOnClickListener(null);
                    pomodoro8Edt.setText("");
                    time8Tv.setText("08:00");
                    time8Tv.setTag(myCalendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
                }else {
                    pomodoro8Edt.setText("");
                    time8Tv.setText("08:00");
                    time8Tv.setTag(myCalendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
                    pomodoro8Edt.setEnabled(true);
                    timerIv8.setOnClickListener(this);
                    time8Tv.setOnClickListener(this);
                }
            }
        }

        int counter =  RealmController.getInstance().getPomodoros(monday).size();

        title.setText(getResources().getQuantityString(R.plurals.sessions_set, counter, counter, getMinAndMaxSession()));
        if(counter < getMinSessionNumber()){ //exitCalendatBtn
            exitCalendatBtn.setText(getString(R.string.exit_calendar_without_starting));
        }else{
            exitCalendatBtn.setText(getString(R.string.save_and_start_program));
        }

        if(counter < getMinSessionNumber()){
            title.setTextColor(getResources().getColor(R.color.red));
        }else{
            title.setTextColor(getResources().getColor(R.color.green));
        }

    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar =  myCalendar.withYear(year);
            myCalendar = myCalendar.withMonth(monthOfYear);
            myCalendar = myCalendar.withDayOfMonth(dayOfMonth);
            updateLabel();
        }

    };


    private String getMinAndMaxSession(){

        switch (AppHelper.getInstance().getLevel()){
            case 0: return "";
            case 1: return "(4 Min)";
            case 2: return "(8 Min)";
            case 3: return "(12 Min)";
            case 4: return "(16 Min)";
            case 5: return "(20 Min)";
            case 6: return "(24 Min)";
            case 7: return "(28 Min)";
            case 8: return "(32 Min)";
            default: return "(4 Min)";
        }

    }

    private int getMinSessionNumber(){

        switch (AppHelper.getInstance().getLevel()){
            case 0: return 1;
            case 1: return 4;
            case 2: return 8;
            case 3: return 12;
            case 4: return 16;
            case 5: return 20;
            case 6: return 24;
            case 7: return 28;
            case 8: return 32;
            default: return 4;
        }

    }

    private void updateLabel() {
        currentDate = sdf.format(myCalendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        setDayOfWeek();
        setSavedPomodoros();
    }

    private void setDayOfWeek(){

          DayOfWeek day = myCalendar.getDayOfWeek();

        if(day == DayOfWeek.MONDAY){
            onMondayClick();
        }else if(day == DayOfWeek.TUESDAY){
            onTuesdayClick();
        }else if(day == DayOfWeek.WEDNESDAY){
            onWednsdayClick();
        }else if(day == DayOfWeek.THURSDAY){
            onThuesdayClick();
        }else if(day == DayOfWeek.FRIDAY){
            onFridayClick();
        }else if(day == DayOfWeek.SATURDAY){
            onSaturdayClick();
        }else if(day == DayOfWeek.SUNDAY){
            onSundayClick();
        }

    }

    public void onTimerClick(final View view){

        TimePickerDialog mTimePicker;

        mTimePicker = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                myCalendar = myCalendar.withHour(selectedHour).withMinute(selectedMinute);
                String minute, hour;
                if(selectedMinute < 10){
                    minute = "0"+selectedMinute;
                }else{
                    minute = selectedMinute +"";
                }
                if(selectedHour < 10){
                    hour = "0"+selectedHour;
                }else{
                    hour = selectedHour +"";
                }
                if(view.getId() == R.id.timerIv1 || view.getId() == R.id.time1Tv ) {
                    time1Tv.setText(hour + ":" + minute);
                    time1Tv.setTag(myCalendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
                }else if(view.getId() == R.id.timerIv2 || view.getId() == R.id.time2Tv ){
                    time2Tv.setText(hour + ":" + minute);
                    time2Tv.setTag(myCalendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
                }else if(view.getId() == R.id.timerIv3 || view.getId() == R.id.time3Tv ){
                    time3Tv.setText(hour + ":" + minute);
                    time3Tv.setTag(myCalendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
                }else if(view.getId() == R.id.timerIv4 || view.getId() == R.id.time4Tv ){
                    time4Tv.setText(hour + ":" + minute);
                    time4Tv.setTag(myCalendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
                }else if(view.getId() == R.id.timerIv5 || view.getId() == R.id.time5Tv ){
                    time5Tv.setText(hour + ":" + minute);
                    time5Tv.setTag(myCalendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
                }else if(view.getId() == R.id.timerIv6 || view.getId() == R.id.time6Tv ){
                    time6Tv.setText(hour + ":" + minute);
                    time6Tv.setTag(myCalendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
                }else if(view.getId() == R.id.timerIv7 || view.getId() == R.id.time7Tv ){
                    time7Tv.setText(hour + ":" + minute);
                    time7Tv.setTag(myCalendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
                }else if(view.getId() == R.id.timerIv8 || view.getId() == R.id.time8Tv ){
                    time8Tv.setText(hour + ":" + minute);
                    time8Tv.setTag(myCalendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
                }

                onBtnSaveClick();
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.save_session), mTimePicker);

        mTimePicker.setTitle(getString(R.string.select_time));
        mTimePicker.show();
    }


    private void setLayoutBasedOnLevel(int level){
        switch (level){
            case 0 :
                container1.setVisibility(View.VISIBLE);
                break;
            case 1 :
                container1.setVisibility(View.VISIBLE);
                break;
            case 2 :
                container1.setVisibility(View.VISIBLE);
                container2.setVisibility(View.VISIBLE);
                break;
            case 3 :
                container1.setVisibility(View.VISIBLE);
                container2.setVisibility(View.VISIBLE);
                container3.setVisibility(View.VISIBLE);
                break;
            case 4 :
                container1.setVisibility(View.VISIBLE);
                container2.setVisibility(View.VISIBLE);
                container3.setVisibility(View.VISIBLE);
                container4.setVisibility(View.VISIBLE);
                break;
            case 5 :
                container1.setVisibility(View.VISIBLE);
                container2.setVisibility(View.VISIBLE);
                container3.setVisibility(View.VISIBLE);
                container4.setVisibility(View.VISIBLE);
                container5.setVisibility(View.VISIBLE);
                break;
            case 6 :
                container1.setVisibility(View.VISIBLE);
                container2.setVisibility(View.VISIBLE);
                container3.setVisibility(View.VISIBLE);
                container4.setVisibility(View.VISIBLE);
                container5.setVisibility(View.VISIBLE);
                container6.setVisibility(View.VISIBLE);
                break;
            case 7 :
                container1.setVisibility(View.VISIBLE);
                container2.setVisibility(View.VISIBLE);
                container3.setVisibility(View.VISIBLE);
                container4.setVisibility(View.VISIBLE);
                container5.setVisibility(View.VISIBLE);
                container6.setVisibility(View.VISIBLE);
                container7.setVisibility(View.VISIBLE);
                break;
            case 8 :
                container1.setVisibility(View.VISIBLE);
                container2.setVisibility(View.VISIBLE);
                container3.setVisibility(View.VISIBLE);
                container4.setVisibility(View.VISIBLE);
                container5.setVisibility(View.VISIBLE);
                container6.setVisibility(View.VISIBLE);
                container7.setVisibility(View.VISIBLE);
                container8.setVisibility(View.VISIBLE);
                break;
        }
    }


    @OnClick(R.id.mondayTv)
    public void onMondayTvClick(){
        onMondayClick();
        myCalendar = myCalendar.with(DayOfWeek.MONDAY);
        currentDate = sdf.format(myCalendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        setCurrentTime();
        setSavedPomodoros();
    }

    private void setCurrentTime(){
        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault()).withNano(0);
        selectedDateTime = myCalendar.withHour(now.getHour()).withMinute(now.getMinute()).withSecond(now.getSecond()).withNano(0);
    }

    public void onMondayClick(){
        mondayTv.setBackgroundResource(R.drawable.red_circle);
        tuesdayTv.setBackgroundResource(R.drawable.light_orange_circle);
        wednsdayTv.setBackgroundResource(R.drawable.light_orange_circle);
        thuesdayTv.setBackgroundResource(R.drawable.light_orange_circle);
        fridayTv.setBackgroundResource(R.drawable.light_orange_circle);
        saturdayTv.setBackgroundResource(R.drawable.light_orange_circle);
        sundayTv.setBackgroundResource(R.drawable.light_orange_circle);

        mondayTv.setTag(Constants.SELECTED);
        tuesdayTv.setTag(Constants.NOT_SELECTED);
        wednsdayTv.setTag(Constants.NOT_SELECTED);
        thuesdayTv.setTag(Constants.NOT_SELECTED);
        fridayTv.setTag(Constants.NOT_SELECTED);
        saturdayTv.setTag(Constants.NOT_SELECTED);
        sundayTv.setTag(Constants.NOT_SELECTED);

    }

    @OnClick(R.id.tuesdayTv)
    public void onTuesdayTvClick(){
        onTuesdayClick();
         myCalendar = myCalendar.with(DayOfWeek.TUESDAY);
        currentDate = sdf.format(myCalendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        setCurrentTime();
        setSavedPomodoros();

    }

    public void onTuesdayClick(){
        mondayTv.setBackgroundResource(R.drawable.light_orange_circle);
        tuesdayTv.setBackgroundResource(R.drawable.red_circle);
        wednsdayTv.setBackgroundResource(R.drawable.light_orange_circle);
        thuesdayTv.setBackgroundResource(R.drawable.light_orange_circle);
        fridayTv.setBackgroundResource(R.drawable.light_orange_circle);
        saturdayTv.setBackgroundResource(R.drawable.light_orange_circle);
        sundayTv.setBackgroundResource(R.drawable.light_orange_circle);

        mondayTv.setTag(Constants.NOT_SELECTED);
        tuesdayTv.setTag(Constants.SELECTED);
        wednsdayTv.setTag(Constants.NOT_SELECTED);
        thuesdayTv.setTag(Constants.NOT_SELECTED);
        fridayTv.setTag(Constants.NOT_SELECTED);
        saturdayTv.setTag(Constants.NOT_SELECTED);
        sundayTv.setTag(Constants.NOT_SELECTED);

    }

    @OnClick(R.id.wednsdayTv)
    public void onWednsdayTvClick(){
        onWednsdayClick();
        myCalendar = myCalendar.with(DayOfWeek.WEDNESDAY);
        currentDate = sdf.format(myCalendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        setCurrentTime();
        setSavedPomodoros();

    }

    public void onWednsdayClick(){
        mondayTv.setBackgroundResource(R.drawable.light_orange_circle);
        tuesdayTv.setBackgroundResource(R.drawable.light_orange_circle);
        wednsdayTv.setBackgroundResource(R.drawable.red_circle);
        thuesdayTv.setBackgroundResource(R.drawable.light_orange_circle);
        fridayTv.setBackgroundResource(R.drawable.light_orange_circle);
        saturdayTv.setBackgroundResource(R.drawable.light_orange_circle);
        sundayTv.setBackgroundResource(R.drawable.light_orange_circle);

        mondayTv.setTag(Constants.NOT_SELECTED);
        tuesdayTv.setTag(Constants.NOT_SELECTED);
        wednsdayTv.setTag(Constants.SELECTED);
        thuesdayTv.setTag(Constants.NOT_SELECTED);
        fridayTv.setTag(Constants.NOT_SELECTED);
        saturdayTv.setTag(Constants.NOT_SELECTED);
        sundayTv.setTag(Constants.NOT_SELECTED);
    }

    @OnClick(R.id.thuesdayTv)
    public void onThuesdayTvClick(){
        onThuesdayClick();
        myCalendar = myCalendar.with(DayOfWeek.THURSDAY);
        currentDate = sdf.format(myCalendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        setCurrentTime();
        setSavedPomodoros();

    }

    public void onThuesdayClick(){
        mondayTv.setBackgroundResource(R.drawable.light_orange_circle);
        tuesdayTv.setBackgroundResource(R.drawable.light_orange_circle);
        wednsdayTv.setBackgroundResource(R.drawable.light_orange_circle);
        thuesdayTv.setBackgroundResource(R.drawable.red_circle);
        fridayTv.setBackgroundResource(R.drawable.light_orange_circle);
        saturdayTv.setBackgroundResource(R.drawable.light_orange_circle);
        sundayTv.setBackgroundResource(R.drawable.light_orange_circle);

        mondayTv.setTag(Constants.NOT_SELECTED);
        tuesdayTv.setTag(Constants.NOT_SELECTED);
        wednsdayTv.setTag(Constants.NOT_SELECTED);
        thuesdayTv.setTag(Constants.SELECTED);
        fridayTv.setTag(Constants.NOT_SELECTED);
        saturdayTv.setTag(Constants.NOT_SELECTED);
        sundayTv.setTag(Constants.NOT_SELECTED);

    }

    @OnClick(R.id.fridayTv)
    public void onFridayTvClick(){
        onFridayClick();
//        String myFormat = "dd.MM.yyyy"; //In which you need put here
//        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        myCalendar = myCalendar.with(DayOfWeek.FRIDAY);
//        dateEdt.setText(sdf.format(myCalendar.getTime()));
        currentDate = sdf.format(myCalendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        setCurrentTime();
        setSavedPomodoros();

    }

    public void onFridayClick(){
        mondayTv.setBackgroundResource(R.drawable.light_orange_circle);
        tuesdayTv.setBackgroundResource(R.drawable.light_orange_circle);
        wednsdayTv.setBackgroundResource(R.drawable.light_orange_circle);
        thuesdayTv.setBackgroundResource(R.drawable.light_orange_circle);
        fridayTv.setBackgroundResource(R.drawable.red_circle);
        saturdayTv.setBackgroundResource(R.drawable.light_orange_circle);
        sundayTv.setBackgroundResource(R.drawable.light_orange_circle);

        mondayTv.setTag(Constants.NOT_SELECTED);
        tuesdayTv.setTag(Constants.NOT_SELECTED);
        wednsdayTv.setTag(Constants.NOT_SELECTED);
        thuesdayTv.setTag(Constants.NOT_SELECTED);
        fridayTv.setTag(Constants.SELECTED);
        saturdayTv.setTag(Constants.NOT_SELECTED);
        sundayTv.setTag(Constants.NOT_SELECTED);

    }

    @OnClick(R.id.saturdayTv)
    public void onSaturdayTvClick(){
        onSaturdayClick();
        myCalendar = myCalendar.with(DayOfWeek.SATURDAY);
        currentDate = sdf.format(myCalendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        setCurrentTime();
        setSavedPomodoros();

    }

    public void onSaturdayClick(){
        mondayTv.setBackgroundResource(R.drawable.light_orange_circle);
        tuesdayTv.setBackgroundResource(R.drawable.light_orange_circle);
        wednsdayTv.setBackgroundResource(R.drawable.light_orange_circle);
        thuesdayTv.setBackgroundResource(R.drawable.light_orange_circle);
        fridayTv.setBackgroundResource(R.drawable.light_orange_circle);
        saturdayTv.setBackgroundResource(R.drawable.red_circle);
        sundayTv.setBackgroundResource(R.drawable.light_orange_circle);

        mondayTv.setTag(Constants.NOT_SELECTED);
        tuesdayTv.setTag(Constants.NOT_SELECTED);
        wednsdayTv.setTag(Constants.NOT_SELECTED);
        thuesdayTv.setTag(Constants.NOT_SELECTED);
        fridayTv.setTag(Constants.NOT_SELECTED);
        saturdayTv.setTag(Constants.SELECTED);
        sundayTv.setTag(Constants.NOT_SELECTED);

    }

    @OnClick(R.id.sundayTv)
    public void onSundayTvClick(){
        onSundayClick();
        myCalendar = myCalendar.with(DayOfWeek.SUNDAY);
        currentDate = sdf.format(myCalendar.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        setCurrentTime();
        setSavedPomodoros();
    }

    public void onSundayClick(){
        mondayTv.setBackgroundResource(R.drawable.light_orange_circle);
        tuesdayTv.setBackgroundResource(R.drawable.light_orange_circle);
        wednsdayTv.setBackgroundResource(R.drawable.light_orange_circle);
        thuesdayTv.setBackgroundResource(R.drawable.light_orange_circle);
        fridayTv.setBackgroundResource(R.drawable.light_orange_circle);
        saturdayTv.setBackgroundResource(R.drawable.light_orange_circle);
        sundayTv.setBackgroundResource(R.drawable.red_circle);

        mondayTv.setTag(Constants.NOT_SELECTED);
        tuesdayTv.setTag(Constants.NOT_SELECTED);
        wednsdayTv.setTag(Constants.NOT_SELECTED);
        thuesdayTv.setTag(Constants.NOT_SELECTED);
        fridayTv.setTag(Constants.NOT_SELECTED);
        saturdayTv.setTag(Constants.NOT_SELECTED);
        sundayTv.setTag(Constants.SELECTED);

    }

    private boolean isSessionWithSameTime(){

        ArrayList<Long> list = new ArrayList<>();
        long text1 = 0, text2 = 0, text3 = 0, text4 = 0, text5 = 0, text6 = 0, text7 = 0, text8 = 0;

        if(!pomodoro1Edt.getText().toString().isEmpty()){
            text1 = (long) time1Tv.getTag();
            list.add(text1);
        }
        if(!pomodoro2Edt.getText().toString().isEmpty()){
            text2 = (long) time2Tv.getTag();
            list.add(text2);

        }
        if(!pomodoro3Edt.getText().toString().isEmpty()){
            text3 = (long) time3Tv.getTag();
            list.add(text3);

        }
        if(!pomodoro4Edt.getText().toString().isEmpty()){
            text4 = (long) time4Tv.getTag();
            list.add(text4);

        }
        if(!pomodoro5Edt.getText().toString().isEmpty()){
            text5 = (long) time5Tv.getTag();
            list.add(text5);

        }
        if(!pomodoro6Edt.getText().toString().isEmpty()){
            text6 = (long) time6Tv.getTag();
            list.add(text6);

        }
        if(!pomodoro7Edt.getText().toString().isEmpty()){
            text7 = (long) time7Tv.getTag();
            list.add(text7);

        }
        if(!pomodoro8Edt.getText().toString().isEmpty()){
            text8 = (long) time8Tv.getTag();
            list.add(text8);

        }
        for(int i =0; i<list.size(); i++){
            if(i != 0 && list.get(i).equals(text1)){
                return true;
            }
            if(i != 1 && list.get(i).equals(text2)){
                return true;
            }
            if(i != 2 && list.get(i).equals(text3)){
                return true;
            }
            if(i != 3 && list.get(i).equals(text4)){
                return true;
            }
            if(i != 4 && list.get(i).equals(text5)){
                return true;
            }
            if(i != 5 && list.get(i).equals(text6)){
                return true;
            }
            if(i != 6 && list.get(i).equals(text7)){
                return true;
            }
            if(i != 7 && list.get(i).equals(text8)){
                return true;
            }
        }

        for(Long item : list){
            if(item > text1 && item < text1 + THIRTY_MINUTES){
                return true;
            }
            if(item > text2 && item < text2 + THIRTY_MINUTES){
                return true;
            }
            if(item > text3 && item < text3 + THIRTY_MINUTES){
                return true;
            }
            if(item > text4 && item < text4 + THIRTY_MINUTES){
                return true;
            }
            if(item > text5 && item < text5 + THIRTY_MINUTES){
                return true;
            }
            if(item > text6 && item < text6 + THIRTY_MINUTES){
                return true;
            }
            if(item > text7 && item < text7 + THIRTY_MINUTES){
                return true;
            }
            if(item > text8 && item < text8 + THIRTY_MINUTES){
                return true;
            }
        }

        return false;
    }


    @OnClick(R.id.btnSave)
    public void onBtnSaveClick(){
        first = RealmController.getInstance().getPomodoroForDay(currentDate, 1);

        if(AppHelper.getInstance().isTestTime() && RealmController.getInstance().getAllPomodoros().size() == 1 && first == null){
            Toast.makeText(activity, getString(R.string.one_test_session), Toast.LENGTH_LONG).show();
        }else if(isSessionWithSameTime()){
            Toast.makeText(activity, getString(R.string.cant_save_session_same_time), Toast.LENGTH_LONG).show();
        }else {

            if (!pomodoro1Edt.getText().toString().isEmpty()) {
                String text = pomodoro1Edt.getText().toString();
                final Pomodoro obj = new Pomodoro();
                obj.setId(1 + currentDate);
                obj.setName(text);
                obj.setDuration(getDuration(1)); //15min
                obj.setTextTime(time1Tv.getText().toString());
                obj.setDateString(currentDate);
                obj.setNumber(1);
                obj.setDate((long) time1Tv.getTag());
                int newId = Integer.parseInt(obj.getId().replaceAll("[.]", ""));
                long t = obj.getDate();
                setReminderNotificationIntent(t - removeMinutes, obj.getId(), obj.getDuration(), newId);
                if (first != null) {
                    obj.setPomodoroCompleted(first.isPomodoroCompleted());
                    obj.setStarted(first.isStarted());
                    obj.setStayedFocused(first.isStayedFocused());
                    obj.setTaskFinished(first.isTaskFinished());
                    obj.setDone(first.isDone());
                    obj.setResultShowed(first.isResultShowed());
                }

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealmOrUpdate(obj);
                    }
                });
            }

            if (!pomodoro2Edt.getText().toString().isEmpty()) {
                String text = pomodoro2Edt.getText().toString();
                final Pomodoro obj = new Pomodoro();
                obj.setId(2 + currentDate);
                obj.setName(text);
                obj.setDuration(getDuration(2)); //15min
                obj.setTextTime(time2Tv.getText().toString());
                obj.setDateString(currentDate);
                obj.setNumber(2);
                obj.setDate((long) time2Tv.getTag());
                int newId = Integer.parseInt(obj.getId().replaceAll("[.]", ""));
                long t = obj.getDate();
                setReminderNotificationIntent(t - removeMinutes, obj.getId(), obj.getDuration(), newId);
                if (second != null) {
                    obj.setPomodoroCompleted(second.isPomodoroCompleted());
                    obj.setStarted(second.isStarted());
                    obj.setStayedFocused(second.isStayedFocused());
                    obj.setTaskFinished(second.isTaskFinished());
                    obj.setDone(second.isDone());
                    obj.setResultShowed(second.isResultShowed());
                }

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealmOrUpdate(obj);
                    }
                });
            }

            if (!pomodoro3Edt.getText().toString().isEmpty()) {
                String text = pomodoro3Edt.getText().toString();
                final Pomodoro obj = new Pomodoro();
                obj.setId(3 + currentDate);
                obj.setName(text);
                obj.setDuration(getDuration(3)); //15min
                obj.setTextTime(time3Tv.getText().toString());
                obj.setDateString(currentDate);
                obj.setNumber(3);
                obj.setDate((long) time3Tv.getTag());
                int newId = Integer.parseInt(obj.getId().replaceAll("[.]", ""));
                long t = obj.getDate();
                setReminderNotificationIntent(t - removeMinutes, obj.getId(), obj.getDuration(), newId);
                if (third != null) {
                    obj.setPomodoroCompleted(third.isPomodoroCompleted());
                    obj.setStarted(third.isStarted());
                    obj.setStayedFocused(third.isStayedFocused());
                    obj.setTaskFinished(third.isTaskFinished());
                    obj.setDone(third.isDone());
                    obj.setResultShowed(third.isResultShowed());
                }
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealmOrUpdate(obj);
                    }
                });
            }

            if (!pomodoro4Edt.getText().toString().isEmpty()) {
                String text = pomodoro4Edt.getText().toString();
                final Pomodoro obj = new Pomodoro();
                obj.setId(4 + currentDate);
                obj.setName(text);
                obj.setDuration(getDuration(4)); //15min
                obj.setTextTime(time4Tv.getText().toString());
                obj.setDateString(currentDate);
                obj.setNumber(4);
                obj.setDate((long) time4Tv.getTag());
                int newId = Integer.parseInt(obj.getId().replaceAll("[.]", ""));
                long t = obj.getDate();
                setReminderNotificationIntent(t - removeMinutes, obj.getId(), obj.getDuration(), newId);
                if (fourth != null) {
                    obj.setPomodoroCompleted(fourth.isPomodoroCompleted());
                    obj.setStarted(fourth.isStarted());
                    obj.setStayedFocused(fourth.isStayedFocused());
                    obj.setTaskFinished(fourth.isTaskFinished());
                    obj.setDone(fourth.isDone());
                    obj.setResultShowed(fourth.isResultShowed());
                }

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        // This wila new object if an object with no primary key = 42
                        realm.copyToRealmOrUpdate(obj);
                    }
                });
            }

            if (!pomodoro5Edt.getText().toString().isEmpty()) {
                String text = pomodoro5Edt.getText().toString();
                final Pomodoro obj = new Pomodoro();
                obj.setId(5 + currentDate);
                obj.setName(text);
                obj.setDuration(getDuration(5)); //15min
                obj.setTextTime(time5Tv.getText().toString());
                obj.setDateString(currentDate);
                obj.setNumber(5);
                obj.setDate((long) time5Tv.getTag());
                int newId = Integer.parseInt(obj.getId().replaceAll("[.]", ""));
                long t = obj.getDate();
                setReminderNotificationIntent(t - removeMinutes, obj.getId(), obj.getDuration(), newId);
                if (fifth != null) {
                    obj.setPomodoroCompleted(fifth.isPomodoroCompleted());
                    obj.setStarted(fifth.isStarted());
                    obj.setStayedFocused(fifth.isStayedFocused());
                    obj.setTaskFinished(fifth.isTaskFinished());
                    obj.setDone(fifth.isDone());
                    obj.setResultShowed(fifth.isResultShowed());
                }
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        realm.copyToRealmOrUpdate(obj);
                    }
                });
            }

            if (!pomodoro6Edt.getText().toString().isEmpty()) {
                String text = pomodoro6Edt.getText().toString();
                final Pomodoro obj = new Pomodoro();
                obj.setId(6 + currentDate);
                obj.setName(text);
                obj.setDuration(getDuration(6)); //15min
                obj.setTextTime(time6Tv.getText().toString());
                obj.setDateString(currentDate);
                obj.setNumber(6);
                obj.setDate((long) time6Tv.getTag());
                int newId = Integer.parseInt(obj.getId().replaceAll("[.]", ""));
                long t = obj.getDate();
                setReminderNotificationIntent(t - removeMinutes, obj.getId(), obj.getDuration(), newId);
                if (sixth != null) {
                    obj.setPomodoroCompleted(sixth.isPomodoroCompleted());
                    obj.setStarted(sixth.isStarted());
                    obj.setStayedFocused(sixth.isStayedFocused());
                    obj.setTaskFinished(sixth.isTaskFinished());
                    obj.setDone(sixth.isDone());
                    obj.setResultShowed(sixth.isResultShowed());
                }


                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        realm.copyToRealmOrUpdate(obj);
                    }
                });
            }

            if (!pomodoro7Edt.getText().toString().isEmpty()) {
                String text = pomodoro7Edt.getText().toString();
                final Pomodoro obj = new Pomodoro();
                obj.setId(7 + currentDate);
                obj.setName(text);
                obj.setDuration(getDuration(7)); //15min
                obj.setTextTime(time7Tv.getText().toString());
                obj.setDateString(currentDate);
                obj.setNumber(7);
                obj.setDate((long) time7Tv.getTag());
                int newId = Integer.parseInt(obj.getId().replaceAll("[.]", ""));
                long t = obj.getDate();
                setReminderNotificationIntent(t - removeMinutes, obj.getId(), obj.getDuration(), newId);
                if (seventh != null) {
                    obj.setPomodoroCompleted(seventh.isPomodoroCompleted());
                    obj.setStarted(seventh.isStarted());
                    obj.setStayedFocused(seventh.isStayedFocused());
                    obj.setTaskFinished(seventh.isTaskFinished());
                    obj.setDone(seventh.isDone());
                    obj.setResultShowed(seventh.isResultShowed());
                }

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        realm.copyToRealmOrUpdate(obj);
                    }
                });
            }

            if (!pomodoro8Edt.getText().toString().isEmpty()) {
                String text = pomodoro8Edt.getText().toString();
                final Pomodoro obj = new Pomodoro();
                obj.setId(8 + currentDate);
                obj.setName(text);
                obj.setDuration(getDuration(8)); //15min
                obj.setTextTime(time8Tv.getText().toString());
                obj.setDateString(currentDate);
                obj.setNumber(8);
                obj.setDate((long) time8Tv.getTag());
                int newId = Integer.parseInt(obj.getId().replaceAll("[.]", ""));
                long t = obj.getDate();
                setReminderNotificationIntent(t - removeMinutes, obj.getId(), obj.getDuration(), newId);
                if (eight != null) {
                    obj.setPomodoroCompleted(eight.isPomodoroCompleted());
                    obj.setStarted(eight.isStarted());
                    obj.setStayedFocused(eight.isStayedFocused());
                    obj.setTaskFinished(eight.isTaskFinished());
                    obj.setDone(eight.isDone());
                    obj.setResultShowed(eight.isResultShowed());
                }

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        realm.copyToRealmOrUpdate(obj);
                    }
                });
            }

            int counter = RealmController.getInstance().getPomodoros(monday).size();
            title.setText(getResources().getQuantityString(R.plurals.sessions_set, counter, counter, getMinAndMaxSession()));

            if (counter < getMinSessionNumber() && !AppHelper.getInstance().isTestTime()) {
                exitCalendatBtn.setText(getString(R.string.exit_calendar_without_starting));
            } else {
                exitCalendatBtn.setText(getString(R.string.save_and_start_program));
            }

            if(counter < getMinSessionNumber()){
                title.setTextColor(getResources().getColor(R.color.red));
            }else{
                title.setTextColor(getResources().getColor(R.color.green));
            }

            Toast.makeText(activity, getString(R.string.saved), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        onTimerClick(v);
    }
    public static final String workTag = "notificationWork";


    private void setReminderNotificationIntent(long date, String id, long duration, int newId){
        Calendar now = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(date));
        if(calendar.getTimeInMillis() > now.getTimeInMillis()) {
            AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);

            Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
            notificationIntent.addCategory("android.intent.category.DEFAULT");
            notificationIntent.putExtra(Constants.NOTIFICATION_TEXT, getString(R.string.your_next_pomodoro_will_start));
            notificationIntent.putExtra(Constants.IS_START, true);
            notificationIntent.putExtra(Constants.POMODORO_ID, id);
            notificationIntent.putExtra(Constants.DURATION, duration);
            notificationIntent.setClass(activity, AlarmReceiver.class);

            PendingIntent broadcast = PendingIntent.getBroadcast(activity, newId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//            PendingIntent broadcast = PendingIntent.getActivity(activity, newId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), broadcast);
            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), broadcast), broadcast);

        }
    }

    private long getDuration(int number){
        switch (number){
            case 1:
                if(AppHelper.getInstance().getLevel() == 1 || AppHelper.getInstance().getLevel() == 0){
                    return 15*60*1000;
                }else{
                    return 25*60*1000;
                }
            case 2:
                if(AppHelper.getInstance().getLevel() == 2){
                    return 15*60*1000;
                }else{
                    return 25*60*1000;
                }
            case 3:
                if(AppHelper.getInstance().getLevel() == 3){
                    return 15*60*1000;
                }else{
                    return 25*60*1000;
                }
            case 4:
                return 25*60*1000;
            case 5:
                return 25*60*1000;
            case 6:
                return 25*60*1000;
            case 7:
                return 25*60*1000;
            case 8:
                return 25*60*1000;
                default: return 25*60*100;
        }
    }

    @OnClick(R.id.exitCalendatBtn)
    public void onExitCalendarClick(){
        if(AppHelper.getInstance().isTestTime()){
            activity.showNewFragment(new OverviewFragment(), R.id.main_container);
            return;
        }
        if(exitCalendatBtn.getText().equals(getString(R.string.save_and_start_program))){
            AppHelper.getInstance().setIsProgramStarted(true);
            RealmResults<Pomodoro> sorted = RealmController.getInstance().getAllPomodorosFromDate(monday);
            AppHelper.getInstance().setSessionCount(sorted.size());
        }else{
            AppHelper.getInstance().setSessionCount(0);
            AppHelper.getInstance().setIsProgramStarted(false);
        }

        activity.showNewFragment(new OverviewFragment(), R.id.main_container);
    }
}
