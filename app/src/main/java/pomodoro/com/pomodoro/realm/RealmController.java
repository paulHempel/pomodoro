package pomodoro.com.pomodoro.realm;

import android.content.Context;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import pomodoro.com.pomodoro.pojo.Pomodoro;

public class RealmController {

    private static RealmController instance;
    Realm realm;
    Calendar cal = Calendar.getInstance();
    Context context;

    public RealmController() {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController getInstance() {
        if (null == instance) {
            instance = new RealmController();
        }
        return instance;
    }

    public void init(Context cont){
        this.context = cont;
    }


    public Realm getRealm() {

        return realm;
    }

    public void refresh() {

        realm.refresh();
    }

    public void clearAll() {
        realm.beginTransaction();
        realm.delete(Pomodoro.class);
        realm.commitTransaction();
    }

    public RealmResults<Pomodoro> getPomodoros(long date) {
        return realm.where(Pomodoro.class).greaterThanOrEqualTo("date", date).findAll().sort("date", Sort.ASCENDING);
    }

    public RealmResults<Pomodoro> getAllPomodoros() {
        return realm.where(Pomodoro.class).findAll();
    }

    public Pomodoro getPomodoroForDay(String date, int number){
        return realm.where(Pomodoro.class).equalTo("dateString", date).and().equalTo("number", number).findFirst();
    }

    public Pomodoro getPomodoroById(String id){
        return realm.where(Pomodoro.class).equalTo("id", id).findFirst();
    }

    public RealmResults<Pomodoro> getSortedPomodorosByDate(long today){

        return realm.where(Pomodoro.class).greaterThanOrEqualTo("date", today).and()
                .equalTo("isPomodoroCompleted", false).and()
                .equalTo("isStarted", false).findAll().sort("date", Sort.ASCENDING);
    }

    public RealmResults<Pomodoro> getAllPomodorosFromDate(long today){

        return realm.where(Pomodoro.class).greaterThanOrEqualTo("date", today).findAll().sort("date", Sort.ASCENDING);
    }


    public RealmResults<Pomodoro> getCompletedPomodoros(){
        return realm.where(Pomodoro.class) //lessThanOrEqualTo("date", today).and()
                .equalTo("isPomodoroCompleted", true).and()
                .equalTo("isResultShowed", true).and()
                .equalTo("isDone", false).and()
                .equalTo("isStarted", true).findAll().sort("date", Sort.ASCENDING);
    }

    public RealmResults<Pomodoro> getTaskFinishedPomodoros(){
        return realm.where(Pomodoro.class) //lessThanOrEqualTo("date", today).and()
                .equalTo("isTaskFinished", true).and()
                .equalTo("isDone", false).and()
                .equalTo("isStarted", true).findAll().sort("date", Sort.ASCENDING);
    }

    public RealmResults<Pomodoro> getStayedFocusedPomodoros(){
        return realm.where(Pomodoro.class) //lessThanOrEqualTo("date", today).and()
                .equalTo("stayedFocused", true).and()
                .equalTo("isDone", false).and()
                .equalTo("isStarted", true).findAll().sort("date", Sort.ASCENDING);
    }


    public RealmResults<Pomodoro> getIncompletedPomodoros(long today){
        return realm.where(Pomodoro.class)
                .lessThan("date", today).and() //lessThanOrEqualTo("date", today).and()
                .equalTo("isPomodoroCompleted", false).and()
                .equalTo("isResultShowed", false).and()
                .equalTo("isDone", false)
                .findAll();
    }

    public RealmResults<Pomodoro> getFinishedPomodoros(){ //Success or failed
        return realm.where(Pomodoro.class)
                .equalTo("isDone", false).and()
                .equalTo("isStarted", true)
                .findAll().sort("date", Sort.ASCENDING);
    }

    public RealmResults<Pomodoro> getDonePomodoros(){ //Success or failed
        return realm.where(Pomodoro.class) //lessThanOrEqualTo("date", today).and()
                .equalTo("isDone", true).and()
                .equalTo("isStarted", true).findAll().sort("date", Sort.ASCENDING);
    }

//            return realm.where(Pomodoro.class).contains("dateString", date).and().contains("id").findAll();
//
//    public Pomodoro getQuote(String quoteDate) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//        String s = sdf.format(quoteDate);
//        Date date = null;
//        try {
//            date = sdf.parse(s);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return realm.where(Pomodoro.class).equalTo("id", date).findFirst();
//    }


}
