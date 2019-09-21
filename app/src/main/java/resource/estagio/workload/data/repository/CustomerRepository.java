package resource.estagio.workload.data.repository;

import java.util.List;

import resource.estagio.workload.infra.ConstantApp;
import resource.estagio.workload.data.remote.CustomerAPI;
import resource.estagio.workload.data.remote.model.CustomerModel;
import resource.estagio.workload.domain.customer.Customer;
import resource.estagio.workload.domain.customer.CustomerContract;
import resource.estagio.workload.infra.BaseCallback;
import resource.estagio.workload.infra.Repository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerRepository extends Repository implements CustomerContract.IRepository {

    public static final int ISR = 500;
    public static final int UNAUTHORIDEZ = 401;

    @Override
    public void getCustomers(String token, BaseCallback<List<CustomerModel>> onResult) {
        super.data.restApi(CustomerAPI.class)
                .getCustomer(ConstantApp.BEARER + token)
                .enqueue(new Callback<List<CustomerModel>>() {
                    @Override
                    public void onResponse(Call<List<CustomerModel>> call,
                                           Response<List<CustomerModel>> response) {
                        if (response.code() == UNAUTHORIDEZ) {
                            onResult.onUnsuccessful(ConstantApp.UNAUTHORIDED_USER);
                            return;
                        }
                        if (response.isSuccessful() && response.body() != null)
                            onResult.onSuccessful(response.body());
                        else if (response.body() == null)
                            onResult.onUnsuccessful(ConstantApp.UNLOADED_LIST);
                        else
                            onResult.onUnsuccessful(response.message());
                    }

                    @Override
                    public void onFailure(Call<List<CustomerModel>> call, Throwable t) {
                        onResult.onUnsuccessful(ConstantApp.CONNECTION_INTERNET);
                    }
                });
    }

    @Override
    public void deleteCustomer(int id, String customerName, String token, BaseCallback<String> onResult) {
        super.data.restApi(CustomerAPI.class)
                .deleteCustomer(id, ConstantApp.BEARER + token)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() == UNAUTHORIDEZ) {
                            onResult.onUnsuccessful(ConstantApp.UNAUTHORIDED_USER);
                            return;
                        }
                        if (response.isSuccessful())
                            onResult.onSuccessful(customerName+ ConstantApp.DELETE_IS_SUCCESS);
                        else if(response.code()== ISR)
                            onResult.onUnsuccessful(customerName+ ConstantApp.CUSTOMER_WITH_PROJECT);
                        else
                            onResult.onUnsuccessful(response.message());
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        onResult.onUnsuccessful(ConstantApp.CONNECTION_INTERNET);
                    }
                });
    }

    @Override
    public void postCustomer(Customer customer, String token, BaseCallback<String> onResult) {
        CustomerModel model = new CustomerModel(customer.getId(), customer.getName());
        super.data.restApi(CustomerAPI.class)
                .postCustomer(model, ConstantApp.BEARER + token)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() == UNAUTHORIDEZ) {
                            onResult.onUnsuccessful(ConstantApp.UNAUTHORIDED_USER);
                            return;
                        }
                        if (response.isSuccessful())
                            onResult.onSuccessful(ConstantApp.CUSTOMER_ADD_SUCCESS);
                        else
                            onResult.onUnsuccessful(ConstantApp.ERROR_CUSTOMER_ADD);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        onResult.onUnsuccessful(ConstantApp.CONNECTION_INTERNET);

                    }
                });
    }


}
