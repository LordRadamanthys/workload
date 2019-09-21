package resource.estagio.workload.ui.home.point;

import android.content.Context;

import java.util.List;

import resource.estagio.workload.data.remote.model.ActivityModel;
import resource.estagio.workload.data.remote.model.CustomerModel;

public class PointContract {

    interface View {

        void notification(String message);

        Context getContext();

        void loadSpinnerCustomer(List<CustomerModel> customerModels);

        void loadSpinnerActivity(List<ActivityModel> activityModels);

        void disableSpinnerActivity();

        void showDialog();

        void showProgressCustomer(final boolean show);

        void showProgressProject(final boolean show);

        void showProgressAdd(final boolean show);

        void setClearFields();

        void setErrorReasonField(String message);

        void showProsseEmployee();

        void setErrorHourField(String message);

        void setErrorProjectField(String message);

        void enabledNavigation(boolean key);

        Context getActivity();
    }

    interface Presenter {

        void setPoint(String date, String hour, String customerName, int customerId,
                      String projectName, int projectId, String demandNumber, String reason);

        void getCustumers();

        void getActivities(int id);
    }
}
