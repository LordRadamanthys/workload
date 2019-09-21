package resource.estagio.workload.ui.home;

import android.view.MenuItem;

import resource.estagio.workload.R;
import resource.estagio.workload.ui.DialogApp;
import resource.estagio.workload.ui.SettingsFragment;
import resource.estagio.workload.ui.admin.historicResult.ResultHistoricFragment;
import resource.estagio.workload.ui.home.point.PointFragment;

public class HomePresenter implements HomeContract.Presenter {

    private HomeContract.View view;

    public HomePresenter(HomeContract.View view) {
        this.view = view;
    }

    @Override
    public void identifyItemClicked(MenuItem menuItem) {

        switch (menuItem.getItemId ()) {
            case R.id.ic_pointing:
                view.showFragment (new PointFragment(view));
                break;
            case R.id.ic_history:
                view.goToResult (new ResultHistoricFragment(view));
                break;
            case R.id.ic_config:
                view.showFragment (new SettingsFragment());
                break;
            case R.id.ic_exit:
                DialogApp.showExitDialog (view.getActivity ());
                break;
        }
    }
}
