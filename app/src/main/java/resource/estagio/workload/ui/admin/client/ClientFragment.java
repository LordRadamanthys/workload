package resource.estagio.workload.ui.admin.client;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import resource.estagio.workload.R;
import resource.estagio.workload.data.remote.model.CustomerModel;
import resource.estagio.workload.ui.DialogApp;
import resource.estagio.workload.ui.HomeDefault;
import resource.estagio.workload.ui.admin.HomeAdminContract;
import resource.estagio.workload.ui.admin.project.ProjectFragment;

public class ClientFragment extends Fragment implements ClientContract.View {

    public static final String CUSTOMER = "customer";
    // ATRIBUTOS DE REFERÊNCIA VISUAL
    private View view;
    private RecyclerView recyclerClient;
    private ConstraintLayout buttonConstraintLayout;
    private ImageView imageViewConfigClient;
    private Button buttonSaveClient;
    private TextView textViewCancelClient;
    private TextView textViewSelectClient;
    private AdapterClient adapterClient;
    private ProgressBar progressBarClient;
    private HomeAdminContract.View activityView;

    // ATRIBUTOS INTERNOS
    private ClientPresenter presenter;
    private List<CustomerModel> customerModels;
    private List<CustomerModel> customerModelsDelete;
    private AdapterClient.AdapterInterface adapterInterface;

    public ClientFragment(HomeAdminContract.View activityView) {
        this.activityView = activityView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_client, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;

        loadUI();
        loadAdapterListener();

        presenter.getCustomers(true);

        loadListernersClick();
    }

    private void loadListernersClick() {
        buttonConstraintLayout.setOnClickListener(v -> presenter.showDialogCustomer());

        imageViewConfigClient.setOnClickListener(v -> presenter.getCustomers(false));

        buttonSaveClient.setOnClickListener(v -> {
            if (customerModelsDelete.size() > 0)
                for (CustomerModel model : customerModelsDelete) presenter.deleteCustomer(model);
            else
                presenter.getCustomers(true);
            customerModelsDelete = new ArrayList<>();
        });

        textViewCancelClient.setOnClickListener(v -> {
            presenter.getCustomers(true);
            customerModelsDelete = new ArrayList<>();
        });
    }

    private void loadUI() {
        presenter = new ClientPresenter(this);
        buttonConstraintLayout = view.findViewById(R.id.button_constraints_project);
        recyclerClient = view.findViewById(R.id.recycler_clients_client);
        textViewCancelClient = view.findViewById(R.id.text_view_cancel_client);
        textViewSelectClient = view.findViewById(R.id.text_view_selecionar_client);
        imageViewConfigClient = view.findViewById(R.id.image_view_config_client);
        buttonSaveClient = view.findViewById(R.id.button_save_client);
        progressBarClient = view.findViewById(R.id.progress_client);
    }

    private void loadAdapterListener() {
        customerModelsDelete = new ArrayList<>();
        adapterInterface = new AdapterClient.AdapterInterface() {
            @Override
            public void removeClient(View v, int position, CustomerModel model) {
                customerModelsDelete.add(model);
            }

            @Override
            public void goToProject(View v, int position) {
                sendToFragmentProject(position);
            }
        };
    }

    private void sendToFragmentProject(int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(CUSTOMER, customerModels.get(position));
        ProjectFragment fragment = new ProjectFragment(activityView);
        fragment.setArguments(bundle);
        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_admin, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void setRecyclerClient(List<CustomerModel> customerModels, boolean status) {
        recyclerClient.setHasFixedSize(true);
        this.customerModels = customerModels;
        showAdapterRecycler(status);
    }

    @Override
    public void showAdapterRecycler(boolean status) {
        setVisibilityIcons(status);
        adapterClient = new AdapterClient(customerModels, status, adapterInterface);
        recyclerClient.setAdapter(adapterClient);
    }

    private void setVisibilityIcons(boolean status) {
        imageViewConfigClient.setVisibility(status ? View.VISIBLE : View.INVISIBLE);
        textViewSelectClient.setVisibility(status ? View.VISIBLE : View.INVISIBLE);
        textViewCancelClient.setVisibility(status ? View.INVISIBLE : View.VISIBLE);
        buttonSaveClient.setVisibility(status ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void showProgressClient(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        recyclerClient.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
        progressBarClient.setVisibility(show ? View.VISIBLE : View.GONE);
        progressBarClient.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressBarClient.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
        activityView.enableNavigation(show);
    }

    @Override
    public void showDialog(String value, boolean status) {
        DialogApp.showDialogConfirm(value, status, getActivity());
    }

    @Override
    public void refleshAdapter() {
        presenter.getCustomers(true);
    }

    @Override
    public void showToast(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
    }

}