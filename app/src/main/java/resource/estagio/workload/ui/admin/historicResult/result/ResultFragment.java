package resource.estagio.workload.ui.admin.historicResult.result;


import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.Calendar;
import java.util.List;

import resource.estagio.workload.R;
import resource.estagio.workload.data.remote.model.EmployeeModel;
import resource.estagio.workload.ui.HomeDefault;
import resource.estagio.workload.ui.admin.historicResult.ResultHistoricContract;
import resource.estagio.workload.ui.admin.historicResult.adapter.AdapterListResult;


public class ResultFragment extends Fragment implements ResultHistoricContract.ResultView {

    private AdapterListResult adapterListResult;
    private RecyclerView recyclerViewListResult;
    private List<newListResultAdmin> newlistResult;
    private ResultHistoricContract.ResultPresenter presenter;
    private View view;
    private Dialog dialog;
    private Button buttonError;
    private TextView textDialogError;
    private HomeDefault.View viewHome;
    private SwipeRefreshLayout swipe_Refresh_result;
    private int month;
    private int year;
    private Calendar calendar;
    private EmployeeModel employeeModel;


    public ResultFragment(HomeDefault.View viewHome, int m, int y, EmployeeModel employee) {
        this.viewHome = viewHome;
        this.year = y;
        this.month = m;
        this.employeeModel = employee;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_result, container, false);

        loadUi();
        actioSwipeRefresh(this);
        verifyMonthForRequest();

        return view;
    }

    private void verifyMonthForRequest() {
        if (year == 0 || month == 0) {
            month = calendar.get(Calendar.MONTH) + 1;
            year = calendar.get(Calendar.YEAR);
            presenter.getListResult(month, year, employeeModel);
        } else {
            presenter.getListResult(month, year, employeeModel);
        }
    }

    private void actioSwipeRefresh(ResultHistoricContract.ResultView view) {
        recyclerViewListResult.setVisibility(View.GONE);

        swipe_Refresh_result.setOnRefreshListener(() -> {
            presenter = new ResultPresenter(view);
            presenter.getListResult(month, year, employeeModel);
        });
    }

    private void loadUi() {
        calendar = Calendar.getInstance();
        recyclerViewListResult = view.findViewById(R.id.recyclerView_result_admin);
        swipe_Refresh_result = view.findViewById(R.id.swipe_Refresh_result);
        presenter = new ResultPresenter(this);
    }


    private void configAdapter() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewListResult.setLayoutManager(layoutManager);
        recyclerViewListResult.setHasFixedSize(true);

        adapterListResult = new AdapterListResult(newlistResult, getContext());
        recyclerViewListResult.setAdapter(adapterListResult);

    }

    @Override
    public void dialog(boolean Key) {
        recyclerViewListResult.setVisibility(Key ? View.INVISIBLE : View.VISIBLE);
        swipe_Refresh_result.setRefreshing(Key);

        viewHome.enableNavigation(Key);
    }

    @Override
    public void showListResult(List<newListResultAdmin> list) {
        newlistResult = list;
        configAdapter();
        swipe_Refresh_result.setRefreshing(false);

    }

    @Override
    public void showErrorMessage(String text) {
        showDialogError(text);
    }

    @Override
    public void showSucessMessage(String text) {

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
}
