package resource.estagio.workload.domain.project;

import resource.estagio.workload.domain.customer.Customer;
import resource.estagio.workload.infra.ConstantApp;
import resource.estagio.workload.data.remote.model.ActivityTypeModel;
import resource.estagio.workload.data.repository.ActivityRepository;
import resource.estagio.workload.infra.App;
import resource.estagio.workload.infra.BaseCallback;

public class Project {

    private int id;
    private String nameProject;
    private String demandNumber;
    private ActivityTypeModel activityTypeModel;
    private Customer customer;
    public ProjectContract.IRepository repository;

    public Project(int id, String nameProject, String demandNumber,
                   ActivityTypeModel activityTypeModel, Customer customer) {

        this.id = id;
        this.nameProject = nameProject.trim();
        this.demandNumber = demandNumber.trim();
        this.activityTypeModel = activityTypeModel;
        this.customer = customer;
        this.repository = new ActivityRepository();
    }

    public Project(int id, String nameProject) {
        this.id = id;
        this.nameProject = nameProject.trim();
    }

    public int getId() {
        return id;
    }

    public String getNameProject() {
        return nameProject;
    }

    public String getDemandNumber() {
        return demandNumber;
    }

    public ActivityTypeModel getActivityTypeModel() {
        return activityTypeModel;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void insertProject(BaseCallback<String> onResult) throws Exception {

        if (repository == null) throw new Exception(ConstantApp.REPOSITORY_NULL);

        if (nameProject.isEmpty() || nameProject == null)
            throw new Exception(ConstantApp.NAME_IS_REQUIRED);

        if (demandNumber.isEmpty() || demandNumber == null)
            throw new Exception(ConstantApp.DEMAND_NUMBER_REQUIRED);

        if (activityTypeModel == null) throw new Exception(ConstantApp.ACTIVITY_TYPE_REQUIRED);

        if (customer == null) throw new Exception(ConstantApp.CUSTOMER_IS_NULL);

        repository.insertProject(this, App.getUser().getAccessToken(), new BaseCallback<String>() {
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

    public void updateProject(BaseCallback<String> onResult) throws Exception{

        if (repository == null) throw new Exception(ConstantApp.REPOSITORY_NULL);

        if (nameProject.isEmpty() || nameProject == null)
            throw new Exception(ConstantApp.NAME_IS_REQUIRED);

        if (demandNumber.isEmpty() || demandNumber == null)
            throw new Exception(ConstantApp.DEMAND_NUMBER_REQUIRED);

        if (activityTypeModel == null) throw new Exception(ConstantApp.ACTIVITY_TYPE_REQUIRED);

        if (customer == null) throw new Exception(ConstantApp.CUSTOMER_IS_NULL);

        repository.updateProject(this, App.getUser().getAccessToken(), new BaseCallback<String>() {
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
    public void deleteActivity(String token, BaseCallback<String> onResult) throws Exception {

        if (repository == null) throw new Exception(ConstantApp.REPOSITORY_NULL);

        if (id == 0) throw new Exception(ConstantApp.ID_IS_REQUIRED);

        if (nameProject == null) throw new Exception(ConstantApp.NAME_IS_REQUIRED);

        repository.deleteProject(id, nameProject, token, new BaseCallback<String>() {
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
