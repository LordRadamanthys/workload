package resource.estagio.workload.domain.employee;

import java.util.List;

import resource.estagio.workload.data.remote.model.EmployeeModel;
import resource.estagio.workload.data.remote.model.TimeEntryModel;
import resource.estagio.workload.infra.BaseCallback;

public class EmployeeContract {

    public interface IRepository{
        void getWorkList(int month, int year, BaseCallback<List<TimeEntryModel>> onResult);

        void getWorkListModel(int month, int year, int idUsuario, BaseCallback<List<TimeEntryModel>> onResult);

        void postEntry(TimeEntryModel timeEntryModel, String token, BaseCallback<Void> onResult);

        void listEmployee(String token, BaseCallback<List<EmployeeModel>> onResult);

        void deleteEntry(long id, BaseCallback<Void> onResult);
    }
}
