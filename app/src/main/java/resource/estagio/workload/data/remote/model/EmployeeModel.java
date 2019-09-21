package resource.estagio.workload.data.remote.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EmployeeModel implements Serializable, Comparable<EmployeeModel> {

    private long id;
    private String name;
    @SerializedName("username")
    private String re;
    private boolean isAdmin;


    public EmployeeModel(String name, String re) {
        this.name = name;
        this.re = re;
    }

    public EmployeeModel() {
    }

    public String getRe() {
        return re;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void setRe(String re) {
        this.re = re;
    }

    @Override
    public int compareTo(EmployeeModel o) {
        return this.name.compareTo(o.name);
    }
}
