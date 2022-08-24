package pomodoro.com.pomodoro.fragments.dashboard;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import io.realm.RealmResults;
import pomodoro.com.pomodoro.MainActivity;
import pomodoro.com.pomodoro.R;
import pomodoro.com.pomodoro.fragments.BaseFragment;
import pomodoro.com.pomodoro.fragments.pomodoro.AdviceDialog;
import pomodoro.com.pomodoro.pojo.Pomodoro;
import pomodoro.com.pomodoro.realm.RealmController;
import pomodoro.com.pomodoro.util.AppHelper;

public class OverviewFragment extends BaseFragment {

    @BindView(R.id.nextPomoTitle)
    TextView nextPomoTitle;
    @BindView(R.id.thisWeekProgress)
    ProgressBar thisWeekProgress;
    @BindView(R.id.thisWeekTasksProgress)
    ProgressBar thisWeekTasksProgress;

    @BindView(R.id.tasksThisWeekTv)
    TextView tasksThisWeekTv;
    @BindView(R.id.pomodoroThisWeekTv)
    TextView pomodoroThisWeekTv;

    @BindView(R.id.adviceOfLevelTv)
    TextView adviceOfLevelTv;

    @BindView(R.id.img1)
    ImageView img1;
    @BindView(R.id.img2)
    ImageView img2;
    @BindView(R.id.img3)
    ImageView img3;
    @BindView(R.id.img4)
    ImageView img4;
    @BindView(R.id.img5)
    ImageView img5;
    @BindView(R.id.img6)
    ImageView img6;
    @BindView(R.id.img7)
    ImageView img7;
    @BindView(R.id.img8)
    ImageView img8;


    MainActivity activity;
    String id = null;
    long duration;
    RealmResults<Pomodoro> pomodoros;

    RealmResults<Pomodoro> taskFinished;
    RealmResults<Pomodoro> successfulPomodoros;


    @Override
    protected int setResourceId() {
        return R.layout.overview_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        activity = getMainActivity();

        Calendar calendar = Calendar.getInstance();
        RealmResults<Pomodoro> sorted = RealmController.getInstance().getSortedPomodorosByDate(calendar.getTimeInMillis());
        if(sorted.size() > 0){
            String myFormat = "dd.MM.yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            String today = sdf.format(calendar.getTime());
            id = sorted.first().getId();
            duration = sorted.first().getDuration();
            if(sorted.first().getDateString().equals(today)){
                nextPomoTitle.setText(getString(R.string.your_next_pomodoro, sorted.first().getName(), getString(R.string.today), sorted.first().getTextTime()));
            }else{
                nextPomoTitle.setText(getString(R.string.your_next_pomodoro, sorted.first().getName(), sorted.first().getDateString(), sorted.first().getTextTime()));
            }
//            startPomoBtn.setVisibility(View.VISIBLE);
        }else{
            nextPomoTitle.setText(getString(R.string.no_pomodoro_setup));
//            startPomoBtn.setVisibility(View.INVISIBLE);
        }

        if(AppHelper.getInstance().getLevel() != 0 && !AppHelper.getInstance().isProgramStarted() && sorted.size() < getMinSessionNumber()){
            AdviceDialog alert = new AdviceDialog();
            alert.showDialog(getMainActivity(), getString(R.string.you_have_not_set_enugh));
        }

        pomodoros = RealmController.getInstance().getFinishedPomodoros();
        taskFinished = RealmController.getInstance().getTaskFinishedPomodoros();
//        successfulPomodoros = RealmController.getInstance().getCompletedPomodoros(); //when stayed focused doesn-t count
        successfulPomodoros = RealmController.getInstance().getStayedFocusedPomodoros();


        if(AppHelper.getInstance().getLevel() == 0 &&
                (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY
                || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY
                || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY
                || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY)){
            AppHelper.getInstance().setLevel(1);
            AppHelper.getInstance().setIsTestTime(false);
            activity.setLevelImage();
            activity.setNameAndLevel();
        }

//        calculateAndShowProgressBasedOnLevels();
        calculateNewProgress();
        setAdviceOfLevel();
    }

//    @OnClick(R.id.startPomoBtn)
//    public void onStartPomodoroBtn(){
//        activity.startActivity(new Intent(activity, NotificationActivity.class).putExtra(Constants.POMODORO_ID, id).putExtra(Constants.DURATION, duration));
//    }

    private void calculateNewProgress(){
        switch (AppHelper.getInstance().getLevel()) {
            case 0:
                RealmResults<Pomodoro> donePomodoros = RealmController.getInstance().getDonePomodoros();

                ArrayList<Pomodoro>  taskFinished1 = new ArrayList<>();
                ArrayList<Pomodoro>  successfulPomodoros1 = new ArrayList<>();

                for(int i=0; i<donePomodoros.size(); i++){
                    if(donePomodoros.get(i).isTaskFinished()){
                        taskFinished1.add(donePomodoros.get(i));
                    }
                    if(donePomodoros.get(i).isStayedFocused()){ //getCompletedPmodoros
                        successfulPomodoros1.add(donePomodoros.get(i));
                    }
                }

                int maxPomodoro0 = donePomodoros.size();
                float num0 = (successfulPomodoros1.size() * 100.0f) / maxPomodoro0;
                int progress0 = Math.round(num0);
                thisWeekProgress.setProgress(progress0);
                pomodoroThisWeekTv.setText(getString(R.string.you_have_done_level_0, progress0 >100 ? 100 + "%" : progress0+"%"));

                int maxTask0 = donePomodoros.size();
                float tasksPer0 = (taskFinished1.size() * 100.0f) / maxTask0;
                int taskRound0 = Math.round(tasksPer0);
                thisWeekTasksProgress.setProgress(taskRound0);
                tasksThisWeekTv.setText(getString(R.string.you_have_done2_level_0, taskRound0 >100 ? 100 + "%" : taskRound0+"%"));

                break;
            case 1:
                int maxPomodoro = pomodoros.size();
                float num = (successfulPomodoros.size() * 100.0f) / maxPomodoro;
                int progress = Math.round(num);
                thisWeekProgress.setProgress(progress);
                pomodoroThisWeekTv.setText(getString(R.string.you_have_done, progress >100 ? 100 + "%" : progress+"%", "100%"));
//                pomodoroProgressTv.setText(progress+"%");
//                circularProgressBar.setProgress(progress);

                int maxTask = pomodoros.size();
                float tasksPer = (taskFinished.size() * 100.0f) / maxTask;
                int taskRound = Math.round(tasksPer);
                thisWeekTasksProgress.setProgress(taskRound);
                tasksThisWeekTv.setText(getString(R.string.you_have_done2, taskRound >100 ? 100 + "%" : taskRound+"%", "0%"));

                if(pomodoros.size() >= 1){
                    img1.setImageResource(R.drawable.half_full_pot);
                }else{
                    img1.setImageResource(R.drawable.empty_pot);
                }
                break;
            case 2:
                int maxPomodoro2 = pomodoros.size();
                float num2 = (successfulPomodoros.size() * 100.0f) / maxPomodoro2;
                int progress2 = Math.round(num2);
                thisWeekProgress.setProgress(progress2);
                pomodoroThisWeekTv.setText(getString(R.string.you_have_done, progress2 >100 ? 100 + "%" : progress2+"%", "87,5%"));
//                pomodoroProgressTv.setText(progress2+"%");
//                circularProgressBar.setProgress(progress2);

                int maxTask9 = pomodoros.size();
                float tasksPer9 = (taskFinished.size() * 100.0f) / maxTask9;
                int taskRound9 = Math.round(tasksPer9);
                thisWeekTasksProgress.setProgress(taskRound9);
                tasksThisWeekTv.setText(getString(R.string.you_have_done2, taskRound9 >100 ? 100 + "%" : taskRound9+"%", "0%"));

                img1.setImageResource(R.drawable.full_pot);
                if(pomodoros.size() >= 1){
                    img2.setImageResource(R.drawable.half_full_pot);
                }else{
                    img2.setImageResource(R.drawable.empty_pot);
                }
                break;
            case 3:
                int maxPomodoro3 = pomodoros.size();
                float num3 = (successfulPomodoros.size() * 100.0f) / maxPomodoro3;
                int progress3 = Math.round(num3);
                thisWeekProgress.setProgress(progress3);
                pomodoroThisWeekTv.setText(getString(R.string.you_have_done, progress3 >100 ? 100 + "%" : progress3+"%", "75%"));
//                pomodoroProgressTv.setText(progress3 >100 ? 100 + "%" : progress3+"%");
//                circularProgressBar.setProgress(progress3 >100 ? 100: progress3);

                int maxTask8 = pomodoros.size();
                float tasksPer8 = (taskFinished.size() * 100.0f) / maxTask8;
                int taskRound8 = Math.round(tasksPer8);
                thisWeekTasksProgress.setProgress(taskRound8);
                tasksThisWeekTv.setText(getString(R.string.you_have_done2, taskRound8 >100 ? 100 + "%" : taskRound8+"%", "12,5%"));

                img1.setImageResource(R.drawable.full_pot);
                img2.setImageResource(R.drawable.full_pot);
                if(pomodoros.size() >= 1){
                    img3.setImageResource(R.drawable.half_full_pot);
                }else{
                    img3.setImageResource(R.drawable.empty_pot);
                }
                break;
            case 4:
                int maxPomodoro4 = pomodoros.size();
                float num4 = (successfulPomodoros.size() * 100.0f) / maxPomodoro4;
                int progress4 = Math.round(num4);
                thisWeekProgress.setProgress(progress4);
                pomodoroThisWeekTv.setText(getString(R.string.you_have_done, progress4 >100 ? 100 + "%" : progress4+"%", "75%"));
//                pomodoroProgressTv.setText(progress4 >100 ? 100 + "%" : progress4+"%");
//                circularProgressBar.setProgress(progress4 >100 ? 100: progress4);

                int maxTask2 = pomodoros.size();
                float tasksPer2 = (taskFinished.size() * 100.0f) / maxTask2;
                int taskRound2 = Math.round(tasksPer2);
                thisWeekTasksProgress.setProgress(taskRound2);
                tasksThisWeekTv.setText(getString(R.string.you_have_done2, taskRound2 >100 ? 100 + "%" : taskRound2+"%", "25%"));

                img1.setImageResource(R.drawable.full_pot);
                img2.setImageResource(R.drawable.full_pot);
                img3.setImageResource(R.drawable.full_pot);
                if(pomodoros.size() >= 1){
                    img4.setImageResource(R.drawable.half_full_pot);
                }else{
                    img4.setImageResource(R.drawable.empty_pot);
                }
                break;
            case 5:
                int maxPomodoro5 = pomodoros.size();
                float num5 = (successfulPomodoros.size() * 100.0f) / maxPomodoro5;
                int progress5 = Math.round(num5);
                thisWeekProgress.setProgress(progress5);
                pomodoroThisWeekTv.setText(getString(R.string.you_have_done, progress5 >100 ? 100 + "%" : progress5+"%", "75%"));
//                pomodoroProgressTv.setText(progress5 >100 ? 100 + "%" : progress5+"%");
//                circularProgressBar.setProgress(progress5 >100 ? 100: progress5);

                int maxTask3 = pomodoros.size();
                float tasksPer3 = (taskFinished.size() * 100.0f) / maxTask3;
                int taskRound3 = Math.round(tasksPer3);
                thisWeekTasksProgress.setProgress(taskRound3);
                tasksThisWeekTv.setText(getString(R.string.you_have_done2, taskRound3 >100 ? 100 + "%" : taskRound3+"%", "50%"));


                img1.setImageResource(R.drawable.full_pot);
                img2.setImageResource(R.drawable.full_pot);
                img3.setImageResource(R.drawable.full_pot);
                img4.setImageResource(R.drawable.full_pot);
                if(pomodoros.size() >= 1){
                    img5.setImageResource(R.drawable.half_full_pot);
                }else{
                    img5.setImageResource(R.drawable.empty_pot);
                }
                break;
            case 6:
                int maxPomodoro6 = pomodoros.size();
                float num6 = (successfulPomodoros.size() * 100.0f) / maxPomodoro6;
                int progress6 = Math.round(num6);
                thisWeekProgress.setProgress(progress6);
                pomodoroThisWeekTv.setText(getString(R.string.you_have_done, progress6 >100 ? 100 + "%" : progress6+"%", "75%"));
//                pomodoroProgressTv.setText(progress6 >100 ? 100 + "%" : progress6+"%");
//                circularProgressBar.setProgress(progress6 >100 ? 100: progress6);

                int maxTask4 = pomodoros.size();
                float tasksPer4 = (taskFinished.size() * 100.0f) / maxTask4;
                int taskRound4 = Math.round(tasksPer4);
                thisWeekTasksProgress.setProgress(taskRound4);
                tasksThisWeekTv.setText(getString(R.string.you_have_done2, taskRound4 >100 ? 100 + "%" : taskRound4+"%", "50%"));

                img1.setImageResource(R.drawable.full_pot);
                img2.setImageResource(R.drawable.full_pot);
                img3.setImageResource(R.drawable.full_pot);
                img4.setImageResource(R.drawable.full_pot);
                img5.setImageResource(R.drawable.full_pot);
                if(pomodoros.size() >= 1){
                    img6.setImageResource(R.drawable.half_full_pot);
                }else{
                    img6.setImageResource(R.drawable.empty_pot);
                }
                break;
            case 7:
                int maxPomodoro7 = pomodoros.size();
                float num7 = (successfulPomodoros.size() * 100.0f) / maxPomodoro7;
                int progress7 = Math.round(num7);
                thisWeekProgress.setProgress(progress7);
                pomodoroThisWeekTv.setText(getString(R.string.you_have_done, progress7 >100 ? 100 + "%" : progress7+"%", "75%"));
//                pomodoroProgressTv.setText(progress7 >100 ? 100 + "%" : progress7+"%");
//                circularProgressBar.setProgress(progress7 >100 ? 100: progress7);

                int maxTask5 = pomodoros.size();
                float tasksPer5 = (taskFinished.size() * 100.0f) / maxTask5;
                int taskRound5 = Math.round(tasksPer5);
                thisWeekTasksProgress.setProgress(taskRound5);
                tasksThisWeekTv.setText(getString(R.string.you_have_done2, taskRound5 >100 ? 100 + "%" : taskRound5+"%", "72,5%"));

                img1.setImageResource(R.drawable.full_pot);
                img2.setImageResource(R.drawable.full_pot);
                img3.setImageResource(R.drawable.full_pot);
                img4.setImageResource(R.drawable.full_pot);
                img5.setImageResource(R.drawable.full_pot);
                img6.setImageResource(R.drawable.full_pot);
                if(pomodoros.size() >= 1){
                    img7.setImageResource(R.drawable.half_full_pot);
                }else{
                    img7.setImageResource(R.drawable.empty_pot);
                }
                break;
            case 8:
                int maxPomodoro8 = pomodoros.size();
                float num8 = (successfulPomodoros.size() * 100.0f) / maxPomodoro8;
                int progress8 = Math.round(num8);
                thisWeekProgress.setProgress(progress8);
                pomodoroThisWeekTv.setText(getString(R.string.you_have_done, progress8 >100 ? 100 + "%" : progress8+"%", "75%"));
//                pomodoroProgressTv.setText(progress8 >100 ? 100 + "%" : progress8+"%");
//                circularProgressBar.setProgress(progress8 >100 ? 100: progress8);

                int maxTask6 = pomodoros.size();
                float tasksPer6 = (taskFinished.size() * 100.0f) / maxTask6;
                int taskRound6 = Math.round(tasksPer6);
                thisWeekTasksProgress.setProgress(taskRound6);
                tasksThisWeekTv.setText(getString(R.string.you_have_done2, taskRound6 >100 ? 100 + "%" : taskRound6+"%", "75%"));

                img1.setImageResource(R.drawable.full_pot);
                img2.setImageResource(R.drawable.full_pot);
                img3.setImageResource(R.drawable.full_pot);
                img4.setImageResource(R.drawable.full_pot);
                img5.setImageResource(R.drawable.full_pot);
                img6.setImageResource(R.drawable.full_pot);
                img7.setImageResource(R.drawable.full_pot);
                if(pomodoros.size() >= 1){
                    img8.setImageResource(R.drawable.half_full_pot);
                }else{
                    img8.setImageResource(R.drawable.empty_pot);
                }
                if(pomodoros.size() >= 48){
                    img8.setImageResource(R.drawable.full_pot);
                }
                break;

        }
    }

    private void setAdviceOfLevel() {
        switch (AppHelper.getInstance().getLevel()) {
            case 0:
                adviceOfLevelTv.setText(getString(R.string.advice_of_level_0));
                break;
            case 1:
                adviceOfLevelTv.setText(getString(R.string.advice_of_level_1));
                break;
            case 2:
                adviceOfLevelTv.setText(getString(R.string.advice_of_level_2));
                break;
            case 3:
                adviceOfLevelTv.setText(getString(R.string.advice_of_level_3));
                break;
            case 4:
                adviceOfLevelTv.setText(getString(R.string.advice_of_level_4));
                break;
            case 5:
                adviceOfLevelTv.setText(getString(R.string.advice_of_level_5));
                break;
            case 6:
                adviceOfLevelTv.setText(getString(R.string.advice_of_level_6));
                break;
            case 7:
                adviceOfLevelTv.setText(getString(R.string.advice_of_level_7));
                break;
            case 8:
                adviceOfLevelTv.setText(getString(R.string.advice_of_level_8));
                break;
            default:
                adviceOfLevelTv.setText(getString(R.string.advice_of_level_1));
                break;
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


}
