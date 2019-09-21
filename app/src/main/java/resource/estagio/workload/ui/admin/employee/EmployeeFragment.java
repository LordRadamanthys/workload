package resource.estagio.workload.ui.admin.employee;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import resource.estagio.workload.R;
import resource.estagio.workload.data.remote.model.EmployeeModel;
import resource.estagio.workload.data.remote.model.TimeEntryModel;
import resource.estagio.workload.infra.ConstantApp;
import resource.estagio.workload.ui.DialogApp;
import resource.estagio.workload.ui.HomeDefault;
import resource.estagio.workload.ui.admin.historicResult.ResultHistoricFragment;
import resource.estagio.workload.ui.admin.HomeAdminContract;
import resource.estagio.workload.ui.admin.employee.adapterEmployee.AdapterEmployee;
import resource.estagio.workload.ui.admin.employee.adapterEmployee.RecyclerItemClickListener;

public class EmployeeFragment extends Fragment implements EmployeeContract.View {

    private EditText editTextEmployee;
    private RecyclerView recyclerViewEmployee;
    private View view;
    private HomeAdminContract.View viewHome;
    private EmployeeContract.Presenter presenter;
    private ProgressBar progressBarEmployee;
    private int shortAnimTime;

    public EmployeeFragment(HomeAdminContract.View view) {
        viewHome = view;
        presenter = new EmployeePresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_employee, container, false);
        loadUI();
        presenter.listEmployee();
        hideKeyboard();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        return view;
    }

    private void hideKeyboard() {
        editTextEmployee.setOnEditorActionListener((textView, id, event) -> {
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(editTextEmployee.getWindowToken(), 0);
            }
            return true;
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        editTextEmployee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                presenter.filter(s.toString().trim());
            }
        });
    }

    private void loadUI() {
        recyclerViewEmployee = view.findViewById(R.id.recyclerview_employee);
        editTextEmployee = view.findViewById(R.id.edit_text_employee);
        progressBarEmployee = view.findViewById(R.id.progressBar_employee);
        presenter = new EmployeePresenter(this);
        shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
    }

    @Override
    public void adapterResult(AdapterEmployee adapterEmployee) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewEmployee.setLayoutManager(layoutManager);
        recyclerViewEmployee.setHasFixedSize(true);
        recyclerViewEmployee.setAdapter(adapterEmployee);
    }

    @Override
    public void showDialog(String message, boolean status) {
        DialogApp.showDialogConfirm(message, status, getActivity());
    }

    @Override
    public void showProgress(boolean b) {
        recyclerViewEmployee.setVisibility(b ? View.GONE : View.VISIBLE);
        progressBarEmployee.setVisibility(b ? View.VISIBLE : View.GONE);
        progressBarEmployee.animate().setDuration(shortAnimTime).alpha(
                b ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressBarEmployee.setVisibility(b ? View.VISIBLE : View.GONE);
            }
        });

        viewHome.enableNavigation(b);
    }

    @Override
    public void goToResult(EmployeeModel model) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ConstantApp.RESULT, model);
        ResultHistoricFragment fragment = new ResultHistoricFragment(viewHome);
        fragment.setArguments(bundle);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_admin, fragment)
                .addToBackStack(null)
                .commit();
    }
}


