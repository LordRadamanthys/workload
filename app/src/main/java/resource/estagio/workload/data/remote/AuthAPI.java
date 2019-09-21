package resource.estagio.workload.data.remote;

import resource.estagio.workload.data.remote.model.LoginModel;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AuthAPI {
    @FormUrlEncoded
    @POST("token")
    Call<LoginModel> login(
            @Field("grant_type") String grantType,
            @Field("username") String username,
            @Field("password") String password);
}
