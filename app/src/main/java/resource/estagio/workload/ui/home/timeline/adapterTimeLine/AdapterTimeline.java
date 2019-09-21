package resource.estagio.workload.ui.home.timeline.adapterTimeLine;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import resource.estagio.workload.R;
import resource.estagio.workload.data.remote.model.EmployeeModel;
import resource.estagio.workload.data.remote.model.TimeEntryModel;
import resource.estagio.workload.infra.ConstantApp;

public class AdapterTimeline extends RecyclerView.Adapter<AdapterTimeline.MyViewHolder> {

    private AdapterTimelineInterface listener;
    private List<TimeEntryModel> listTimeline;
    private int sizeList;
    private boolean admin;

    public AdapterTimeline(List<TimeEntryModel> listTimeline, boolean admin, AdapterTimeline.AdapterTimelineInterface listener) {
        this.listTimeline = listTimeline;
        sizeList = listTimeline.size() - 1;
        this.admin = admin;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemList = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_adapter_timeline, parent, false);

        return new MyViewHolder(itemList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        TimeEntryModel model = listTimeline.get(position);

        if(position == sizeList) {
            setHolderConfiguration(holder, model);
            holder.line.setVisibility(View.INVISIBLE);

        } else {
            setHolderConfiguration(holder, model);
            holder.line.setVisibility(View.VISIBLE);
        }
        if(position == 0){
            holder.constraintLayoutTimeline.setPadding(0, 20, 0, 0);
        }

    }

    private void setHolderConfiguration(@NonNull MyViewHolder holder, TimeEntryModel model) {
        holder.activityName.setText(model.getActivityName());
        holder.customerName.setText(model.getCustomerName());
        holder.date.setText(model.getDate().substring(0, 5));
        holder.hours.setText(model.getHours() + ConstantApp.HOURS);
        holder.reason.setText(model.getReason());
        if (!admin)
            holder.setInvisibleIcons();
        else
            holder.setClickRemove(listTimeline.get(holder.getLayoutPosition()));
    }

    @Override
    public int getItemCount() {
        return listTimeline.size();
    }

    public interface AdapterTimelineInterface {
        void deletePoint(TimeEntryModel timeEntryModel, AdapterUpdateTimelineInterface updateListener);

        void updateLoadList();
    }

    public interface AdapterUpdateTimelineInterface{
        void updateTimelineList(boolean chooser);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView activityName;
        TextView customerName;
        TextView hours;
        TextView date;
        TextView reason;
        View line;
        ImageView deleteTimeline;
        ConstraintLayout constraintLayoutTimeline;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            activityName = itemView.findViewById(R.id.textView_name_project);
            customerName = itemView.findViewById(R.id.button_name_customer);
            hours = itemView.findViewById(R.id.button_hours);
            date = itemView.findViewById(R.id.textView_date_history);
            reason = itemView.findViewById(R.id.textView_reason_history);
            line = itemView.findViewById(R.id.view5);
            deleteTimeline = itemView.findViewById(R.id.image_view_delete_timeline);
            constraintLayoutTimeline = itemView.findViewById(R.id.constraint_layout_timeline);
        }

        public void setInvisibleIcons() {
            deleteTimeline.setVisibility(View.GONE);
        }

        public void setClickRemove(TimeEntryModel timeEntryModel) {
            AdapterUpdateTimelineInterface updateListener = chooser -> listener.updateLoadList();
            deleteTimeline.setOnClickListener( v -> {
                try{
                    listener.deletePoint(timeEntryModel, updateListener);
                }catch(ArrayIndexOutOfBoundsException e){
                    notifyDataSetChanged();
                }
            });
        }


    }
}
