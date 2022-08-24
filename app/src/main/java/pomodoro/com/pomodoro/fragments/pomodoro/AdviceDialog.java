package pomodoro.com.pomodoro.fragments.pomodoro;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import pomodoro.com.pomodoro.MainActivity;
import pomodoro.com.pomodoro.NotificationActivity;
import pomodoro.com.pomodoro.R;
import pomodoro.com.pomodoro.pojo.Pomodoro;
import pomodoro.com.pomodoro.realm.RealmController;
import pomodoro.com.pomodoro.util.AppHelper;

public class AdviceDialog{

    Realm realm;
    NotificationActivity activity;

//    StartPodmodoroFragment fragment;

    public void showDialog(final NotificationActivity activity, String msg, final boolean isAdvice){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.advice_dialog);
        this.activity = activity;
        this.realm = RealmController.getInstance().getRealm();
//        this.fragment = fragment;

        TextView text = (TextView) dialog.findViewById(R.id.label5);
        TextView title = (TextView) dialog.findViewById(R.id.label4);

        if(isAdvice){
            title.setText(activity.getString(R.string.advice));
        }else{
            title.setText(activity.getString(R.string.result));
        }

        text.setText(msg);

        Button dialogButton = (Button) dialog.findViewById(R.id.doneBtn);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isAdvice){
                    activity.startActivity(new Intent(activity, MainActivity.class));
                    dialog.dismiss();
                }else{
                    dialog.dismiss();
                    activity.calculateLevel();
                }
            }
        });

        dialog.show();
        if(!isAdvice) {
            Window window = dialog.getWindow();
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        }

    }

    public void showDialog(final Activity activity, String msg, String message){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.advice_dialog);
//        this.activity = activity;
//        this.realm = RealmController.getInstance().getRealm();
//        this.fragment = fragment;

        TextView text = (TextView) dialog.findViewById(R.id.label5);
        TextView title = (TextView) dialog.findViewById(R.id.label4);

//        if(isAdvice){
            title.setText(message);
//        }

        text.setText(msg);

        Button dialogButton = (Button) dialog.findViewById(R.id.doneBtn);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               AppHelper.getInstance().setIsTestMessageShowed(true);
               dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void showDialog(final MainActivity activity, String msg){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.advice_dialog);
//        this.activity = activity;
//        this.realm = RealmController.getInstance().getRealm();
//        this.fragment = fragment;

        TextView text = (TextView) dialog.findViewById(R.id.label5);
//        TextView title = (TextView) dialog.findViewById(R.id.label4);

//        if(isAdvice){
//        title.setText(message);
//        }

        text.setText(msg);

        Button dialogButton = (Button) dialog.findViewById(R.id.doneBtn);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void showDialog(final NotificationActivity activity, String msg, final boolean isAdvice, int level){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.advice_dialog);
        this.activity = activity;
        this.realm = RealmController.getInstance().getRealm();
//        this.fragment = fragment;

        TextView text = (TextView) dialog.findViewById(R.id.label5);
        TextView title = (TextView) dialog.findViewById(R.id.label4);
        ImageView image = (ImageView) dialog.findViewById(R.id.levelImage);

        if(isAdvice){
            title.setText(activity.getString(R.string.advice));
        }else{
            title.setText(activity.getString(R.string.result));
        }

        text.setText(msg);

        image.setVisibility(View.VISIBLE);
        setLevelImage(image);

        Button dialogButton = (Button) dialog.findViewById(R.id.doneBtn);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isAdvice){
                    activity.startActivity(new Intent(activity, MainActivity.class));
                    dialog.dismiss();
                }else{
                    dialog.dismiss();
                    activity.calculateLevel();
                }
            }
        });

        dialog.show();
        if(!isAdvice) {
            Window window = dialog.getWindow();
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        }

    }

    public void setLevelImage(ImageView levelIv){
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
}
