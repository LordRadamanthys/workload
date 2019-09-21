package resource.estagio.workload.domain.user;

import resource.estagio.workload.infra.BaseCallback;

public class UserContract {
    public interface IRepository {
        void login(String username, String password, BaseCallback<User> onResult);
    }
}
