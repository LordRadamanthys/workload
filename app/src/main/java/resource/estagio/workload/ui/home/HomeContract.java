package resource.estagio.workload.ui.home;

import android.app.Activity;

import androidx.fragment.app.Fragment;

import resource.estagio.workload.ui.HomeDefault;

public class HomeContract {

    public interface View extends HomeDefault.View {

        Activity getActivity();

        void goToResult(Fragment fragment);
    }

    public interface Presenter extends HomeDefault.Presenter {
    }
}
