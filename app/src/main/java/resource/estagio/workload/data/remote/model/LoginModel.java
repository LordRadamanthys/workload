package resource.estagio.workload.data.remote.model;

import com.google.gson.annotations.SerializedName;

import resource.estagio.workload.domain.user.User;

public class LoginModel {
    @SerializedName("access_token")
    private String accessToken;
    private long id;
    private String name;
    private boolean isAdmin;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
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

    public User toDomain() {
        return new User(accessToken, id, name, isAdmin);
    }
}
