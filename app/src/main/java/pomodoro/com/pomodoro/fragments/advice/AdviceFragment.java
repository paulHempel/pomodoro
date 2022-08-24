package pomodoro.com.pomodoro.fragments.advice;

import android.os.Bundle;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import pomodoro.com.pomodoro.MainActivity;
import pomodoro.com.pomodoro.R;
import pomodoro.com.pomodoro.adapters.ExpandableListAdapter;
import pomodoro.com.pomodoro.fragments.BaseFragment;

public class AdviceFragment extends BaseFragment {

    @BindView(R.id.lvExp)
    ExpandableListView expListView;

    ExpandableListAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    MainActivity activity;

    @Override
    protected int setResourceId() {
        return R.layout.advice_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
// get the listview
        activity = getMainActivity();
        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(activity, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add(getString(R.string.this_app));
        listDataHeader.add(getString(R.string.procrastination));
        listDataHeader.add(getString(R.string.pomodoro));
        listDataHeader.add(getString(R.string.habits));

        listDataHeader.add(getString(R.string.your_future_self));
        listDataHeader.add(getString(R.string.meditation));
        listDataHeader.add(getString(R.string.sleep));
        listDataHeader.add(getString(R.string.failure));
        listDataHeader.add(getString(R.string.nature));
        listDataHeader.add(getString(R.string.nutrition));
        listDataHeader.add(getString(R.string.use_your_time));
        listDataHeader.add(getString(R.string.work_together));
        listDataHeader.add(getString(R.string.references));

        listDataChild.put(listDataHeader.get(0), new ArrayList<String>() {{ add(getString(R.string.this_app_text));}}); // Header, Child data
        listDataChild.put(listDataHeader.get(1), new ArrayList<String>() {{ add(getString(R.string.procrastination_text));}}); // Header, Child data
        listDataChild.put(listDataHeader.get(2), new ArrayList<String>() {{ add(getString(R.string.pomodoro_text));}}); // Header, Child data
        listDataChild.put(listDataHeader.get(3), new ArrayList<String>() {{ add(getString(R.string.habits_text));}}); // Header, Child data
        listDataChild.put(listDataHeader.get(4), new ArrayList<String>() {{ add(getString(R.string.your_future_self_text));}}); // Header, Child data
        listDataChild.put(listDataHeader.get(5), new ArrayList<String>() {{ add(getString(R.string.meditation_text));}}); // Header, Child data
        listDataChild.put(listDataHeader.get(6), new ArrayList<String>() {{ add(getString(R.string.sleep_text));}}); // Header, Child data
        listDataChild.put(listDataHeader.get(7), new ArrayList<String>() {{ add(getString(R.string.failure_text));}}); // Header, Child data
        listDataChild.put(listDataHeader.get(8), new ArrayList<String>() {{ add(getString(R.string.nature_text));}}); // Header, Child data
        listDataChild.put(listDataHeader.get(9), new ArrayList<String>() {{ add(getString(R.string.nutrition_text));}}); // Header, Child data
        listDataChild.put(listDataHeader.get(10), new ArrayList<String>() {{ add(getString(R.string.use_your_time_text));}}); // Header, Child data
        listDataChild.put(listDataHeader.get(11), new ArrayList<String>() {{ add(getString(R.string.work_together_text));}}); // Header, Child data
        listDataChild.put(listDataHeader.get(12), new ArrayList<String>() {{ add(getString(R.string.references_text));}}); // Header, Child data

    }

}
