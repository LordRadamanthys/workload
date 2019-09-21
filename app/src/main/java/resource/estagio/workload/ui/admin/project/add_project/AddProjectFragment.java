package resource.estagio.workload.ui.admin.project.add_project;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.List;

import resource.estagio.workload.R;
import resource.estagio.workload.data.remote.model.ActivityModel;
import resource.estagio.workload.data.remote.model.ActivityTypeModel;
import resource.estagio.workload.domain.customer.Customer;
import resource.estagio.workload.infra.ConstantApp;
import resource.estagio.workload.ui.admin.HomeAdminContract;

public class AddProjectFragment extends Fragment implements AddProjectContract.View {

    private View view;
    private EditText nameProject;
    private EditText demandNumber;
    private Spinner spinnerProjectType;
    private Button buttonProjectConfirm;
    private Customer customer;
    private HomeAdminContract.View activityView;
    private AddProjectContract.Presenter presenter;
    private ActivityModel project;
    private ProgressBar progressSaveAddProject;
    private ImageView imageViewBack;

    public AddProjectFragment(Customer customer, HomeAdminContract.View activityView) {
        this.customer = customer;
        this.activityView = activityView;
        presenter = new AddProjectPresenter(this, activityView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_project, container, false);

        loadUI();
        checkArgument();
        presenter.loadSpinnerActivityType();
        selectItemInSpinner();
        actionSaveProject();
        backToProject();

        return view;
    }

    private void checkArgument() {
        if (getArguments().getSerializable(ConstantApp.PROJECT) != null) {
            project = (ActivityModel) getArguments().getSerializable(ConstantApp.PROJECT);
            nameProject.setText(project.getName());
            demandNumber.setText(project.getDemandNumber());
        }
    }

    private void actionSaveProject() {
        buttonProjectConfirm.setOnClickListener(v -> {
            if (project == null) {
                presenter.addProject(nameProject.getText().toString(),
                        demandNumber.getText().toString(), customer);
            } else {
                presenter.updateProject(project.getId(), nameProject.getText().toString(),
                        demandNumber.getText().toString(), customer);
            }

        });
    }

    private void loadUI() {
        nameProject = view.findViewById(R.id.edit_text_name_project);
        imageViewBack = view.findViewById(R.id.image_view_back_add_project);
        demandNumber = view.findViewById(R.id.edit_text_demand_number_project);
        spinnerProjectType = view.findViewById(R.id.spinner_type_activity_project);
        buttonProjectConfirm = view.findViewById(R.id.button_project_confirm);
        progressSaveAddProject = view.findViewById(R.id.progress_save_add_project);
    }

    private void selectItemInSpinner() {

        spinnerProjectType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presenter.getItemInSpinner(parent, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void showToast(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void spinnerListActivityType(List<ActivityTypeModel> value) {
        try {

            ArrayAdapter<ActivityTypeModel> activityTypeModelArrayAdapter = new ArrayAdapter<>(
                    getActivity(), android.R.layout.simple_spinner_item, value);
            activityTypeModelArrayAdapter.setDropDownViewResource(R.layout.spinner_custom_dropdown);
            spinnerProjectType.setAdapter(activityTypeModelArrayAdapter);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void showProgressSave(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        buttonProjectConfirm.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
        progressSaveAddProject.setVisibility(show ? View.VISIBLE : View.GONE);
        progressSaveAddProject.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressSaveAddProject.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void backToProject() {

        imageViewBack.setOnClickListener(v -> {
            getActivity().onBackPressed();
            activityView.enableNavigation(false);
        });
    }
}
