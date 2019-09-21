package resource.estagio.workload.ui.admin.project.adapterProject;

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
import resource.estagio.workload.data.remote.model.ActivityModel;
import resource.estagio.workload.data.remote.model.CustomerModel;
import resource.estagio.workload.ui.admin.client.AdapterClient;

public class AdapterProject extends RecyclerView.Adapter<AdapterProject.MyViewHolder> {

    private List<ActivityModel> listProject;
    private boolean status;
    private AdapterProjectInterface listener;

    public AdapterProject(List<ActivityModel> listProject, boolean status, AdapterProjectInterface listener) {
        this.listProject = listProject;
        this.status = status;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemList = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_adapter_project, parent, false);

        return new MyViewHolder(itemList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ActivityModel activityModel = listProject.get(position);
        holder.loadFilds(activityModel);
        holder.showIconActions();
        if (status) {
            holder.showIconActions();
            holder.textRenameProject.setOnClickListener(v ->
                    listener.renameProject(position)
            );

            holder.imageDeleteProject.setOnClickListener(v -> {
                try{
                    listener.deleteProject(holder.getLayoutPosition());
                   verifyRemove(holder);
                }catch(ArrayIndexOutOfBoundsException e){
                    notifyDataSetChanged();
                }

            });

        } else {
            holder.setInvisibleIcons();
        }
    }

    private void verifyRemove(@NonNull AdapterProject.MyViewHolder holder) {
        if (listProject.size() - 1 == holder.getLayoutPosition()) {
            listProject.remove(holder.getLayoutPosition());
            notifyDataSetChanged();
            holder.loadFilds(listProject.get(listProject.size() - 1));
        } else {
            listProject.remove(holder.getLayoutPosition());
            notifyItemRemoved(holder.getLayoutPosition());
        }
    }

    @Override
    public int getItemCount() {
        return listProject.size();
    }

    public interface AdapterProjectInterface {

        void renameProject(int position);

        void deleteProject(int position);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textActivityName;
        TextView textRenameProject;
        ImageView imageDeleteProject;
        View viewBarProject;
        ActivityModel model;
        ConstraintLayout constraintProject;



        public void showIconActions() {
            textRenameProject.setVisibility(View.VISIBLE);
            imageDeleteProject.setVisibility(View.VISIBLE);
            viewBarProject.setVisibility(View.VISIBLE);
            textActivityName.setAlpha(0.7F);
        }

        public void setInvisibleIcons() {
            textRenameProject.setVisibility(View.INVISIBLE);
            imageDeleteProject.setVisibility(View.INVISIBLE);
            viewBarProject.setVisibility(View.INVISIBLE);
            textActivityName.setAlpha(1);
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textActivityName = itemView.findViewById(R.id.text_view_name_project);
            textRenameProject = itemView.findViewById(R.id.text_view_rename_project);
            imageDeleteProject = itemView.findViewById(R.id.image_view_delete_project);
            viewBarProject = itemView.findViewById(R.id.view_bar_project);
            constraintProject = itemView.findViewById(R.id.constraint_project);
        }

        public void loadFilds(ActivityModel activityModel) {
            textActivityName.setText(activityModel.getName());
            model = activityModel;
            if(listProject.get(listProject.size() - 1).getId() == model.getId())
                constraintProject.setPadding(0,0,0,100);
            else
                constraintProject.setPadding(0,0,0,0);
        }
    }
}
