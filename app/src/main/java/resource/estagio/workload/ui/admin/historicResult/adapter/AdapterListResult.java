package resource.estagio.workload.ui.admin.historicResult.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import resource.estagio.workload.R;
import resource.estagio.workload.infra.ConstantApp;
import resource.estagio.workload.ui.admin.historicResult.result.newListResultAdmin;

public class AdapterListResult extends RecyclerView.Adapter<AdapterListResult.MyViewHolder> {


    private List<newListResultAdmin> listTimeline;
    private Context context;
    private int sizeList;

    public AdapterListResult(List<newListResultAdmin> listTimeline, Context context) {
        this.listTimeline = listTimeline;
        //Collections.reverse(listTimeline);
        this.context = context;
        sizeList = listTimeline.size() - 1;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemList = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_adapter_result_admin, parent, false);

        return new MyViewHolder(itemList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        newListResultAdmin model = listTimeline.get(position);
        if (position == 0) {
            holder.texAllHoursProjects.setText(model.getAllHours() + ":00");
            holder.cardViewAllHours.setVisibility(View.VISIBLE);
            holder.cardViewAllProjects.setVisibility(View.GONE);
        } else {
            holder.cardViewAllHours.setVisibility(View.GONE);
            holder.cardViewAllProjects.setVisibility(View.VISIBLE);
        }

        if(model.getAllHours() == 0) {
            holder.cardViewAllProjects.setVisibility(View.GONE);
            Toast.makeText(context, ConstantApp.LIST_IS_NULL, Toast.LENGTH_LONG).show();
        }


        String project = "";
        String hours = "";
        int allHours = 0;
        String[] split = model.getProjectName().split("!");
        for (int i = 0; i < split.length - 1; i++) {
            if (i % 2 == 0) {
                project += split[i] + "\n";
                hours += split[i + 1] + ":00\n";
                allHours += Integer.parseInt(split[i + 1]);
            }
        }
        holder.projectName.setText(project);
        holder.customerName.setText(model.getCustomerName());
        holder.hours.setText(hours);
        holder.Allhours.setText(allHours + ":00");

        if(sizeList == holder.getLayoutPosition())
            holder.constraintAdapterResult.setPadding(0,0,0,100);
        else
            holder.constraintAdapterResult.setPadding(0,0,0,0);

    }

    @Override
    public int getItemCount() {
        return listTimeline.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView projectName;
        TextView customerName;
        TextView Allhours;
        TextView hours;

        CardView cardViewAllHours;
        CardView cardViewAllProjects;
        TextView texAllHoursProjects;
        ConstraintLayout constraintAdapterResult;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            projectName = itemView.findViewById(R.id.textView_name_project_result);
            customerName = itemView.findViewById(R.id.textView_name_customer_result);
            Allhours = itemView.findViewById(R.id.button_all_hours_result);
            hours = itemView.findViewById(R.id.textView_hour_project_result);

            cardViewAllHours = itemView.findViewById(R.id.cardView_AllHours);
            cardViewAllProjects = itemView.findViewById(R.id.cardView_all_projects);
            constraintAdapterResult = itemView.findViewById(R.id.constraint_adapter_result);
            texAllHoursProjects = itemView.findViewById(R.id.textView5_all_hours_result);

        }
    }
}
