package resource.estagio.workload.domain.customer;

import java.util.List;

import resource.estagio.workload.data.remote.model.CustomerModel;
import resource.estagio.workload.infra.BaseCallback;

public class CustomerContract {

    public interface IRepository {
        void getCustomers(String token, BaseCallback<List<CustomerModel>> onResult);

        void deleteCustomer(int id, String customerName, String token, BaseCallback<String> onResult);

        void postCustomer(Customer customer, String token, BaseCallback<String> onResult);
    }



}
