package resource.estagio.workload.ui.admin.client;

import android.app.Dialog;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import resource.estagio.workload.R;
import resource.estagio.workload.data.remote.model.CustomerModel;
import resource.estagio.workload.data.repository.CustomerRepository;
import resource.estagio.workload.domain.customer.Customer;
import resource.estagio.workload.infra.App;
import resource.estagio.workload.infra.BaseCallback;
import resource.estagio.workload.infra.ConstantApp;
import resource.estagio.workload.ui.DialogApp;

public class ClientPresenter implements ClientContract.Presenter {

    private ClientFragment view;

    public ClientPresenter(ClientFragment view) {
        this.view = view;
    }

    @Override
    public void getCustomers(boolean status) {
        view.showProgressClient(true);
        new CustomerRepository().getCustomers(App.getUser().getAccessToken(),
                new BaseCallback<List<CustomerModel>>() {
                    @Override
                    public void onSuccessful(List<CustomerModel> value) {
                        view.setRecyclerClient(value, status);
                        view.showProgressClient(false);
                    }

                    @Override
                    public void onUnsuccessful(String error) {
                        view.showProgressClient(false);
                        if (errorConnection(error)) return;
                        view.showDialog(error, false);
                    }
                });
    }

    @Override
    public void deleteCustomer(CustomerModel model) {
        view.showProgressClient(true);
        try {
            Customer customer = new Customer(model.getId(), model.getName());
            customer.repository = new CustomerRepository();
            customer.deleteCustomer(App.getUser().getAccessToken(), new BaseCallback<String>() {
                @Override
                public void onSuccessful(String value) {
                    view.showProgressClient(false);
                    view.showDialog(value, true);
                    view.refleshAdapter();
                }

                @Override
                public void onUnsuccessful(String error) {
                    view.showProgressClient(false);
                    if (errorConnection(error)) return;
                    view.showDialog(error, false);
                }
            });
            getCustomers(true);
        } catch (Exception e) {
            view.showDialog(e.getMessage(), false);
        }
    }

    @Override
    public void showDialogCustomer() {
        loadComponentsDialog(DialogApp.createDialog(view.getActivity(), R.layout.dialog_new_customer));
    }

    private void loadComponentsDialog(Dialog dialog) {
        TextInputLayout name = dialog.findViewById(R.id.text_add_customer);
        Button save = dialog.findViewById(R.id.button_save_customer);
        Button cancel = dialog.findViewById(R.id.button_cancel_customer);

        clickButtonSave(dialog, name, save);

        cancel.setOnClickListener(v -> dialog.dismiss());
    }

    private void clickButtonSave(Dialog dialog, TextInputLayout name, Button save) {
        save.setOnClickListener(v -> {
            Customer customer = new Customer(0, name.getEditText().getText().toString().trim());
            customer.repository = new CustomerRepository();
            try {
                customer.postCustomer(App.getUser().getAccessToken(), new BaseCallback<String>() {
                    @Override
                    public void onSuccessful(String value) {
                        view.showDialog(value, true);
                        view.refleshAdapter();
                        dialog.dismiss();
                    }

                    @Override
                    public void onUnsuccessful(String error) {
                        if (errorConnection(error)) return;
                        view.showDialog(error, false);
                        dialog.dismiss();
                    }
                });
            } catch (Exception e) {
                view.showToast(e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private boolean errorConnection(String error) {
        if (error.equals(ConstantApp.CONNECTION_INTERNET)) {
            DialogApp.showDialogConnection(view.getActivity());
            return true;
        }
        return false;
    }
}