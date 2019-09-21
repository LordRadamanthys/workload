package resource.estagio.workload.ui.admin.employee.adapterEmployee;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import resource.estagio.workload.R;
import resource.estagio.workload.data.remote.model.EmployeeModel;
import resource.estagio.workload.infra.App;

public class AdapterEmployee extends RecyclerView.Adapter<AdapterEmployee.MyViewHolder> {

    public List<EmployeeModel> listEmployee;
    private AdapterEmployeeInterface listerner;

    public AdapterEmployee(List<EmployeeModel> listEmployee, AdapterEmployeeInterface listerner) {
        this.listEmployee = setFirstEmployee(listEmployee);
        this.listerner = listerner;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listEmployee = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_adapter_employee, parent, false);
        return new MyViewHolder(listEmployee);
    }

    private List<EmployeeModel> setFirstEmployee(List<EmployeeModel> listEmployee) {
        String name = App.getUser().getName();
        ArrayList<EmployeeModel> employeeModels = new ArrayList<>(listEmployee);
        EmployeeModel firstModel = null;
        for (int i = 0; i < employeeModels.size(); i++) {
            if (employeeModels.get(i).getName().contains(name)) {
                firstModel = employeeModels.get(i);
                listEmployee.remove(listEmployee.get(i));
                break;
            }
        }
        Collections.sort(listEmployee);
        if(firstModel == null) return listEmployee;
        employeeModels = new ArrayList<>(listEmployee);

        List<EmployeeModel> employeeModelsSort = new ArrayList<>();
        employeeModelsSort.add(firstModel);
        employeeModelsSort.addAll(employeeModels);
        return employeeModelsSort;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        EmployeeModel model = listEmployee.get(position);
        holder.nome.setText(model.getName());
        holder.re.setText(model.getRe());

        if (listEmployee.size()-1 == holder.getLayoutPosition())
            holder.constraintEmployee.setPadding(0, 0, 0, 100);
        else
            holder.constraintEmployee.setPadding(0, 0, 0, 0);

        holder.constraintEmployee.setOnClickListener(v -> listerner.goToResult(v, position,
                listEmployee.get(holder.getLayoutPosition())));
    }

    @Override
    public int getItemCount() {
        return listEmployee.size();
    }

    public void filterAdapter(List<EmployeeModel> filterList) {
        listEmployee = filterList;
        notifyDataSetChanged();
    }

    public interface AdapterEmployeeInterface {

        public void goToResult(View v, int position, EmployeeModel model);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nome;
        TextView re;
        ConstraintLayout constraintEmployee;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.text_name_employee);
            re = itemView.findViewById(R.id.text_re_employee);
            constraintEmployee = itemView.findViewById(R.id.constraint_employee);
        }
    }
}
