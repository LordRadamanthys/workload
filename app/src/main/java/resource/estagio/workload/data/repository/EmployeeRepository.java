package resource.estagio.workload.data.repository;

import java.util.List;

import resource.estagio.workload.data.remote.model.EmployeeModel;
import resource.estagio.workload.infra.ConstantApp;
import resource.estagio.workload.data.remote.EmployeeAPI;
import resource.estagio.workload.data.remote.model.TimeEntryModel;
import resource.estagio.workload.domain.employee.EmployeeContract;
import resource.estagio.workload.infra.App;
import resource.estagio.workload.infra.BaseCallback;
import resource.estagio.workload.infra.Repository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeRepository extends Repository implements EmployeeContract.IRepository {

    public static final int UNAUTHORIDEZ = 401;

    @Override
    public void getWorkList(int month, int year, BaseCallback<List<TimeEntryModel>> onResult) {
        super.data.restApi(EmployeeAPI.class).getWorkList(month, year, ConstantApp.BEARER + App.getUser().getAccessToken())
                .enqueue(new Callback<List<TimeEntryModel>>() {
                    @Override
                    public void onResponse(Call<List<TimeEntryModel>>
                                                   call, Response<List<TimeEntryModel>> response) {
                        if (response.code() == UNAUTHORIDEZ) {
                            onResult.onUnsuccessful(ConstantApp.UNAUTHORIDED_USER);
                            return;
                        }

                        if (!response.isSuccessful() || response.body() == null) {
                            onResult.onUnsuccessful(ConstantApp.UNLOADED_LIST);
                            return;
                        }

                        onResult.onSuccessful(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<TimeEntryModel>> call, Throwable t) {
                        onResult.onUnsuccessful(ConstantApp.CONNECTION_INTERNET);
                    }
                });
    }

    @Override
    public void getWorkListModel(int month, int year, int idUsuario, BaseCallback<List<TimeEntryModel>> onResult) {
        super.data.restApi(EmployeeAPI.class).getWorkListModel(month, year, idUsuario, ConstantApp.BEARER + App.getUser().getAccessToken())
                .enqueue(new Callback<List<TimeEntryModel>>() {
                    @Override
                    public void onResponse(Call<List<TimeEntryModel>>
                                                   call, Response<List<TimeEntryModel>> response) {
                        if (response.code() == UNAUTHORIDEZ) {
                            onResult.onUnsuccessful(ConstantApp.UNAUTHORIDED_USER);
                            return;
                        }

                        if (!response.isSuccessful() || response.body() == null) {
                            onResult.onUnsuccessful(ConstantApp.UNLOADED_LIST);
                            return;
                        }

                        onResult.onSuccessful(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<TimeEntryModel>> call, Throwable t) {
                        onResult.onUnsuccessful(ConstantApp.CONNECTION_INTERNET);
                    }
                });
    }


    @Override
    public void postEntry(TimeEntryModel timeEntryModel, String token, BaseCallback<Void> onResult) {
        super.data.restApi(EmployeeAPI.class)
                .postEntry(timeEntryModel, ConstantApp.BEARER + token)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful())
                            onResult.onSuccessful(response.body());
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
    public void listEmployee(String token, BaseCallback<List<EmployeeModel>> onResult) {
        super.data.restApi(EmployeeAPI.class)
                .listEmployee(ConstantApp.BEARER + token)
                .enqueue(new Callback<List<EmployeeModel>>() {
                    @Override
                    public void onResponse(Call<List<EmployeeModel>> call,
                                           Response<List<EmployeeModel>> response) {

                        if (!response.isSuccessful()) {
                            onResult.onUnsuccessful(ConstantApp.UNLOADED_LIST);
                            return;
                        }
                        if (response.body() == null) {
                            onResult.onUnsuccessful(ConstantApp.LIST_IS_NULL);
                            return;
                        }
                        if (response.code() == UNAUTHORIDEZ) {
                            onResult.onUnsuccessful(ConstantApp.UNAUTHORIDED_USER);
                            return;
                        }
                        onResult.onSuccessful(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<EmployeeModel>> call, Throwable t) {
                        onResult.onUnsuccessful(ConstantApp.CONNECTION_INTERNET);
                    }
                });
    }

    @Override
    public void deleteEntry(long id, BaseCallback<Void> onResult) {
        super.data.restApi(EmployeeAPI.class)
                .deleteEntry(ConstantApp.BEARER + App.getUser().getAccessToken(), id)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (!response.isSuccessful()) {
                            onResult.onUnsuccessful(ConstantApp.UNLOADED_LIST);
                            return;
                        }
                        if (response.code() == UNAUTHORIDEZ) {
                            onResult.onUnsuccessful(ConstantApp.UNAUTHORIDED_USER);
                            return;
                        }
                        onResult.onSuccessful(response.body());
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        onResult.onUnsuccessful(ConstantApp.CONNECTION_INTERNET);
                    }
                });
    }
}
