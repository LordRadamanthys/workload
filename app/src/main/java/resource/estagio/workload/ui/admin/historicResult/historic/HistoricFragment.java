package resource.estagio.workload.ui.admin.historicResult.historic;


import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
import resource.estagio.workload.data.remote.model.EmployeeModel;
import resource.estagio.workload.data.remote.model.TimeEntryModel;
import resource.estagio.workload.infra.App;
import resource.estagio.workload.infra.ConstantApp;
import resource.estagio.workload.ui.DialogApp;
import resource.estagio.workload.ui.HomeDefault;
import resource.estagio.workload.ui.admin.historicResult.ResultHistoricContract;
import resource.estagio.workload.ui.home.timeline.adapterTimeLine.AdapterTimeline;


public class HistoricFragment extends Fragment implements ResultHistoricContract.historicView,
                                                            ResultHistoricContract.mainHistoric {
    private View view;
    private RecyclerView recyclerViewTimeline;
    private List<TimeEntryModel> listWorkLoad;
    private TextView textViewMonth;
    private TextView textViewYear;
    private ImageView imageViewMonthLeft;
    private ImageView imageViewMonthRight;
    private Calendar calendar;
    private ResultHistoricContract.historicPresenter presenter;
    private int month;
    private int year;
    private TextView textDialogError;
    private ResultHistoricContract.mainResult mainResult;

    private Dialog dialog;
    private HomeDefault.View viewHome;
    private SwipeRefreshLayout swipeRefreshHistoric;
    private EmployeeModel employeeModel;
    private AdapterTimeline.AdapterTimelineInterface listener;
    private DialogApp.DialogListener listenerDialog;
    private AdapterTimeline.AdapterUpdateTimelineInterface updateTimelineListener = null;

    private Button buttonError;

    public HistoricFragment(HomeDefault.View view, ResultHistoricContract.mainResult mainResult, EmployeeModel employee) {
        this.viewHome = view;
        this.mainResult = mainResult;
        this.employeeModel = employee;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_historic, container, false);

        loadUi();
        actionAdminRemove();
        setTextDate();
        final int thisMonth = calendar.get(Calendar.MONTH);
        actionArrowsButton(thisMonth);
        actioSwipeRefresh(this);

        return view;
    }

    private void actionArrowsButton(int thisMonth) {
        imageViewMonthLeft.setOnClickListener(v -> {

            if (month >= thisMonth - 2) {
                calendar.add(Calendar.MONTH, -1);
                setTextDate();
                imageViewMonthLeft.setVisibility(View.VISIBLE);
                imageViewMonthRight.setVisibility(View.VISIBLE);
            } else {
                imageViewMonthLeft.setVisibility(View.INVISIBLE);
            }
            if (month == thisMonth - 6) {
                imageViewMonthLeft.setVisibility(View.INVISIBLE);
            }
        });

        imageViewMonthRight.setOnClickListener(v -> {

            if (month <= thisMonth) {
                calendar.add(Calendar.MONTH, +1);
                setTextDate();
                imageViewMonthRight.setVisibility(View.VISIBLE);
                imageViewMonthLeft.setVisibility(View.VISIBLE);
            } else {
                imageViewMonthRight.setVisibility(View.INVISIBLE);
            }
            if (month == thisMonth + 1) {
                imageViewMonthRight.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void actionAdminRemove(){
        AtomicInteger positionChooser = new AtomicInteger();
        listenerDialog = chooser -> {
            if(chooser) presenter.deleteEntry(positionChooser.get());
        };
        HistoricFragment view = this;
        listener = new AdapterTimeline.AdapterTimelineInterface() {
            @Override
            public void deletePoint(TimeEntryModel timeEntryModel, AdapterTimeline.AdapterUpdateTimelineInterface updateListener) {
                updateTimelineListener = updateListener;
                DialogApp.showRemoveDialog(getActivity(), ConstantApp.CHOOSER_DELETE +
                        timeEntryModel.getActivityName()+"?", listenerDialog);
                positionChooser.set(timeEntryModel.getId());
            }

            @Override
            public void updateLoadList() {
                presenter.getTimeline(month, year, employeeModel);
            }
        };


    }

    private void actioSwipeRefresh(ResultHistoricContract.historicView view) {
        recyclerViewTimeline.setVisibility(View.GONE);

        swipeRefreshHistoric.setOnRefreshListener(() -> {
            presenter = new HistoricFragmentPresenter(view);
            presenter.getTimeline(month, year, employeeModel);
        });
    }

    private void loadUi() {
        calendar = Calendar.getInstance();
        swipeRefreshHistoric = view.findViewById(R.id.swipe_Refresh_historic);
        presenter = new HistoricFragmentPresenter(this);

        recyclerViewTimeline = view.findViewById(R.id.id_recyclerview_historic_admin);
        textViewMonth = view.findViewById(R.id.text_view_month_historic_admin);
        textViewYear = view.findViewById(R.id.text_view_year_historic_admin);
        imageViewMonthLeft = view.findViewById(R.id.image_view_month_left_historic_admin);
        imageViewMonthRight = view.findViewById(R.id.image_view_month_right_historic_admin);
        imageViewMonthRight.setVisibility(View.INVISIBLE);
    }

    private void setTextDate() {

        textViewMonth.setText(new SimpleDateFormat("MMM").format(calendar.getTime()));
        textViewYear.setText(String.valueOf(calendar.get(Calendar.YEAR)));
        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);
        presenter.getTimeline(month, year, employeeModel);
        setDateResultFragment(month, year,textViewMonth.getText().toString());
    }

    private void configAdapter() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewTimeline.setLayoutManager(layoutManager);
        recyclerViewTimeline.setHasFixedSize(true);
        AdapterTimeline adapterTimeline = new AdapterTimeline(listWorkLoad, App.getUser().isAdmin(), listener);
        recyclerViewTimeline.setAdapter(adapterTimeline);
    }

    @Override
    public void dialog(boolean Key) {

        recyclerViewTimeline.setVisibility(Key ? View.INVISIBLE : View.VISIBLE);
        swipeRefreshHistoric.setRefreshing(Key);
        viewHome.enableNavigation(Key);
    }

    @Override
    public void showListTimeline(List<TimeEntryModel> list) {
        listWorkLoad = list;
        configAdapter();
    }

    @Override
    public void showErrorMessage(String text) {
        showDialogError(text);
    }


    private void showDialogError(String text) {
        dialog = new Dialog(getActivity(), R.style.CustomAlertDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_dialog_error);
        dialog.setCancelable(false);

        textDialogError = dialog.findViewById(R.id.text_dialog_error);
        textDialogError.setText(text);

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.
                SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.show();
        buttonError = dialog.findViewById(R.id.button_dialog_error);
        buttonError.setOnClickListener(v -> dialog.dismiss());

    }

    @Override
    public void reloadListRemove(boolean chooser) {
        updateTimelineListener.updateTimelineList(chooser);
    }

    @Override
    public void setDateResultFragment(int m, int y, String textMonth) {
        mainResult.getDateResultFragment(m,y,textMonth);
    }
}
