package pomodoro.com.pomodoro.fragments.report;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import io.realm.RealmResults;
import pomodoro.com.pomodoro.MainActivity;
import pomodoro.com.pomodoro.R;
import pomodoro.com.pomodoro.fragments.BaseFragment;
import pomodoro.com.pomodoro.pojo.Pomodoro;
import pomodoro.com.pomodoro.realm.RealmController;
import pomodoro.com.pomodoro.util.AppHelper;

public class ReportSummaryFragment extends BaseFragment {

    @BindView(R.id.level_1_txt)
    TextView level_1_txt;
    @BindView(R.id.level_2_txt)
    TextView level_2_txt;
    @BindView(R.id.level_3_txt)
    TextView level_3_txt;
    @BindView(R.id.level_4_txt)
    TextView level_4_txt;
    @BindView(R.id.level_5_txt)
    TextView level_5_txt;
    @BindView(R.id.level_6_txt)
    TextView level_6_txt;
    @BindView(R.id.level_7_txt)
    TextView level_7_txt;
    @BindView(R.id.level_8_txt)
    TextView level_8_txt;

    @BindView(R.id.acc_pomo_1)
    TextView acc_pomo_1;
    @BindView(R.id.acc_pomo_2)
    TextView acc_pomo_2;
    @BindView(R.id.acc_pomo_3)
    TextView acc_pomo_3;
    @BindView(R.id.acc_pomo_4)
    TextView acc_pomo_4;
    @BindView(R.id.acc_pomo_5)
    TextView acc_pomo_5;
    @BindView(R.id.acc_pomo_6)
    TextView acc_pomo_6;
    @BindView(R.id.acc_pomo_7)
    TextView acc_pomo_7;
    @BindView(R.id.acc_pomo_8)
    TextView acc_pomo_8;

    @BindView(R.id.pomo_prog_1)
    ProgressBar pomo_prog_1;
    @BindView(R.id.pomo_prog_2)
    ProgressBar pomo_prog_2;
    @BindView(R.id.pomo_prog_3)
    ProgressBar pomo_prog_3;
    @BindView(R.id.pomo_prog_4)
    ProgressBar pomo_prog_4;
    @BindView(R.id.pomo_prog_5)
    ProgressBar pomo_prog_5;
    @BindView(R.id.pomo_prog_6)
    ProgressBar pomo_prog_6;
    @BindView(R.id.pomo_prog_7)
    ProgressBar pomo_prog_7;
    @BindView(R.id.pomo_prog_8)
    ProgressBar pomo_prog_8;

    @BindView(R.id.acc_tasks1)
    TextView acc_tasks1;
    @BindView(R.id.acc_tasks2)
    TextView acc_tasks2;
    @BindView(R.id.acc_tasks3)
    TextView acc_tasks3;
    @BindView(R.id.acc_tasks4)
    TextView acc_tasks4;
    @BindView(R.id.acc_tasks5)
    TextView acc_tasks5;
    @BindView(R.id.acc_tasks6)
    TextView acc_tasks6;
    @BindView(R.id.acc_tasks7)
    TextView acc_tasks7;
    @BindView(R.id.acc_tasks8)
    TextView acc_tasks8;

    @BindView(R.id.task_prog_1)
    ProgressBar task_prog_1;
    @BindView(R.id.task_prog_2)
    ProgressBar task_prog_2;
    @BindView(R.id.task_prog_3)
    ProgressBar task_prog_3;
    @BindView(R.id.task_prog_4)
    ProgressBar task_prog_4;
    @BindView(R.id.task_prog_5)
    ProgressBar task_prog_5;
    @BindView(R.id.task_prog_6)
    ProgressBar task_prog_6;
    @BindView(R.id.task_prog_7)
    ProgressBar task_prog_7;
    @BindView(R.id.task_prog_8)
    ProgressBar task_prog_8;

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

    MainActivity activity;
    RealmResults<Pomodoro> pomodoros;
    RealmResults<Pomodoro> taskFinished;
    RealmResults<Pomodoro> successfulPomodoros;

    @Override
    protected int setResourceId() {
        return R.layout.report_summary_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        activity = getMainActivity();

        pomodoros = RealmController.getInstance().getFinishedPomodoros();
        taskFinished = RealmController.getInstance().getTaskFinishedPomodoros();
        successfulPomodoros = RealmController.getInstance().getStayedFocusedPomodoros();

        switch (AppHelper.getInstance().getLevel()){
            case 0:
                level_2_txt.setText(getString(R.string.level_2_locked));
                level_3_txt.setText(getString(R.string.level_3_locked));
                level_4_txt.setText(getString(R.string.level_4_locked));
                level_5_txt.setText(getString(R.string.level_5_locked));
                level_6_txt.setText(getString(R.string.level_6_locked));
                level_7_txt.setText(getString(R.string.level_7_locked));
                level_8_txt.setText(getString(R.string.level_8_locked));

                container2.setVisibility(View.GONE);
                container3.setVisibility(View.GONE);
                container4.setVisibility(View.GONE);
                container5.setVisibility(View.GONE);
                container6.setVisibility(View.GONE);
                container7.setVisibility(View.GONE);
                container8.setVisibility(View.GONE);

                level_1_txt.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                int maxPomodoro0 =pomodoros.size();
                float num0 = (successfulPomodoros.size() * 100.0f) / maxPomodoro0;
                int progress0 = Math.round(num0);
                pomo_prog_1.setProgress(progress0);
                acc_pomo_1.setText(getString(R.string.your_accomplished_pomodoros, progress0 > 100 ? 100 + "%" : progress0+"%"));

                int maxTask0 = pomodoros.size();
                float tasksPer0 = (taskFinished.size() * 100.0f) / maxTask0;
                int taskRound0 = Math.round(tasksPer0);
                task_prog_1.setProgress(taskRound0);
                acc_tasks1.setText(getString(R.string.your_accomplished_tasks, taskRound0 > 100 ? 100 + "%" : taskRound0+"%"));

                break;
            case 1:
                level_2_txt.setText(getString(R.string.level_2_locked));
                level_3_txt.setText(getString(R.string.level_3_locked));
                level_4_txt.setText(getString(R.string.level_4_locked));
                level_5_txt.setText(getString(R.string.level_5_locked));
                level_6_txt.setText(getString(R.string.level_6_locked));
                level_7_txt.setText(getString(R.string.level_7_locked));
                level_8_txt.setText(getString(R.string.level_8_locked));

                container2.setVisibility(View.GONE);
                container3.setVisibility(View.GONE);
                container4.setVisibility(View.GONE);
                container5.setVisibility(View.GONE);
                container6.setVisibility(View.GONE);
                container7.setVisibility(View.GONE);
                container8.setVisibility(View.GONE);

                level_1_txt.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                int maxPomodoro =pomodoros.size();
                float num = (successfulPomodoros.size() * 100.0f) / maxPomodoro;
                int progress = Math.round(num);
                pomo_prog_1.setProgress(progress);
                acc_pomo_1.setText(getString(R.string.your_accomplished_pomodoros, progress > 100 ? 100 + "%" : progress+"%"));

                int maxTask = pomodoros.size();
                float tasksPer = (taskFinished.size() * 100.0f) / maxTask;
                int taskRound = Math.round(tasksPer);
                task_prog_1.setProgress(taskRound);
                acc_tasks1.setText(getString(R.string.your_accomplished_tasks, taskRound > 100 ? 100 + "%" : taskRound+"%"));

                break;
            case 2:
                level_3_txt.setText(getString(R.string.level_3_locked));
                level_4_txt.setText(getString(R.string.level_4_locked));
                level_5_txt.setText(getString(R.string.level_5_locked));
                level_6_txt.setText(getString(R.string.level_6_locked));
                level_7_txt.setText(getString(R.string.level_7_locked));
                level_8_txt.setText(getString(R.string.level_8_locked));

                container3.setVisibility(View.GONE);
                container4.setVisibility(View.GONE);
                container5.setVisibility(View.GONE);
                container6.setVisibility(View.GONE);
                container7.setVisibility(View.GONE);
                container8.setVisibility(View.GONE);


                level_2_txt.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                level_1_txt.setTextColor(getResources().getColor(R.color.blue));

                int maxPomodoro2 = pomodoros.size();
                float num2 = (successfulPomodoros.size() * 100.0f) / maxPomodoro2;
                int progress2 = Math.round(num2);
                pomo_prog_2.setProgress(progress2);
                acc_pomo_2.setText(getString(R.string.your_accomplished_pomodoros, progress2 > 100 ? 100 + "%" : progress2+"%"));

                int maxTask2 = pomodoros.size();
                float tasksPer2 = (taskFinished.size() * 100.0f) / maxTask2;
                int taskRound2 = Math.round(tasksPer2);
                task_prog_2.setProgress(taskRound2);
                acc_tasks2.setText(getString(R.string.your_accomplished_tasks, taskRound2 > 100 ? 100 + "%" : taskRound2+"%"));

                pomo_prog_1.setProgress(AppHelper.getInstance().getLevel1Pomo());
                acc_pomo_1.setText(getString(R.string.your_accomplished_pomodoros, AppHelper.getInstance().getLevel1Pomo() + "%"));
                task_prog_1.setProgress(AppHelper.getInstance().getLevel1Tasks());
                acc_tasks1.setText(getString(R.string.your_accomplished_tasks, AppHelper.getInstance().getLevel1Tasks() + "%"));

                break;
            case 3:
                level_4_txt.setText(getString(R.string.level_4_locked));
                level_5_txt.setText(getString(R.string.level_5_locked));
                level_6_txt.setText(getString(R.string.level_6_locked));
                level_7_txt.setText(getString(R.string.level_7_locked));
                level_8_txt.setText(getString(R.string.level_8_locked));

                container4.setVisibility(View.GONE);
                container5.setVisibility(View.GONE);
                container6.setVisibility(View.GONE);
                container7.setVisibility(View.GONE);
                container8.setVisibility(View.GONE);

                level_3_txt.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                level_1_txt.setTextColor(getResources().getColor(R.color.blue));
                level_2_txt.setTextColor(getResources().getColor(R.color.blue));

                int maxPomodoro3 = pomodoros.size();
                float num3 = (successfulPomodoros.size() * 100.0f) / maxPomodoro3;
                int progress3 = Math.round(num3);
                pomo_prog_3.setProgress(progress3);
                acc_pomo_3.setText(getString(R.string.your_accomplished_pomodoros, progress3 > 100 ? 100 + "%" : progress3+"%"));

                int maxTask3 = pomodoros.size();
                float tasksPer3 = (taskFinished.size() * 100.0f) / maxTask3;
                int taskRound3 = Math.round(tasksPer3);
                task_prog_3.setProgress(taskRound3);
                acc_tasks3.setText(getString(R.string.your_accomplished_tasks, taskRound3 > 100 ? 100 + "%" : taskRound3+"%"));

                pomo_prog_1.setProgress(AppHelper.getInstance().getLevel1Pomo());
                acc_pomo_1.setText(getString(R.string.your_accomplished_pomodoros, AppHelper.getInstance().getLevel1Pomo() + "%"));
                task_prog_1.setProgress(AppHelper.getInstance().getLevel1Tasks());
                acc_tasks1.setText(getString(R.string.your_accomplished_tasks, AppHelper.getInstance().getLevel1Tasks() + "%"));

                pomo_prog_2.setProgress(AppHelper.getInstance().getLevel2Pomo());
                acc_pomo_2.setText(getString(R.string.your_accomplished_pomodoros, AppHelper.getInstance().getLevel2Pomo() + "%"));
                task_prog_2.setProgress(AppHelper.getInstance().getLevel2Tasks());
                acc_tasks2.setText(getString(R.string.your_accomplished_tasks, AppHelper.getInstance().getLevel2Tasks() + "%"));

                break;
            case 4:
                level_5_txt.setText(getString(R.string.level_5_locked));
                level_6_txt.setText(getString(R.string.level_6_locked));
                level_7_txt.setText(getString(R.string.level_7_locked));
                level_8_txt.setText(getString(R.string.level_8_locked));

                container5.setVisibility(View.GONE);
                container6.setVisibility(View.GONE);
                container7.setVisibility(View.GONE);
                container8.setVisibility(View.GONE);

                level_4_txt.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                level_1_txt.setTextColor(getResources().getColor(R.color.blue));
                level_2_txt.setTextColor(getResources().getColor(R.color.blue));
                level_3_txt.setTextColor(getResources().getColor(R.color.blue));

                int maxPomodoro4 = pomodoros.size();
                float num4 = (successfulPomodoros.size() * 100.0f) / maxPomodoro4;
                int progress4 = Math.round(num4);
                pomo_prog_4.setProgress(progress4);
                acc_pomo_4.setText(getString(R.string.your_accomplished_pomodoros, progress4 > 100 ? 100 + "%" : progress4+"%"));

                int maxTask4 = pomodoros.size();
                float tasksPer4 = (taskFinished.size() * 100.0f) / maxTask4;
                int taskRound4 = Math.round(tasksPer4);
                task_prog_4.setProgress(taskRound4);
                acc_tasks4.setText(getString(R.string.your_accomplished_tasks, taskRound4 > 100 ? 100 + "%" : taskRound4+"%"));

                pomo_prog_1.setProgress(AppHelper.getInstance().getLevel1Pomo());
                acc_pomo_1.setText(getString(R.string.your_accomplished_pomodoros, AppHelper.getInstance().getLevel1Pomo() + "%"));
                task_prog_1.setProgress(AppHelper.getInstance().getLevel1Tasks());
                acc_tasks1.setText(getString(R.string.your_accomplished_tasks, AppHelper.getInstance().getLevel1Tasks() + "%"));

                pomo_prog_2.setProgress(AppHelper.getInstance().getLevel2Pomo());
                acc_pomo_2.setText(getString(R.string.your_accomplished_pomodoros, AppHelper.getInstance().getLevel2Pomo() + "%"));
                task_prog_2.setProgress(AppHelper.getInstance().getLevel2Tasks());
                acc_tasks2.setText(getString(R.string.your_accomplished_tasks, AppHelper.getInstance().getLevel2Tasks() + "%"));

                pomo_prog_3.setProgress(AppHelper.getInstance().getLevel3Pomo());
                acc_pomo_3.setText(getString(R.string.your_accomplished_pomodoros, AppHelper.getInstance().getLevel3Pomo() + "%"));
                task_prog_3.setProgress(AppHelper.getInstance().getLevel3Tasks());
                acc_tasks3.setText(getString(R.string.your_accomplished_tasks, AppHelper.getInstance().getLevel3Tasks() + "%"));

                break;
            case 5:
                level_6_txt.setText(getString(R.string.level_6_locked));
                level_7_txt.setText(getString(R.string.level_7_locked));
                level_8_txt.setText(getString(R.string.level_8_locked));

                container6.setVisibility(View.GONE);
                container7.setVisibility(View.GONE);
                container8.setVisibility(View.GONE);

                level_5_txt.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                level_1_txt.setTextColor(getResources().getColor(R.color.blue));
                level_2_txt.setTextColor(getResources().getColor(R.color.blue));
                level_3_txt.setTextColor(getResources().getColor(R.color.blue));
                level_4_txt.setTextColor(getResources().getColor(R.color.blue));

                int maxPomodoro5 = pomodoros.size();
                float num5 = (successfulPomodoros.size() * 100.0f) / maxPomodoro5;
                int progress5 = Math.round(num5);
                pomo_prog_5.setProgress(progress5);
                acc_pomo_5.setText(getString(R.string.your_accomplished_pomodoros, progress5 > 100 ? 100 + "%" : progress5+"%"));

                int maxTask5 = pomodoros.size();
                float tasksPer5 = (taskFinished.size() * 100.0f) / maxTask5;
                int taskRound5 = Math.round(tasksPer5);
                task_prog_5.setProgress(taskRound5);
                acc_tasks5.setText(getString(R.string.your_accomplished_tasks, taskRound5 > 100 ? 100 + "%" : taskRound5+"%"));

                pomo_prog_1.setProgress(AppHelper.getInstance().getLevel1Pomo());
                acc_pomo_1.setText(getString(R.string.your_accomplished_pomodoros, AppHelper.getInstance().getLevel1Pomo() + "%"));
                task_prog_1.setProgress(AppHelper.getInstance().getLevel1Tasks());
                acc_tasks1.setText(getString(R.string.your_accomplished_tasks, AppHelper.getInstance().getLevel1Tasks() + "%"));

                pomo_prog_2.setProgress(AppHelper.getInstance().getLevel2Pomo());
                acc_pomo_2.setText(getString(R.string.your_accomplished_pomodoros, AppHelper.getInstance().getLevel2Pomo() + "%"));
                task_prog_2.setProgress(AppHelper.getInstance().getLevel2Tasks());
                acc_tasks2.setText(getString(R.string.your_accomplished_tasks, AppHelper.getInstance().getLevel2Tasks() + "%"));

                pomo_prog_3.setProgress(AppHelper.getInstance().getLevel3Pomo());
                acc_pomo_3.setText(getString(R.string.your_accomplished_pomodoros, AppHelper.getInstance().getLevel3Pomo() + "%"));
                task_prog_3.setProgress(AppHelper.getInstance().getLevel3Tasks());
                acc_tasks3.setText(getString(R.string.your_accomplished_tasks, AppHelper.getInstance().getLevel3Tasks() + "%"));

                pomo_prog_4.setProgress(AppHelper.getInstance().getLevel4Pomo());
                acc_pomo_4.setText(getString(R.string.your_accomplished_pomodoros, AppHelper.getInstance().getLevel4Pomo() + "%"));
                task_prog_4.setProgress(AppHelper.getInstance().getLevel4Tasks());
                acc_tasks4.setText(getString(R.string.your_accomplished_tasks, AppHelper.getInstance().getLevel4Tasks() + "%"));

                break;
            case 6:
                level_7_txt.setText(getString(R.string.level_7_locked));
                level_8_txt.setText(getString(R.string.level_8_locked));

                container7.setVisibility(View.GONE);
                container8.setVisibility(View.GONE);

                level_6_txt.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                level_1_txt.setTextColor(getResources().getColor(R.color.blue));
                level_2_txt.setTextColor(getResources().getColor(R.color.blue));
                level_3_txt.setTextColor(getResources().getColor(R.color.blue));
                level_4_txt.setTextColor(getResources().getColor(R.color.blue));
                level_5_txt.setTextColor(getResources().getColor(R.color.blue));

                int maxPomodoro6 = pomodoros.size();
                float num6 = (successfulPomodoros.size() * 100.0f) / maxPomodoro6;
                int progress6 = Math.round(num6);
                pomo_prog_6.setProgress(progress6);
                acc_pomo_6.setText(getString(R.string.your_accomplished_pomodoros, progress6 > 100 ? 100 + "%" : progress6+"%"));

                int maxTask6 = pomodoros.size();
                float tasksPer6 = (taskFinished.size() * 100.0f) / maxTask6;
                int taskRound6 = Math.round(tasksPer6);
                task_prog_6.setProgress(taskRound6);
                acc_tasks6.setText(getString(R.string.your_accomplished_tasks, taskRound6 > 100 ? 100 + "%" : taskRound6+"%"));

                pomo_prog_1.setProgress(AppHelper.getInstance().getLevel1Pomo());
                acc_pomo_1.setText(getString(R.string.your_accomplished_pomodoros, AppHelper.getInstance().getLevel1Pomo() + "%"));
                task_prog_1.setProgress(AppHelper.getInstance().getLevel1Tasks());
                acc_tasks1.setText(getString(R.string.your_accomplished_tasks, AppHelper.getInstance().getLevel1Tasks() + "%"));

                pomo_prog_2.setProgress(AppHelper.getInstance().getLevel2Pomo());
                acc_pomo_2.setText(getString(R.string.your_accomplished_pomodoros, AppHelper.getInstance().getLevel2Pomo() + "%"));
                task_prog_2.setProgress(AppHelper.getInstance().getLevel2Tasks());
                acc_tasks2.setText(getString(R.string.your_accomplished_tasks, AppHelper.getInstance().getLevel2Tasks() + "%"));

                pomo_prog_3.setProgress(AppHelper.getInstance().getLevel3Pomo());
                acc_pomo_3.setText(getString(R.string.your_accomplished_pomodoros, AppHelper.getInstance().getLevel3Pomo() + "%"));
                task_prog_3.setProgress(AppHelper.getInstance().getLevel3Tasks());
                acc_tasks3.setText(getString(R.string.your_accomplished_tasks, AppHelper.getInstance().getLevel3Tasks() + "%"));

                pomo_prog_4.setProgress(AppHelper.getInstance().getLevel4Pomo());
                acc_pomo_4.setText(getString(R.string.your_accomplished_pomodoros, AppHelper.getInstance().getLevel4Pomo() + "%"));
                task_prog_4.setProgress(AppHelper.getInstance().getLevel4Tasks());
                acc_tasks4.setText(getString(R.string.your_accomplished_tasks, AppHelper.getInstance().getLevel4Tasks() + "%"));

                pomo_prog_5.setProgress(AppHelper.getInstance().getLevel5Pomo());
                acc_pomo_5.setText(getString(R.string.your_accomplished_pomodoros, AppHelper.getInstance().getLevel5Pomo() + "%"));
                task_prog_5.setProgress(AppHelper.getInstance().getLevel5Tasks());
                acc_tasks5.setText(getString(R.string.your_accomplished_pomodoros, AppHelper.getInstance().getLevel5Tasks() + "%"));

                break;
            case 7:
                level_8_txt.setText(getString(R.string.level_8_locked));

                container8.setVisibility(View.GONE);

                level_7_txt.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                level_1_txt.setTextColor(getResources().getColor(R.color.blue));
                level_2_txt.setTextColor(getResources().getColor(R.color.blue));
                level_3_txt.setTextColor(getResources().getColor(R.color.blue));
                level_4_txt.setTextColor(getResources().getColor(R.color.blue));
                level_5_txt.setTextColor(getResources().getColor(R.color.blue));
                level_6_txt.setTextColor(getResources().getColor(R.color.blue));

                int maxPomodoro7 = pomodoros.size();
                float num7 = (successfulPomodoros.size() * 100.0f) / maxPomodoro7;
                int progress7 = Math.round(num7);
                pomo_prog_7.setProgress(progress7);
                acc_pomo_7.setText(getString(R.string.your_accomplished_pomodoros, progress7 > 100 ? 100 + "%" : progress7+"%"));

                int maxTask7 = pomodoros.size();
                float tasksPer7 = (taskFinished.size() * 100.0f) / maxTask7;
                int taskRound7 = Math.round(tasksPer7);
                task_prog_7.setProgress(taskRound7);
                acc_tasks7.setText(getString(R.string.your_accomplished_tasks, taskRound7 > 100 ? 100 + "%" : taskRound7+"%"));

                pomo_prog_1.setProgress(AppHelper.getInstance().getLevel1Pomo());
                acc_pomo_1.setText(getString(R.string.your_accomplished_pomodoros, AppHelper.getInstance().getLevel1Pomo() + "%"));
                task_prog_1.setProgress(AppHelper.getInstance().getLevel1Tasks());
                acc_tasks1.setText(getString(R.string.your_accomplished_tasks, AppHelper.getInstance().getLevel1Tasks() + "%"));

                pomo_prog_2.setProgress(AppHelper.getInstance().getLevel2Pomo());
                acc_pomo_2.setText(getString(R.string.your_accomplished_pomodoros, AppHelper.getInstance().getLevel2Pomo() + "%"));
                task_prog_2.setProgress(AppHelper.getInstance().getLevel2Tasks());
                acc_tasks2.setText(getString(R.string.your_accomplished_tasks, AppHelper.getInstance().getLevel2Tasks() + "%"));

                pomo_prog_3.setProgress(AppHelper.getInstance().getLevel3Pomo());
                acc_pomo_3.setText(getString(R.string.your_accomplished_pomodoros, AppHelper.getInstance().getLevel3Pomo() + "%"));
                task_prog_3.setProgress(AppHelper.getInstance().getLevel3Tasks());
                acc_tasks3.setText(getString(R.string.your_accomplished_tasks, AppHelper.getInstance().getLevel3Tasks() + "%"));

                pomo_prog_4.setProgress(AppHelper.getInstance().getLevel4Pomo());
                acc_pomo_4.setText(getString(R.string.your_accomplished_pomodoros, AppHelper.getInstance().getLevel4Pomo() + "%"));
                task_prog_4.setProgress(AppHelper.getInstance().getLevel4Tasks());
                acc_tasks4.setText(getString(R.string.your_accomplished_tasks, AppHelper.getInstance().getLevel4Tasks() + "%"));

                pomo_prog_5.setProgress(AppHelper.getInstance().getLevel5Pomo());
                acc_pomo_5.setText(getString(R.string.your_accomplished_pomodoros, AppHelper.getInstance().getLevel5Pomo() + "%"));
                task_prog_5.setProgress(AppHelper.getInstance().getLevel5Tasks());
                acc_tasks5.setText(getString(R.string.your_accomplished_tasks, AppHelper.getInstance().getLevel5Tasks() + "%"));

                pomo_prog_6.setProgress(AppHelper.getInstance().getLevel6Pomo());
                acc_pomo_6.setText(getString(R.string.your_accomplished_pomodoros, AppHelper.getInstance().getLevel6Pomo() + "%"));
                task_prog_6.setProgress(AppHelper.getInstance().getLevel6Tasks());
                acc_tasks6.setText(getString(R.string.your_accomplished_tasks, AppHelper.getInstance().getLevel6Tasks() + "%"));

                break;
            case 8:
                level_8_txt.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                level_1_txt.setTextColor(getResources().getColor(R.color.blue));
                level_2_txt.setTextColor(getResources().getColor(R.color.blue));
                level_3_txt.setTextColor(getResources().getColor(R.color.blue));
                level_4_txt.setTextColor(getResources().getColor(R.color.blue));
                level_5_txt.setTextColor(getResources().getColor(R.color.blue));
                level_6_txt.setTextColor(getResources().getColor(R.color.blue));
                level_7_txt.setTextColor(getResources().getColor(R.color.blue));

                int maxPomodoro8 = pomodoros.size();
                float num8 = (successfulPomodoros.size() * 100.0f) / maxPomodoro8;
                int progress8 = Math.round(num8);
                pomo_prog_8.setProgress(progress8);
                acc_pomo_8.setText(getString(R.string.your_accomplished_pomodoros, progress8 > 100 ? 100 + "%" : progress8+"%"));

                int maxTask8 = pomodoros.size();
                float tasksPer8 = (taskFinished.size() * 100.0f) / maxTask8;
                int taskRound8 = Math.round(tasksPer8);
                task_prog_8.setProgress(taskRound8);
                acc_tasks8.setText(getString(R.string.your_accomplished_tasks, taskRound8 > 100 ? 100 + "%" : taskRound8+"%"));

                pomo_prog_1.setProgress(AppHelper.getInstance().getLevel1Pomo());
                acc_pomo_1.setText(getString(R.string.your_accomplished_pomodoros, AppHelper.getInstance().getLevel1Pomo() + "%"));
                task_prog_1.setProgress(AppHelper.getInstance().getLevel1Tasks());
                acc_tasks1.setText(getString(R.string.your_accomplished_tasks, AppHelper.getInstance().getLevel1Tasks() + "%"));

                pomo_prog_2.setProgress(AppHelper.getInstance().getLevel2Pomo());
                acc_pomo_2.setText(getString(R.string.your_accomplished_pomodoros, AppHelper.getInstance().getLevel2Pomo() + "%"));
                task_prog_2.setProgress(AppHelper.getInstance().getLevel2Tasks());
                acc_tasks2.setText(getString(R.string.your_accomplished_tasks, AppHelper.getInstance().getLevel2Tasks() + "%"));

                pomo_prog_3.setProgress(AppHelper.getInstance().getLevel3Pomo());
                acc_pomo_3.setText(getString(R.string.your_accomplished_pomodoros, AppHelper.getInstance().getLevel3Pomo() + "%"));
                task_prog_3.setProgress(AppHelper.getInstance().getLevel3Tasks());
                acc_tasks3.setText(getString(R.string.your_accomplished_tasks, AppHelper.getInstance().getLevel3Tasks() + "%"));

                pomo_prog_4.setProgress(AppHelper.getInstance().getLevel4Pomo());
                acc_pomo_4.setText(getString(R.string.your_accomplished_pomodoros, AppHelper.getInstance().getLevel4Pomo() + "%"));
                task_prog_4.setProgress(AppHelper.getInstance().getLevel4Tasks());
                acc_tasks4.setText(getString(R.string.your_accomplished_tasks, AppHelper.getInstance().getLevel4Tasks() + "%"));

                pomo_prog_5.setProgress(AppHelper.getInstance().getLevel5Pomo());
                acc_pomo_5.setText(getString(R.string.your_accomplished_pomodoros, AppHelper.getInstance().getLevel5Pomo() + "%"));
                task_prog_5.setProgress(AppHelper.getInstance().getLevel5Tasks());
                acc_tasks5.setText(getString(R.string.your_accomplished_tasks, AppHelper.getInstance().getLevel5Tasks() + "%"));

                pomo_prog_6.setProgress(AppHelper.getInstance().getLevel6Pomo());
                acc_pomo_6.setText(getString(R.string.your_accomplished_pomodoros, AppHelper.getInstance().getLevel6Pomo() + "%"));
                task_prog_6.setProgress(AppHelper.getInstance().getLevel6Tasks());
                acc_tasks6.setText(getString(R.string.your_accomplished_tasks, AppHelper.getInstance().getLevel6Tasks() + "%"));

                pomo_prog_7.setProgress(AppHelper.getInstance().getLevel7Pomo());
                acc_pomo_7.setText(getString(R.string.your_accomplished_pomodoros, AppHelper.getInstance().getLevel7Pomo() + "%"));
                task_prog_7.setProgress(AppHelper.getInstance().getLevel7Tasks());
                acc_tasks7.setText(getString(R.string.your_accomplished_tasks, AppHelper.getInstance().getLevel7Tasks() + "%"));

                break;
        }
    }
}
