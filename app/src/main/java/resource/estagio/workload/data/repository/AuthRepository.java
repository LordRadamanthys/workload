package resource.estagio.workload.data.repository;

import resource.estagio.workload.data.remote.AuthAPI;
import resource.estagio.workload.infra.ConstantApp;
import resource.estagio.workload.data.remote.model.LoginModel;
import resource.estagio.workload.domain.user.User;
import resource.estagio.workload.domain.user.UserContract;
import resource.estagio.workload.infra.BaseCallback;
import resource.estagio.workload.infra.Repository;
import resource.estagio.workload.infra.mock.FakeRestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository extends Repository implements UserContract.IRepository {

//    @Override
//    public void login(String username, String password, BaseCallback<User> onResult) {
//        FakeRestClient.getApiInterface()
//                .login(ConstantApp.PASSWORD, username, password)
//                .enqueue(new Callback<LoginModel>() {
//                    @Override
//                    public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
//                        if (response.isSuccessful() && response.body() != null)
//                            onResult.onSuccessful(response.body().toDomain());
//                        else if (response.code() == 400)
//                            onResult.onUnsuccessful(ConstantApp.USERNAME_OR_PASSWORD_INVALID);
//                        else
//                            onResult.onUnsuccessful(response.message());
//                    }
//
//                    @Override
//                    public void onFailure(Call<LoginModel> call, Throwable t) {
//                        onResult.onUnsuccessful(ConstantApp.CONNECTION_INTERNET);
//                    }
//                });
//    }
//
    @Override
    public void login(String username, String password, BaseCallback<User> onResult) {
        super.data.restApi(AuthAPI.class)
                .login("password", username, password)
                .enqueue(new Callback<LoginModel>() {
                    @Override
                    public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                       if (response.isSuccessful() && response.body() != null)
                            onResult.onSuccessful(response.body().toDomain());
                        else if (response.code() == 400)
                            onResult.onUnsuccessful(ConstantApp.USERNAME_OR_PASSWORD_INVALID);
                        else
                            onResult.onUnsuccessful(response.message());
                    }

                    @Override
                    public void onFailure(Call<LoginModel> call, Throwable t) {
                         onResult.onUnsuccessful(ConstantApp.CONNECTION_INTERNET);
                    }
                });
    }

}
