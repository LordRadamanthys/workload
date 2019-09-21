package resource.estagio.workload.data.remote.model;

import java.util.Objects;

public class TimeEntryModel implements Comparable<TimeEntryModel> {

    private int id;
    private int activityId;
    private String activityName;
    private int customerId;
    private String customerName;
    private String demandNumber;
    private int hours;
    private String date;
    private String reason;

    public TimeEntryModel(int activityId, String activityName, int customerId, String customerName,
                          String demandNumber, int hours, String date, String reason) {
        this.activityId = activityId;
        this.activityName = activityName;
        this.customerId = customerId;
        this.customerName = customerName;
        this.demandNumber = demandNumber;
        this.hours = hours;
        this.date = date;
        this.reason = reason;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
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

    public String getDemandNumber() {
        return demandNumber;
    }

    public void setDemandNumber(String demandNumber) {
        this.demandNumber = demandNumber;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }



    @Override
    public int compareTo(TimeEntryModel o) {
        return this.activityName.compareTo(o.activityName);
    }
}

