package resource.estagio.workload.data.remote;

import java.util.List;

import resource.estagio.workload.data.remote.model.CustomerModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CustomerAPI {
    @GET("customer")
    Call<List<CustomerModel>> getCustomer(
            @Header("Authorization") String token);

    @DELETE("customer")
    Call<Void> deleteCustomer(
            @Query("id") long id,
            @Header("Authorization") String token);

    @POST("customer")
    Call<Void> postCustomer(
            @Body CustomerModel model,
            @Header("Authorization") String token);
}

