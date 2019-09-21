package resource.estagio.workload.infra.mock;

import resource.estagio.workload.data.remote.model.LoginModel;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface FakeApiInterface {

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @FormUrlEncoded
    @POST("login")
    Call<LoginModel> login(
            @Field("grant_type") String grantType,
            @Field("username") String username,
            @Field("password") String password);
}
