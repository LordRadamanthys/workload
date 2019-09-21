package resource.estagio.workload.ui.admin.client;

import java.util.List;

import resource.estagio.workload.data.remote.model.CustomerModel;

public class ClientContract {

    interface View {

        void setRecyclerClient(List<CustomerModel> customerModels, boolean status);

        void showAdapterRecycler(boolean status);

        void showProgressClient(final boolean show);

        void showDialog(String value, boolean status);

        void refleshAdapter();

        void showToast(String error);
    }

    interface Presenter {

        void getCustomers(boolean status);

        void deleteCustomer(CustomerModel model);

        void showDialogCustomer();
    }
}
