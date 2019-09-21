package resource.estagio.workload.ui.login;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import resource.estagio.workload.R;
import resource.estagio.workload.domain.user.User;
import resource.estagio.workload.infra.App;
import resource.estagio.workload.infra.SaveLoginSharedPref;
import resource.estagio.workload.ui.admin.HomeAdminActivity;
import resource.estagio.workload.ui.home.HomeActivity;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    public static final String BW_MODELICA_MEDIUM_OTF = "BwModelica-Medium.otf";
    private EditText usernameView;
    private EditText passwordView;
    private View progressView;
    private Button signInButton;
    private TextView text_view_lacamento_login;
    private Switch switchSavePassword;
    private  AlertDialog.Builder builder;

    private AlertDialog alerta;
    private SaveLoginSharedPref saveLoginSharedPref;

    private KeyStore keyStore;
    private static final String KEY_NAME = "EDMTDev";
    private Cipher cipher;

    private LoginContract.Presenter presenter;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow ().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_login);

        saveLoginSharedPref = new SaveLoginSharedPref (this);

        presenter = new LoginPresenter (this);

        loadUI ();
        loadActions ();
        setFonts ();
        initSwitch ();
        verifyFingerPrint ();//verificar se é login por digital
        verifyLogin ();//verifica se existe login salvo

        switchSavePassword.setOnCheckedChangeListener ((buttonView, isChecked) -> {
            setActionSwitchSavePassword (isChecked);
        });
    }

    private void loadUI() {
        switchSavePassword = findViewById (R.id.switch1);
        usernameView = findViewById (R.id.username);
        passwordView = findViewById (R.id.password);
        //loginFormView = findViewById(R.id.login_form);
        progressView = findViewById (R.id.login_progress);
        signInButton = findViewById (R.id.sign_in_button);
        text_view_lacamento_login = findViewById (R.id.text_view_lacamento_login);
        builder = new AlertDialog.Builder (this);
        alerta = builder.create ();
    }

    private void setActionSwitchSavePassword(boolean isChecked) {
        if (isChecked) {
            if (!usernameView.getText ().toString ().isEmpty () &&
                    !passwordView.getText ().toString ().isEmpty ()) {

                saveLogin ();
            } else {
                switchSavePassword.setChecked (false);
                Toast.makeText (getContext (), getString (R.string.fill_the_fields), Toast.LENGTH_SHORT).show ();
            }
        } else {
            App.getPref ().setItsSave (0);
        }
    }

    private void initSwitch() {
        if (App.getPref ().getItsSave () == 1) {
            switchSavePassword.setChecked (true);
        } else {
            switchSavePassword.setChecked (false);
        }
    }

    private void loadActions() {
        passwordView.setOnEditorActionListener ((textView, id, keyEvent) -> {
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                presenter.login (
                        usernameView.getText ().toString (), passwordView.getText ().toString ());
                return true;
            }
            return false;
        });

        signInButton.setOnClickListener (view -> presenter.login (
                usernameView.getText ().toString (), passwordView.getText ().toString ()));
    }

    @Override
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources ().getInteger (android.R.integer.config_shortAnimTime);

        signInButton.setVisibility (show ? View.INVISIBLE : View.VISIBLE);
        signInButton.animate ().setDuration (shortAnimTime).alpha (
                show ? 0 : 1).setListener (new AnimatorListenerAdapter () {
            @Override
            public void onAnimationEnd(Animator animation) {
                signInButton.setVisibility (show ? View.INVISIBLE : View.VISIBLE);
            }
        });

        progressView.setVisibility (show ? View.VISIBLE : View.GONE);
        progressView.animate ().setDuration (shortAnimTime).alpha (
                show ? 1 : 0).setListener (new AnimatorListenerAdapter () {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressView.setVisibility (show ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public void showError(String error) {
        Toast.makeText (this, error, Toast.LENGTH_SHORT).show ();
        alerta.cancel();
    }

    @Override
    public void navigateToHome(User user) {
        if (switchSavePassword.isChecked ()) {
            saveLogin ();
        }
        if (user.isAdmin ()) {

            Intent intent = new Intent (this, HomeAdminActivity.class);
            startActivity (intent);
            overridePendingTransition (R.anim.botton_in, R.anim.top_out);
            finish ();

        } else {
            Intent intent = new Intent (this, HomeActivity.class);
            startActivity (intent);
            overridePendingTransition (R.anim.botton_in, R.anim.top_out);
            finish ();

        }
    }

    @Override
    public void navigateToHomeByFingerPrint() {
        presenter.login (
                saveLoginSharedPref.getUser (), saveLoginSharedPref.getPassword ()
        );
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public Context getActivity() {
        return this;
    }

    public void saveLogin() {
        saveLoginSharedPref.saveLogin (usernameView.getText ().toString (),
                passwordView.getText ().toString ());
    }

    public void verifyLogin() {
        //saveLoginSharedPref.clear();

        if (saveLoginSharedPref.getItsSave () == 1) {
            usernameView.setText (saveLoginSharedPref.getUser ());
            passwordView.setText (saveLoginSharedPref.getPassword ());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void verifyFingerPrint() {
        if (saveLoginSharedPref.getItsSave () == 1 && saveLoginSharedPref.getFingerPrint () == 1) {
            //Cria o gerador do AlertDialog
            builder = new AlertDialog.Builder (this);

            //define o titulo
            builder.setTitle (getString (R.string.login));
            builder.setMessage (getString (R.string.press_the_fingerprint_reader));

            builder.setIcon (R.mipmap.ic_finge_print);

            //cria o AlertDialog
            alerta = builder.create ();
            //Exibe
            alerta.show ();
            alerta.setCancelable (false);
            loadFinger (); //leitor de digital
        }
    }

    public void setFonts() {
        Typeface medium = Typeface.createFromAsset (getAssets (),
                BW_MODELICA_MEDIUM_OTF);

        text_view_lacamento_login.setTypeface (medium);
    }

    //fingerPrint
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void loadFinger() {
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService (KEYGUARD_SERVICE);
        FingerprintManager fingerprintManager = (FingerprintManager) getSystemService (FINGERPRINT_SERVICE);

        if (ActivityCompat.checkSelfPermission (this,
                Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        if (!fingerprintManager.isHardwareDetected ())
            Toast.makeText (this, getString (R.string.fingerprint_authentication_permission_not_enable),
                    Toast.LENGTH_SHORT).show ();
        else {
            if (!fingerprintManager.hasEnrolledFingerprints ())
                Toast.makeText (this, getString (R.string.register_at_least_one_fingerprint_in_settings),
                        Toast.LENGTH_SHORT).show ();
            else {
                if (!keyguardManager.isKeyguardSecure ())
                    Toast.makeText (this, getString (R.string.lock_screen_security_not_enabled_in_settings),
                            Toast.LENGTH_SHORT).show ();
                else
                    genKey ();

                if (cipherInit ()) {
                    FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject (cipher);

                    presenter.validateFingerPrint (fingerprintManager, cryptoObject, this);
                }
            }
        }
    }

    private boolean cipherInit() {
        try {
            cipher = Cipher.getInstance (KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace ();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace ();
        }

        try {
            keyStore.load (null);
            SecretKey key = (SecretKey) keyStore.getKey (KEY_NAME, null);
            cipher.init (Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (IOException e1) {

            e1.printStackTrace ();
            return false;
        } catch (NoSuchAlgorithmException e1) {

            e1.printStackTrace ();
            return false;
        } catch (CertificateException e1) {

            e1.printStackTrace ();
            return false;
        } catch (UnrecoverableKeyException e1) {

            e1.printStackTrace ();
            return false;
        } catch (KeyStoreException e1) {

            e1.printStackTrace ();
            return false;
        } catch (InvalidKeyException e1) {

            e1.printStackTrace ();
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void genKey() {
        try {
            keyStore = KeyStore.getInstance ("AndroidKeyStore");
        } catch (KeyStoreException e) {
            e.printStackTrace ();
        }

        KeyGenerator keyGenerator = null;

        try {
            keyGenerator = KeyGenerator.getInstance (KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace ();
        } catch (NoSuchProviderException e) {
            e.printStackTrace ();
        }

        try {
            keyStore.load (null);
            keyGenerator.init (new KeyGenParameterSpec.Builder (KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT).setBlockModes (KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired (true)
                    .setEncryptionPaddings (KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build ()
            );
            keyGenerator.generateKey ();
        } catch (IOException e) {
            e.printStackTrace ();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace ();
        } catch (CertificateException e) {
            e.printStackTrace ();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace ();
        }
    }

}
