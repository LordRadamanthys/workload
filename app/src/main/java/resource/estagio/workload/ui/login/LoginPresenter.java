package resource.estagio.workload.ui.login;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

import resource.estagio.workload.data.repository.AuthRepository;
import resource.estagio.workload.domain.user.User;
import resource.estagio.workload.infra.App;
import resource.estagio.workload.infra.BaseCallback;
import resource.estagio.workload.infra.ConstantApp;
import resource.estagio.workload.infra.FingerprintHandler;
import resource.estagio.workload.ui.DialogApp;

@RequiresApi(api = Build.VERSION_CODES.M)
public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View view;
    public Context contextA;
    private  FingerprintHandler helper;

    public LoginPresenter(LoginContract.View view) {
        this.view = view;
        contextA = view.getContext();
        helper = new FingerprintHandler(contextA);
    }

    @Override
    public void login(String username, String password) {
        final User user = new User(username, password);
        user.repository = new AuthRepository();

        try {
            view.showProgress(true);
            user.login(new BaseCallback<User>() {
                @Override
                public void onSuccessful(User u) {
                    App.setUser(u);
                    view.navigateToHome(u);
                    view.showProgress(false);
                }

                @Override
                public void onUnsuccessful(String error) {
                    view.showProgress(false);
                    if (errorConnection (error)) return;
                    view.showError(error);
                    helper.cancel();
                }
            });
        } catch (Exception e) {
            view.showProgress(false);
            view.showError(e.getMessage());
            helper.cancel();
        }
    }

    @Override
    public void fingerResponse(String s) {
        view.showError(s);
        helper.cancel();
    }

    @Override
    public void fingerResponseSuccess(boolean key) {
        if (key) {
            view.navigateToHomeByFingerPrint();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void validateFingerPrint(FingerprintManager fingerprintManager,
                                    FingerprintManager.CryptoObject cryptoObject, Context context) {
//        this.contextA = context;


        helper.presenter = this;
        helper.startAuthentication(fingerprintManager, cryptoObject);

    }

    private boolean errorConnection(String error) {
        if (error.equals (ConstantApp.CONNECTION_INTERNET)) {
            DialogApp.showDialogConnection (view.getActivity ());
            return true;
        }
        return false;
    }
}
