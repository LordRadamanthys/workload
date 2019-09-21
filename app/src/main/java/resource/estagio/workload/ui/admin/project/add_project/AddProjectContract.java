package resource.estagio.workload.ui.admin.project.add_project;

import android.widget.AdapterView;

import androidx.fragment.app.FragmentActivity;

import java.util.List;

import resource.estagio.workload.data.remote.model.ActivityTypeModel;
import resource.estagio.workload.domain.customer.Customer;

public class AddProjectContract {

    interface View {

        void showToast(String error);

        void spinnerListActivityType(List<ActivityTypeModel> value);

        FragmentActivity getActivity();

        void showProgressSave(final boolean show);
    }

    interface Presenter {

        void loadSpinnerActivityType();

        void getItemInSpinner(AdapterView<?> parent, int position);

        void addProject(String nameProject, String demandNumber, Customer customer);

        void updateProject(int id, String nameProject, String demandNumber, Customer customer);
    }

}
