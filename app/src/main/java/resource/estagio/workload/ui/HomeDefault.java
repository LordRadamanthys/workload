package resource.estagio.workload.ui;

import android.view.MenuItem;

import androidx.fragment.app.Fragment;


public class HomeDefault {

    public interface Presenter {

        void identifyItemClicked(MenuItem menuItem);
    }

    public interface View {

        void showFragment(Fragment fragment);

        void enableNavigation(boolean key);
    }
}