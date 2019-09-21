package resource.estagio.workload.ui.home.timeline;

import android.content.Context;

import java.util.List;

import resource.estagio.workload.data.remote.model.TimeEntryModel;

public class TimeLineContract {

    public interface Presenter{
        void getTimeline(int month, int year);

        void deleteEntry(int id);

    }
    public interface View{

        void showRecycler(boolean Key);
        void showListTimeline(List<TimeEntryModel> list);

        Context getActivity();

        void showMessage(String error, boolean status);

        void reloadListRemove(boolean chooser);
    }
}
