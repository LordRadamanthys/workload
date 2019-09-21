package resource.estagio.workload.infra;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import resource.estagio.workload.ui.login.LoginContract;

/**
 * Created by reale on 25/11/2016.
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private Context context;
    public LoginContract.Presenter presenter;
    private CancellationSignal cenCancellationSignal = new CancellationSignal();

    public FingerprintHandler(Context context) {
        this.context = context;
//        CancellationSignal cenCancellationSignal = new CancellationSignal();
    }

    public void startAuthentication(FingerprintManager fingerprintManager,
                                    FingerprintManager.CryptoObject cryptoObject) {

        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED)
            return;

        fingerprintManager.authenticate(cryptoObject,cenCancellationSignal,0,this,null);

    }


    public void cancel() {
        cenCancellationSignal.cancel();
    }

    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();
        presenter.fingerResponse("Digital invalida");
        //Toast.makeText(context, "Fingerprint Authentication failed!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);
        //context.startActivity(new Intent(context,HomeActivity.class));
        //Toast.makeText(context, "Foi", Toast.LENGTH_SHORT).show();
        presenter.fingerResponseSuccess(true);
    }
}
