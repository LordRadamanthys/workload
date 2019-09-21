package resource.estagio.workload.ui.admin;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import resource.estagio.workload.R;
import resource.estagio.workload.ui.home.point.PointFragment;

public class HomeAdminActivity extends AppCompatActivity implements HomeAdminContract.View{

    private HomeAdminContract.Presenter presenter;
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow ().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_home_admin);

        presenter = new HomeAdminPresenter(this);
        navigation = findViewById (R.id.bottom_navigation_admin);

        getSupportFragmentManager ()
                .beginTransaction ()
                .replace (R.id.frame_admin, new PointFragment(this))
                .commit ();
        navigation.setOnNavigationItemSelectedListener (menuItem -> {
            presenter.identifyItemClicked (menuItem);
            return true;
        });
    }

    @Override
    public void showFragment(Fragment fragment) {
        getSupportFragmentManager ()
                .beginTransaction ()
                .replace (R.id.frame_admin, fragment)
                .commit ();
    }

    @Override
    public void enableNavigation(boolean key) {
        for (int i = 0; i < navigation.getMenu ().size (); i++) {
            navigation.getMenu ().getItem (i).setEnabled (!key);
        }
    }
}