package resource.estagio.workload.domain.employee;

import java.util.List;

import resource.estagio.workload.data.remote.model.EmployeeModel;
import resource.estagio.workload.infra.ConstantApp;
import resource.estagio.workload.data.remote.model.TimeEntryModel;
import resource.estagio.workload.infra.BaseCallback;

public class EmployeeDomain {

    public EmployeeContract.IRepository irepository;

    private String name;
    private String username;
    private int activityId;
    private String activityName;
    private int customerId;
    private String customerName;
    private String demandNumber;
    private int hours;
    private String date;
    private String reason;

    public EmployeeDomain(int activityId, String activityName, int customerId,
                          String customerName, String demandNumber, int hours, String date, String reason) {
        this.activityId = activityId;
        this.activityName = activityName;
        this.customerId = customerId;
        this.customerName = customerName;
        this.demandNumber = demandNumber;
        this.hours = hours;
        this.date = date;
        this.reason = reason.trim();
    }

    public EmployeeDomain() {

    }

    public EmployeeContract.IRepository getIrepository() {
        return irepository;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public void getPostEntry() {

    }

    public void postEntry(String token, final BaseCallback<Void> listener) throws Exception {
        if (irepository == null)
            throw new Exception(ConstantApp.REPOSITORY_NULL);
        if (activityId == 0 || activityName.isEmpty() || demandNumber.isEmpty())
            throw new Exception(ConstantApp.CUSTOMER_WITHOUT_PROJECT);
        if (customerId == 0 || customerName.isEmpty())
            throw new Exception(ConstantApp.CUSTOMER_NOT_FOUND);
        if (hours == 0)
            throw new Exception(ConstantApp.POINTING_MIN_HOUR);
        if (date == null)
            throw new Exception(ConstantApp.DATE_IS_NULL);
        if (reason.isEmpty())
            throw new Exception(ConstantApp.REASON_IS_REQUIRED);

        TimeEntryModel model = new TimeEntryModel(activityId, activityName, customerId,
                customerName, demandNumber, hours, date, reason);
        irepository.postEntry(model, token, new BaseCallback<Void>() {
            @Override
            public void onSuccessful(Void value) {
                listener.onSuccessful(null);
            }

            @Override
            public void onUnsuccessful(String error) {
                listener.onUnsuccessful(error);
            }
        });
    }

    public void getWorkList(int month, int year, final BaseCallback<List<TimeEntryModel>> listener) {
        irepository.getWorkList(month, year, new BaseCallback<List<TimeEntryModel>>() {
            @Override
            public void onSuccessful(List<TimeEntryModel> value) {
                listener.onSuccessful(value);
            }

            @Override
            public void onUnsuccessful(String error) {
                listener.onUnsuccessful(error);
            }
        });
    }

    public void getWorkListModel(int month, int year, EmployeeModel model, final BaseCallback<List<TimeEntryModel>> listener) {
        irepository.getWorkListModel(month, year, (int) model.getId(), new BaseCallback<List<TimeEntryModel>>() {
            @Override
            public void onSuccessful(List<TimeEntryModel> value) {
                listener.onSuccessful(value);
            }

            @Override
            public void onUnsuccessful(String error) {
                listener.onUnsuccessful(error);
            }
        });
    }

    public void deleteEntry(long id, final BaseCallback<Void> listener ) throws Exception {
        if(id <= 0)
            throw new Exception(ConstantApp.ENTRY_INVALID);
        irepository.deleteEntry(id, new BaseCallback<Void>() {
            @Override
            public void onSuccessful(Void value) {
                listener.onSuccessful(value);
            }

            @Override
            public void onUnsuccessful(String error) {
                listener.onUnsuccessful(error);
            }
        });
    }

}