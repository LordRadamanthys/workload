package resource.estagio.workload.ui.admin.historicResult;

import android.content.Context;

import java.util.List;

import resource.estagio.workload.data.remote.model.EmployeeModel;
import resource.estagio.workload.data.remote.model.TimeEntryModel;
import resource.estagio.workload.ui.admin.historicResult.result.newListResultAdmin;

public class ResultHistoricContract {

    public interface historicPresenter{
        void getTimeline(int month, int year, EmployeeModel employeeModel);

        void deleteEntry(int id);


    }
    public interface historicView{

        void dialog(boolean Key);

        void showListTimeline(List<TimeEntryModel> list);

        void showErrorMessage(String text);

        Context getActivity();

        void reloadListRemove(boolean chooser);
    }


    public interface ResultPresenter{

        void getListResult(int month, int year, EmployeeModel employeeModel);

    }
    public interface ResultView{

        void dialog(boolean Key);

        void showListResult(List<newListResultAdmin> list);

        void showErrorMessage(String text);

        void showSucessMessage(String text);

        Context getActivity();
    }


    public interface mainHistoric{
        void setDateResultFragment(int m, int y, String textMonth);
    }

    public interface mainResult{
        void getDateResultFragment(int m, int y,String textMonth);
    }
}
