package resource.estagio.workload.ui.admin;

import android.view.MenuItem;

import resource.estagio.workload.R;
import resource.estagio.workload.ui.SettingsFragment;
import resource.estagio.workload.ui.admin.client.ClientFragment;
import resource.estagio.workload.ui.admin.employee.EmployeeFragment;
import resource.estagio.workload.ui.home.point.PointFragment;

public class HomeAdminPresenter implements HomeAdminContract.Presenter {
    private HomeAdminContract.View view;

    public HomeAdminPresenter(HomeAdminContract.View view) {
        this.view = view;
    }

    @Override
    public void identifyItemClicked(MenuItem menuItem) {
        switch (menuItem.getItemId ()) {
            case R.id.ic_employee_admin:
                view.showFragment (new EmployeeFragment (view));
                break;
            case R.id.ic_appointments_admin:
                view.showFragment (new PointFragment (view));
                break;
            case R.id.ic_customer_admin:
                view.showFragment (new ClientFragment (view));
                break;
            case R.id.ic_config_admin:
                view.showFragment (new SettingsFragment ());
                break;
        }
    }
}