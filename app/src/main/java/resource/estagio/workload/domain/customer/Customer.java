package resource.estagio.workload.domain.customer;

import resource.estagio.workload.infra.ConstantApp;
import resource.estagio.workload.infra.BaseCallback;

public class Customer {

    public CustomerContract.IRepository repository;

    private int id;
    private String name;

    public Customer(int id, String name) {
        this.id = id;
        this.name = name.trim();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void postCustomer(String token, BaseCallback<String> onResult) throws Exception {
        if (repository == null) throw new Exception(ConstantApp.REPOSITORY_NULL);

        if (name.isEmpty() || name == null) throw new Exception(ConstantApp.NAME_IS_REQUIRED);

        if (id < 0) throw new Exception(ConstantApp.ID_IS_REQUIRED);

        repository.postCustomer(this, token, new BaseCallback<String>() {
            @Override
            public void onSuccessful(String value) {
                onResult.onSuccessful(value);
            }

            @Override
            public void onUnsuccessful(String error) {
                onResult.onUnsuccessful(error);
            }
        });
    }


    public void deleteCustomer(String token, BaseCallback<String> onResult) throws Exception {
        if (repository == null) throw new Exception(ConstantApp.REPOSITORY_NULL);

        if (id == 0) throw new Exception(ConstantApp.ID_IS_REQUIRED);

        repository.deleteCustomer(id, name, token, new BaseCallback<String>() {
            @Override
            public void onSuccessful(String value) {
                onResult.onSuccessful(value);
            }

            @Override
            public void onUnsuccessful(String error) {
                onResult.onUnsuccessful(error);
            }
        });
    }
}
