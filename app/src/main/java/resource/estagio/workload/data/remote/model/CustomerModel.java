package resource.estagio.workload.data.remote.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

import resource.estagio.workload.domain.customer.Customer;

public class CustomerModel implements Serializable {

    private int id;
    private String name;

    public CustomerModel() {
    }

    public CustomerModel(int id, String name) {
        this.id = id;
        this.name = name;
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

    public Customer toDomain() {
        return new Customer(id, name);
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
