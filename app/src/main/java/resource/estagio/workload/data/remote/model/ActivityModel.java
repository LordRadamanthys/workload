package resource.estagio.workload.data.remote.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class ActivityModel implements Serializable {

    private int id;
    private String name;
    private String demandNumber;
    private int activityType;
    private int customerId;
    private String customerName;

    public ActivityModel() {
    }

    public ActivityModel(int id, String name, String demandNumber, int activityType,
                         int customerId, String customerName) {
        this.id = id;
        this.name = name;
        this.demandNumber = demandNumber;
        this.activityType = activityType;
        this.customerId = customerId;
        this.customerName = customerName;
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

    public String getDemandNumber() {
        return demandNumber;
    }

    public void setDemandNumber(String demandNumber) {
        this.demandNumber = demandNumber;
    }

    public int getActivityType() {
        return activityType;
    }

    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
