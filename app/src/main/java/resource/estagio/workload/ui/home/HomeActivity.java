package resource.estagio.workload.ui.home;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import resource.estagio.workload.R;
import resource.estagio.workload.data.remote.model.EmployeeModel;
import resource.estagio.workload.infra.ConstantApp;
import resource.estagio.workload.ui.home.point.PointFragment;

public class HomeActivity extends AppCompatActivity implements HomeContract.View {

    private FrameLayout frameLayoutHome;
    private long backPresssedTime;
    private Toast backToast;
    private BottomNavigationView navigation;
    private HomeContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_home);

        presenter = new HomePresenter(this);
        loadUi ();
        showFragment (new PointFragment(this));

        navigation.setOnNavigationItemSelectedListener (menuItem -> {

            presenter.identifyItemClicked (menuItem);
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        if (backPresssedTime + 20000 > System.currentTimeMillis ()) {
            backToast.cancel ();
            super.onBackPressed ();
            return;

        } else {
            backToast = Toast.makeText (getBaseContext (),
                    getString (R.string.press_again_to_exit), Toast.LENGTH_SHORT);
            backToast.show ();
        }
        backPresssedTime = System.currentTimeMillis ();
    }

    private void loadUi() {
        frameLayoutHome = findViewById (R.id.frame_layout_home);
        navigation = findViewById (R.id.bottom_navigation_employee);
    }

    @Override
    public void showFragment(Fragment fragment) {
        getSupportFragmentManager ()
                .beginTransaction ()
                .replace (R.id.frame_layout_home, fragment)
                .commit ();
    }

    @Override
    public void enableNavigation(boolean key) {

        for (int i = 0; i < navigation.getMenu ().size (); i++) {
            navigation.getMenu ().getItem (i).setEnabled (!key);
        }
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void goToResult(Fragment fragment) {
        Bundle bundle = new Bundle();
        EmployeeModel model = new EmployeeModel();
        bundle.putSerializable(ConstantApp.RESULT, model);
        fragment.setArguments(bundle);
        getSupportFragmentManager ()
                .beginTransaction()
                .replace(R.id.frame_layout_home, fragment)
                .commit();
    }

}