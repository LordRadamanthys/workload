package resource.estagio.workload.ui.home.point;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import resource.estagio.workload.R;
import resource.estagio.workload.data.remote.model.ActivityModel;
import resource.estagio.workload.data.remote.model.CustomerModel;
import resource.estagio.workload.infra.DateDialog;
import resource.estagio.workload.infra.InputFilterMinMax;
import resource.estagio.workload.ui.HomeDefault;

public class PointFragment extends Fragment implements PointContract.View,
        DatePickerDialog.OnDateSetListener {

    DecimalFormat f = new DecimalFormat ("##00");

    private PointContract.Presenter presenter;

    private HomeDefault.View viewHome;
    private View view;
    private EditText editTextDatePoint;
    private EditText editTextHourPoint;
    private EditText editTextReasonPoint;
    private Spinner spinnerCustomerPoint;
    private Spinner spinnerProjectPoint;
    private SwipeRefreshLayout swipeRefreshPoint;

    private Button buttonPointConfirm;
    private Button buttonConfirmCheck;
    private Calendar date;
    private int customerId;
    private String customerName;
    private int projectId;
    private String projectName;
    private String demandNumber;
    private Dialog dialog;
    private ProgressBar progressCustomerPoint;
    private ProgressBar progressProjectPoint;
    private ProgressBar progressAddPoint;
    private TextInputLayout inputLayoutHourPoint;
    private TextInputLayout inputLayoutReasonPoint;

    // Construtor Vazio
    public PointFragment(HomeDefault.View view) {
        this.viewHome = view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);
        this.view = view;
        loadUI ();
        loadDateHourSave ();
        presenter.getCustumers ();
        saveAddPoint ();
        actionSwipeRefresh ();
    }

    private void actionSwipeRefresh() {
        swipeRefreshPoint.setOnRefreshListener (() -> presenter.getCustumers ());
    }

    private void saveAddPoint() {
        buttonPointConfirm.setOnClickListener (v -> presenter.setPoint (
                editTextDatePoint.getText ().toString (), editTextHourPoint.getText ().toString (),
                customerName, customerId, projectName, projectId, demandNumber,
                editTextReasonPoint.getText ().toString ()));
    }

    public void showDialog() {
        dialog = new Dialog (getActivity (), R.style.CustomAlertDialog);
        dialog.requestWindowFeature (Window.FEATURE_NO_TITLE);
        dialog.setContentView (R.layout.activity_check);
        dialog.setCancelable (false);
        dialog.getWindow ().setSoftInputMode (WindowManager.LayoutParams.
                SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.show ();

        buttonConfirmCheck = dialog.findViewById (R.id.button_dialog_error);
        buttonConfirmCheck.setOnClickListener (v -> {
            dialog.dismiss ();
//            getActivity().finish();
        });
    }

    private void loadDateHourSave() {
        Calendar c = Calendar.getInstance ();
        setDateEditTextPoint (c.get (Calendar.YEAR), c.get (Calendar.MONTH),
                c.get (Calendar.DAY_OF_MONTH));
        editTextDatePoint.setOnClickListener (v -> {
            DialogFragment datePicker = new DateDialog (this);
            datePicker.show (getChildFragmentManager (), getActivity ().getString (R.string.choose_one_day));
        });
    }

    private void loadUI() {
        editTextDatePoint = view.findViewById (R.id.edit_text_date_point);
        editTextHourPoint = view.findViewById (R.id.edit_text_hour_point);
        editTextHourPoint.setFilters (new InputFilter[]{new InputFilterMinMax (1, 8)});
        editTextReasonPoint = view.findViewById (R.id.edit_text_name_project);
        spinnerCustomerPoint = view.findViewById (R.id.spinner_type_activity_project);
        spinnerProjectPoint = view.findViewById (R.id.spinner_project_point);
        swipeRefreshPoint = view.findViewById (R.id.swipe_refresh_point);
        presenter = new PointPresenter (this);
        buttonPointConfirm = view.findViewById (R.id.button_project_confirm);
        progressCustomerPoint = view.findViewById (R.id.progress_customer_point);
        progressProjectPoint = view.findViewById (R.id.progress_project_point);
        progressAddPoint = view.findViewById (R.id.progress_add_point);
        inputLayoutHourPoint = view.findViewById (R.id.input_layout_hour_point);
        inputLayoutReasonPoint = view.findViewById (R.id.input_layout_name_project);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity ().getWindow ().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        return inflater.inflate (R.layout.fragment_point, null);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        setDateEditTextPoint (year, month, dayOfMonth);
    }

    @SuppressLint("DefaultLocale")
    private void setDateEditTextPoint(int year, int month, int dayOfMonth) {
        date = new GregorianCalendar (year, month, dayOfMonth);
        editTextDatePoint.setText (String.format ("%s/%s/%d",
                f.format (date.get (Calendar.DATE)),
                f.format (date.get (Calendar.MONTH) + 1),
                date.get (Calendar.YEAR)));
    }

    @Override
    public void notification(String message) {
        Toast.makeText (getActivity (), message, Toast.LENGTH_SHORT).show ();
    }

    @Override
    public void loadSpinnerCustomer(List<CustomerModel> customerModels) {
        ArrayAdapter<CustomerModel> adapterCustomer = new ArrayAdapter<> (getActivity (),
                android.R.layout.simple_spinner_item, customerModels);
        adapterCustomer.setDropDownViewResource (R.layout.spinner_custom_dropdown);
        spinnerCustomerPoint.setAdapter (adapterCustomer);
        spinnerCustomerPoint.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt (0)).setTextColor (Color.WHITE);
                CustomerModel customerModel = (CustomerModel) parent.getItemAtPosition (position);
                customerId = customerModel.getId ();
                presenter.getActivities (customerModel.getId ());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    @Override
    public void loadSpinnerActivity(List<ActivityModel> activityModels) {
        ArrayAdapter<ActivityModel> adapterActivity = new ArrayAdapter<> (getActivity (),
                android.R.layout.simple_spinner_item, activityModels);
        adapterActivity.setDropDownViewResource (R.layout.spinner_custom_dropdown);
        spinnerProjectPoint.setAdapter (adapterActivity);

        spinnerProjectPoint.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt (0)).setTextColor (Color.WHITE);
                ActivityModel activityModel = (ActivityModel) parent.getItemAtPosition (position);
                customerName = activityModel.getCustomerName ();
                projectId = activityModel.getId ();
                projectName = activityModel.getName ();
                demandNumber = activityModel.getDemandNumber ();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void disableSpinnerActivity() {
        customerName = null;
        projectId = 0;
        projectName = null;
        demandNumber = null;
        spinnerProjectPoint.setVisibility (View.INVISIBLE);
    }

    @Override
    public void showProgressCustomer(final boolean show) {
        int shortAnimTime = getResources ().getInteger (android.R.integer.config_shortAnimTime);

        swipeRefreshPoint.setRefreshing (show);
        buttonPointConfirm.setEnabled (!show);
        spinnerCustomerPoint.setVisibility (show ? View.INVISIBLE : View.VISIBLE);
        progressCustomerPoint.setVisibility (show ? View.VISIBLE : View.GONE);
        progressCustomerPoint.animate ().setDuration (shortAnimTime).alpha (
                show ? 1 : 0).setListener (new AnimatorListenerAdapter () {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressCustomerPoint.setVisibility (show ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public void showProgressProject(final boolean show) {
        int shortAnimTime = getResources ().getInteger (android.R.integer.config_shortAnimTime);

        buttonPointConfirm.setEnabled (!show);
        spinnerProjectPoint.setVisibility (show ? View.INVISIBLE : View.VISIBLE);
        progressProjectPoint.setVisibility (show ? View.VISIBLE : View.GONE);
        progressProjectPoint.animate ().setDuration (shortAnimTime).alpha (
                show ? 1 : 0).setListener (new AnimatorListenerAdapter () {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressProjectPoint.setVisibility (show ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public void showProgressAdd(final boolean show) {
        int shortAnimTime = getResources ().getInteger (android.R.integer.config_shortAnimTime);

        buttonPointConfirm.setVisibility (show ? View.GONE : View.VISIBLE);
        progressAddPoint.setVisibility (show ? View.VISIBLE : View.GONE);
        progressAddPoint.animate ().setDuration (shortAnimTime).alpha (
                show ? 1 : 0).setListener (new AnimatorListenerAdapter () {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressAddPoint.setVisibility (show ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public void setClearFields() {
        editTextHourPoint.setText ("");
        editTextReasonPoint.setText ("");

    }

    @Override
    public void setErrorHourField(String message) {
        setErrorMessage (editTextHourPoint, inputLayoutHourPoint, message);
    }

    @Override
    public void setErrorReasonField(String message) {
        setErrorMessage (editTextReasonPoint, inputLayoutReasonPoint, message);
    }

    @Override
    public void showProsseEmployee() {
    }

    @Override
    public void setErrorProjectField(String message) {
    }

    @Override
    public void enabledNavigation(boolean key) {
        viewHome.enableNavigation (key);
    }

    @SuppressLint("ResourceAsColor")
    public void setErrorMessage(EditText editText, TextInputLayout textInputLayout, String message) {
        textInputLayout.setErrorEnabled (true);
        textInputLayout.setError (message);
        editText.addTextChangedListener (new TextWatcher () {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textInputLayout.setErrorEnabled (false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
