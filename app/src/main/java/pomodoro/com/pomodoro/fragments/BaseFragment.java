package pomodoro.com.pomodoro.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import pomodoro.com.pomodoro.MainActivity;
import pomodoro.com.pomodoro.NotificationActivity;

/**
 * Created by ema on 10/25/16.
 */

public abstract class BaseFragment extends Fragment {

    abstract protected int setResourceId();
    abstract protected void init(Bundle savedInstanceState);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(setResourceId(), container, false);

        ButterKnife.bind(this, view);

        init(savedInstanceState);

        return view;
    }

    public MainActivity getMainActivity(){
        return (MainActivity) getActivity();
    }

    public NotificationActivity getNotificationActivity(){
        return (NotificationActivity) getActivity();
    }

}
