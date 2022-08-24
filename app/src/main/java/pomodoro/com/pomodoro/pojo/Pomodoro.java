package pomodoro.com.pomodoro.pojo;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Pomodoro extends RealmObject {

    @PrimaryKey
    private String id;
    private String name;
    private String textTime;
    private boolean stayedFocused;
    private boolean isTaskFinished;
    public boolean isPomodoroCompleted;
    private long duration;
    private String dateString;
    private int number;
    private long date;
    private boolean isStarted;
    private boolean isDone;
    private boolean isResultShowed;

    public boolean isResultShowed() {
        return isResultShowed;
    }

    public void setResultShowed(boolean resultShowed) {
        isResultShowed = resultShowed;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }

    public boolean isPomodoroCompleted() {
        return isPomodoroCompleted;
    }

    public void setPomodoroCompleted(boolean pomodoroCompleted) {
        isPomodoroCompleted = pomodoroCompleted;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStayedFocused() {
        return stayedFocused;
    }

    public void setStayedFocused(boolean stayedFocused) {
        this.stayedFocused = stayedFocused;
    }

    public boolean isTaskFinished() {
        return isTaskFinished;
    }

    public void setTaskFinished(boolean taskFinished) {
        isTaskFinished = taskFinished;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTextTime() {
        return textTime;
    }

    public void setTextTime(String textTime) {
        this.textTime = textTime;
    }
}
