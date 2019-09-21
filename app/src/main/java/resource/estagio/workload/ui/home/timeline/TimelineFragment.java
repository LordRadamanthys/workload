package resource.estagio.workload.ui.home.timeline;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import resource.estagio.workload.R;
import resource.estagio.workload.data.remote.model.TimeEntryModel;
import resource.estagio.workload.infra.App;
import resource.estagio.workload.infra.ConstantApp;
import resource.estagio.workload.ui.DialogApp;
import resource.estagio.workload.ui.home.HomeContract;
import resource.estagio.workload.ui.home.timeline.adapterTimeLine.AdapterTimeline;

public class TimelineFragment extends Fragment implements TimeLineContract.View {

    public static final String MMM = "MMM";
    private TextView textViewMonth;
    private TextView textViewYear;
    private ImageView imageViewMonthLeft;
    private ImageView imageViewMonthRight;
    private RecyclerView recyclerViewTimeline;
    private SwipeRefreshLayout swipeRefreshTimeline;
    private View view;
    private AdapterTimeline adapterTimeline;
    private List<TimeEntryModel> listWorkLoad;
    private Calendar calendar;
    private TimeLineContract.Presenter presenter;
    private int month;
    private int year;
    private HomeContract.View viewHome;
    private AdapterTimeline.AdapterTimelineInterface listener;
    private DialogApp.DialogListener listenerDialog;
    private AdapterTimeline.AdapterUpdateTimelineInterface updateTimelineListener = null;

    public TimelineFragment(HomeContract.View view) {
        this.viewHome = view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate (R.layout.fragment_timeline, container, false);

        loadUI ();
        actionAdminRemove();
        setTextDate ();
        final int thisMonth = calendar.get (Calendar.MONTH);
        actionArrowsButtons (thisMonth);
        actioSwipeRefresh (this);

        return view;
    }

    private void actionArrowsButtons(int thisMonth) {
        imageViewMonthLeft.setOnClickListener (v -> {

            if (month >= thisMonth - 2) {
                calendar.add (Calendar.MONTH, -1);
                setTextDate ();
                imageViewMonthLeft.setVisibility (View.VISIBLE);
                imageViewMonthRight.setVisibility (View.VISIBLE);
            } else {
                imageViewMonthLeft.setVisibility (View.INVISIBLE);
            }
            if (month == thisMonth - 6) {
                imageViewMonthLeft.setVisibility (View.INVISIBLE);
            }
        });

        imageViewMonthRight.setOnClickListener (v -> {

            if (month <= thisMonth) {
                calendar.add (Calendar.MONTH, +1);
                setTextDate ();
                imageViewMonthRight.setVisibility (View.VISIBLE);
                imageViewMonthLeft.setVisibility (View.VISIBLE);
            } else {
                imageViewMonthRight.setVisibility (View.INVISIBLE);
            }
            if (month == thisMonth + 1) {
                imageViewMonthRight.setVisibility (View.INVISIBLE);
            }
        });
    }


    private void actioSwipeRefresh(TimeLineContract.View view) {
        recyclerViewTimeline.setVisibility (View.GONE);

        swipeRefreshTimeline.setOnRefreshListener (() -> {
            presenter = new TimeLinePresenter (view);
            presenter.getTimeline (month, year);
        });
    }


    @SuppressLint("SimpleDateFormat")
    private void setTextDate() {
        swipeRefreshTimeline = view.findViewById (R.id.swipe_Refresh_timeline);
        textViewMonth.setText (new SimpleDateFormat (MMM).format (calendar.getTime ()));
        textViewYear.setText (String.valueOf (calendar.get (Calendar.YEAR)));

        month = calendar.get (Calendar.MONTH) + 1;
        year = calendar.get (Calendar.YEAR);

        presenter.getTimeline (month, year);
    }

    private void loadUI() {
        calendar = Calendar.getInstance ();
        presenter = new TimeLinePresenter (this);
        recyclerViewTimeline = view.findViewById (R.id.id_recyclerview_timeline);
        textViewMonth = view.findViewById (R.id.text_view_month_timeline);
        textViewYear = view.findViewById (R.id.text_view_year_timeline);
        imageViewMonthLeft = view.findViewById (R.id.image_view_month_left_timeline);
        imageViewMonthRight = view.findViewById (R.id.image_view_month_right_timeline);
        imageViewMonthRight.setVisibility (View.INVISIBLE);
    }


    @Override
    public void showRecycler(boolean Key) {
        recyclerViewTimeline.setVisibility (Key ? View.INVISIBLE : View.VISIBLE);
        swipeRefreshTimeline.setRefreshing (Key);
        viewHome.enableNavigation (Key);
    }


    @Override
    public void showListTimeline(List<TimeEntryModel> list) {
        listWorkLoad = list;
        configAdapter ();
    }

    @Override
    public void showMessage(String message, boolean status) {
        DialogApp.showDialogConfirm (message, status, getActivity ());
    }

    private void actionAdminRemove(){
        AtomicInteger positionChooser = new AtomicInteger();
        listenerDialog = chooser -> {
            if(chooser) presenter.deleteEntry(positionChooser.get());
        };
        TimelineFragment view = this;
        listener =new AdapterTimeline.AdapterTimelineInterface() {
            @Override
            public void deletePoint(TimeEntryModel timeEntryModel, AdapterTimeline.AdapterUpdateTimelineInterface updateListener) {
                updateTimelineListener = updateListener;
                DialogApp.showRemoveDialog(getActivity(), ConstantApp.CHOOSER_DELETE + timeEntryModel.getActivityName()+"?" , listenerDialog);
                positionChooser.set(timeEntryModel.getId());
            }

            @Override
            public void updateLoadList() {
                presenter.getTimeline(month, year);
            }
        };

    }

    @Override
    public void reloadListRemove(boolean chooser) {
        updateTimelineListener.updateTimelineList(chooser);
    }

    private void configAdapter() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager (getContext ());
        recyclerViewTimeline.setLayoutManager (layoutManager);
        recyclerViewTimeline.setHasFixedSize (true);

        adapterTimeline = new AdapterTimeline (listWorkLoad, App.getUser().isAdmin(), listener);
        recyclerViewTimeline.setAdapter (adapterTimeline);
    }
}
