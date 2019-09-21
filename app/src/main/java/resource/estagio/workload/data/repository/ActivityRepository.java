package resource.estagio.workload.data.repository;

import java.util.List;

import resource.estagio.workload.infra.ConstantApp;
import resource.estagio.workload.data.remote.ActivityAPI;
import resource.estagio.workload.data.remote.model.ActivityModel;
import resource.estagio.workload.data.remote.model.ActivityTypeModel;
import resource.estagio.workload.domain.project.Project;
import resource.estagio.workload.domain.project.ProjectContract;
import resource.estagio.workload.infra.BaseCallback;
import resource.estagio.workload.infra.Repository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityRepository extends Repository implements ProjectContract.IRepository {

    public static final int UNAUTHORIDEZ = 401;
    @Override
    public void getActivity(int id, String token, BaseCallback<List<ActivityModel>> onResult) {
        super.data.restApi(ActivityAPI.class)
                .getActivity(id, ConstantApp.BEARER + token)
                .enqueue(new Callback<List<ActivityModel>>() {
                    @Override
                    public void onResponse(Call<List<ActivityModel>>
                                                   call, Response<List<ActivityModel>> response) {
                        if (response.code() == UNAUTHORIDEZ) {
                            onResult.onUnsuccessful(ConstantApp.UNAUTHORIDED_USER);
                            return;
                        }
                        if (response.isSuccessful() && response.body() != null)
                            onResult.onSuccessful(response.body());
                        else if(response.body() == null)
                            onResult.onUnsuccessful(ConstantApp.UNLOADED_LIST);
                        else
                            onResult.onUnsuccessful(response.message());
                    }

                    @Override
                    public void onFailure(Call<List<ActivityModel>> call, Throwable t) {
                        onResult.onUnsuccessful(ConstantApp.CONNECTION_INTERNET);
                    }
                });

    }

    @Override
    public void getActivityType(String token, BaseCallback<List<ActivityTypeModel>> onResult) {
        super.data.restApi(ActivityAPI.class)
                .getActivityType(ConstantApp.BEARER + token)
                .enqueue(new Callback<List<ActivityTypeModel>>() {
                    @Override
                    public void onResponse(Call<List<ActivityTypeModel>> call, Response<List<ActivityTypeModel>> response) {
                        if (response.code() == UNAUTHORIDEZ) {
                            onResult.onUnsuccessful(ConstantApp.UNAUTHORIDED_USER);
                            return;
                        }
                        if (response.isSuccessful() && response.body() != null)
                            onResult.onSuccessful(response.body());
                        else
                            onResult.onUnsuccessful(response.message());
                    }

                    @Override
                    public void onFailure(Call<List<ActivityTypeModel>> call, Throwable t) {
                        onResult.onUnsuccessful(ConstantApp.CONNECTION_INTERNET);
                    }
                });
    }

    @Override
    public void insertProject(Project project, String token, BaseCallback<String> onResult) {
        ActivityModel activityModel = new ActivityModel(project.getId(), project.getNameProject(),
                project.getDemandNumber(), project.getActivityTypeModel().getId(),
                project.getCustomer().getId(), project.getCustomer().getName());
        super.data.restApi(ActivityAPI.class)
                .insertProject(activityModel, ConstantApp.BEARER + token)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() == UNAUTHORIDEZ) {
                            onResult.onUnsuccessful(ConstantApp.UNAUTHORIDED_USER);
                            return;
                        }
                        if (response.isSuccessful())
                            onResult.onSuccessful(ConstantApp.PROJECT_SEND_SUCCESS);
                        else
                            onResult.onUnsuccessful(ConstantApp.ERROR_ADD_PROJECT);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        onResult.onUnsuccessful(ConstantApp.CONNECTION_INTERNET);
                    }
                });

    }

    @Override
    public void updateProject(Project project, String token, BaseCallback<String> onResult) {
        ActivityModel activityModel = new ActivityModel(project.getId(), project.getNameProject(), project.getDemandNumber(),
                project.getActivityTypeModel().getId(), project.getCustomer().getId(),
                project.getCustomer().getName());
        super.data.restApi(ActivityAPI.class)
                .updateProject(activityModel, ConstantApp.BEARER + token)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() == UNAUTHORIDEZ) {
                            onResult.onUnsuccessful(ConstantApp.UNAUTHORIDED_USER);
                            return;
                        }
                        if (response.isSuccessful())
                            onResult.onSuccessful(ConstantApp.PROJECT_UPDATE_SUCCESS);
                        else
                            onResult.onUnsuccessful(ConstantApp.ERROR_UPDATE_PROJECT);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        onResult.onUnsuccessful(ConstantApp.CONNECTION_INTERNET);
                    }
                });

    }

    @Override
    public void deleteProject(long id, String name, String token, BaseCallback<String> onResult) {
        super.data.restApi(ActivityAPI.class)
                .deleteProject(id, ConstantApp.BEARER + token)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() == UNAUTHORIDEZ) {
                            onResult.onUnsuccessful(ConstantApp.UNAUTHORIDED_USER);
                            return;
                        }
                        if (response.isSuccessful())
                            onResult.onSuccessful(name+ConstantApp.DELETE_IS_SUCCESS);
                        else
                            onResult.onUnsuccessful(name+ConstantApp.DELETE_PROJECT_SUCCESS);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        onResult.onUnsuccessful(ConstantApp.CONNECTION_INTERNET);
                    }
                });

    }
}

