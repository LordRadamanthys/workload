package resource.estagio.workload.domain.user;

import resource.estagio.workload.infra.ConstantApp;
import resource.estagio.workload.infra.BaseCallback;

public class User {

    public UserContract.IRepository repository;

    private String username;
    private String password;

    private String accessToken;
    private long id;
    private String name;
    private boolean isAdmin;

    public User(String username, String password) {
        this.username = username.trim();
        this.password = password.trim();
    }

    public User(String accessToken, long id, String name, boolean isAdmin) {
        this.accessToken = accessToken;
        this.id = id;
        this.name = name;
        this.isAdmin = isAdmin;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void login(final BaseCallback<User> listener) throws Exception {

        if (repository == null) throw new Exception(ConstantApp.REPOSITORY_NULL);

        if (username == null || username.isEmpty())
            throw new Exception(ConstantApp.USERNAME_IS_REQUIRED);

        if (password == null || password.isEmpty())
            throw new Exception(ConstantApp.PASSWORD_IS_REQUIRED);

        if (password.length() < 4) throw new Exception(ConstantApp.PASSWORD_MIN_FOUR);

        repository.login(username, password, new BaseCallback<User>() {
            @Override
            public void onSuccessful(User user) {
                listener.onSuccessful(user);
            }

            @Override
            public void onUnsuccessful(String error) {
                listener.onUnsuccessful(error);
            }
        });
    }
}
