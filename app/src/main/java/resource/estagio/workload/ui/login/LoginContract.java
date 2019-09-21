package resource.estagio.workload.ui.login;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;

import resource.estagio.workload.domain.user.User;

public class LoginContract {

    public interface View {
        void showProgress(final boolean show);

        void showError(String error);

        void navigateToHome(User user);

        void navigateToHomeByFingerPrint();

        Context getContext();

        Context getActivity();
    }

    public interface Presenter {
        void login(String username, String password);

        void fingerResponse(String s);
        void fingerResponseSuccess(boolean key);
        void validateFingerPrint(FingerprintManager fingerprintManager,
                                 FingerprintManager.CryptoObject cryptoObject, Context context);
    }
}
