package resource.estagio.workload.ui.admin.project;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import resource.estagio.workload.R;
import resource.estagio.workload.data.remote.model.ActivityModel;
import resource.estagio.workload.data.remote.model.CustomerModel;
import resource.estagio.workload.infra.ConstantApp;
import resource.estagio.workload.ui.DialogApp;
import resource.estagio.workload.ui.HomeDefault;
import resource.estagio.workload.ui.admin.HomeAdminContract;
import resource.estagio.workload.ui.admin.client.ClientFragment;
import resource.estagio.workload.ui.admin.project.adapterProject.AdapterProject;
import resource.estagio.workload.ui.admin.project.add_project.AddProjectFragment;

public class ProjectFragment extends Fragment implements ProjectContract.View {

    private RecyclerView recyclerProject;
    private TextView textViewCustomer;
    private ImageView imageViewBackCustomers;
    private ImageView imageViewFilterProject;
    private ConstraintLayout buttonNewProject;
    private Button buttonSaveProject;
    private TextView textViewCancelProject;
    private ProgressBar progressBarProjectAdmin;
    private TextView textViewInsideProject;

    private View view;
    private ProjectContract.Presenter presenter;
    private CustomerModel customer;
    private HomeAdminContract.View activityView;
    private AdapterProject.AdapterProjectInterface listener;
    private List<ActivityModel> activityModels;
    private List<ActivityModel> activityModelsDelete;
    private int shortAnimTime;

    public ProjectFragment(HomeAdminContract.View activityView) {
        this.activityView = activityView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_project_admin, container, false);

        presenter = new ProjectPresenter(this);

        Bundle arguments = getArguments();
        customer = (CustomerModel) arguments.getSerializable(ConstantApp.CUSTOMER);
        loadUI();
        loadListenerAdapter();
        setCustomerName();
        presenter.loadList(customer.getId(), false);
        showInvisibility();
        backToCustomers();
        filterClick();
        clickButtonAdd();
        return view;
    }

    private void loadListenerAdapter() {

        activityModelsDelete = new ArrayList<>();
        listener = new AdapterProject.AdapterProjectInterface() {

            @Override
            public void renameProject(int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(ConstantApp.PROJECT, activityModels.get(position));
                AddProjectFragment fragment = new AddProjectFragment(customer.toDomain(),
                        activityView);
                fragment.setArguments(bundle);
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_admin, fragment)
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            public void deleteProject(int position) {
                activityModelsDelete.add(activityModels.get(position));
            }
        };
    }

    private void filterClick() {

        imageViewFilterProject.setOnClickListener(v -> {
            presenter.loadList(customer.getId(), true);
            showVisibility();
        });

        buttonSaveProject.setOnClickListener(v -> {

            if (activityModelsDelete.size() > 0) {

                for (ActivityModel model : activityModelsDelete) {
                    presenter.deleteCustomer(model);
                }
                activityModelsDelete = new ArrayList<>();
            } else {
                presenter.loadList(customer.getId(), false);
                showInvisibility();
            }
        });

        textViewCancelProject.setOnClickListener(v -> {
            presenter.loadList(customer.getId(), false);
            showInvisibility();
        });
    }

    private void clickButtonAdd() {

        buttonNewProject.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(ConstantApp.ADD_PROJECT, null);
            AddProjectFragment fragment = new AddProjectFragment(customer.toDomain(),
                    activityView);
            fragment.setArguments(bundle);
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_admin, fragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    private void backToCustomers() {

        imageViewBackCustomers.setOnClickListener(v -> {
            getActivity().onBackPressed();
            activityView.enableNavigation(false);
        });
    }

    private void setCustomerName() {
        textViewCustomer.setText(customer.getName());
    }

    private void loadUI() {

        recyclerProject = view.findViewById(R.id.recycler_project);
        textViewCustomer = view.findViewById(R.id.text_view_customer_project);
        imageViewBackCustomers = view.findViewById(R.id.image_view_back_collaborator);
        imageViewFilterProject = view.findViewById(R.id.image_view_filter_project);
        progressBarProjectAdmin = view.findViewById(R.id.progress_bar_project_admin);
        buttonNewProject = view.findViewById(R.id.button_constraints_project);
        buttonSaveProject = view.findViewById(R.id.button_save_project);
        textViewCancelProject = view.findViewById(R.id.text_view_cancel_project);
        textViewInsideProject = view.findViewById(R.id.text_view_inside_project);
        shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
    }

    @Override
    public void listAdapter(List<ActivityModel> value, boolean status) {
        activityModels = value;
        AdapterProject adapter = new AdapterProject(value, status, listener);
        recyclerProject.setAdapter(adapter);
    }

    @Override
    public void reloadList() {
        showInvisibility();
        presenter.loadList(customer.getId(), false);
    }

    @Override
    public void showToast(String message, boolean status) {
        DialogApp.showDialogConfirm(message, status, getActivity());
    }

    @Override
    public void showProgress(boolean result) {

        recyclerProject.setVisibility(result ? View.GONE : View.VISIBLE);
        progressBarProjectAdmin.setVisibility(result ? View.VISIBLE : View.GONE);
        progressBarProjectAdmin.animate().setDuration(shortAnimTime).alpha(
                result ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressBarProjectAdmin.setVisibility(result ? View.VISIBLE : View.GONE);
            }
        });

        activityView.enableNavigation(result);
    }

    @Override
    public void showVisibility() {
        buttonSaveProject.setVisibility(View.VISIBLE);
        textViewCancelProject.setVisibility(View.VISIBLE);
        imageViewFilterProject.setVisibility(View.INVISIBLE);
        textViewInsideProject.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showInvisibility() {
        buttonSaveProject.setVisibility(View.INVISIBLE);
        textViewCancelProject.setVisibility(View.INVISIBLE);
        imageViewFilterProject.setVisibility(View.VISIBLE);
        textViewInsideProject.setVisibility(View.VISIBLE);
    }

}

