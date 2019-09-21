package resource.estagio.workload.domain.project;

import java.util.List;

import resource.estagio.workload.data.remote.model.ActivityModel;
import resource.estagio.workload.data.remote.model.ActivityTypeModel;
import resource.estagio.workload.infra.BaseCallback;

public class ProjectContract {

    public interface IRepository {

        void getActivity(int id, String token, BaseCallback<List<ActivityModel>> onResult);
        void getActivityType(String token, BaseCallback<List<ActivityTypeModel>> onResult);
        void insertProject (Project project, String token, BaseCallback<String> onResult);
        void updateProject(Project project, String token, BaseCallback<String> onResult);

        void deleteProject(long id, String name, String token, BaseCallback<String> onResult);
    }
}
