package resource.estagio.workload.ui.admin.client;

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
import resource.estagio.workload.data.remote.model.CustomerModel;

public class AdapterClient extends RecyclerView.Adapter<AdapterClient.MyViewHolder> {

    private List<CustomerModel> list;
    private AdapterInterface listener;
    private boolean setStatusList;

    public AdapterClient(List<CustomerModel> list, boolean status, AdapterInterface listener) {
        this.list = list;
        this.setStatusList = status;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View listView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_adapter_client, parent, false);
        return new MyViewHolder(listView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CustomerModel customerModel = list.get(position);
        holder.setItens(customerModel);
        getClickAdapter(holder, position);
        setPaddingButton(holder);
    }

    private void setPaddingButton(@NonNull MyViewHolder holder) {
        if (list.size() - 1 == holder.getLayoutPosition())
            holder.constraintClient.setPadding(0, 0, 0, 100);
        else
            holder.constraintClient.setPadding(0, 0, 0, 0);
    }

    private void getClickAdapter(@NonNull MyViewHolder holder, int position) {
        if (setStatusList) {
            holder.buttonCardClient.setOnClickListener(v -> listener.goToProject(v, position));
            holder.setIconClient();
        } else
            deleteCustomer(holder, position);
    }

    private void deleteCustomer(@NonNull MyViewHolder holder, int position) {
        holder.imageViewDeleteClient.setOnClickListener(v -> {
            try {
                listener.removeClient(v, position, list.get(holder.getLayoutPosition()));
                verifyRemove(holder);
            } catch (ArrayIndexOutOfBoundsException e) {
                notifyDataSetChanged();
            }
        });
        holder.setIconRemove();
    }

    private void verifyRemove(@NonNull MyViewHolder holder) {
        if (list.size() - 1 == holder.getLayoutPosition()) {
            list.remove(holder.getLayoutPosition());
            notifyDataSetChanged();
            holder.setItens(list.get(list.size() - 1));
        } else {
            list.remove(holder.getLayoutPosition());
            notifyItemRemoved(holder.getLayoutPosition());
        }
    }

    @Override
    public int getItemCount() {
        if (list == null)
            return 0;
        else
            return list.size();
    }

    public interface AdapterInterface {
        void removeClient(View v, int position, CustomerModel model);

        void goToProject(View v, int position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout constraintClient;
        ConstraintLayout buttonCardClient;
        TextView textViewClientAdapter;
        ImageView imageViewIconClientAdapter;
        ImageView imageViewDeleteClient;
        CustomerModel model;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            constraintClient = itemView.findViewById(R.id.constraint_client);
            textViewClientAdapter = itemView.findViewById(R.id.text_view_client_adapter);
            buttonCardClient = itemView.findViewById(R.id.button_card_client);
            imageViewIconClientAdapter = itemView.findViewById(R.id.image_view_icon_adapter_client);
            imageViewDeleteClient = itemView.findViewById(R.id.image_view_delete_client);
        }

        void setIconRemove() {
            imageViewDeleteClient.setVisibility(View.VISIBLE);
            imageViewIconClientAdapter.setVisibility(View.INVISIBLE);
            textViewClientAdapter.setAlpha(0.7F);
        }

        void setIconClient() {
            imageViewDeleteClient.setVisibility(View.INVISIBLE);
            imageViewIconClientAdapter.setVisibility(View.VISIBLE);
            textViewClientAdapter.setAlpha(1);
        }

        public void setItens(CustomerModel customerModel) {
            textViewClientAdapter.setText(customerModel.getName());
            model = customerModel;
            if (list.get(list.size() - 1).getId() == model.getId())
                constraintClient.setPadding(0, 0, 0, 100);
            else
                constraintClient.setPadding(0, 0, 0, 0);
        }
    }
}