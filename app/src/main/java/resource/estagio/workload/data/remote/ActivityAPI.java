package resource.estagio.workload.data.remote;


import java.util.List;

import resource.estagio.workload.data.remote.model.ActivityModel;
import resource.estagio.workload.data.remote.model.ActivityTypeModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ActivityAPI {
    @GET("activity/{customerId}")
    Call<List<ActivityModel>> getActivity(
            @Path("customerId") int id,
            @Header("Authorization") String token);

    @GET("activity/type")
    Call<List<ActivityTypeModel>> getActivityType(
            @Header("Authorization") String token);

    @POST("activity")
    Call<Void> insertProject(
            @Body ActivityModel activityModel,
            @Header("Authorization") String token);
    @POST("activity/update")
    Call<Void> updateProject (
            @Body ActivityModel activityModel,
            @Header("Authorization") String token);

    @DELETE("activity/{activityId}")
    Call<Void> deleteProject(
            @Path("activityId") long id,
            @Header("Authorization") String token);

}
