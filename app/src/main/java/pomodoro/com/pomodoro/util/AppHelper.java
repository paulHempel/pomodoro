package pomodoro.com.pomodoro.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;
import java.util.Locale;

import pomodoro.com.pomodoro.constants.SharedPrefsConst;

/**
 * Created by ema on 10/26/16.
 */

public class AppHelper {
    public static final String TAG = AppHelper.class.getSimpleName();

    private static AppHelper instance;
    private Context context;


    public static AppHelper getInstance(){
        if(instance == null){
            instance = new AppHelper();
        }
        return instance;
    }

    public void init(Context cont){
        this.context = cont;
    }


    public boolean isTutorialDone(){
        return readFromSharedPrefrences(SharedPrefsConst.IS_TUTORIAL_DONE, false);
    }

    public void setIsTutorialDone(boolean isTutorialDone){
        writeToSharedPreferences(SharedPrefsConst.IS_TUTORIAL_DONE, isTutorialDone);
    }

    public boolean isProgramStarted(){
        return readFromSharedPrefrences(SharedPrefsConst.IS_PROGRAM_STARTED, false);
    }

    public void setIsProgramStarted(boolean isProgramStarted){
        writeToSharedPreferences(SharedPrefsConst.IS_PROGRAM_STARTED, isProgramStarted);
    }

    public int getSessionCount(){
        return readFromSharedPrefrences(SharedPrefsConst.SESSION_COUNT, 0);
    }

    public void setSessionCount(int sessionCount){
        writeToSharedPreferences(SharedPrefsConst.SESSION_COUNT, sessionCount);
    }

    public int getNextWeek(){
        return readFromSharedPrefrences(SharedPrefsConst.NEXT_WEEK, 0);
    }

    public void setNextWeek(int nextWeek){
        writeToSharedPreferences(SharedPrefsConst.NEXT_WEEK, nextWeek);
    }


    public boolean isDoneDeleted(){
        return readFromSharedPrefrences(SharedPrefsConst.DELETE_DONE, false);
    }

    public void setDoneDeleted(boolean deleted){
        writeToSharedPreferences(SharedPrefsConst.DELETE_DONE, deleted);
    }

    public boolean isFirstTimeOpen(){
        return readFromSharedPrefrences(SharedPrefsConst.IS_FIRST_TIME_OPEN, false);
    }

    public void setIsFirstTimeOpen(boolean openned){
        writeToSharedPreferences(SharedPrefsConst.IS_FIRST_TIME_OPEN, openned);
    }

    public boolean isTestTime(){
        return readFromSharedPrefrences(SharedPrefsConst.IS_TEST_TIME,  false);
    }

    public void setIsTestTime(boolean isTest){
        writeToSharedPreferences(SharedPrefsConst.IS_TEST_TIME, isTest);
    }

    public boolean isTestMessageShowed(){
        return readFromSharedPrefrences(SharedPrefsConst.IS_TEST_LEVEL_MESSAGE_SHOWED,  false);
    }

    public void setIsTestMessageShowed(boolean isTest){
        writeToSharedPreferences(SharedPrefsConst.IS_TEST_LEVEL_MESSAGE_SHOWED, isTest);
    }

    public int getLevel(){
        return readFromSharedPrefrences(SharedPrefsConst.LEVEL,  1);
    }

    public void setLevel(int level){
        writeToSharedPreferences(SharedPrefsConst.LEVEL, level);
    }

    public int getLevel1Pomo(){
        return readFromSharedPrefrences(SharedPrefsConst.LEVEL_1_POMO,  0);
    }

    public void setLevel1Pomo(int progress){
        writeToSharedPreferences(SharedPrefsConst.LEVEL_1_POMO, progress);
    }

    public int getLevel2Pomo(){
        return readFromSharedPrefrences(SharedPrefsConst.LEVEL_2_POMO,  0);
    }

    public void setLevel2Pomo(int progress){
        writeToSharedPreferences(SharedPrefsConst.LEVEL_2_POMO, progress);
    }

    public int getLevel3Pomo(){
        return readFromSharedPrefrences(SharedPrefsConst.LEVEL_3_POMO,  0);
    }

    public void setLevel3Pomo(int progress){
        writeToSharedPreferences(SharedPrefsConst.LEVEL_3_POMO, progress);
    }

    public int getLevel4Pomo(){
        return readFromSharedPrefrences(SharedPrefsConst.LEVEL_4_POMO,  0);
    }

    public void setLevel4Pomo(int progress){
        writeToSharedPreferences(SharedPrefsConst.LEVEL_4_POMO, progress);
    }

    public int getLevel5Pomo(){
        return readFromSharedPrefrences(SharedPrefsConst.LEVEL_5_POMO,  0);
    }

    public void setLevel5Pomo(int progress){
        writeToSharedPreferences(SharedPrefsConst.LEVEL_5_POMO, progress);
    }

    public int getLevel6Pomo(){
        return readFromSharedPrefrences(SharedPrefsConst.LEVEL_6_POMO,  0);
    }

    public void setLevel6Pomo(int progress){
        writeToSharedPreferences(SharedPrefsConst.LEVEL_6_POMO, progress);
    }

    public int getLevel7Pomo(){
        return readFromSharedPrefrences(SharedPrefsConst.LEVEL_7_POMO,  0);
    }

    public void setLevel7Pomo(int progress){
        writeToSharedPreferences(SharedPrefsConst.LEVEL_7_POMO, progress);
    }

    public int getLevel8Pomo(){
        return readFromSharedPrefrences(SharedPrefsConst.LEVEL_8_POMO,  0);
    }

    public void setLevel8Pomo(int progress){
        writeToSharedPreferences(SharedPrefsConst.LEVEL_8_POMO, progress);
    }

    public int getLevel1Tasks(){
        return readFromSharedPrefrences(SharedPrefsConst.LEVEL_1_TASKS,  0);
    }

    public void setLevel1Tasks(int progress){
        writeToSharedPreferences(SharedPrefsConst.LEVEL_1_TASKS, progress);
    }

    public int getLevel2Tasks(){
        return readFromSharedPrefrences(SharedPrefsConst.LEVEL_2_TASKS,  0);
    }

    public void setLevel2Tasks(int progress){
        writeToSharedPreferences(SharedPrefsConst.LEVEL_2_TASKS, progress);
    }

    public int getLevel3Tasks(){
        return readFromSharedPrefrences(SharedPrefsConst.LEVEL_3_TASKS,  0);
    }

    public void setLevel3Tasks(int progress){
        writeToSharedPreferences(SharedPrefsConst.LEVEL_3_TASKS, progress);
    }

    public int getLevel4Tasks(){
        return readFromSharedPrefrences(SharedPrefsConst.LEVEL_4_TASKS,  0);
    }

    public void setLevel4Tasks(int progress){
        writeToSharedPreferences(SharedPrefsConst.LEVEL_4_TASKS, progress);
    }

    public int getLevel5Tasks(){
        return readFromSharedPrefrences(SharedPrefsConst.LEVEL_5_TASKS,  0);
    }

    public void setLevel5Tasks(int progress){
        writeToSharedPreferences(SharedPrefsConst.LEVEL_5_TASKS, progress);
    }

    public int getLevel6Tasks(){
        return readFromSharedPrefrences(SharedPrefsConst.LEVEL_6_TASKS,  0);
    }

    public void setLevel6Tasks(int progress){
        writeToSharedPreferences(SharedPrefsConst.LEVEL_6_TASKS, progress);
    }

    public int getLevel7Tasks(){
        return readFromSharedPrefrences(SharedPrefsConst.LEVEL_7_TASKS,  0);
    }

    public void setLevel7Tasks(int progress){
        writeToSharedPreferences(SharedPrefsConst.LEVEL_7_TASKS, progress);
    }

    public int getLevel8Tasks(){
        return readFromSharedPrefrences(SharedPrefsConst.LEVEL_8_TASKS,  0);
    }

    public void setLevel8Tasks(int progress){
        writeToSharedPreferences(SharedPrefsConst.LEVEL_8_TASKS, progress);
    }

//    public String getFutureName(){
//        return readFromSharedPrefrences(SharedPrefsConst.FUTURE_NAME,  "Name");
//    }

    public void setFutureName(String name){
        writeToSharedPreferences(SharedPrefsConst.FUTURE_NAME, name);
    }

    private void writeToSharedPreferences(String key, String value){
        SharedPreferences.Editor editor = context.getSharedPreferences(SharedPrefsConst.SHARED_PREFS_KEY, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    private void writeToSharedPreferences(String key, boolean value){
        SharedPreferences.Editor editor = context.getSharedPreferences(SharedPrefsConst.SHARED_PREFS_KEY, Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private void writeToSharedPreferences(String key, int value){
        SharedPreferences.Editor editor = context.getSharedPreferences(SharedPrefsConst.SHARED_PREFS_KEY, Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    private void writeToSharedPreferences(String key, long value){
        SharedPreferences.Editor editor = context.getSharedPreferences(SharedPrefsConst.SHARED_PREFS_KEY, Context.MODE_PRIVATE).edit();
        editor.putLong(key, value);
        editor.apply();
    }

    private void writeToSharedPreferences(String key, float value){
        SharedPreferences.Editor editor = context.getSharedPreferences(SharedPrefsConst.SHARED_PREFS_KEY, Context.MODE_PRIVATE).edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    private long readFromSharedPrefrences(String key, long defaultValue){
        return context.getSharedPreferences(SharedPrefsConst.SHARED_PREFS_KEY, Context.MODE_PRIVATE).getLong(key, defaultValue);
    }

    private String readFromSharedPrefrences(String key, String defaultValue){
        return context.getSharedPreferences(SharedPrefsConst.SHARED_PREFS_KEY, Context.MODE_PRIVATE).getString(key, defaultValue);
    }

    private boolean readFromSharedPrefrences(String key, boolean defaultValue){
        return context.getSharedPreferences(SharedPrefsConst.SHARED_PREFS_KEY, Context.MODE_PRIVATE).getBoolean(key, defaultValue);
    }

    private int readFromSharedPrefrences(String key, int defaultValue){
        return context.getSharedPreferences(SharedPrefsConst.SHARED_PREFS_KEY, Context.MODE_PRIVATE).getInt(key, defaultValue);
    }

    private float readFromSharedPrefrences(String key, float defaultValue){
        return context.getSharedPreferences(SharedPrefsConst.SHARED_PREFS_KEY, Context.MODE_PRIVATE).getFloat(key, defaultValue);
    }

}
