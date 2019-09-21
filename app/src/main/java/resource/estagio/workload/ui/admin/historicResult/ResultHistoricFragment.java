package resource.estagio.workload.ui.admin.historicResult;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import resource.estagio.workload.R;
import resource.estagio.workload.data.remote.model.EmployeeModel;
import resource.estagio.workload.infra.ConstantApp;
import resource.estagio.workload.ui.HomeDefault;
import resource.estagio.workload.ui.admin.historicResult.historic.HistoricFragment;
import resource.estagio.workload.ui.admin.historicResult.result.ResultFragment;

public class ResultHistoricFragment extends Fragment implements ResultHistoricContract.mainResult {

    private View view;
    private TabLayout tabLayout;
    private TextView textTitle, textSubtitle;
    private Fragment fragment;
    private HomeDefault.View viewHome;
    private ImageView imageViewBackCollaborator;
    private ResultHistoricContract.mainResult mainResult = this;
    private int month = 0;
    private int year = 0;
    private String textMonth = "";
    private Calendar calendar;
    private EmployeeModel employee;

    public ResultHistoricFragment(HomeDefault.View view) {
        viewHome = view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_result_historic, container, false);

        loadUI();
        verifyMonth();
        getArgumentsResult();
        actionTabSelected();
        backToCustomers();

        return view;
    }


    private void getArgumentsResult() {
        Bundle arguments = getArguments();
        employee = (EmployeeModel) arguments.getSerializable(ConstantApp.RESULT);
    }

    private void verifyMonth() {
        if (textMonth.isEmpty()) {
//            textSubtitle.setText(ConstantApp.SUBTITLE_RESULT_MAIN_FRAGMENT +
//                    new SimpleDateFormat("MMM").format(calendar.getTime()));
            textMonth=new SimpleDateFormat("MMM").format(calendar.getTime());
        }
    }

    private void actionTabSelected() {


        //textSubtitle.setText(ConstantApp.SUBTITLE_RESULT_MAIN_FRAGMENT + textMonth);
        putTitleAndSubtitle(ConstantApp.TITLE_RESULT_MAIN_FRAGMENT,
                ConstantApp.SUBTITLE_HISTORIC_MAIN_FRAGMENT);
        fragment = new ResultFragment(viewHome, month, year, employee);
        loadFragment(fragment);
        tabLayout.getTabAt(0).setText("Resultado / "+textMonth.toUpperCase());
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        putTitleAndSubtitle(ConstantApp.TITLE_RESULT_MAIN_FRAGMENT,
                                ConstantApp.SUBTITLE_HISTORIC_MAIN_FRAGMENT);
                        tabLayout.getTabAt(0).setText("Resultado / "+textMonth.toUpperCase());
                        //textSubtitle.setText(ConstantApp.SUBTITLE_RESULT_MAIN_FRAGMENT + textMonth);
                        fragment = new ResultFragment(viewHome, month, year, employee);
                        break;

                    case 1:
                        putTitleAndSubtitle(ConstantApp.TITLE_HISTORIC_MAIN_FRAGMENT,
                                ConstantApp.SUBTITLE_HISTORIC_MAIN_FRAGMENT);
                        fragment = new HistoricFragment(viewHome, mainResult, employee);
                        break;
                }
                loadFragment(fragment);
            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void putTitleAndSubtitle(String title, String subTitle) {
        if(employee.getName() == null || employee.getRe() == null) {
            textTitle.setText(title);
            textSubtitle.setText(subTitle);
        }else {
            textTitle.setText(employee.getName());
            textSubtitle.setText(employee.getRe());
        }
    }

    private void loadUI() {
        calendar = Calendar.getInstance();
        calendar.get(Calendar.MONTH);
        textTitle = view.findViewById(R.id.textTitle);
        textSubtitle = view.findViewById(R.id.textSubtitle);
        fragment = new ResultFragment(viewHome, month, year, employee);
        tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.setSelectedTabIndicator(0);
        imageViewBackCollaborator = view.findViewById(R.id.image_view_back_collaborator);
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frameLayout, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    private void backToCustomers() {
        if(employee.getRe() == null || employee.getName() == null)
            imageViewBackCollaborator.setVisibility(View.INVISIBLE);
        else {
            imageViewBackCollaborator.setOnClickListener(v -> {
                getActivity().onBackPressed();
                viewHome.enableNavigation(false);
            });
        }
    }

    @Override
    public void getDateResultFragment(int m, int y, String textMonth) {
        //Toast.makeText(getContext(), "" + m + "/" + y, Toast.LENGTH_SHORT).show();
        month = m;
        year = y;
        this.textMonth = textMonth;
    }
}
