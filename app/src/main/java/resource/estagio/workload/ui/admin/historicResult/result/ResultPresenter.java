package resource.estagio.workload.ui.admin.historicResult.result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import resource.estagio.workload.data.remote.model.CustomerModel;
import resource.estagio.workload.data.remote.model.EmployeeModel;
import resource.estagio.workload.data.remote.model.TimeEntryModel;
import resource.estagio.workload.data.repository.CustomerRepository;
import resource.estagio.workload.data.repository.EmployeeRepository;
import resource.estagio.workload.domain.employee.EmployeeDomain;
import resource.estagio.workload.infra.App;
import resource.estagio.workload.infra.BaseCallback;
import resource.estagio.workload.infra.ConstantApp;
import resource.estagio.workload.ui.DialogApp;
import resource.estagio.workload.ui.admin.historicResult.ResultHistoricContract;

public class ResultPresenter implements ResultHistoricContract.ResultPresenter {

    private ResultHistoricContract.ResultView view;
    private int hours = 0;
    private List<newListResultAdmin> newListResult = new ArrayList();
    private List<CustomerModel> listCustomer = new ArrayList<>();
    private List<TimeEntryModel> timeLineValue;
    private List<ResultProject> listAuxProjects = new ArrayList<>();

    private String aux = "";
    private String auxCustomer = "";
    private int total = 0;


    public ResultPresenter(ResultHistoricContract.ResultView view) {
        this.view = view;
    }

    @Override
    public void getListResult(int month, int year, EmployeeModel employeeModel) {
        hours = 0;
        view.dialog(true);

        EmployeeDomain employeeDomain = new EmployeeDomain();
        employeeDomain.irepository = new EmployeeRepository();
        if(employeeModel.getId() == 0){
            employeeDomain.getWorkList(month, year, new BaseCallback<List<TimeEntryModel>>() {
                @Override
                public void onSuccessful(List<TimeEntryModel> value) {
                    timeLineValue = value;
                    getCustomers();
                }

                @Override
                public void onUnsuccessful(String error) {
                    view.dialog(false);
                    if (errorConnection(error)) return;
                    view.showErrorMessage(error);
                }
            });
        }else {
            employeeDomain.getWorkListModel(month, year, employeeModel, new BaseCallback<List<TimeEntryModel>>() {
                @Override
                public void onSuccessful(List<TimeEntryModel> value) {
                    timeLineValue = value;
                    getCustomers();
                }

                @Override
                public void onUnsuccessful(String error) {
                    view.dialog(false);
                    if (errorConnection(error)) return;
                    view.showErrorMessage(error);
                }
            });
        }
    }

    private void prepareListToAdapter(List<TimeEntryModel> value, List<ResultProject> listAuxProjects) {
        Collections.sort(value);
        listAuxProjects.add(new ResultProject("0", "0", 0));
        for (CustomerModel model : listCustomer) {

            for (TimeEntryModel timeEntryModel : value) {

                if (timeEntryModel.getCustomerName().contains(model.getName())) {

                    if (aux.isEmpty() || aux.equals(timeEntryModel.getActivityName())) {
                        total += timeEntryModel.getHours();
                        aux = timeEntryModel.getActivityName();
                        auxCustomer = timeEntryModel.getCustomerName();
                        hours += timeEntryModel.getHours();
                    } else {
                        hours += timeEntryModel.getHours();
                        listAuxProjects.add(new ResultProject(auxCustomer, aux, total));
                        total = 0;
                        total += timeEntryModel.getHours();
                        aux = timeEntryModel.getActivityName();
                        auxCustomer = timeEntryModel.getCustomerName();
                    }

                    if (aux.isEmpty() || aux.equals(timeEntryModel.getActivityName())) {

                        aux = timeEntryModel.getActivityName();
                    } else {
                        aux = timeEntryModel.getActivityName();
                    }

                }

            }

        }


        listAuxProjects.add(new ResultProject(auxCustomer, aux, total));

        String auxNameCustomer = "";
        StringBuilder auxNameProject = new StringBuilder();
        for (ResultProject project : listAuxProjects) {

            if (auxNameCustomer.equals("") || auxNameCustomer.equals(project.getClient())) {
                auxNameProject.append(project.getName()).append("!").append(project.getHoras()).append("!");

                auxNameCustomer = project.getClient();
            } else {
                newListResultAdmin listResultAdmin = new newListResultAdmin(auxNameCustomer,
                        auxNameProject.toString(), hours, 1);
                newListResult.add(listResultAdmin);
                auxNameProject = new StringBuilder();
                auxNameProject.append(project.getName()).append("!").append(project.getHoras()).append("!");
                auxNameCustomer = project.getClient();
            }

        }
        newListResultAdmin listResultAdmin = new newListResultAdmin(auxNameCustomer,
                auxNameProject.toString(), hours, 1);
        newListResult.add(listResultAdmin);


        view.showListResult(newListResult);
        view.showSucessMessage(ConstantApp.SUCCESS);
        view.dialog(false);
    }


    private void getCustomers() {
        CustomerRepository repository = new CustomerRepository();
        repository.getCustomers(App.getUser().getAccessToken(), new BaseCallback<List<CustomerModel>>() {
            @Override
            public void onSuccessful(List<CustomerModel> value) {
                listCustomer = value;
                prepareListToAdapter(timeLineValue, listAuxProjects);
            }

            @Override
            public void onUnsuccessful(String error) {
                view.dialog(false);
                if(errorConnection(error)) return;
            }
        });
    }

    private boolean errorConnection(String error) {
        if (error.equals(ConstantApp.CONNECTION_INTERNET)) {
            DialogApp.showDialogConnection(view.getActivity());
            return true;
        }
        return false;
    }

}
